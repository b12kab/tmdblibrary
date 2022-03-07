/*
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
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.entities.BaseResultsPage;
import com.b12kab.tmdblibrary.entities.CollectionResultsPage;
import com.b12kab.tmdblibrary.entities.CompanyResultsPage;
import com.b12kab.tmdblibrary.entities.KeywordResultsPage;
import com.b12kab.tmdblibrary.entities.Media;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.PersonResultsPage;
import com.b12kab.tmdblibrary.entities.TvResultsPage;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SearchServiceTest extends BaseTestCase {

    @Test
    public void test_companySearch() throws ParseException {
        final String funcName = "test_companySearch ";
        CompanyResultsPage companyResults = null;

        try {
            companyResults = getManager().searchService().company("Sony Pictures", null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertResultsPage(funcName, companyResults);
        assertNotNull(companyResults.results, funcName + "companyResults results is null");
        assertTrue(companyResults.results.size() > 0, funcName + "companyResults results is < 1");
        assertNotNull(companyResults.results.get(0), funcName + "companyResults results.get(0) is null");
        assertNotNull(companyResults.results.get(0).id, funcName + "companyResults id is null");
        assertNotNull(companyResults.results.get(0).logo_path, funcName + "companyResults logo_path is null");
    }
    
    @Test
    public void test_collectionSearch() throws ParseException {
        final String funcName = "test_collectionSearch ";
        CollectionResultsPage collectionResults = null;

        try {
            collectionResults = getManager().searchService().collection("The Avengers Collection",
                    null, null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertResultsPage(funcName, collectionResults);

        assertNotNull(collectionResults.results, funcName + "collectionResults results is null");
        assertTrue(collectionResults.results.size() > 0, funcName + "collectionResults results is < 1");
        assertNotNull(collectionResults.results.get(0).id, funcName + "collectionResults id is null");
        assertNotNull(collectionResults.results.get(0).backdrop_path, funcName + "collectionResults backdrop_path is null");
        assertNotNull(collectionResults.results.get(0).name, funcName + "collectionResults name is null");
        assertNotNull(collectionResults.results.get(0).poster_path, funcName + "collectionResults poster_path is null");
    }
    
    @Test
    public void test_keywordSearch() throws ParseException {
        final String funcName = "test_keywordSearch ";
        KeywordResultsPage keywordResults = null;

        try {
            keywordResults = getManager().searchService().keyword("fight", null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertResultsPage(funcName, keywordResults);
        assertNotNull(keywordResults.results, funcName + "keywordResults results is null");
        assertTrue(keywordResults.results.size() > 0, funcName + "keywordResults results is < 1");
        assertNotNull(keywordResults.results.get(0).id, funcName + "keywordResults id is null");
        assertNotNull(keywordResults.results.get(0).name, funcName + "keywordResults name is null");
    }
    
    @Test
    public void test_movieSearch() throws ParseException {
        final String funcName = "test_movieSearch ";
        MovieResultsPage movieResults = null;
        try {
            movieResults = getManager().searchService().movie(TestData.MOVIE_TITLE, null, null,
                    null, null, null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertResultsPage(funcName, movieResults);
        assertNotNull(movieResults.results, funcName + "movieResults results is null");
        assertTrue( movieResults.results.size() > 0, funcName + "movieResults results is < 1");
    }
    
    @Test
    public void test_personSearch() throws ParseException {
        final String funcName = "test_personSearch ";
        PersonResultsPage movieResults = null;

        try {
            movieResults = getManager().searchService().person(TestData.PERSON_NAME, null, null, null, null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertResultsPage(funcName, movieResults);
        assertNotNull(movieResults.results.get(0), funcName + "movieResults results.get(0) is null");
        assertNotNull(movieResults.results.get(0).id, funcName + "movieResults id is null");
        assertNotNull(movieResults.results.get(0).name, funcName + "movieResults name is null");
        assertNotNull(movieResults.results.get(0).popularity, funcName + "movieResults popularity is null");
        assertNotNull(movieResults.results.get(0).profile_path, funcName + "movieResults profile_path is null");
        assertNotNull(movieResults.results.get(0).adult, funcName + "movieResults adult is null");

        for (Media media : movieResults.results.get(0).known_for) {
            assertNotNull(media, funcName + "media is null");
            assertNotNull(media.id, funcName + "media id is null");
            assertNotNull(media.adult, funcName + "media adult is null");
            assertNotNull(media.backdrop_path, funcName + "media backdrop_path is null");
            assertNotNull(media.original_title, funcName + "media original_title is null");
            assertNotNull(media.release_date, funcName + "media release_date is null");
            assertNotNull(media.poster_path, funcName + "media poster_path is null");
//            assertNotNull(media.popularity, funcName + "media popularity is null");
//            assertTrue(media.popularity > 0, funcName + "media popularity results is < 1");
            assertNotNull(media.title, funcName + "media title is null");

            assertNotNull(media.vote_average, funcName + "media vote_average is null");
            assertTrue(media.vote_average > 0, funcName + "media vote_average is < 1");
            assertNotNull(media.vote_count, funcName + "media vote_count is null");
            assertTrue(media.vote_count > 0, funcName + "media vote_count is < 1");
            assertNotNull(media.media_type, funcName + "media media_type is null");
        }
    }

    @Test
    public void test_tv() {
        final String funcName = "test_tv ";
        TvResultsPage tvResults = null;

        try {
            tvResults = getManager().searchService().tv(TestData.TVSHOW_TITLE, null, null, null, null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertResultsPage(funcName, tvResults);

        assertNotNull(tvResults, funcName + "tvResults is null");
        assertNotNull(tvResults.results, funcName + "tvResults results is null");
        assertTrue(tvResults.results.size() > 0, funcName + "tvResults results List < 1");
        assertNotNull(tvResults.results.get(0), funcName + "tvResults get(0) is null");
        assertNotNull(tvResults.results.get(0).name, funcName + "tvResults name is null");
        assertEquals(TestData.TVSHOW_TITLE, tvResults.results.get(0).name, funcName + "tvResults name is not equal to " + TestData.TVSHOW_TITLE);
    }

    private void assertResultsPage(String funcName, BaseResultsPage results) {
        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.page, funcName + "results page is null");
        assertTrue(results.page > 0, funcName + "results page is < 1");
        assertNotNull(results.total_pages, funcName + "results total_pages is null");
        assertTrue(results.total_pages > 0, funcName + "results total_pages is < 1");
        assertNotNull(results.total_results, funcName + "results total_results is null");
        assertTrue(results.total_results > 0, funcName + "results total_results is < 1");
    }
}
