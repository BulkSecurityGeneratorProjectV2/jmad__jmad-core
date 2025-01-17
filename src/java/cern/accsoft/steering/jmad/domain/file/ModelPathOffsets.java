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
package cern.accsoft.steering.jmad.domain.file;

/**
 * interface for defining the path offsets within repository and resources. this is valid for one model definition.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public interface ModelPathOffsets {

    /**
     * this method must return the offset within the resource-path-tree.
     * 
     * @return the offset
     */
    String getResourceOffset();

    /**
     * this method must return the offset within the repository.
     * 
     * @return the offset
     */
    String getRepositoryOffset();

    /**
     * this method must return the prefix of the resource-path-tree.
     *
     * @return the prefix
     */
    String getResourcePrefix();

    /**
     * this method must return the prefix of the repository path tree.
     *
     * @return the offset
     */
    String getRepositoryPrefix();

}
