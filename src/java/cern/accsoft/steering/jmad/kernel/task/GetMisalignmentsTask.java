/**
 * Copyright (c) 2014 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.kernel.task;

import cern.accsoft.steering.jmad.domain.result.ResultType;
import cern.accsoft.steering.jmad.kernel.cmd.Command;
import cern.accsoft.steering.jmad.kernel.cmd.EsaveCommand;
import cern.accsoft.steering.jmad.kernel.cmd.SelectCommand;

import java.util.ArrayList;
import java.util.List;

import static cern.accsoft.steering.jmad.kernel.cmd.SelectCommand.SELECT_FLAG_ERROR;

/**
 * This task is intended to call kernel in order to get actual machine imperfections.
 * 
 * @author agorzaws
 */
public class GetMisalignmentsTask extends AbstractTask {

    private static final String ALL_ELEMENTS_PATTERN = "";
    private final String patternToSelect;

    /**
     * @param patternToSelect
     */
    public GetMisalignmentsTask(String patternToSelect) {
        this.patternToSelect = patternToSelect;
    }

    public GetMisalignmentsTask() {
        patternToSelect = ALL_ELEMENTS_PATTERN;
    }

    @Override
    protected List<Command> getCommands() {

        SelectCommand selectCommand = new SelectCommand();
        selectCommand.setFlag(SELECT_FLAG_ERROR);
        if (!ALL_ELEMENTS_PATTERN.equals(patternToSelect)) {
            selectCommand.setPattern(patternToSelect);
        } else {
            selectCommand.setFull(true);
        }

        List<Command> commands = new ArrayList<Command>();
        commands.add(selectCommand);
        commands.add(new EsaveCommand(getOutputFile()));
        return commands;
    }

    @Override
    public ResultType getResultType() {
        if (getOutputFile() == null) {
            return super.getResultType();
        } else {
            return ResultType.TFS_RESULT;
        }
    }
}
