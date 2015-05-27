package com.wavemaker.tools.apidocs.tools.core.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractModel extends AbstractExtensibleEntity implements Model {
    private static final String TAG_EXT = "TAGS";
    private static final String FULLY_QUALIFIED_NAME_EXT = "FULLY_QUALIFIED_NAME";

    private ExternalDocs externalDocs;

    @Override
    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocs value) {
        externalDocs = value;
    }

    public void cloneTo(Object clone) {
        AbstractModel cloned = (AbstractModel) clone;
        cloned.externalDocs = this.externalDocs;
    }

    public Object clone() {
        return null;
    }

    public void addTag(String tag) {
        synchronized (this) {
            Set<String> tags = (Set<String>) getWMExtension(TAG_EXT);
            if (tags == null) {
                tags = new LinkedHashSet<>();
            }
            tags.add(tag);
            addWMExtension(TAG_EXT, tags);
        }
    }

    @JsonIgnore
    public Set<String> getTags() {
        Object tags = getWMExtension(TAG_EXT);
        return (tags == null) ? Collections.<String>emptySet() : (Set<String>) tags;
    }

    @JsonIgnore
    public void setFullyQualifiedName(String fullyQualifiedName) {
        addWMExtension(FULLY_QUALIFIED_NAME_EXT, fullyQualifiedName);
    }

    @JsonIgnore
    public String getFullyQualifiedName() {
        return (String) getWMExtension(FULLY_QUALIFIED_NAME_EXT);
    }
}
