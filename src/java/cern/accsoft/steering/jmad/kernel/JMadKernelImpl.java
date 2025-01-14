// @formatter:off
/*******************************************************************************
 * This file is part of JMad. Copyright (c) 2008-2011, CERN. All rights reserved. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
// @formatter:on

package cern.accsoft.steering.jmad.kernel;

import static com.google.common.base.Preconditions.checkState;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cern.accsoft.steering.jmad.JMadException;
import cern.accsoft.steering.jmad.bin.MadxBin;
import cern.accsoft.steering.jmad.domain.result.Result;
import cern.accsoft.steering.jmad.domain.result.ResultType;
import cern.accsoft.steering.jmad.io.DynapOutputParser;
import cern.accsoft.steering.jmad.io.MatchOutputParser;
import cern.accsoft.steering.jmad.io.StrengthFileParser;
import cern.accsoft.steering.jmad.io.TfsFileParser;
import cern.accsoft.steering.jmad.io.TrackOutputParser;
import cern.accsoft.steering.jmad.util.FileMonitor;
import cern.accsoft.steering.jmad.util.FileMonitor.ProcessTerminatedUnexpectedlyException;
import cern.accsoft.steering.jmad.util.FileUtil;
import cern.accsoft.steering.jmad.util.JMadPreferences;
import cern.accsoft.steering.jmad.util.ProcTools;
import cern.accsoft.steering.jmad.util.ProcessTerminationMonitor;
import cern.accsoft.steering.jmad.util.StringUtil;
import cern.accsoft.steering.jmad.util.TempFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this is the implementation of the {@link JMadKernel} which controls one MadX-Process.
 *
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class JMadKernelImpl implements JMadKernel, JMadKernelConfig {

    private static final int MAX_REPORTED_OUTPUT_LINES = 10;
    private static final int MAX_REPORTED_ERROR_LINES = 10;

    /**
     * the logger for the class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JMadKernelImpl.class);

    /**
     * This value is returned by the waitUntilReady - method. It is only used internally.
     */
    private static final int EXIT_VALUE_DESTROYED = -9999;

    /**
     * the command which is used to stop madx.
     */
    private static final String CMD_STOP = "stop;";

    /*
     * various file names which are used to communicate with MadX and for logging
     */
    private static final String FILENAME_READY = "madx-ready.out";
    private static final String FILENAME_RESULT = "madx-result.out";
    private static final String FILENAME_LOG_IN = "madx-input.log";
    private static final String FILENAME_LOG_OUT = "madx-output.log";
    private static final String FILENAME_LOG_ERROR = "madx-error.log";

    private File readyFile = null;
    private File resultFile = null;
    private File madxInputLogFile = null;
    private File madxOutputLogFile = null;
    private File madxErrorLogFile = null;

    /**
     * wait this amount of ms, when deletion failed and retry
     */
    private static final int RETRY_DELAY_IN_MILLISECONDS = 100;

    /**
     * how often to retry deleting a file before throwing an exception?
     */
    private static final int RETRY_ATTEMPTS = 3;

    /**
     * the (optional) timeout in ms which the kernel waits until Madx finishes the command/task. If the timeout is set
     * to null, then the kernel waits forever. Null is the default.
     */
    private Long timeout = null;

    /**
     * the process for madx
     */
    private Process process = null;

    /**
     * the stream for providing madx with input
     */
    private PrintWriter input = null;

    /**
     * the logger, where the inputs to madx are logged
     */
    private BufferedWriter inputLogWriter = null;

    /**
     * The preferences to be injected
     */
    private JMadPreferences preferences;

    /**
     * the file util to be injected
     */
    private TempFileUtil fileUtil;

    /**
     * the class which takes care of the madx-binary
     */
    private MadxBin madxBin;

    /**
     * does not delete the outputFile after executing a command/task. Keeping the output-file is especially useful for
     * debugging.
     */
    private boolean keepOutputFile = true;

    /**
     * if true, then the temp dirs are deleted when cleaning up, if false they are kept. If this is null (default) then
     * the settings are taken from the preferences.
     */
    private Boolean cleanupDirs = null;

    /**
     * the listeners to the kernel
     */
    private final List<JMadKernelListener> listeners = new ArrayList<>();

    private final ExecutorService logFileWriteExecutor = Executors.newCachedThreadPool();

    @Override
    public void start() throws JMadException {

        checkState(fileUtil != null, "fileUtil not injected. Fix Spring configuration");
        checkState(madxBin != null, "madxBin not injected. Fix Spring configuration");
        /*
         * First we have to get temp-files related to this kernel
         */
        readyFile = fileUtil.getOutputFile(this, FILENAME_READY);
        resultFile = fileUtil.getOutputFile(this, FILENAME_RESULT);
        madxInputLogFile = fileUtil.getOutputFile(this, FILENAME_LOG_IN);
        madxOutputLogFile = fileUtil.getOutputFile(this, FILENAME_LOG_OUT);
        madxErrorLogFile = fileUtil.getOutputFile(this, FILENAME_LOG_ERROR);

        deleteReadyFile();

        madxInputLogFile.delete();

        try {
            process = madxBin.execute();
            input = new PrintWriter(process.getOutputStream());
            inputLogWriter = new BufferedWriter(new FileWriter(madxInputLogFile));
            logFileWriteExecutor.submit(() -> Files.copy(process.getInputStream(), madxOutputLogFile.toPath()));
            logFileWriteExecutor.submit(() -> Files.copy(process.getErrorStream(), madxErrorLogFile.toPath()));
            fireStartedKernel();
        } catch (IOException e) {
            throw new JMadException("Error while executing madx.", e);
        }
    }

    @Override
    public int stop() throws JMadException {
        int exitValue = 0;
        writeCommand(CMD_STOP);
        try {
            if (timeout == null) {
                LOGGER.debug("No timeout set. Waiting until madx-process terminates.");
                exitValue = process.waitFor();
            } else {
                ProcessTerminationMonitor processTerminationMonitor = new ProcessTerminationMonitor(process);
                processTerminationMonitor.start();
                processTerminationMonitor.join(timeout);
                if (processTerminationMonitor.isAlive()) {
                    processTerminationMonitor.interrupt();
                    process.destroy();
                    exitValue = EXIT_VALUE_DESTROYED;
                    LOGGER.warn("Waiting for terminating madx timed out! (timeout={} ms)", timeout);
                } else {
                    exitValue = process.exitValue();
                }
            }

            if (exitValue == 0) {
                LOGGER.debug("madx terminated correctly with exit-value {}.", exitValue);
            } else if (exitValue == EXIT_VALUE_DESTROYED) {
                LOGGER.debug("Tried to destroy madx-process. -> set exitValue to {}", exitValue);
            } else {
                LOGGER.warn("madx terminated with exit-value {}.", exitValue);
            }

            deleteReadyFile();
            closeInputLogger();
        } catch (InterruptedException e) {
            throw new JMadException("Error while trying to stop MadX", e);
        }

        /* delete the dir corresponding to the kernel. */
        if (isCleanupDirs() && (fileUtil != null)) {
            fileUtil.cleanup(this);
        }

        fireStoppedKernel();
        return exitValue;
    }

    private void deleteReadyFile() {
        readyFile.delete();
    }

    private void closeInputLogger() {
        try {
            inputLogWriter.flush();
            inputLogWriter.close();
        } catch (IOException e) {
            LOGGER.error("Error while flushing input logger.", e);
        }
    }

    @Override
    public Result execute(JMadExecutable executable) throws JMadException {
        resultFile.delete();
        executable.setOutputFile(resultFile);

        /* execute the commands and wait. */
        writeCommand(executable.compose());
        waitUntilReady();

        /* parse result */
        Result result = null;
        if ((executable.getResultType() != null) && (ResultType.NO_RESULT != executable.getResultType())) {
            LOGGER.debug("parsing madx output-file ({})", resultFile.getAbsolutePath());
            try {
                if (ResultType.TFS_RESULT == executable.getResultType()) {
                    TfsFileParser parser = new TfsFileParser(resultFile);
                    parser.parse();
                    result = parser.getResult();
                } else if (ResultType.VALUES_RESULT == executable.getResultType()) {
                    StrengthFileParser parser = new StrengthFileParser(resultFile);
                    parser.parse(false);
                    result = parser.getResult();
                } else if (ResultType.MATCH_RESULT == executable.getResultType()) {
                    MatchOutputParser parser = new MatchOutputParser(executable.getOutputFile());
                    parser.parse();
                    result = parser.getResult();
                } else if (ResultType.TRACK_RESULT == executable.getResultType()) {
                    TrackOutputParser parser = new TrackOutputParser(executable.getOutputFile());
                    parser.parse();
                    result = parser.getResult();
                } else if (ResultType.DYNAP_RESULT == executable.getResultType()) {
                    DynapOutputParser parser = new DynapOutputParser(executable.getOutputFile());
                    parser.parse();
                    result = parser.getResult();
                }
            } catch (Exception e) {
                throw new JMadException("File '" + resultFile.getAbsolutePath() + "' could not be parsed."
                        + "\nProbably madx did not produce it?" + "\n\n" + madxOutputMessage());
            }

            if (!this.keepOutputFile) {
                if (!resultFile.delete()) {
                    throw new JMadException("Could not delete result file '" + resultFile.getAbsolutePath());
                }
                LOGGER.debug("deleted madx output file ({})", resultFile.getAbsolutePath());
            }
        }

        return result;
    }

    /**
     * writes the command(s) as String to MadX-input. This method does not wait for the end of the execution and does
     * not return any result. Use with care!
     *
     * @param command the command to be executed by madx
     * @throws JMadException
     */
    /* package visibility for testing! */
    void writeCommand(String command) throws JMadException { // NOPMD by kaifox on 6/25/10 4:01 PM
        String commandString = command + "\n";
        if (!isMadxRunning()) {
            throw new JMadException("MadX is not running -> cannot write commands.");
        }

        LOGGER.debug("writing command(s) to madx:\n{}", commandString);
        input.println(commandString);
        input.flush();

        /* also log in separate file for simple executing in madx */
        try {
            inputLogWriter.write(commandString);
            inputLogWriter.flush();
        } catch (IOException e) {
            LOGGER.warn("Error while logging commands!", e);
        }
    }

    @Override
    public boolean isMadxRunning() {
        return ProcTools.isRunning(process);
    }

    /**
     * writes a file through madx and waits until it exists or reaching timeout (if set).
     *
     * @throws JMadException
     * @see #getTimeout()
     * @see #setTimeout(Long)
     */
    /* package visibility for testing! */
    void waitUntilReady() throws JMadException {
        if (!isMadxRunning()) {
            throw new JMadException("MadX is not running!");
        }

        writeCommand("\nsystem, \"echo > " + readyFile.getAbsolutePath() + "\"; // wait until ready\n");

        /* wait for the file, which tells us, that madx finished */
        FileMonitor fileMonitor = new FileMonitor(readyFile, process);

        boolean fileCreated = false;
        try {
            fileCreated = fileMonitor.waitForFile(timeout);
        } catch (ProcessTerminatedUnexpectedlyException e) {
            closeInputLogger();
            throwTerminatedException(e);
        }

        if (!fileCreated) {
            throw new WaitForMadxTimedOutException("madx command timed out! (timeout=" + timeout + "ms).");
        }

        deleteReadyFileWithRetries();
    }

    private void throwTerminatedException(ProcessTerminatedUnexpectedlyException e) throws MadxTerminatedException {
        throw new MadxTerminatedException("Madx terminated unexpectedly.\n\n" + madxOutputMessage(), e);
    }

    private String madxOutputMessage() {
        return fileSnippet("output", madxOutputLogFile, MAX_REPORTED_OUTPUT_LINES) //
                + fileSnippet("error output", madxErrorLogFile, MAX_REPORTED_ERROR_LINES) //
                + "\nFull MadX Input Log: '" + madxInputLogFile.getAbsolutePath() + "'" //
                + "\nFull MadX Output Log: '" + madxOutputLogFile.getAbsolutePath() + "'" //
                + "\nFull MadX Error Log: '" + madxErrorLogFile.getAbsolutePath() + "'\n";
    }

    private String fileSnippet(String fileQualifier, File file, int maxLines) {
        List<String> lastMadxErrorLines = FileUtil.tail(file, maxLines);
        return "MadX " + fileQualifier + "(Max last " + maxLines + " lines):\n---\n'" + StringUtil
                .join(lastMadxErrorLines, "\n") + "'.\n---\n";
    }

    private void deleteReadyFileWithRetries() throws JMadException {
        if (!readyFile.delete()) {
            boolean deleted = false;
            for (int i = 0; i < RETRY_ATTEMPTS; i++) {
                LOGGER.debug("deletion of file '" + readyFile.getAbsolutePath() + "' failed. Retrying again in "
                        + RETRY_DELAY_IN_MILLISECONDS + "ms");
                try {
                    Thread.sleep(RETRY_DELAY_IN_MILLISECONDS);
                } catch (InterruptedException e) {
                    LOGGER.error("Error while waiting for retry ...", e);
                }
                if (readyFile.delete()) {
                    deleted = true;
                    break;
                }
            }
            if (!deleted) {
                throw new JMadException("error while deleting file '" + readyFile.getAbsolutePath() + "'");
            }
        }
    }

    /**
     * When the class is destroyed it takes care, that MadX is closed in a proper way.
     *
     * @throws Throwable if the finalization fails
     */
    @Override
    protected void finalize() throws Throwable {
        if (isMadxRunning()) {
            LOGGER.warn("Madx is still running! - trying to stop...");
            try {
                stop();
            } catch (JMadException e) {
                LOGGER.error("Error while trying to stop MadX.", e);
            }
        }
        super.finalize();
    }

    @Override
    public void addListener(JMadKernelListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(JMadKernelListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * notifies the listeners, that the kernel has started
     */
    private void fireStartedKernel() {
        for (JMadKernelListener listener : this.listeners) {
            listener.startedKernel(this.process);
        }
    }

    /**
     * notifies all listeners, that the kernel has stopped.
     */
    private void fireStoppedKernel() {
        for (JMadKernelListener listener : this.listeners) {
            listener.stoppedKernel();
        }
    }

    //
    // Methods which allow special configuration
    //

    /**
     * @return the actual set timeout
     */
    @Override
    public Long getTimeout() {
        return timeout;
    }

    /**
     * sets the actual timeout in ms. If null then the kernel waits forever for Madx.
     *
     * @param timeout the timeout to set
     */
    @Override
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    /**
     * sets the flag, if the output-file shall be kept or deleted after task/command execution.
     *
     * @param keepOutputFile true if the output-file shall be kept, false otherwise
     */
    @Override
    public void setKeepOutputFile(boolean keepOutputFile) {
        this.keepOutputFile = keepOutputFile;
    }

    /**
     * @return true, if the outputfile shall be kept or false if it shall be deleted after command/task execution.
     */
    @Override
    public boolean isKeepOutputFile() {
        return keepOutputFile;
    }

    @Override
    public void setCleanupDirs(boolean cleanupDirs) {
        this.cleanupDirs = cleanupDirs;
    }

    @Override
    public boolean isCleanupDirs() {
        if (this.cleanupDirs != null) {
            return cleanupDirs;
        }
        return preferences.isCleanupKernelFiles();
    }

    @Override
    public File getOutputFile() {
        return this.resultFile;
    }

    public void setPreferences(JMadPreferences preferences) {
        this.preferences = preferences;
    }

    public void setFileUtil(TempFileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public void setMadxBin(MadxBin madxBin) {
        this.madxBin = madxBin;
    }
}
