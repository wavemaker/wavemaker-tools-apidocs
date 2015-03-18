package com.wavemaker.tools.apidocs.tools.core.model.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wavemaker.tools.apidocs.tools.core.model.ExtensibleEntity;

public interface Parameter extends ExtensibleEntity {
    String getIn();

    void setIn(String in);

    @JsonIgnore
    String getAccess();

    @JsonIgnore
    void setAccess(String access);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    boolean getRequired();

    void setRequired(boolean required);
}