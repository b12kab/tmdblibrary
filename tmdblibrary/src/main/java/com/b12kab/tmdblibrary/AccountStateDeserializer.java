/*
 * Copyright 2016 Keith Beatty
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

import android.util.Log;

import com.b12kab.tmdblibrary.entities.AccountState;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

public class AccountStateDeserializer implements JsonDeserializer<AccountState>  {
    private static final String TAG = AccountStateDeserializer.class.getSimpleName();

    @Override
    public AccountState deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
        AccountState accountState = new AccountState();
        String convertVariable = "";
        JsonObject obj = (JsonObject) json;
        JsonElement element;
        JsonElement ratingElement;
        JsonPrimitive rating;
        try {
            convertVariable = "id";
            element = obj.get("id");
            if (element != null) {
                if (element.isJsonPrimitive()) {
                    if (element.getAsJsonPrimitive().isNumber()) {
                        accountState.setId(element.getAsInt());
                    }
                }
            }

            convertVariable = "favorite";
            element = obj.get("favorite");
            if (element != null) {
                if (element.isJsonPrimitive()) {
                    if (element.getAsJsonPrimitive().isBoolean()) {
                        accountState.setFavorite(element.getAsBoolean());
                    }
                }
            }

            accountState.setRated(null);
            convertVariable = "rated";
            element = obj.get("rated");
            if (element != null) {
                if (element.isJsonObject()) {
                    if (element.getAsJsonObject().has("value")) {
                        ratingElement = element.getAsJsonObject().get("value");
                        if (ratingElement.isJsonPrimitive()) {
//                        rating
                            if (ratingElement.getAsJsonPrimitive().isNumber()) {
                                accountState.setRated(ratingElement.getAsFloat());
                            }
                        }
                    }
                }
            }

            convertVariable = "watchlist";
            element = obj.get("watchlist");
            if (element != null) {
                if (element.isJsonPrimitive()) {
                    if (element.getAsJsonPrimitive().isBoolean()) {
                        accountState.setWatchlist(element.getAsBoolean());
                    }
                }
            }
        } catch (NullPointerException npe) {
            Log.e(TAG, "Processing " + convertVariable + " response from Tmdb, NullPointerException occurred" +
                    npe.toString());
            return null;
        } catch (NumberFormatException npe) {
            Log.e(TAG, "Processing " + convertVariable + " response from Tmdb, NumberFormatException occurred" +
                    npe.toString());
            return null;
        } catch (IllegalStateException ise) {
            Log.e(TAG, "Processing " + convertVariable + " response from Tmdb, IllegalStateException occurred" +
                    ise.toString());
        }

        return accountState;
    }
}
