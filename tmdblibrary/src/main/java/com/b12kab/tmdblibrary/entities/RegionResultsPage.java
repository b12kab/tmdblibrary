package com.b12kab.tmdblibrary.entities;

import java.util.List;

public class RegionResultsPage {

    public static class Region {
        public String iso_3166_1;
        public String english_name;
        public String native_name;
    }

    public List<Region> results;
}
