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

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.jmad.domain.beam.Beam;
import cern.accsoft.steering.jmad.kernel.cmd.param.GenericParameter;
import cern.accsoft.steering.jmad.kernel.cmd.param.Parameter;

/**
 * represents the madX 'beam' command BEAM, PARTICLE=name,MASS=real,CHARGE=real, ENERGY=real,PC=real,GAMMA=real,
 * EX=real,EXN=real,EY=real,EYN=real, ET=real,SIGT=real,SIGE=real, KBUNCH=integer,NPART=real,BCURRENT=real,
 * BUNCHED=logical,RADIATE=logical,BV=integer,SEQUENCE=name;
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class BeamCommand extends AbstractCommand {
    private static final String CMD_NAME = "beam";

    private final Beam beam;

    public BeamCommand(Beam beam) {
        this.beam = beam;
    }

    @Override
    public String getName() {
        return CMD_NAME;
    }

    @Override
    public List<Parameter> getParameters() {
        ArrayList<Parameter> parameters = new ArrayList<>();

        /*
         * define the mapping of the member-fields to the parameters.
         */
        /*
         * for particle, there may be set throug enum or through a arbitrary string. DANGER: not checked, if boths are
         * set!
         */
        parameters.add(new GenericParameter<>("particle", beam.getParticle()));
        parameters.add(new GenericParameter<>("particle", beam.getParticleName()));

        parameters.add(new GenericParameter<>("mass", beam.getMass()));
        parameters.add(new GenericParameter<>("charge", beam.getCharge()));
        parameters.add(new GenericParameter<>("energy", beam.getEnergy()));
        parameters.add(new GenericParameter<>("pc", beam.getMomentum()));
        parameters.add(new GenericParameter<>("gamma", beam.getGamma()));
        parameters.add(new GenericParameter<>("ex", beam.getHorizontalEmittance()));
        parameters.add(new GenericParameter<>("ey", beam.getVerticalEmittance()));
        parameters.add(new GenericParameter<>("et", beam.getLongitudinalEmittance()));
        parameters.add(new GenericParameter<>("exn", beam.getNormalisedHorizontalEmittance()));
        parameters.add(new GenericParameter<>("eyn", beam.getNormalisedVerticalEmittance()));
        parameters.add(new GenericParameter<>("sigt", beam.getBunchLength()));
        parameters.add(new GenericParameter<>("sige", beam.getRelativeEnergySpread()));
        parameters.add(new GenericParameter<>("kbunch", beam.getBunchNumber()));
        parameters.add(new GenericParameter<>("npart", beam.getParticleNumber()));
        parameters.add(new GenericParameter<>("bcurrent", beam.getBunchCurrent()));
        parameters.add(new GenericParameter<>("bunched", beam.getBunched()));
        parameters.add(new GenericParameter<>("radiate", beam.getRadiate()));
        parameters.add(new GenericParameter<>("bv", beam.getDirection()));
        parameters.add(new GenericParameter<>("sequence", beam.getSequence()));

        return parameters;
    }

}
