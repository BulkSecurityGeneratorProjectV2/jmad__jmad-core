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

/**
 * 
 */
package cern.accsoft.steering.jmad.kernel.cmd.match;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.jmad.domain.result.match.input.MadxVaryParameter;
import cern.accsoft.steering.jmad.kernel.cmd.AbstractCommand;
import cern.accsoft.steering.jmad.kernel.cmd.param.GenericParameter;
import cern.accsoft.steering.jmad.kernel.cmd.param.Parameter;

/**
 * @author muellerg
 */
public class MatchVaryParameterCommand extends AbstractCommand {
    private static final String CMD_NAME = "vary";

    @Override
    public String getName() {
        return CMD_NAME;
    }

    private final MadxVaryParameter actVaryParameter;

    public MatchVaryParameterCommand(MadxVaryParameter varyParameter) {
        super();
        this.actVaryParameter = varyParameter;
    }

    @Override
    public List<Parameter> getParameters() {
        List<Parameter> params = new ArrayList<Parameter>();

        params.add(new GenericParameter<String>("name", this.actVaryParameter.getName()));
        params.add(new GenericParameter<Double>("step", this.actVaryParameter.getStep()));
        params.add(new GenericParameter<Double>("lower", this.actVaryParameter.getLower()));
        params.add(new GenericParameter<Double>("upper", this.actVaryParameter.getUpper()));
        return params;
    }
}
