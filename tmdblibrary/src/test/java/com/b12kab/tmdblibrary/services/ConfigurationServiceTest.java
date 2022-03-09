/*
 * Unknown author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Keith Beatty 2017 - Converted to jUnit 4.12
 */

package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.assertations.ConfigurationAsserts;
import com.b12kab.tmdblibrary.entities.Configuration;
import com.b12kab.tmdblibrary.entities.ConfigurationCountries;
import com.b12kab.tmdblibrary.entities.ConfigurationLanguages;
import com.b12kab.tmdblibrary.entities.Jobs;
import com.b12kab.tmdblibrary.entities.Timezones;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ConfigurationServiceTest extends BaseTestCase {

    @Test
    public void test_configuration() {
        Configuration config = null;

        try {
            config = getManager().configurationService().configuration().execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on test_configuration: " + e.toString());
        }

        ConfigurationAsserts.assertApiConfiguration(config);
    }

    @Test
    public void test_config_countries() {
        List<ConfigurationCountries> configuration = null;

        try {
            configuration = getManager().configurationService().countries().execute().body();
        } catch (Exception e) {
            fail("Exception occurred on test_config_countries: " + e.toString());
        }

        assertNotNull(configuration, "countries is null");
        assertTrue(configuration.size() > 0, "countries is empty");
    }

    @Test
    public void test_config_jobs() {
        List<Jobs> jobsList = null;

        try {
            jobsList = getManager().configurationService().jobs().execute().body();
        } catch (Exception e) {
            fail("Exception occurred on test_config_jobs: " + e.toString());
        }

        assertNotNull(jobsList, "jobs is null");
        assertTrue(jobsList.size() > 0, "jobs is empty");
    }

    @Test
    public void test_config_languages() {
        ConfigurationLanguages configuration = null;

        try {
            configuration = getManager().configurationService().languages().execute().body();
        } catch (Exception e) {
            fail("Exception occurred on test_config_languages: " + e.toString());
        }

        ConfigurationAsserts.assertConfigLanguage(configuration);
    }

    @Test
    public void test_config_translations() {
        List<String> configuration = null;

        try {
            configuration = getManager().configurationService().translations().execute().body();
        } catch (Exception e) {
            fail("Exception occurred on test_config_translations: " + e.toString());
        }

        assertNotNull(configuration, "translations is null");
        assertTrue(configuration.size() > 0, "translations is empty");
    }

    @Test
    public void test_config_timezones() {
        Timezones configuration = null;

        try {
            configuration = getManager().configurationService().timezones().execute().body();
        } catch (Exception e) {
            fail("Exception occurred on test_config_languages: " + e.toString());
        }

        assertNotNull(configuration, "translations is null");
        assertTrue(configuration.size() > 0, "translations is empty");
        assertNotNull(configuration.get(0).iso_3166_1);
        assertNotNull(configuration.get(0).zones);
        HashMap<String, List<String>> mappy = configuration.createMap();
        int a = 1;
        assertNotNull(mappy);
        assertNotNull(mappy.get("US"));
        assertTrue(mappy.get("US").size() > 0);
    }
}
