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

package cern.accsoft.steering.jmad.factory;

import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.jmad.model.JMadModelImpl;
import cern.accsoft.steering.jmad.model.KnobManager;
import cern.accsoft.steering.jmad.model.knob.custom.DeltaPKnob;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;

public abstract class JMadModelFactoryImpl implements JMadModelFactory {

    @Override
    public final JMadModel createModel(JMadModelDefinition modelDefinition) {
        JMadModelImpl model = createJMadModelImpl();
        model.setModelDefinition(modelDefinition);
        createDefaultKnobs(model);
        return model;
    }

    @Override
    public final void createDefaultKnobs(JMadModel model) {
        KnobManager knobManager = model.getKnobManager();
        knobManager.addCustomKnob(new DeltaPKnob(model));
    }

    /**
     * This method will be injected by spring in order to create a preconfigured model
     * 
     * @return the preinitialized instance of a model
     */
    protected abstract JMadModelImpl createJMadModelImpl();

}
