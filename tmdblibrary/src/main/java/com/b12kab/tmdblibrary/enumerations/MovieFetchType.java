package com.b12kab.tmdblibrary.enumerations;

import androidx.annotation.NonNull;

public enum MovieFetchType {
    NowPlaying("now_playing"),
    Popular("popular"),
    TopRated("top_rated"),
    Upcoming("upcoming"),
    UserRating("user_rating"),
    UserFavorite("user_favorite");

    private final String value;

    MovieFetchType(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }
}
