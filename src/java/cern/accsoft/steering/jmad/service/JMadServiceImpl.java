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
package cern.accsoft.steering.jmad.service;

import org.slf4j.Logger;

import cern.accsoft.steering.jmad.factory.JMadModelFactory;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.jmad.model.manage.JMadModelManager;
import cern.accsoft.steering.jmad.modeldefs.JMadModelDefinitionManager;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionExporter;
import cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionImporter;
import cern.accsoft.steering.jmad.util.JMadPreferences;
import org.slf4j.LoggerFactory;

/**
 * The default implementation of the jmad-service. This class is configured by spring.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class JMadServiceImpl implements JMadService {

    /** The logger for the class */
    private static final Logger LOGGER = LoggerFactory.getLogger(JMadServiceImpl.class);

    /** The preferences, injected by spring */
    private JMadPreferences preferences;

    /** The model factory to use */
    private JMadModelFactory modelFactory;

    /** The model definition manager to use */
    private JMadModelDefinitionManager modelDefinitionManager;

    /** The model definition exporter */
    private JMadModelDefinitionExporter modelDefinitionExporter;

    /** The class to import model definitions */
    private JMadModelDefinitionImporter modelDefinitionImporter;

    /** The manager for the models */
    private JMadModelManager modelManager;

    /*
     * methods of interface JMadService
     */

    @Override
    public JMadModel createModel(JMadModelDefinition definition) {
        JMadModel model = getModelFactory().createModel(definition);
        if ((model != null) && (getModelManager() != null)) {
            getModelManager().addModel(model);
        }
        return model;
    }

    @Override
    public JMadPreferences getPreferences() {
        if (this.preferences == null) {
            LOGGER.warn("Preferences not set. Maybe config error?");
        }
        return this.preferences;
    }

    @Override
    public JMadModelDefinitionManager getModelDefinitionManager() {
        if (this.modelDefinitionManager == null) {
            LOGGER.warn("ModelDefinitionManager not set. Maybe config error.");
        }
        return modelDefinitionManager;
    }

    /*
     * Getters and setters used for spring
     */

    public void setPreferences(JMadPreferences preferences) {
        this.preferences = preferences;
    }

    public void setModelFactory(JMadModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    private JMadModelFactory getModelFactory() {
        if (this.modelFactory == null) {
            LOGGER.warn("ModelFactory not set. Maybe config error.");
        }
        return modelFactory;
    }

    public void setModelDefinitionManager(JMadModelDefinitionManager modelDefinitionManager) {
        this.modelDefinitionManager = modelDefinitionManager;
    }

    @Override
    public JMadModelManager getModelManager() {
        if (this.modelManager == null) {
            LOGGER.warn("ModelManager not set. Maybe config error.");
        }
        return this.modelManager;
    }

    public void setModelManager(JMadModelManager modelManager) {
        this.modelManager = modelManager;
    }

    @Override
    public JMadModelDefinitionExporter getModelDefinitionExporter() {
        if (this.modelDefinitionExporter == null) {
            LOGGER.warn("ModelDefinitionExporter not set. Maybe config error.");
        }
        return this.modelDefinitionExporter;
    }

    public void setModelDefinitionExporter(JMadModelDefinitionExporter modelDefinitionExporter) {
        this.modelDefinitionExporter = modelDefinitionExporter;
    }

    public void setModelDefinitionImporter(JMadModelDefinitionImporter modelDefinitionImporter) {
        this.modelDefinitionImporter = modelDefinitionImporter;
    }

    @Override
    public JMadModelDefinitionImporter getModelDefinitionImporter() {
        return modelDefinitionImporter;
    }

    @Override
    public void deleteModel(JMadModel model) {
        getModelManager().removeModel(model);
    }
}
