package com.b12kab.tmdblibrary.entities;

import com.google.gson.annotations.SerializedName;

public class BaseStatusResponse {
    @SerializedName("status_code")
    Integer statusCode;

    @SerializedName("status_message")
    String statusMessage;

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
