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

package cern.accsoft.steering.jmad.domain.result.tfs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cern.accsoft.steering.jmad.JMadConstants;
import cern.accsoft.steering.jmad.domain.var.GlobalVariable;
import cern.accsoft.steering.jmad.util.MadxVarType;

import static cern.accsoft.steering.jmad.domain.result.tfs.TfsDoubles.parseTfsDouble;

public class TfsSummaryImpl implements TfsSummary {

    /** the logger for this class */
    private static final Logger LOGGER = LoggerFactory.getLogger(TfsSummaryImpl.class);

    /** All the values in their String representation */
    private final Map<String, String> stringValues = new HashMap<String, String>();

    /** The valuetypes for each key */
    private final Map<String, MadxVarType> valueTypes = new HashMap<String, MadxVarType>();

    /** The calculated double values for those variables who are of type double. */
    private final Map<String, Double> doubleValues = new HashMap<String, Double>();

    /**
     * adds a value to the summary
     * 
     * @param madxName the key of the value
     * @param value the value as string
     * @param varType the type of the value
     */
    public void addValue(String madxName, String value, MadxVarType varType) {
        String key = unifyKey(madxName);
        this.stringValues.put(key, value);
        this.valueTypes.put(key, varType);
    }

    /**
     * converts all the values that have VarType Double to double values, so that they can be retrieved quickly
     * afterwards.
     * 
     * @throws TfsResultException if the conversion fails
     */
    public void convert() throws TfsResultException {
        this.doubleValues.clear();
        for (String key : getKeys()) {
            if (MadxVarType.DOUBLE.equals(getVarType(key))) {
                String strValue = getStringValue(key);

                if (strValue == null) {
                    throw new TfsResultException("Data contains no value for key '" + key + "'.");
                }

                try {
                    this.doubleValues.put(unifyKey(key), parseTfsDouble(strValue));
                } catch (NumberFormatException e) {
                    throw new TfsResultException("Error while converting value '" + strValue + "' to Double", e);
                }
            }
        }
    }

    /**
     * ensures that all keys are the same case.
     * 
     * @param key the key to unify
     * @return the converted key.
     */
    private static final String unifyKey(String key) {
        return key.toUpperCase(JMadConstants.DEFAULT_LOCALE);
    }

    @Override
    public Double getDoubleValue(String key) {
        Double value = this.doubleValues.get(unifyKey(key));
        if (value == null) {
            LOGGER.warn("TwissSummary seems not to contain a double value for key '" + key + "'");
        }
        return value;
    }

    @Override
    public Double getDoubleValue(GlobalVariable variable) {
        return getDoubleValue(variable.getMadxName());
    }

    @Override
    public Collection<String> getKeys() {
        return this.stringValues.keySet();
    }

    @Override
    public String getStringValue(String key) {
        String value = this.stringValues.get(unifyKey(key));
        if (value == null) {
            LOGGER.warn("TwissSummary seems not to contain a string value for key '" + key + "'");
        }
        return value;
    }

    @Override
    public String getStringValue(GlobalVariable variable) {
        return getStringValue(variable.getMadxName());
    }

    @Override
    public MadxVarType getVarType(String key) {
        MadxVarType value = this.valueTypes.get(unifyKey(key));
        if (value == null) {
            LOGGER.warn("TwissSummary seems not to contain a value type for key '" + key + "'");
        }
        return value;
    }

    @Override
    public MadxVarType getVarType(GlobalVariable variable) {
        return getVarType(variable.getMadxName());
    }

    @Override
    public String toString() {
        return "TfsSummaryImpl [stringValues=" + stringValues + ", valueTypes=" + valueTypes + ", doubleValues="
                + doubleValues + "]";
    }
}
