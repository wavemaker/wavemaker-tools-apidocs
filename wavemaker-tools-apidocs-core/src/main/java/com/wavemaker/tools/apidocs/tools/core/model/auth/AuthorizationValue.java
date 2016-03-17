/**
 * Copyright © 2013 - 2016 WaveMaker, Inc.
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
package com.wavemaker.tools.apidocs.tools.core.model.auth;

public class AuthorizationValue {
    private String value, type, keyName;

    public AuthorizationValue() {
    }

    public AuthorizationValue(String keyName, String value, String type) {
        this.setKeyName(keyName);
        this.setValue(value);
        this.setType(type);
    }

    public AuthorizationValue value(String value) {
        this.value = value;
        return this;
    }

    public AuthorizationValue type(String type) {
        this.type = type;
        return this;
    }

    public AuthorizationValue keyName(String keyName) {
        this.keyName = keyName;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}