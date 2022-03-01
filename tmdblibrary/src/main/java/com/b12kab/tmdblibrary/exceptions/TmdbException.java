package com.b12kab.tmdblibrary.exceptions;

import java.io.IOException;

public class TmdbException extends IOException {
    public enum RetrofitErrorKind { Unset, Retry, Timeout, ConversionError, Other };
    private TmdbException.RetrofitErrorKind errorKind;

    private int code;
    private String message;
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
        this.setCode(code);
    }

    private void initialize() {
        this.setCode(-1);
        this.setErrorKind(RetrofitErrorKind.Unset);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
}
