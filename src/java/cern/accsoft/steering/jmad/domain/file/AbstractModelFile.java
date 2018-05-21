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

package cern.accsoft.steering.jmad.domain.file;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class AbstractModelFile implements ModelFile {

    /** The model file location we use when the location is null after reading. */
    public static final ModelFileLocation DEFAULT_MODEL_FILE_LOCATION = ModelFileLocation.REPOSITORY;

    /** The location of the file, which is the repository by default. */
    @XStreamAlias("location")
    @XStreamAsAttribute
    private ModelFileLocation location = DEFAULT_MODEL_FILE_LOCATION;

    /** The relative path of the file */
    @XStreamAlias("path")
    @XStreamAsAttribute
    private String path = null;

    /**
     * the constructor with the name only. (location stays to default)
     * 
     * @param path the relative path of the file
     */
    public AbstractModelFile(String path) {
        this.path = path;
    }

    /**
     * The constructor with filename and location
     * 
     * @param path the relative path of the model file
     * @param location the location (repository or resources) where to search for the file
     */
    public AbstractModelFile(String path, ModelFileLocation location) {
        this(path);
        this.location = location;
    }

    @Override
    public ModelFileLocation getLocation() {
        return this.location;
    }

    @Override
    public String getName() {
        return this.path;
    }

    @Override
    public CallableModelFileImpl clone() throws CloneNotSupportedException {
        return (CallableModelFileImpl) super.clone();
    }

    /**
     * is called just before serialization. Removes some information so that the default values are not written to xml
     */
    protected void fillWriteReplace(AbstractModelFile writtenObj) {
        if (DEFAULT_MODEL_FILE_LOCATION == this.location) {
            writtenObj.location = null;
        }
    }

    /**
     * is called just after deserialization. It configures the object with default values, if none were stored in the
     * serialized version.
     */
    protected void abstractReadResolve() {
        if (location == null) {
            location = DEFAULT_MODEL_FILE_LOCATION;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractModelFile other = (AbstractModelFile) obj;
        if (location != other.location) {
            return false;
        }
        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }
        return true;
    }

}
