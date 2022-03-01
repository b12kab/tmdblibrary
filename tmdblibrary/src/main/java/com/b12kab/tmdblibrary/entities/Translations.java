package com.b12kab.tmdblibrary.entities;

import java.util.List;

public class Translations {

    public static class TranslationData {
        public String title;
        public String overview;
        public String homepage;
    }

    public static class Translation {
        public String iso_3166_1;
        public String iso_639_1;
        public String name;
        public String english_name;
        public TranslationData data;
    }

    public Integer id;
    public List<Translation> translations;
}
