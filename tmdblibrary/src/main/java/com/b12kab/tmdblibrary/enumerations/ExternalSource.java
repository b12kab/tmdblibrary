package com.b12kab.tmdblibrary.enumerations;

public enum ExternalSource {

    IMDB_ID("imdb_id"),
    FREEBASE_MID("freebase_mid"),
    FREEBASE_ID("freebase_id"),
    TVRAGE_ID("tvrage_id"),
    TVDB_ID("tvdb_id");

    private final String value;

    private ExternalSource(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
