/*
 * Copyright (c) 2016 by Keith Beatty.
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
 */

package com.b12kab.tmdblibrary.entities;

import com.google.gson.annotations.SerializedName;

public class AccountResponse {
    @SerializedName("avatar")
    Avatar avatar;

    @SerializedName("id")
    private Integer id;

    @SerializedName("iso_639_1")
    private String languageRepresentation;

    @SerializedName("iso_3166_1")
    private String countryCodes;

    @SerializedName("name")
    private String actualName;

    @SerializedName("include_adult")
    private Boolean includeAdult;

    @SerializedName("username")
    private String userName;

    public Avatar getAvatar() { return avatar; }

    public String getActualName() {
        return actualName;
    }

    public String getCountryCodes() {
        return countryCodes;
    }

    public Integer getId() {
        return id;
    }

    public Boolean isIncludeAdult() {
        return includeAdult;
    }

    public String getLanguageRepresentation() {
        return languageRepresentation;
    }

    public String getUserName() {
        return userName;
    }

    public class Avatar {
        @SerializedName("gravatar")
        Gravatar gravatar;

        public Gravatar getGravatar() {
            return gravatar;
        }
    }

    public class Gravatar {
        @SerializedName("hash")
        String hash;

        public String getHash() {
            return hash;
        }
    }
}
