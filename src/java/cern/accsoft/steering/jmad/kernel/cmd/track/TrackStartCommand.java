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

package cern.accsoft.steering.jmad.kernel.cmd.track;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.jmad.domain.track.RelativeParticleCoordinate;
import cern.accsoft.steering.jmad.kernel.cmd.AbstractCommand;
import cern.accsoft.steering.jmad.kernel.cmd.param.GenericParameter;
import cern.accsoft.steering.jmad.kernel.cmd.param.Parameter;

/**
 * command START, x= double, px= double, y= double, py= double, t= double, pt= double;
 * 
 * @author xbuffat
 */

public class TrackStartCommand extends AbstractCommand {

    private static final String CMD_NAME = "start";

    private final RelativeParticleCoordinate relatvieParticleCoordinate;

    public TrackStartCommand(RelativeParticleCoordinate relativeParticleCoordinate) {
        this.relatvieParticleCoordinate = relativeParticleCoordinate;
    }

    @Override
    public String getName() {
        return TrackStartCommand.CMD_NAME;
    }

    @Override
    public List<Parameter> getParameters() {
        List<Parameter> parameters = new ArrayList<>();
        if(this.relatvieParticleCoordinate.isActionAngle())
        {
            parameters.add(new GenericParameter<>("fx", this.relatvieParticleCoordinate.getXRelatviePosition()));
            parameters.add(new GenericParameter<>("phix", this.relatvieParticleCoordinate.getXRelativeMomentum()));
            parameters.add(new GenericParameter<>("fy", this.relatvieParticleCoordinate.getYRelativePosition()));
            parameters.add(new GenericParameter<>("phiy", this.relatvieParticleCoordinate.getYRelatvieMomentum()));
            parameters.add(new GenericParameter<>("ft", this.relatvieParticleCoordinate.getRelativeTimeDifference()));
            parameters.add(new GenericParameter<>("phit", this.relatvieParticleCoordinate.getRelativeTimeDifference()));
        }
        else
        {
            parameters.add(new GenericParameter<>("x", this.relatvieParticleCoordinate.getXRelatviePosition()));
            parameters.add(new GenericParameter<>("px", this.relatvieParticleCoordinate.getXRelativeMomentum()));
            parameters.add(new GenericParameter<>("y", this.relatvieParticleCoordinate.getYRelativePosition()));
            parameters.add(new GenericParameter<>("py", this.relatvieParticleCoordinate.getYRelatvieMomentum()));
            parameters.add(new GenericParameter<>("t", this.relatvieParticleCoordinate.getRelativeTimeDifference()));
            parameters.add(new GenericParameter<>("pt", this.relatvieParticleCoordinate.getRelativeTimeDifference()));
        }

        return parameters;
    }

}
