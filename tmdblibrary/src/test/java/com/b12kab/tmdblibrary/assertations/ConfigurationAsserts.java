package com.b12kab.tmdblibrary.assertations;

import com.b12kab.tmdblibrary.entities.Configuration;
import com.b12kab.tmdblibrary.entities.ConfigurationLanguages;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigurationAsserts {

    public static void assertApiConfiguration(Configuration configuration) {
        assertNotNull(configuration, "Configuration is null");
        assertNotNull(configuration.images, "Configuration images is null");

        assertNotNull(configuration.images.base_url, "Configuration images base_url is null");
        assertFalse(configuration.images.base_url.isEmpty(), "Configuration images base_url string length is empty" );

        assertNotNull(configuration.images.secure_base_url, "Configuration images secure_base_url is null");
        assertFalse(configuration.images.secure_base_url.isEmpty(), "Configuration images secure_base_url string is empty" );

        assertNotNull(configuration.images.poster_sizes, "Configuration images poster_sizes is null");
        assertNotEquals(configuration.images.poster_sizes.size(), 0, "Configuration images poster_sizes List length is 0" );

        assertNotNull(configuration.images.backdrop_sizes, "Configuration images backdrop_sizes is null");
        assertNotEquals(configuration.images.backdrop_sizes.size(), 0, "Configuration images backdrop_sizes List length is 0");

        assertNotNull(configuration.images.profile_sizes, "Configuration images profile_sizes is null");
        assertNotEquals(configuration.images.profile_sizes.size(), 0, "Configuration images profile_sizes List length is 0");

        assertNotNull(configuration.images.logo_sizes, "Configuration images logo_sizes is null");
        assertNotEquals(configuration.images.logo_sizes.size(), 0, "Configuration images logo_sizes List length is 0");

        assertNotNull(configuration.images.still_sizes, "Configuration images still_sizes is null");
        assertNotEquals(configuration.images.still_sizes.size(), 0, "Configuration images still_sizes List length is 0");

        assertNotNull(configuration.change_keys, "Configuration images change_keys is null");
        assertNotEquals(configuration.change_keys.size(), 0, "Configuration images change_keys List length is 0");
    }

    public static void assertConfigLanguage(ConfigurationLanguages configuration) {
        assertNotNull(configuration, "language is null");
        assertNotEquals(0, configuration.size(), "language is empty");

        HashMap<String, ConfigurationLanguages.BaseConfigurationLanguage> mappy = configuration.createMap();
        assertNotNull(mappy);
        assertNotNull(mappy.get("zh"));
        ConfigurationLanguages.BaseConfigurationLanguage bl = mappy.get("zh");
        assertNotNull(bl);
        assertNotNull(bl.getName());
        assertNotNull(bl.getEnglishName());
    }
}
