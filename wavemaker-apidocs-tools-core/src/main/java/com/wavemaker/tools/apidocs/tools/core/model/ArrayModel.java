package com.wavemaker.tools.apidocs.tools.core.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wavemaker.tools.apidocs.tools.core.model.properties.ArrayProperty;
import com.wavemaker.tools.apidocs.tools.core.model.properties.Property;

public class ArrayModel extends AbstractModel {
    private Map<String, Property> properties;
    private String type;
    private String description;
    private Property items;
    private String example;

    public ArrayModel() {
        this.type = "array";
    }

    public ArrayModel description(String description) {
        this.setDescription(description);
        return this;
    }

    public ArrayModel items(Property items) {
        this.setItems(items);
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Property getItems() {
        return items;
    }

    public void setItems(Property items) {
        this.items = items;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Object clone() {
        ArrayModel cloned = new ArrayModel();
        super.cloneTo(cloned);

        cloned.properties = this.properties;
        cloned.type = this.type;
        cloned.description = this.description;
        cloned.items = this.items;
        cloned.example = this.example;
        cloned.vendorExtensions = this.vendorExtensions;

        return cloned;
    }

    @JsonIgnore
    public void setIsList(boolean list) {
        addWMExtension(ArrayProperty.IS_LIST_EXT, list);
    }

    @JsonIgnore
    public boolean isList() {
        Object list = getWMExtension(ArrayProperty.IS_LIST_EXT);
        return (list != null) && (boolean) list;
    }
}