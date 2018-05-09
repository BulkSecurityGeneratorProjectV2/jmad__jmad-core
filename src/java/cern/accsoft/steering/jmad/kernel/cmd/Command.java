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

package cern.accsoft.steering.jmad.kernel.cmd;

import java.util.List;

import cern.accsoft.steering.jmad.kernel.JMadExecutable;
import cern.accsoft.steering.jmad.kernel.cmd.param.Parameter;

public interface Command extends JMadExecutable {

    /**
     * has to be implemented in order to return the name of the command (keyword);
     * 
     * @return the name of the command (MadX keyword)
     */
    public abstract String getName();

    /**
     * has to be implemented in subclass in order to return the ArrayList of parameters of the command.
     * 
     * @return all the parameter for the command
     */
    public abstract List<Parameter> getParameters();

    /**
     * composes the final command and returns it as String.
     * 
     * @return the total Command-String.
     */
    public abstract String compose();

}