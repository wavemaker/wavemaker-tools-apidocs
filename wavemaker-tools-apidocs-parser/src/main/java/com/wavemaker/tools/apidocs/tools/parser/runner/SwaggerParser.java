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
package com.wavemaker.tools.apidocs.tools.parser.runner;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.wavemaker.tools.apidocs.tools.core.model.Path;
import com.wavemaker.tools.apidocs.tools.core.model.Resource;
import com.wavemaker.tools.apidocs.tools.core.model.Swagger;
import com.wavemaker.tools.apidocs.tools.core.model.Tag;
import com.wavemaker.tools.apidocs.tools.parser.config.SwaggerConfiguration;
import com.wavemaker.tools.apidocs.tools.parser.context.ResourceParserContext;
import com.wavemaker.tools.apidocs.tools.parser.context.SwaggerParserContext;
import com.wavemaker.tools.apidocs.tools.parser.exception.ClassScannerException;
import com.wavemaker.tools.apidocs.tools.parser.exception.SwaggerParserException;
import com.wavemaker.tools.apidocs.tools.parser.exception.TimeOutException;
import com.wavemaker.tools.apidocs.tools.parser.util.ClassLoaderUtil;
import com.wavemaker.tools.apidocs.tools.parser.util.MethodUtils;

/**
 * @author <a href="mailto:dilip.gundu@wavemaker.com">Dilip Kumar</a>
 * @since 12/11/14
 */
public abstract class SwaggerParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerParser.class);

    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    protected final SwaggerConfiguration configuration;

    public SwaggerParser(final SwaggerConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * It will generates {@link Swagger} from given {@link SwaggerConfiguration}.
     *
     * @return {@link Swagger}
     * @throws SwaggerParserException
     */
    public Swagger generate() throws SwaggerParserException {
        try {
            long startTime = System.currentTimeMillis();
            Swagger swagger = doWithParserClassLoader();
            long endTime = System.currentTimeMillis();
            LOGGER.info("Swagger Parser Runner took {} milli seconds to generate documents", (endTime - startTime));
            return swagger;
        } catch (Exception e) {
            if (e instanceof SwaggerParserException) {
                throw (SwaggerParserException) e;
            }
            throw new SwaggerParserException("Exception while parsing documents", e);
        }
    }

    /**
     * It will returns the {@link Method} associated with given unique key.
     *
     * @param uniqueKey key to be search
     * @param type type to search methods
     * @return {@link Method} matching with given uniqueKey.
     */
    public Method getMethodForUniqueKey(String uniqueKey, Class<?> type) {
        return MethodUtils.getMethodForUniqueIdentifier(uniqueKey, type);
    }

    /**
     * It will returns the {@link Map} of unique key vs {@link Method} for a given {@link Class}.
     *
     * @param type class to be scan for methods.
     * @return {@link Map} of unique vs {@link Method}s.
     */
    public Map<String, Method> getUniqueKeyVsMethods(Class<?> type) {
        return MethodUtils.getMethodUniqueIdentifierIdMap(type);
    }

    /**
     * It will calls the {@link #startProcessing()} in a custom class loader.
     *
     * @return {@link Map}.
     * @throws Exception
     */
    protected Swagger doWithParserClassLoader() throws Exception {
        return ClassLoaderUtil.doWithCustomClassLoader(configuration.getClassLoader(),
                new Callable<Swagger>() {
                    @Override
                    public Swagger call() throws Exception {
                        return startProcessing();
                    }
                });
    }

    /**
     * It will scans given classes, then filters the rest classes and finally returns the Map of {@link Resource}s.
     *
     * @return {@link Map} of Rest {@link Class} and {@link Resource}.
     */
    protected Swagger startProcessing() throws SwaggerParserException {
        SwaggerParserContext context = new SwaggerParserContext(configuration);
        Set<Class<?>> classToScan = configuration.getClassScanner().classesToScan();
        Set<Class<?>> restClasses;
        try {
            restClasses = filterRestClasses(classToScan);
        } catch (Exception e) {
            LOGGER.error("Error while scanning classes:", e);
            throw new ClassScannerException(e);
        }
        Swagger swagger;
        try {
            swagger = generateDocuments(context, restClasses);
        } catch (InterruptedException e) {
            LOGGER.error("Error while generating documents", e);
            throw new TimeOutException("Error while generating documents", e);
        }
        swagger.setDefinitions(context.getTypesContext().getDefinitionsMap());
        return swagger;
    }


    /**
     * Actual Class parsing starts here. It will parse all given classes and returns {@link Map} of {@link Class} vs
     * {@link Resource}.
     *
     * @param context
     * @param restClasses rest classes to look for documents.
     * @return {@link Map} of {@link Class} vs {@link Resource}s.
     */
    protected Swagger generateDocuments(
            final SwaggerParserContext context, Set<Class<?>> restClasses) throws InterruptedException {
        final Map<Class<?>, Resource> resourceMap = new ConcurrentHashMap<>();
        for (final Class<?> restClass : restClasses) {
            LOGGER.debug("Started parsing {} controller", restClass);
            ResourceParserContext.initContext(context);// creating parser context for each class.
            try {
                resourceMap.put(restClass, parseRestClass(restClass));
            } catch (Throwable th) {
                throw new SwaggerParserException("Error while generating swagger for class:" + restClass.getName(), th);
            } finally {
                ResourceParserContext.destroyContext(); // destroying parser context after parsing.
            }
        }
        return swaggerFrom(resourceMap);
    }

    protected Swagger swaggerFrom(Map<Class<?>, Resource> resourceMap) {
        Swagger swagger = new Swagger();
        swagger.basePath(configuration.getBaseUrl());
        swagger.schemes(Lists.newLinkedList(configuration.getSchemes()));
        for (final Map.Entry<Class<?>, Resource> resourceEntry : resourceMap.entrySet()) {
            Resource resource = resourceEntry.getValue();
            Tag tag = resource.asTag();
            swagger.tag(tag);
            resource.setTag(tag.getName());
            for (final Map.Entry<String, Path> pathEntry : resource.getPathMap().entrySet()) {
                swagger.path(pathEntry.getKey(), pathEntry.getValue());
            }
        }
        swagger.setInfo(configuration.getInfo());

        return swagger;
    }


    /**
     * It should parses the given restClass and generates {@link Resource}.
     *
     * @param restClass class to be parsed.
     * @return generated {@link Resource}.
     */
    protected abstract Resource parseRestClass(Class<?> restClass);


    /**
     * It should filters the Rest classes from given set of classes.
     *
     * @param classSet list of classes to filter
     * @return {@link Set} of Rest {@link Class}es.
     */
    protected abstract Set<Class<?>> filterRestClasses(Set<Class<?>> classSet);
}
