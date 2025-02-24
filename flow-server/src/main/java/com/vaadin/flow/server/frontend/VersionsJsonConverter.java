/*
 * Copyright 2000-2024 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.server.frontend;

import java.util.Objects;

import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

/**
 * Converts platform versions file to internal format which doesn't contain
 * extra information.
 * <p>
 * The result contains all framework dependencies as keys and their versions as
 * value.
 * <p>
 * For internal use only. May be renamed or removed in a future release.
 *
 * @author Vaadin Ltd
 */
class VersionsJsonConverter {

    static final String VAADIN_CORE_NPM_PACKAGE = "@vaadin/vaadin-core";
    private static final String JS_VERSION = "jsVersion";
    private static final String NPM_NAME = "npmName";
    private static final String NPM_VERSION = "npmVersion";
    private final JsonObject convertedObject;

    private boolean reactEnabled;

    VersionsJsonConverter(JsonObject platformVersions,
            boolean collectReactComponents) {
        this.reactEnabled = collectReactComponents;
        convertedObject = Json.createObject();

        collectDependencies(platformVersions);
    }

    /**
     * Collect framework managed versions to enforce that the user hasn't
     * changed.
     *
     * @return flatten the platform versions Json
     */
    JsonObject getConvertedJson() {
        return convertedObject;
    }

    private void collectDependencies(JsonObject obj) {
        for (String key : obj.keys()) {
            JsonValue value = obj.get(key);
            if (!(value instanceof JsonObject)
                    || (!reactEnabled && isReactObject(key))) {
                continue;
            }
            JsonObject json = (JsonObject) value;
            if (json.hasKey(NPM_NAME)) {
                addDependency(json);
            } else {
                collectDependencies(json);
            }
        }
    }

    private boolean isReactObject(String key) {
        return key.equals("react") || key.equals("react-pro");
    }

    private void addDependency(JsonObject obj) {
        assert obj.hasKey(NPM_NAME);
        String npmName = obj.getString(NPM_NAME);
        // #11025
        if (Objects.equals(npmName, VAADIN_CORE_NPM_PACKAGE)) {
            return;
        }
        if (obj.hasKey(NPM_VERSION)) {
            convertedObject.put(npmName, obj.getString(NPM_VERSION));
        } else if (obj.hasKey(JS_VERSION)) {
            convertedObject.put(npmName, obj.getString(JS_VERSION));
        } else {
            throw new IllegalStateException("Vaadin code versions file "
                    + "contains unexpected data: dependency '" + npmName
                    + "' has" + " no 'npmVersion'/'jsVersion' . "
                    + "Please report a bug in https://github.com/vaadin/platform/issues/new");
        }
    }

}
