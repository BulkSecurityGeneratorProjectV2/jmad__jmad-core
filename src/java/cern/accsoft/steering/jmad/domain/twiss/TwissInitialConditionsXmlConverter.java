// @formatter:off
/*******************************************************************************
 * This file is part of JMad. Copyright (c) 2008-2011, CERN. All rights reserved. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
// @formatter:on

package cern.accsoft.steering.jmad.domain.twiss;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import cern.accsoft.steering.jmad.domain.var.enums.MadxTwissVariable;

public class TwissInitialConditionsXmlConverter implements Converter {

    /** The name off the attribute value */
    private static final String ATTR_NAME_VALUE = "value";

    /**
     * @param ctx unused
     */
    @Override
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext ctx) {

        TwissInitialConditionsImpl twiss = (TwissInitialConditionsImpl) object;
        writer.addAttribute("name", twiss.getName());

        writer.startNode("chrom");
        writer.addAttribute(ATTR_NAME_VALUE, String.valueOf(twiss.isCalcChromaticFunctions()));
        writer.endNode();

        writer.startNode("closed-orbit");
        writer.addAttribute(ATTR_NAME_VALUE, String.valueOf(twiss.isClosedOrbit()));
        writer.endNode();

        writer.startNode("centre");
        writer.addAttribute(ATTR_NAME_VALUE, String.valueOf(twiss.isCalcAtCenter()));
        writer.endNode();

        if (twiss.getPtcPhaseSpaceDimension() != null) {
            writer.startNode("ptc-icase");
            writer.addAttribute(ATTR_NAME_VALUE, String.valueOf(twiss.getPtcPhaseSpaceDimension()));
            writer.endNode();
        }

        if (twiss.getPtcMapOrder() != null) {
            writer.startNode("ptc-no");
            writer.addAttribute(ATTR_NAME_VALUE, String.valueOf(twiss.getPtcMapOrder()));
            writer.endNode();
        }

        if (twiss.getPtcBetz() != null) {
            writer.startNode("ptc-betz");
            writer.addAttribute(ATTR_NAME_VALUE, String.valueOf(twiss.getPtcBetz()));
            writer.endNode();
        }

        for (MadxTwissVariable variable : twiss.getMadxVariables()) {
            Double value = twiss.getValue(variable);
            if (value != null) {
                writer.startNode(variable.getMadxName().toLowerCase());
                writer.addAttribute(ATTR_NAME_VALUE, value.toString());
                writer.endNode();
            }
        }

    }

    /**
     * @param ctx unused
     */
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext ctx) {

        String name = reader.getAttribute("name");
        TwissInitialConditionsImpl retVal = new TwissInitialConditionsImpl(name);

        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String nodeName = reader.getNodeName();
            if ("chrom".equals(nodeName)) {
                retVal.setCalcChromaticFunctions(Boolean.parseBoolean(reader.getAttribute(ATTR_NAME_VALUE)));
            } else if ("closed-orbit".equals(nodeName)) {
                retVal.setClosedOrbit(Boolean.parseBoolean(reader.getAttribute(ATTR_NAME_VALUE)));
            } else if ("centre".equals(nodeName)) {
                retVal.setCalcAtCenter(Boolean.parseBoolean(reader.getAttribute(ATTR_NAME_VALUE)));
            } else if ("ptc-icase".equals(nodeName)) {
                retVal.setPtcPhaseSpaceDimension(Integer.parseInt(reader.getAttribute(ATTR_NAME_VALUE)));
            } else if ("ptc-no".equals(nodeName)) {
                retVal.setPtcMapOrder(Integer.parseInt(reader.getAttribute(ATTR_NAME_VALUE)));
            } else if ("ptc-betz".equals(nodeName)) {
                retVal.setPtcBetz(Double.parseDouble(reader.getAttribute(ATTR_NAME_VALUE)));
            } else {
                MadxTwissVariable twissVariable = MadxTwissVariable.fromMadxName(nodeName);
                if (retVal.getMadxVariables().contains(twissVariable)) {
                    retVal.setValue(twissVariable, Double.valueOf(reader.getAttribute(ATTR_NAME_VALUE)));
                }
            }
            reader.moveUp();
        }
        return retVal;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(TwissInitialConditionsImpl.class);
    }
}
