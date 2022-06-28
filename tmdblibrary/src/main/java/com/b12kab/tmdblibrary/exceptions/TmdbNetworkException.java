package com.b12kab.tmdblibrary.exceptions;

import java.io.IOException;

import androidx.annotation.NonNull;

public class TmdbNetworkException extends IOException {
    private String message;
    private Integer httpResponseCode;
    private Integer retryTime;

    public TmdbNetworkException() {
        super("TmdbNetworkException");
    }

    public TmdbNetworkException(int responseCode) {
        this();
        this.setHttpResponseCode(responseCode);
    }

    public TmdbNetworkException(String message) {
        this();
        this.setMessage(message);
    }

    public TmdbNetworkException(int responseCode, String message) {
        this();
        this.setHttpResponseCode(responseCode);
        this.setMessage(message);
    }

    public TmdbNetworkException(int responseCode, String message, int retryTime) {
        this(responseCode, message);
        this.setRetryTime(retryTime);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getHttpResponseCode() {
        return httpResponseCode;
    }

    public void setHttpResponseCode(Integer httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    public Integer getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(Integer retryTime) {
        this.retryTime = retryTime;
    }

    @NonNull
    public String toString() {
        String results = "";
        if (httpResponseCode != null) {
            results += "http response: " + httpResponseCode + " ";
        }

        if (message != null) {
            results += "message: " + message + " ";
        }

        if (retryTime != null) {
            results += "retry time: " + retryTime  + " ";
        }

        return results;
    }
}
