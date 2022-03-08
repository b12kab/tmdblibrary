package com.b12kab.tmdblibrary.entities;

import com.b12kab.tmdblibrary.enumerations.MediaType;
import com.google.gson.annotations.SerializedName;

public class AccountFavorite {
    @SerializedName("media_type")
    public String mediaType;

    @SerializedName("media_id")
    public Integer id;

    @SerializedName("favorite")
    public Boolean favorite;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        if (mediaType == null)
            return;

        this.setMediaType(mediaType.toString());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
