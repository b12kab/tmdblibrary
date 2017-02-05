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
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigurationServiceTest extends BaseTestCase {

    @Test
    public void test_configuration() {
        Configuration config = getManager().configurationService().configuration();
        assertNotNull("Configuration is null", config);
        assertNotNull("Configuration images is null", config.images);

        assertNotNull("Configuration images base_url is null", config.images.base_url);
        assertFalse("Configuration images base_url string length is empty", config.images.base_url.isEmpty());

        assertNotNull("Configuration images secure_base_url is null", config.images.secure_base_url);
        assertFalse("Configuration images secure_base_url string is empty", config.images.secure_base_url.isEmpty());

        assertNotNull("Configuration images poster_sizes is null", config.images.poster_sizes);
        assertNotEquals("Configuration images poster_sizes List length is 0", config.images.poster_sizes.size(), 0);

        assertNotNull("Configuration images backdrop_sizes is null", config.images.backdrop_sizes);
        assertNotEquals("Configuration images backdrop_sizes List length is 0", config.images.backdrop_sizes.size(), 0);

        assertNotNull("Configuration images profile_sizes is null", config.images.profile_sizes);
        assertNotEquals("Configuration images profile_sizes List length is 0", config.images.profile_sizes.size(), 0);

        assertNotNull("Configuration images logo_sizes is null", config.images.logo_sizes);
        assertNotEquals("Configuration images logo_sizes List length is 0", config.images.logo_sizes.size(), 0);

        assertNotNull("Configuration images change_keys is null", config.change_keys);
        assertNotEquals("Configuration images change_keys List length is 0", config.change_keys.size(), 0);
    }
}
