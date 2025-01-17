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

package cern.accsoft.steering.jmad.modeldefs;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.io.impl.XmlModelDefinitionPersistenceService;

public class ClassPathModelDefinitionFinderTest {

    @Test
    public void testFindAllModelDefinitions() {

        ClassPathModelDefinitionFinder finder = new ClassPathModelDefinitionFinder();
        finder.setPersistenceService(new XmlModelDefinitionPersistenceService());

        List<JMadModelDefinition> modelDefinitions = finder.findAllModelDefinitions();

        assertThat(modelDefinitions.size()).describedAs("We should find at least one modelDefinition.")
                .isGreaterThan(0);

        Set<String> modelNames = modelDefinitions.stream().map(JMadModelDefinition::getName).collect(toSet());
        assertThat(modelNames).describedAs("The model definition should be on of the example model definitions")
                .contains("example_THIN", "example", "Example LEIREXTR");
    }

}
