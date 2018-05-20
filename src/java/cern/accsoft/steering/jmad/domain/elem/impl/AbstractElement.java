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

package cern.accsoft.steering.jmad.domain.elem.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cern.accsoft.steering.jmad.domain.elem.Element;
import cern.accsoft.steering.jmad.domain.elem.ElementAttributeReader;
import cern.accsoft.steering.jmad.domain.elem.ElementListener;
import cern.accsoft.steering.jmad.domain.elem.MadxElementType;
import cern.accsoft.steering.jmad.domain.elem.Position;
import cern.accsoft.steering.jmad.domain.ex.JMadModelException;

public abstract class AbstractElement implements Element {

    /** the logger for the class */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractElement.class);

    /** the length of the element [m] */
    public static final String ATTR_LENGTH = "l";

    /** The name of the element */
    private final String name;

    /** the position of the element in the sequence [m] */
    private Position position = new Position(0.0);

    /** Collection for the listeners */
    private final List<ElementListener> listeners = new ArrayList<ElementListener>();

    /** the additional attributes of a Element */
    private final Map<String, Double> attributes = new HashMap<String, Double>();

    /** the attribute-names */
    private final List<String> attributeNames = new ArrayList<String>();

    /** the corresponding element type as given e.g. in sequences. */
    private final MadxElementType madxElementType;

    /** the class to read attributes from madx */
    private ElementAttributeReader attributesReader;

    /** set to true, if the attributes are initialized */
    private boolean attributesInitialized = false;

    /** if true, the listeners are notified, if false then not */
    private boolean listenersEnabled = true;

    /**
     * default constructor to enforce that the element has madx element type and a name.
     * 
     * @param madxElementType the type of the element, as it is represented in MadX
     * @param name the name of the element.
     */
    public AbstractElement(MadxElementType madxElementType, String name) {
        attributeNames.add(ATTR_LENGTH);
        this.madxElementType = madxElementType;
        this.name = name;
    }

    @Override
    public List<String> getAttributeNames() {
        return attributeNames;
    }

    @Override
    public final MadxElementType getMadxElementType() {
        return this.madxElementType;
    }

    /**
     * adds an attribute to the available attributes of this element
     * 
     * @param attributeName the name of the attribute to add
     */
    protected final void addAttribute(String attributeName) {
        this.attributeNames.add(attributeName);
    }

    /*
     * handling listeners
     */

    @Override
    public void addListener(ElementListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ElementListener listener) {
        listeners.remove(listener);
    }

    private void fireChangedAttribute(String attributeName) {
        if (!isListenersEnabled()) {
            return;
        }

        for (ElementListener listener : listeners) {
            listener.changedAttribute(this, attributeName);
        }
    }

    /*
     * general getter / setter for attributes
     */
    @Override
    public void setAttribute(String name, Double value) {
        attributes.put(name, value);
        fireChangedAttribute(name);
    }

    @Override
    public Double getAttribute(String attributeName) {
        if (!isAttributesInitialized()) {
            initAttributes();
        }
        return attributes.get(attributeName);
    }

    /**
     * reads the attributes for this element
     */
    private void initAttributes() {
        try {
            getAttributesReader().readAttributes(this);
        } catch (JMadModelException e) {
            LOGGER.error("Could not load attributes for element '" + getName() + "'.", e);
        }
    }

    /*
     * simple getters and setters.
     */

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(double position) {
        this.position = new Position(position);
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    public double getLength() {
        return getAttribute(ATTR_LENGTH);
    }

    public void setLength(double length) {
        setAttribute(ATTR_LENGTH, length);
    }

    @Override
    public void setAttributesInitialized(boolean attributesInitialized) {
        this.attributesInitialized = attributesInitialized;
    }

    private boolean isAttributesInitialized() {
        return attributesInitialized;
    }

    @Override
    public void setListenersEnabled(boolean listenersEnabled) {
        this.listenersEnabled = listenersEnabled;
    }

    private boolean isListenersEnabled() {
        return listenersEnabled;
    }

    public void setAttributesReader(ElementAttributeReader attributesReader) {
        this.attributesReader = attributesReader;
    }

    @Override
    public ElementAttributeReader getAttributesReader() {
        return attributesReader;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [name=" + name + ", madxElementType=" + madxElementType + "]";
    }

}
