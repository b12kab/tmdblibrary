package com.b12kab.tmdblibrary;

import android.util.Log;

import com.b12kab.tmdblibrary.entities.Status;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class StatusDeserializer implements JsonDeserializer<Status>  {
    private static final String TAG = StatusDeserializer.class.getSimpleName();

    @Override
    public Status deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Status accountState = new Status();
        String convertVariable = "";
        JsonObject obj = (JsonObject) json;
        JsonElement element;

        try {
            convertVariable = "success";
            element = obj.get(convertVariable);
            if (element != null) {
                if (element.isJsonPrimitive()) {
                    if (element.getAsJsonPrimitive().isBoolean()) {
                        accountState.setSuccess(element.getAsBoolean());
                    }
                }
            }

            convertVariable = "status_message";
            element = obj.get(convertVariable);
            if (element != null) {
                if (element.isJsonPrimitive()) {
                    if (element.getAsJsonPrimitive().isString()) {
                        accountState.setStatusMessage(element.getAsString());
                    }
                }
            }

            convertVariable = "status_code";
            element = obj.get(convertVariable);
            if (element != null) {
                if (element.isJsonPrimitive()) {
                    if (element.getAsJsonPrimitive().isNumber()) {
                        accountState.setStatusCode(element.getAsInt());
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
