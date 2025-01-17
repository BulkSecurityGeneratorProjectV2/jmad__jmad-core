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

import cern.accsoft.steering.jmad.domain.result.tfs.TfsResultRequest;
import cern.accsoft.steering.jmad.domain.twiss.TwissInitialConditions;
import cern.accsoft.steering.jmad.kernel.cmd.Command;
import cern.accsoft.steering.jmad.kernel.cmd.DeleteCommand;
import cern.accsoft.steering.jmad.kernel.cmd.TwissCommand;

import java.util.ArrayList;
import java.util.List;

import static cern.accsoft.steering.jmad.kernel.cmd.SelectCommand.SELECT_FLAG_TWISS;

public class RunTwiss extends AbstractResultSelectableTask {

    private TwissInitialConditions twiss = null;

    public RunTwiss(TwissInitialConditions twiss) {
        this(twiss, null);
    }

    public RunTwiss(TwissInitialConditions twissInitialConditions, TfsResultRequest resultRequest) {
        super(resultRequest);
        this.twiss = twissInitialConditions;
    }

    @Override
    protected List<Command> getCommands() {
        List<Command> commands = new ArrayList<>();
        commands.add(new DeleteCommand("twiss"));
        commands.add(new DeleteCommand("summ"));
        commands.addAll(createSelectCommands(SELECT_FLAG_TWISS));
        commands.add(new TwissCommand(twiss));
        return commands;
    }
}
