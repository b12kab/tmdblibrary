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
import com.b12kab.tmdblibrary.entities.Configuration;
import com.b12kab.tmdblibrary.entities.Jobs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        assertNotNull(config, "Configuration is null");
        assertNotNull(config.images, "Configuration images is null");

        assertNotNull(config.images.base_url, "Configuration images base_url is null");
        assertFalse(config.images.base_url.isEmpty(), "Configuration images base_url string length is empty" );

        assertNotNull(config.images.secure_base_url, "Configuration images secure_base_url is null");
        assertFalse(config.images.secure_base_url.isEmpty(), "Configuration images secure_base_url string is empty" );

        assertNotNull(config.images.poster_sizes, "Configuration images poster_sizes is null");
        assertNotEquals(config.images.poster_sizes.size(), 0, "Configuration images poster_sizes List length is 0" );

        assertNotNull(config.images.backdrop_sizes, "Configuration images backdrop_sizes is null");
        assertNotEquals(config.images.backdrop_sizes.size(), 0, "Configuration images backdrop_sizes List length is 0");

        assertNotNull(config.images.profile_sizes, "Configuration images profile_sizes is null");
        assertNotEquals(config.images.profile_sizes.size(), 0, "Configuration images profile_sizes List length is 0");

        assertNotNull(config.images.logo_sizes, "Configuration images logo_sizes is null");
        assertNotEquals(config.images.logo_sizes.size(), 0, "Configuration images logo_sizes List length is 0");

        assertNotNull(config.images.still_sizes, "Configuration images still_sizes is null");
        assertNotEquals(config.images.still_sizes.size(), 0, "Configuration images still_sizes List length is 0");

        assertNotNull(config.change_keys, "Configuration images change_keys is null");
        assertNotEquals(config.change_keys.size(), 0, "Configuration images change_keys List length is 0");
    }

    @Test
    public void test_config_jobs() {
        List<Jobs> jobsList = null;

        try {
            jobsList = getManager().configurationService().jobs().execute().body();
        } catch (Exception e) {
            fail("Exception occurred on test_configuration: " + e.toString());
        }

        assertNotNull(jobsList, "jobs is null");
        assertTrue(jobsList.size() > 0, "Configuration images is null");
    }
}
