/*
 * Copyright 2015 Miguel Teixeira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Keith Beatty 2017 - Converted to jUnit 4.12
 * Keith Beatty 2019 - Converted to jUnit 5.x
 */

package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.entities.AppendToDiscoverResponse;
import com.b12kab.tmdblibrary.entities.BaseResultsPage;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.TvResultsPage;
import com.b12kab.tmdblibrary.enumerations.SortBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class DiscoverServiceTest extends BaseTestCase {
    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd");

    @Test
    public void test_discover_movie() throws ParseException {
        MovieResultsPage results = null;

        try {
            results = getManager().discoverService().discoverMovie(
                false,
                true,
                null,
                1,
                null,
                "1990-01-01",
                null,
                "1990-01-01",
                null,
                SortBy.POPULARITY_DESC,
                null,
                null,
                null,
                null,
                new AppendToDiscoverResponse(287),
                new AppendToDiscoverResponse(7467),
                null,
                new AppendToDiscoverResponse(10749),
                null,
                null,
                null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on test_discover_movie: " + e.toString());
        }

        assertResultsPage(results);
        assertNotNull(results.getResults(), "results is null" );
        assertTrue(results.getResults().size() > 0, "results size < 1");
    }

    @Test
    public void test_discover_tv() throws ParseException {
        TvResultsPage results = null;
        try
        {
            results = getManager().discoverService().discoverTv(
                null,
                null,
                SortBy.VOTE_AVERAGE_DESC,
                null,
                null,
                null,
                new AppendToDiscoverResponse(18, 10765),
                new AppendToDiscoverResponse(49),
                "2010-01-01",
                "2014-01-01").execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on test_discover_tv: " + e.toString());
        }

        assertResultsPage(results);
        assertNotNull(results.results, "results is null");
        assertTrue(results.results.size() > 0, "results size < 1");
    }

    private void assertResultsPage(BaseResultsPage results) {
        assertNotNull(results);
        assertTrue(results.page > 0, "BaseResultsPage results List length is < 1");
        assertTrue(results.total_pages > 0, "BaseResultsPage results total_pages length is < 1");
        assertTrue(results.total_results > 0, "BaseResultsPage results total_results length is < 1");
    }
}
