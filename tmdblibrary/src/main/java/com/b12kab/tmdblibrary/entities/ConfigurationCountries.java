package com.b12kab.tmdblibrary.entities;

import com.google.gson.annotations.SerializedName;

public class ConfigurationCountries {
    @SerializedName("iso_3166_1")
    String iso_3166_1;

    @SerializedName("english_name")
    String englishName;

    @SerializedName("native_name")
    String nativeName;

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }
}
