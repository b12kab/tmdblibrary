/*
 * Copyright 2014 Chris Banes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.b12kab.tmdblibrary.enumerations;

public enum AppendToResponseItem {

    VIDEOS("videos"),
    RELEASES("release_dates"),
    CREDITS("credits"),
    SIMILAR("similar"),
    IMAGES("images"),
    REVIEWS("reviews"),
    EXTERNAL_IDS("external_ids"),
    STATES("account_states");

    private final String value;

    private AppendToResponseItem(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
