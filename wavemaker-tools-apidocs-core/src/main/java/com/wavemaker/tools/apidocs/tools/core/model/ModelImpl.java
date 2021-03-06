/**
 * Copyright © 2013 - 2017 WaveMaker, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wavemaker.tools.apidocs.tools.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.wavemaker.tools.apidocs.tools.core.model.properties.Property;

@XmlType(propOrder = {"required", "properties"})
@JsonPropertyOrder({"required", "properties"})
public class ModelImpl extends AbstractModel {
    private String type;
    private String name;
    private List<String> required;
    private Map<String, Property> properties;
    private boolean isSimple = false;
    private String description;
    private String example;
    private Property additionalProperties;
    private String discriminator;
    private Xml xml;

    public ModelImpl discriminator(String discriminator) {
        this.setDiscriminator(discriminator);
        return this;
    }

    public ModelImpl name(String name) {
        this.setName(name);
        return this;
    }

    public ModelImpl description(String description) {
        this.setDescription(description);
        return this;
    }

    public ModelImpl property(String key, Property property) {
        this.addProperty(key, property);
        return this;
    }

    public ModelImpl example(String example) {
        this.setExample(example);
        return this;
    }

    public ModelImpl additionalProperties(Property additionalProperties) {
        this.setAdditionalProperties(additionalProperties);
        return this;
    }

    public ModelImpl required(String name) {
        this.addRequired(name);
        return this;
    }

    public ModelImpl xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public String getDiscriminator() {
        return this.discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    @JsonIgnore
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public boolean isSimple() {
        return isSimple;
    }

    public void setSimple(boolean isSimple) {
        this.isSimple = isSimple;
    }

    public Property getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Property additionalProperties) {
        this.setType("object");
        this.additionalProperties = additionalProperties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addRequired(String name) {
        Property p = properties.get(name);
        if (p != null)
            p.setRequired(true);
    }

    public List<String> getRequired() {
        List<String> output = new ArrayList<String>();
        if (properties != null) {
            for (String key : properties.keySet()) {
                Property prop = properties.get(key);
                if (prop != null && prop.getRequired())
                    output.add(key);
            }
        }
        Collections.sort(output);
        if (output.size() > 0)
            return output;
        else
            return null;
    }

    public void setRequired(List<String> required) {
        this.required = required;
        for (String s : required) {
            if (properties != null) {
                Property p = properties.get(s);
                if (p != null) {
                    p.setRequired(true);
                }
            }
        }
    }

    public void addProperty(String key, Property property) {
        if (property == null)
            return;
        if (properties == null)
            properties = new LinkedHashMap<String, Property>();
        if (required != null) {
            for (String ek : required) {
                if (key.equals(ek))
                    property.setRequired(true);
            }
        }
        properties.put(key, property);
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Property> properties) {
        if (properties != null)
            for (String key : properties.keySet()) {
                this.addProperty(key, properties.get(key));
            }
    }

    public String getExample() {
        if (example == null) {
            // TODO: will add logic to construct examples based on payload here
        }

        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Xml getXml() {
        return xml;
    }

    public void setXml(Xml xml) {
        this.xml = xml;
    }

    public Object clone() {
        ModelImpl cloned = new ModelImpl();
        super.cloneTo(cloned);
        cloned.type = this.type;
        cloned.name = this.name;
        cloned.required = this.required;
        cloned.properties = this.properties;
        cloned.isSimple = this.isSimple;
        cloned.description = this.description;
        cloned.example = this.example;
        cloned.additionalProperties = this.additionalProperties;
        cloned.discriminator = this.discriminator;
        cloned.xml = this.xml;

        return cloned;
    }
}
