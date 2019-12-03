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

package cern.accsoft.steering.jmad.domain.var.enums;

import java.util.Set;

import cern.accsoft.steering.jmad.domain.var.TwissVariable;
import cern.accsoft.steering.jmad.domain.var.VariableUtil;
import cern.accsoft.steering.jmad.util.MadxVarType;

public enum PtcTwissVariable implements TwissVariable {

    // ptc-variables
    PTC_DISP_X("disp1"), PTC_DISP2("disp2"), PTC_DISP_Y("disp3"), PTC_DISP4("disp4"), //
    PTC_BETA11("beta11"), PTC_BETA12("beta12"), PTC_BETA13("beta13"), //
    PTC_BETA21("beta21"), PTC_BETA22("beta22"), PTC_BETA23("beta23"), //
    PTC_BETA31("beta31"), PTC_BETA32("beta32"), PTC_BETA33("beta33"), //
    PTC_MU_X("mu1"), PTC_MU_Y("mu2"), PTC_MU_Z("mu3"), //
    PTC_ALFA_11("alfa11"), PTC_ALFA_12("alfa12"), PTC_ALFA_13("alfa13"), //
    PTC_ALFA_21("alfa21"), PTC_ALFA_22("alfa22"), PTC_ALFA_23("alfa23"), //
    PTC_ALFA_31("alfa31"), PTC_ALFA_32("alfa32"), PTC_ALFA_33("alfa23"), //
    PTC_DISP_XP("disp1p"), PTC_DISP_2P("disp2p"), PTC_DISP_YP("disp3p"), PTC_DISP_4P("disp4p"), //
    PTC_DISP_XP2("disp1p2"), PTC_DISP_2P2("disp2p2"), PTC_DISP_YP2("disp3p2"), PTC_DISP_4P2("disp4p2"), //
    PTC_DISP_XP3("disp1p3"), PTC_DISP_2P3("disp2p3"), PTC_DISP_YP3("disp3p3"), PTC_DISP_4P3("disp4p3"), //

    // if something wrong:
    UNKNOWN("jmad_unknown", MadxVarType.UNKNOWN);

    private String name;
    private String unit = null;
    private MadxVarType type;

    private PtcTwissVariable(String tag, MadxVarType type) {
        this.name = tag;
        this.type = type;
    }

    private PtcTwissVariable(String tag) {
        this(tag, MadxVarType.DOUBLE);
    }

    private PtcTwissVariable(String tag, String unit) {
        this(tag, MadxVarType.DOUBLE);
        this.unit = unit;
    }

    @Override
    public String getMadxName() {
        return name;
    }

    @Override
    public MadxVarType getVarType() {
        return type;
    }

    /**
     * Determine the correct Value of ResultVariable for a given tag.
     *
     * @param madxName the tag for which to get the VarType - Value.
     * @return The VarType corresponding to the given tag.
     */
    public static final PtcTwissVariable fromMadxName(String madxName) {
        return VariableUtil.findFromMadxName(PtcTwissVariable.class, madxName, PtcTwissVariable.UNKNOWN);
    }

    /**
     * returns a set of variables of a given type.
     *
     * @param type which variables to retrieve
     * @return the variables.
     */
    public static final Set<PtcTwissVariable> allOfType(MadxVarType type) {
        return VariableUtil.findFromVarType(PtcTwissVariable.class, type);
    }

    @Override
    public boolean isApertureVariable() {
        return false;
    }

    @Override
    public String getUnit() {
        return this.unit;
    }

    @Override
    public String getName() {
        return getMadxName();
    }

    @Override
    public String toString() {
        return VariableUtil.toString(this);
    }

    @Override
    public Class<?> getValueClass() {
        return getVarType().getValueClass();
    }
}
