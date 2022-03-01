package com.b12kab.tmdblibrary.entities;

import com.google.gson.annotations.SerializedName;

public class Status extends BaseStatusResponse{
    @SerializedName("success")
    Boolean success;

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }
}
