/**
 * Copyright (c) 2013 - 2014 WaveMaker, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of WaveMaker, Inc.
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the source code license agreement you entered into with WaveMaker, Inc.
 */
package com.wavemaker.tools.api.core.parsers.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.wavemaker.tools.api.core.builders.ParameterBuilder;
import com.wavemaker.tools.api.core.models.Parameter;
import com.wavemaker.tools.api.core.parsers.ParameterParser;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * @author <a href="mailto:dilip.gundu@wavemaker.com">Dilip Kumar</a>
 * @since 13/11/14
 */
public abstract class AbstractParameterParser extends AbstractPropertyTypeParser<Parameter, ParameterBuilder> implements
        ParameterParser {

    protected final int index;
    protected final Map<Class<? extends Annotation>, Annotation> annotations;

    public AbstractParameterParser(final int index, final Type type, final Annotation[] annotations) {
        super(type);
        this.index = index;
        this.annotations = new HashMap<>();
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                this.annotations.put(annotation.annotationType(), annotation);
            }
        }
    }

    @Override
    protected Parameter postProcessBuilder(final ParameterBuilder builder) {
        builder.setIndex(index);
        builder.setEditable(true); // setting all fields as editable, lets decide in framework and resolver specific
        builder.setRequired(true);
        if (annotations.get(ApiParam.class) != null) {
            ApiParam param = (ApiParam) annotations.get(ApiParam.class);
            builder.setDescription(param.value());
            builder.setId(param.name());
        }
        handleFrameworkSpecific(annotations, builder);

        return builder.build();
    }


    @Override
    protected ParameterBuilder newBuilder() {
        return new ParameterBuilder();
    }

    protected abstract void handleFrameworkSpecific(
            Map<Class<? extends Annotation>, Annotation> annotationMap,
            ParameterBuilder builder);

}
