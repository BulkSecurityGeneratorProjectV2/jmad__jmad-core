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

package cern.accsoft.steering.jmad.kernel.task;

import java.util.List;

import cern.accsoft.steering.jmad.kernel.AbstractJMadExecutable;
import cern.accsoft.steering.jmad.kernel.cmd.Command;

/**
 * A MadX task is a combination of simple madx commands.
 * 
 * @author kfuchsbe
 */
public abstract class AbstractTask extends AbstractJMadExecutable {

    /**
     * has to be implemented in subclass in order to retrieve the commands
     * 
     * @return the ArrayList of commands
     */
    protected abstract List<Command> getCommands();

    @Override
    public final String compose() {
        List<Command> commands = getCommands();

        StringBuffer taskString = new StringBuffer("\n" + getHeader() + "\n"); // add
        // header,
        // just
        // for
        // readability;
        for (int i = 0; i < commands.size(); i++) {
            Command command = commands.get(i);
            command.setOutputFile(getOutputFile());
            taskString.append(command.compose());
            taskString.append('\n');
        }
        taskString.append(getFooter());
        return taskString.toString();
    }

    private final String getHeader() {
        return "// ***** BEGIN autogenerated task: " + this.getClass().getName() + " *****";
    }

    private final String getFooter() {
        return "// ***** END autogenerated task: " + this.getClass().getName() + " *****";
    }

}
