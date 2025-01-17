// @formatter:off
 /*******************************************************************************
 *
 * This file is part of JMad.
 * 
 * Copyright (c) 2008-2011, CERN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ******************************************************************************/
// @formatter:on

package cern.accsoft.steering.jmad.kernel;

import java.io.File;

import cern.accsoft.steering.jmad.JMadException;
import cern.accsoft.steering.jmad.domain.result.Result;

/**
 * this is the interface for central class which controls an instance of MadX. The kernel only is able to do the most
 * primitives tasks: start the MadX-process, stop it and send commands to it. Furthermore it takes care of the logging
 * of input and output of the MadX process. Listeners can also be attached, which then get notified, when the status of
 * the kernel changes.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public interface JMadKernel {

    /**
     * starts madx as a separate thread and configures the needed streams.
     * 
     * @throws JMadException if the starting of the MadX process fails
     */
    void start() throws JMadException;

    /**
     * stops the madx-thread
     * 
     * @return the exit-value of madx
     * @throws JMadException if the stopping of the MadX process fails
     */
    int stop() throws JMadException;

    /**
     * executes a Command or Task, waits for completion (our timeout if set), and returns result, if command/task
     * provides one.
     * 
     * @param executable the command or task to execute
     * @return the result, if available, otherwise null
     * @throws JMadException if the execution fails
     */
    Result execute(JMadExecutable executable) throws JMadException;

    /**
     * @return true, if madx was started before, false otherwise
     */
    boolean isMadxRunning();

    /**
     * @param listener the listener to add
     */
    void addListener(JMadKernelListener listener);

    /**
     * @param listener the listener to remove
     */
    void removeListener(JMadKernelListener listener);

    /**
     * @return the JMadKernel OutputFile
     */
    File getOutputFile();
}
