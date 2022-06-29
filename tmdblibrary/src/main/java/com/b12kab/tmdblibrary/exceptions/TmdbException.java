package com.b12kab.tmdblibrary.exceptions;

import com.b12kab.tmdblibrary.entities.Status;

import java.io.IOException;

import androidx.annotation.NonNull;

public class TmdbException extends IOException {
    public enum RetrofitErrorKind { Unset, None, Retry, IOError, ConversionError, Tmdb }
    public enum UseMessage { Unset, Yes, No}

    private TmdbException.RetrofitErrorKind errorKind;
    private TmdbException.UseMessage useMessage;
    private String message;
    private Status status;
    private Integer httpResponseCode;
    private Integer retryTime;

    public TmdbException() {
        super();
        this.initialize();
    }

    public TmdbException(int code, String message) {
        super(message);
        this.initialize();
        this.setMessage(message);
    }

    private void initialize() {
        this.setErrorKind(RetrofitErrorKind.Unset);
        this.setUseMessage(UseMessage.Unset);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RetrofitErrorKind getErrorKind() {
        return errorKind;
    }

    public void setErrorKind(RetrofitErrorKind errorKind) {
        this.errorKind = errorKind;
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

    public UseMessage getUseMessage() {
        return useMessage;
    }

    public void setUseMessage(UseMessage useMessage) {
        this.useMessage = useMessage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @NonNull
    public String toString() {
        String result = "";

        if (this.httpResponseCode != null) {
            result += " http response code: " + this.httpResponseCode + " ";
        }

        if (this.retryTime != null) {
            result += " retry time: " + this.retryTime + " ";
        }

        if (this.message != null) {
            result += " message: " + this.message + " ";
        }

        if (this.status != null) {
            if (this.status.getStatusCode() != null) {
                result += " status code: " + this.status.getStatusCode() + " ";
            }
            if (this.status.getSuccess() != null) {
                result += " status success: " + this.status.getSuccess() + " ";
            }
            if (this.status.getStatusMessage() != null) {
                result += " status message: " + this.status.getStatusMessage() + " ";
            }
        }

        return result;
    }
}
