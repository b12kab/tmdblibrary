package com.b12kab.tmdblibrary.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigurationLanguages extends ArrayList<ConfigurationLanguages.ConfigurationLanguage>  {

    public static class BaseConfigurationLanguage {
        @SerializedName("english_name")
        String englishName;
        @SerializedName("name")
        String name;

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ConfigurationLanguage extends BaseConfigurationLanguage {
        @SerializedName("iso_639_1")
        String iso_639_1;

        public String getIso_639_1() {
            return iso_639_1;
        }

        public void setIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public BaseConfigurationLanguage getBase() {
            return this;
        }
    }

    public HashMap<String, BaseConfigurationLanguage> createMap() {
        HashMap<String, BaseConfigurationLanguage> hashMap = new HashMap<>();

        for (ConfigurationLanguages.ConfigurationLanguage language : this) {
            BaseConfigurationLanguage base = language.getBase();

            hashMap.put(language.getIso_639_1(), base);
        }

        return hashMap;
    }
}
