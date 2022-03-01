/*
 * Copyright 2013 Uwe Trottmann
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

package com.b12kab.tmdblibrary;

import com.b12kab.tmdblibrary.entities.AccountState;
import com.b12kab.tmdblibrary.entities.Status;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TmdbHelper {

    /** Format for decoding JSON dates in string format. */
    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd");

    /**
     * Create a {@link com.google.gson.GsonBuilder} and register all of the custom types needed in
     * order to properly deserialize complex TMDb-specific types.
     *
     * @return Assembled GSON builder instance.
     */
    public static GsonBuilder getGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();

        // class types
        builder.registerTypeAdapter(AccountState.class, new AccountStateDeserializer());
        builder.registerTypeAdapter(Status.class, new StatusDeserializer());
        // is this needed or have the upgrades over time added this to work OK?
        builder.registerTypeAdapter(Integer.class, (JsonDeserializer<Integer>) (json, typeOfT, context) -> {

            try {
                if (json.isJsonNull())
                    return null;
                return json.getAsInt();
            } catch (Exception e) {
                return null;
            }
        });
        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {

            try {
                if (json.isJsonNull())
                    return null;
                return JSON_STRING_DATE.parse(json.getAsString());
            } catch (ParseException e) {
                return null;
            }
        });

        return builder;
    }
}
