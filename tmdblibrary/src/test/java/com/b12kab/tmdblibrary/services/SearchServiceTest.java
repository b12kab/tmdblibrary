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

import junit.framework.Assert;

import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SearchServiceTest extends BaseTestCase {

    @Test
    public void test_companySearch() throws ParseException {
        final String funcName = "test_companySearch ";
        CompanyResultsPage companyResults = getManager().searchService().company("Sony Pictures", null);
        
        assertResultsPage(funcName, companyResults);
        assertNotNull(funcName + "companyResults results is null", companyResults.results);
        assertTrue(funcName + "companyResults results is < 1", companyResults.results.size() > 0);
        assertNotNull(funcName + "companyResults results.get(0) is null", companyResults.results.get(0));
        assertNotNull(funcName + "companyResults id is null", companyResults.results.get(0).id);
        assertNotNull(funcName + "companyResults logo_path is null", companyResults.results.get(0).logo_path);
    }
    
    @Test
    public void test_collectionSearch() throws ParseException {
        final String funcName = "test_collectionSearch ";
        CollectionResultsPage collectionResults = getManager().searchService().collection("The Avengers Collection",
                null, null);
        
        assertResultsPage(funcName, collectionResults);

        assertNotNull(funcName + "collectionResults results is null", collectionResults.results);
        assertTrue(funcName + "collectionResults results is < 1", collectionResults.results.size() > 0);
        assertNotNull(funcName + "collectionResults id is null", collectionResults.results.get(0).id);
        assertNotNull(funcName + "collectionResults backdrop_path is null", collectionResults.results.get(0).backdrop_path);
        assertNotNull(funcName + "collectionResults name is null", collectionResults.results.get(0).name);
        assertNotNull(funcName + "collectionResults poster_path is null", collectionResults.results.get(0).poster_path);
    }
    
    @Test
    public void test_keywordSearch() throws ParseException {
        final String funcName = "test_keywordSearch ";
        KeywordResultsPage keywordResults = getManager().searchService().keyword("fight", null);
        
        assertResultsPage(funcName, keywordResults);
        assertNotNull(funcName + "keywordResults results is null", keywordResults.results);
        assertTrue(funcName + "keywordResults results is < 1", keywordResults.results.size() > 0);
        assertNotNull(funcName + "keywordResults id is null", keywordResults.results.get(0).id);
        assertNotNull(funcName + "keywordResults name is null", keywordResults.results.get(0).name);
    }
    
    @Test
    public void test_movieSearch() throws ParseException {
        final String funcName = "test_movieSearch ";
        MovieResultsPage movieResults = getManager().searchService().movie(TestData.MOVIE_TITLE, null, null,
                null, null, null, null);
        
        assertResultsPage(funcName, movieResults);
        assertNotNull(funcName + "movieResults results is null", movieResults.results);
        assertTrue(funcName + "movieResults results is < 1", movieResults.results.size() > 0);
    }
    
    @Test
    public void test_personSearch() throws ParseException {
        final String funcName = "test_personSearch ";
        PersonResultsPage movieResults = getManager().searchService().person(TestData.PERSON_NAME, null, null, null);
        
        assertResultsPage(funcName, movieResults);
        assertNotNull(funcName + "movieResults results.get(0) is null", movieResults.results.get(0));
        assertNotNull(funcName + "movieResults id is null", movieResults.results.get(0).id);
        assertNotNull(funcName + "movieResults name is null", movieResults.results.get(0).name);
        assertNotNull(funcName + "movieResults popularity is null", movieResults.results.get(0).popularity);
        assertNotNull(funcName + "movieResults profile_path is null", movieResults.results.get(0).profile_path);
        assertNotNull(funcName + "movieResults adult is null", movieResults.results.get(0).adult);

        for (Media media : movieResults.results.get(0).known_for) {
            assertNotNull(funcName + "media is null", media);
            assertNotNull(funcName + "media id is null", media.id);
            assertNotNull(funcName + "media adult is null", media.adult);
            assertNotNull(funcName + "media backdrop_path is null", media.backdrop_path);
            assertNotNull(funcName + "media original_title is null", media.original_title);
            assertNotNull(funcName + "media release_date is null", media.release_date);
            assertNotNull(funcName + "media poster_path is null", media.poster_path);
            assertNotNull(funcName + "media popularity is null", media.popularity);
            assertTrue(funcName + "media popularity results is < 1", media.popularity > 0);
            assertNotNull(funcName + "media title is null", media.title);

            assertNotNull(funcName + "media vote_average is null", media.vote_average);
            assertTrue(funcName + "media vote_average is < 1", media.vote_average > 0);
            assertNotNull(funcName + "media vote_count is null", media.vote_count);
            assertTrue(funcName + "media vote_count is < 1", media.vote_count > 0);
            assertNotNull(funcName + "media media_type is null", media.media_type);
        }
        
    }

    @Test
    public void test_tv() {
        final String funcName = "test_tv ";
        TvResultsPage tvResults = getManager().searchService().tv(TestData.TVSHOW_TITLE, null, null, null, null);
        
        assertResultsPage(funcName, tvResults);

        assertNotNull(funcName + "tvResults is null", tvResults);
        assertNotNull(funcName + "tvResults results is null", tvResults.results);
        assertTrue(funcName + "tvResults results List < 1", tvResults.results.size() > 0);
        assertNotNull(funcName + "tvResults get(0) is null", tvResults.results.get(0));
        assertNotNull(funcName + "tvResults name is null", tvResults.results.get(0).name);
        assertTrue(funcName + "tvResults name is not equal to " + TestData.TVSHOW_TITLE,
                tvResults.results.get(0).name.equals(TestData.TVSHOW_TITLE));
    }

    private void assertResultsPage(String funcName, BaseResultsPage results) {
        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results page is null", results.page);
        assertTrue(funcName + "results page is < 1", results.page > 0);
        assertNotNull(funcName + "results total_pages is null", results.total_pages);
        assertTrue(funcName + "results total_pages is < 1", results.total_pages > 0);
        assertNotNull(funcName + "results total_results is null", results.total_results);
        assertTrue(funcName + "results total_results is < 1", results.total_results > 0);
    }

}
