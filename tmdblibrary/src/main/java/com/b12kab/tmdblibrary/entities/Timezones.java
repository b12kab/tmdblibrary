package com.b12kab.tmdblibrary.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class Timezones extends ArrayList<Timezones.Timezone>
{
    public static class Timezone {
        public String iso_3166_1;
        public List<String> zones;
    }

    public HashMap<String, List<String>> createMap() {
        HashMap<String, List<String>> hashMap = new HashMap<>();

        for (Timezone zone : this) {
            hashMap.put(zone.iso_3166_1, zone.zones);
        }

        return hashMap;
    }
}
