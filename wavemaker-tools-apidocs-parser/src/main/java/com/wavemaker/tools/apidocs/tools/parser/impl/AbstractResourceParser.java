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
package com.wavemaker.tools.apidocs.tools.parser.impl;

import java.util.Set;

import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.apidocs.tools.core.model.Path;
import com.wavemaker.tools.apidocs.tools.core.model.Resource;
import com.wavemaker.tools.apidocs.tools.parser.context.ResourceParserContext;
import com.wavemaker.tools.apidocs.tools.parser.parser.PathsParser;
import com.wavemaker.tools.apidocs.tools.parser.parser.ResourceParser;
import com.wavemaker.tools.apidocs.tools.parser.util.ContextUtil;
import com.wavemaker.tools.apidocs.tools.parser.util.DataTypeUtil;
import com.wordnik.swagger.annotations.Api;

/**
 * @author <a href="mailto:dilip.gundu@wavemaker.com">Dilip Kumar</a>
 * @since 13/11/14
 */
public abstract class AbstractResourceParser implements ResourceParser {

    protected final Class<?> type;

    protected AbstractResourceParser(Class<?> type) {
        this.type = type;
    }

    @Override
    public Resource parse() {
        Resource resource = new Resource();

        if (type.isAnnotationPresent(Api.class)) {
            resource.setDescription(type.getAnnotation(Api.class).description());
        }
        resource.setName(ContextUtil.getUniqueName(type));
        resource.setFullyQualifiedName(DataTypeUtil.getFullyQualifiedName(type));
        resource.setControllerName(getResourceName());

        ResourceParserContext.getContext().setTag(resource.asTag().getName());
        ResourceParserContext.getContext().setResourcePath(getResourcePath());
        resource.setVersion(""); // XXX think it later?

        // For now it is commented, because we dropped BaseModel dependency while refactoring.
        // resource.setEditable(ParserRunnerContext.getInstance().getConfiguration().isEditable());

        //for end points use
        if (type.isAnnotationPresent(WMAccessVisibility.class)) {
            ResourceParserContext.getContext().setSpecifier(type.getAnnotation(WMAccessVisibility.class).value());
        }
        ResourceParserContext.getContext().setConsumes(getConsumes());
        ResourceParserContext.getContext().setProduces(getProduces());

        resource.setPathMap(getPathParser(type).parse());
        return resource;
    }

    protected abstract Set<String> getProduces();

    protected abstract Set<String> getConsumes();

    protected abstract String getResourceName();

    /**
     * It should return the path of the api.
     *
     * @return path of the api.
     */
    protected abstract String getResourcePath();

    /**
     * It should give the {@link PathsParser} instance.
     *
     * @param typeToParse class to parse for {@link Path}.
     * @return {@link PathsParser} instance.
     */
    protected abstract PathsParser getPathParser(Class<?> typeToParse);

}
