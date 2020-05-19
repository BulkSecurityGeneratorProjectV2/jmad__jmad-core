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

package cern.accsoft.steering.jmad.domain.twiss;

import java.util.Optional;

import cern.accsoft.steering.jmad.domain.var.enums.MadxTwissVariable;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class TwissInitialConditionsJsonConverter implements Converter {

    @Override
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext ctx) {

        TwissInitialConditionsImpl twiss = (TwissInitialConditionsImpl) object;

        JsonWriter underWriter = (JsonWriter) writer.underlyingWriter();

        underWriter.addAttribute("name", twiss.getName());
        Optional.ofNullable(twiss.getPtcPhaseSpaceDimension())
                .ifPresent(p -> underWriter.addAttribute("ptc-icase", String.valueOf(p)));
        Optional.ofNullable(twiss.getPtcMapOrder())
                .ifPresent(p -> underWriter.addAttribute("ptc-no", String.valueOf(p)));
        Optional.ofNullable(twiss.getPtcBetz())
                .ifPresent(p -> underWriter.addAttribute("ptc-betz", String.valueOf(p)));
        Optional.ofNullable(twiss.getSaveBetaName())
                .ifPresent(p -> underWriter.addAttribute("beta0", p));
        underWriter.addAttribute("chrom", String.valueOf(twiss.isCalcChromaticFunctions()));
        underWriter.addAttribute("closed-orbit", String.valueOf(twiss.isClosedOrbit()));
        underWriter.addAttribute("centre", String.valueOf(twiss.isCalcAtCenter()));

        for (MadxTwissVariable variable : twiss.getMadxVariables()) {
            Double value = twiss.getValue(variable);
            if (value != null) {
                underWriter.addAttribute(variable.getMadxName().toLowerCase(), value.toString());

            }
        }

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext ctx) {

        String name = reader.getAttribute("name");
        TwissInitialConditionsImpl retVal = new TwissInitialConditionsImpl(name);

        java.util.Iterator<String> iter = reader.getAttributeNames();
        while (iter.hasNext()) {
            String attrName = iter.next();
            if ("chrom".equals(attrName)) {
                retVal.setCalcChromaticFunctions(Boolean.parseBoolean(reader.getAttribute(attrName)));
            } else if ("closed-orbit".equals(attrName)) {
                retVal.setClosedOrbit(Boolean.parseBoolean(reader.getAttribute(attrName)));
            } else if ("centre".equals(attrName)) {
                retVal.setCalcAtCenter(Boolean.parseBoolean(reader.getAttribute(attrName)));
            } else if ("ptc-icase".equals(attrName)) {
                retVal.setPtcPhaseSpaceDimension(Integer.parseInt(reader.getAttribute(attrName)));
            } else if ("ptc-no".equals(attrName)) {
                retVal.setPtcMapOrder(Integer.parseInt(reader.getAttribute(attrName)));
            } else if ("ptc-betz".equals(attrName)) {
                retVal.setPtcBetz(Double.parseDouble(reader.getAttribute(attrName)));
            } else if ("beta0".equals(attrName)) {
                retVal.setSaveBetaName(reader.getAttribute(attrName));
            } else {
                MadxTwissVariable twissVariable = MadxTwissVariable.fromMadxName(attrName);
                if (retVal.getMadxVariables().contains(twissVariable)) {
                    retVal.setValue(twissVariable, Double.valueOf(reader.getAttribute(attrName)));
                }
            }
            reader.moveUp();
        }
        return retVal;
    }

    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
        return clazz.equals(TwissInitialConditionsImpl.class);
    }
}
