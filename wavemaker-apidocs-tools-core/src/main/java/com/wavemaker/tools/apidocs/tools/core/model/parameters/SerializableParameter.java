package com.wavemaker.tools.apidocs.tools.core.model.parameters;

import java.util.List;

import com.wavemaker.tools.apidocs.tools.core.model.properties.Property;

public interface SerializableParameter extends Parameter {
    String getType();

    void setType(String type);

    void setItems(Property items);

    Property getItems();

    String getFormat();

    void setFormat(String format);

    String getCollectionFormat();

    void setCollectionFormat(String collectionFormat);

    List<String> getEnum();

    void setEnum(List<String> _enum);
}