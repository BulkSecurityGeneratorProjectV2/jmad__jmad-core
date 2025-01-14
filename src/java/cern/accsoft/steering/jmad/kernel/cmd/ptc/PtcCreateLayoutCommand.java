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
package cern.accsoft.steering.jmad.kernel.cmd.ptc;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.jmad.kernel.cmd.AbstractCommand;
import cern.accsoft.steering.jmad.kernel.cmd.param.GenericParameter;
import cern.accsoft.steering.jmad.kernel.cmd.param.Parameter;

/**
 * This class represents the madx-command to transfer the madx data to ptc: <a
 * href="http://mad.web.cern.ch/mad/ptc_general/ptc_general.html">PTC_CREATE_LAYOUT</a>
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class PtcCreateLayoutCommand extends AbstractCommand {

    /** the name of the command */
    private static final String CMD_NAME = "ptc_create_layout";

    /*
     * the possible parameters
     */
    private Boolean time = null;
    private Integer model = null;
    private Integer method = null;
    private Integer nst = null;
    private Boolean exact = null;
    private Double offsetDeltaP = null;
    private Boolean errorsOut = null;
    private Boolean errorsIn = null;
    private String magnetName = null;
    private Boolean resplit = null;
    private Double thin = null;
    private Double xbend = null;
    private Boolean even = null;

    /**
     * the default constructor, which sets no values
     */
    public PtcCreateLayoutCommand() {
        super();
    }

    /**
     * the constructor, which just sets the time-option
     * 
     * @param time determines how the time coordinate is treated by ptc. (see <a
     *            href="http://mad.web.cern.ch/mad/ptc_general/ptc_general.html">PTC_CREATE_LAYOUT</a>)
     */
    public PtcCreateLayoutCommand(Boolean time) {
        super();
        this.time = time;
    }

    @Override
    public String getName() {
        return CMD_NAME;
    }

    @Override
    public List<Parameter> getParameters() {
        List<Parameter> parameters = new ArrayList<>();

        /*
         * this is kind of special boolean parameter: it does not follow the convention, that it is omitted, when it is
         * meant to be false! It has to be set explicitly to false. We therefore use a string-parameter.
         */
        if (time != null) {
            parameters.add(new GenericParameter<>("time", time.toString()));
        }

        parameters.add(new GenericParameter<>("model", model));
        parameters.add(new GenericParameter<>("method", method));
        parameters.add(new GenericParameter<>("nst", nst));
        parameters.add(new GenericParameter<>("exact", exact));
        parameters.add(new GenericParameter<>("offset_deltap", offsetDeltaP));
        parameters.add(new GenericParameter<>("errors_in", errorsIn));
        parameters.add(new GenericParameter<>("errors_out", errorsOut));
        parameters.add(new GenericParameter<>("magnet_name", magnetName));
        parameters.add(new GenericParameter<>("resplit", resplit));
        parameters.add(new GenericParameter<>("thin", thin));
        parameters.add(new GenericParameter<>("xbend", xbend));
        /* same as for "time": default is true! */
        if (even != null) {
            parameters.add(new GenericParameter<>("even", even.toString()));
        }
        return parameters;
    }

    public Boolean getTime() {
        return time;
    }

    public void setTime(Boolean time) {
        this.time = time;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public Integer getNst() {
        return nst;
    }

    public void setNst(Integer nst) {
        this.nst = nst;
    }

    public Boolean getExact() {
        return exact;
    }

    public void setExact(Boolean exact) {
        this.exact = exact;
    }

    public Double getOffsetDeltaP() {
        return offsetDeltaP;
    }

    public void setOffsetDeltaP(Double offsetDeltaP) {
        this.offsetDeltaP = offsetDeltaP;
    }

    public Boolean getErrorsOut() {
        return errorsOut;
    }

    public void setErrorsOut(Boolean errorsOut) {
        this.errorsOut = errorsOut;
    }

    public Boolean getErrorsIn() {
        return errorsIn;
    }

    public void setErrorsIn(Boolean errorsIn) {
        this.errorsIn = errorsIn;
    }

    public String getMagnetName() {
        return magnetName;
    }

    public void setMagnetName(String magnetName) {
        this.magnetName = magnetName;
    }

    public Boolean getResplit() {
        return resplit;
    }

    public void setResplit(Boolean resplit) {
        this.resplit = resplit;
    }

    public Double getThin() {
        return thin;
    }

    public void setThin(Double thin) {
        this.thin = thin;
    }

    public Double getXbend() {
        return xbend;
    }

    public void setXbend(Double xbend) {
        this.xbend = xbend;
    }

    public Boolean getEven() {
        return even;
    }

    public void setEven(Boolean even) {
        this.even = even;
    }
}
