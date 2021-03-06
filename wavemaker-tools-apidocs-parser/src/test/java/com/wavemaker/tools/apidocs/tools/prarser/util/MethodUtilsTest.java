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
package com.wavemaker.tools.apidocs.tools.prarser.util;

import org.junit.Test;

import com.wavemaker.tools.apidocs.tools.parser.util.MethodUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MethodUtilsTest {

    @Test
    public void testIsGetterMethod() throws Exception {
        assertTrue(MethodUtils.isGetterMethod("isValid"));
        assertTrue(MethodUtils.isGetterMethod("isEmpty"));
        assertTrue(MethodUtils.isGetterMethod("is"));
        assertTrue(MethodUtils.isGetterMethod("getValue"));
        assertTrue(MethodUtils.isGetterMethod("getUser"));
        assertTrue(MethodUtils.isGetterMethod("getThings"));
        assertTrue(MethodUtils.isGetterMethod("get"));
    }

    @Test
    public void testIsNotGetterMethod() throws Exception {
        assertFalse(MethodUtils.isGetterMethod("i"));
        assertFalse(MethodUtils.isGetterMethod("valid"));
        assertFalse(MethodUtils.isGetterMethod("emptyIs"));
        assertFalse(MethodUtils.isGetterMethod("g"));
        assertFalse(MethodUtils.isGetterMethod("ge"));
        assertFalse(MethodUtils.isGetterMethod("valueGet"));
        assertFalse(MethodUtils.isGetterMethod("userGet"));
        assertFalse(MethodUtils.isGetterMethod("someget"));
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals(MethodUtils.DEFAULT_PROPERTY_NAME, MethodUtils.getPropertyName("is"));
        assertEquals(MethodUtils.DEFAULT_PROPERTY_NAME, MethodUtils.getPropertyName("get"));

        assertEquals("user", MethodUtils.getPropertyName("getUser"));
        assertEquals("user", MethodUtils.getPropertyName("isUser"));
        assertEquals("userIf", MethodUtils.getPropertyName("getUserIf"));
        assertEquals("userIf", MethodUtils.getPropertyName("isUserIf"));
    }

    @Test
    public void testGetAllMethods() throws Exception {

    }
}