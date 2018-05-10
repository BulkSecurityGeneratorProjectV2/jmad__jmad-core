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
package cern.accsoft.steering.jmad.modeldefs;

import static cern.accsoft.steering.jmad.modeldefs.io.impl.ModelDefinitionUtil.BASE_CLASS;
import static cern.accsoft.steering.jmad.modeldefs.io.impl.ModelDefinitionUtil.PACKAGE_OFFSET;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinitionImpl;
import cern.accsoft.steering.jmad.modeldefs.domain.SourceInformation.SourceType;
import cern.accsoft.steering.jmad.modeldefs.domain.SourceInformationImpl;
import cern.accsoft.steering.jmad.modeldefs.io.ModelDefinitionPersistenceService;
import cern.accsoft.steering.jmad.modeldefs.io.impl.ModelDefinitionUtil;
import cern.accsoft.steering.jmad.util.ResourceUtil;
import cern.accsoft.steering.jmad.util.xml.PersistenceServiceException;
import org.slf4j.LoggerFactory;

/**
 * This class is an implementation of a {@link ModelDefinitionFinder} which searches in the classpath for all available
 * model definitions.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class ClassPathModelDefinitionFinder implements ModelDefinitionFinder {

    /** The logger for the class */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathModelDefinitionFinder.class);

    /** the persistence service to use to load the definitions */
    private ModelDefinitionPersistenceService persistenceService;

    @Override
    public List<JMadModelDefinition> findAllModelDefinitions() {
        Collection<String> definitionFileNames = findAllModelDefinitionFiles();

        List<JMadModelDefinition> modelDefinitions = new ArrayList<JMadModelDefinition>();

        for (String resourceName : definitionFileNames) {
            String resourcePath = PACKAGE_OFFSET + "/" + resourceName;
            InputStream inputStream = BASE_CLASS.getResourceAsStream(resourcePath);
            String parentPath = ResourceUtil.packageToPath(BASE_CLASS.getPackage().getName()) + "/" + PACKAGE_OFFSET;

            JMadModelDefinition modelDefinition = null;
            try {
                modelDefinition = getPersistenceService().load(inputStream);
            } catch (PersistenceServiceException e) {
                LOGGER.error("could not load model definition from resource '" + resourceName + "' relative to class "
                        + ModelDefinitionUtil.BASE_CLASS.getCanonicalName(), e);
            }
            if (modelDefinition instanceof JMadModelDefinitionImpl) {
                ((JMadModelDefinitionImpl) modelDefinition)
                        .setSourceInformation(new SourceInformationImpl(SourceType.JAR, null, resourceName, parentPath));
            }
            if (modelDefinition != null) {
                modelDefinitions.add(modelDefinition);
            }
        }

        return modelDefinitions;
    }

    private Collection<String> findAllModelDefinitionFiles() {
        String packageName = ModelDefinitionUtil.BASE_CLASS.getPackage().getName() + "."
                + ModelDefinitionUtil.PACKAGE_OFFSET;

        /*
         * the names directly in the package.
         */
        List<String> definitionFileNames = filterForXmlFiles(packageName, ResourceUtil.getResourceNames(packageName));
        return definitionFileNames;
    }

    private List<String> filterForXmlFiles(String packageName, Collection<String> packageFileNames) {
        List<String> definitionFileNames = new ArrayList<String>();
        for (String fileName : packageFileNames) {
            if (ModelDefinitionUtil.isXmlFileName(fileName)) {
                definitionFileNames.add(fileName);
                LOGGER.debug("Found model definition file '" + fileName + "' in package '" + packageName + "'.");
            }
        }
        return definitionFileNames;
    }

    public void setPersistenceService(ModelDefinitionPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    private ModelDefinitionPersistenceService getPersistenceService() {
        return persistenceService;
    }

}
