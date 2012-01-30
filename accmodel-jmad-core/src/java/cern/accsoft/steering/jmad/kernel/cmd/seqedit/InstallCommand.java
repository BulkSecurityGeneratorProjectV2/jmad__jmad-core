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

package cern.accsoft.steering.jmad.kernel.cmd.seqedit;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.jmad.domain.elem.Element;
import cern.accsoft.steering.jmad.domain.elem.Position;
import cern.accsoft.steering.jmad.domain.elem.RelativePosition;
import cern.accsoft.steering.jmad.kernel.cmd.AbstractCommand;
import cern.accsoft.steering.jmad.kernel.cmd.param.GenericParameter;
import cern.accsoft.steering.jmad.kernel.cmd.param.Parameter;

public class InstallCommand extends AbstractCommand {

    private static final String CMD_NAME = "install";

    private String elementName;
    private String elementClass;
    private Position elementPosition;

    public InstallCommand(Element element) {
        this.elementName = element.getName();
        this.elementClass = element.getMadxElementType().getMadxName();
        this.elementPosition = element.getPosition();
    }

    @Override
    public String getName() {
        return InstallCommand.CMD_NAME;
    }

    @Override
    public List<Parameter> getParameters() {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new GenericParameter<String>("element", this.elementName));
        parameters.add(new GenericParameter<String>("class", this.elementClass));
        parameters.add(new GenericParameter<Double>("at", this.elementPosition.getValue()));
        if (this.elementPosition instanceof RelativePosition) {
            parameters.add(new GenericParameter<String>("from", ((RelativePosition) this.elementPosition).getElement()));
        }

        return parameters;
    }

}
