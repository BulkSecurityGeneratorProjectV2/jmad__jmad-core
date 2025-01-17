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

package cern.accsoft.steering.jmad.domain.result.match;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.jmad.domain.result.match.input.MadxVaryParameter;
import cern.accsoft.steering.jmad.domain.result.match.input.MatchConstraint;
import cern.accsoft.steering.jmad.domain.result.match.methods.MatchMethod;
import cern.accsoft.steering.jmad.domain.result.match.methods.MatchMethodLmdif;
import cern.accsoft.steering.jmad.domain.twiss.TwissInitialConditions;

public class MatchResultRequestImpl implements MatchResultRequest {

    private String sequenceName = null;
    private MatchMethod matchMethod = new MatchMethodLmdif();
    private final List<MadxVaryParameter> varyParameters = new ArrayList<MadxVaryParameter>();
    private final List<MatchConstraint> matchConstraints = new ArrayList<MatchConstraint>();

    private TwissInitialConditions initTwiss = null;
    private String saveBetaName = null;

    @Override
    public List<MadxVaryParameter> getMadxVaryParameters() {
        return this.varyParameters;
    }

    public void addMadxVaryParameter(MadxVaryParameter varyParameter) {
        this.varyParameters.add(varyParameter);
    }

    @Override
    public List<MatchConstraint> getMatchConstraints() {
        return this.matchConstraints;
    }

    public void addMatchConstraint(MatchConstraint matchConstraint) {
        this.matchConstraints.add(matchConstraint);
    }

    @Override
    public MatchMethod getMatchMethod() {
        return this.matchMethod;
    }

    public void setMatchMethod(MatchMethod matchMethod) {
        this.matchMethod = matchMethod;
    }

    @Override
    public String getSequenceName() {
        return this.sequenceName;
    }

    @Override
    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public void setInitTwiss(TwissInitialConditions initTwiss) {
        /* TODO: Set non available values for matching to zero if necessary */
        this.initTwiss = initTwiss;
    }

    @Override
    public TwissInitialConditions getInitialOpticsValues() {
        return this.initTwiss;
    }

    public void setSaveBetaName(String saveBetaName) {
        this.saveBetaName = saveBetaName;
    }

    @Override
    public String getSaveBetaName() {
        return this.saveBetaName;
    }
}
