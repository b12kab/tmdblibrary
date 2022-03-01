package com.b12kab.tmdblibrary.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/***
 * Link to this page to display all options and provide deep links to the actual providers (and to support TMDb)
 */
public class WatchProviders {

    public static class CountryInfo {
        public String link;
        @NonNull public java.util.List<WatchProvider> flatrate = new ArrayList<>();
        @NonNull public java.util.List<WatchProvider> free = new ArrayList<>();
        @NonNull public java.util.List<WatchProvider> ads = new ArrayList<>();
        @NonNull public java.util.List<WatchProvider> buy = new ArrayList<>();
    }

    public Integer id;
    /**
     * Mapped by ISO 3166-1 two letter country code, e.g. DE and US.
     */
    @NonNull public Map<String, CountryInfo> results = new HashMap<>();
}
