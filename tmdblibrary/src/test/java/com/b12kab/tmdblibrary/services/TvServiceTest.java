/*
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
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.CastMember;
import com.b12kab.tmdblibrary.entities.CreditResults;
import com.b12kab.tmdblibrary.entities.CrewMember;
import com.b12kab.tmdblibrary.entities.ExternalIds;
import com.b12kab.tmdblibrary.entities.Image;
import com.b12kab.tmdblibrary.entities.Images;
import com.b12kab.tmdblibrary.entities.Person;
import com.b12kab.tmdblibrary.entities.TvAlternativeTitles;
import com.b12kab.tmdblibrary.entities.TvKeywords;
import com.b12kab.tmdblibrary.entities.TvResultsPage;
import com.b12kab.tmdblibrary.entities.TvSeason;
import com.b12kab.tmdblibrary.entities.TvShowComplete;
import com.b12kab.tmdblibrary.entities.VideoResults;
import com.b12kab.tmdblibrary.enumerations.AppendToResponseItem;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TvServiceTest extends BaseTestCase {

    @Test
    public void test_tvshow() {
        final String funcName = "test_tvshow ";
        TvShowComplete show = getManager().tvService().tv(TestData.TVSHOW_ID, null, null);
        assertTvShow(funcName, show);
    }

    @Test
    public void test_tvshow_with_append_to_response() {
        final String funcName = "test_tvshow_with_append_to_response ";
        TvShowComplete show = getManager().tvService().tv(TestData.TVSHOW_ID, null,
                new AppendToResponse(AppendToResponseItem.CREDITS, AppendToResponseItem.EXTERNAL_IDS, AppendToResponseItem.IMAGES));
        assertTvShow(funcName, show);

        // credits
        assertNotNull(funcName + "show credits is null", show.credits);

        assertCrewCredits(funcName, show.credits.getCrew());
        assertCastCredits(funcName, show.credits.getCast());

        // images
        assertNotNull(funcName + "show images is null", show.images);
        assertImages(funcName, show.images.backdrops);
        assertImages(funcName, show.images.posters);

        // external ids
        assertNotNull(funcName + "show external_ids is null", show.external_ids);
        assertNotNull(funcName + "show freebase_id is null", show.external_ids.freebase_id);
        assertNotNull(funcName + "show freebase_mid is null", show.external_ids.freebase_mid);
        assertNotNull(funcName + "show tvdb_id is null", show.external_ids.tvdb_id);
        assertNotNull(funcName + "show imdb_id is null", show.external_ids.imdb_id);
        assertNotNull(funcName + "show tvrage_id is null", show.external_ids.tvrage_id);
    }
    
    @Test
    public void test_alternative_titles() {
        final String funcName = "test_alternative_titles ";
        TvAlternativeTitles titles = getManager().tvService().alternativeTitles(TestData.TVSHOW_ID);

        assertNotNull(funcName + "titles is null", titles);
        assertNotNull(funcName + "titles id is null", titles.id);
        assertTrue(funcName + "titles id is != " + TestData.TVSHOW_ID,
                titles.id == TestData.TVSHOW_ID);
        assertNotNull(funcName + "titles results is null", titles.results);
        assertTrue(funcName + "titles results size < 1", titles.results.size() > 0);
        assertNotNull(funcName + "titles results get(0) is null", titles.results.get(0));
        assertNotNull(funcName + "titles iso_3166_1 is null", titles.results.get(0).iso_3166_1);
        assertNotNull(funcName + "titles title is null", titles.results.get(0).title);
    }
    
    @Test
    public void test_credits() {
        final String funcName = "test_credits ";
        CreditResults credits = getManager().tvService().credits(TestData.TVSHOW_ID, null);

        assertNotNull(funcName + "credits is null", credits);
        assertCrewCredits(funcName, credits.getCrew());
        assertCastCredits(funcName, credits.getCast());
    }
    
    @Test
    public void test_externalIds() {
        final String funcName = "test_externalIds ";
        ExternalIds ids = getManager().tvService().externalIds(TestData.TVSHOW_ID, null);

        assertNotNull(funcName + "ids is null", ids);
        assertNotNull(funcName + "ids id is null", ids.id);
        assertNotNull(funcName + "ids freebase_id is null", ids.freebase_id);
        assertNotNull(funcName + "ids freebase_mid is null", ids.freebase_mid);
        assertNotNull(funcName + "ids tvdb_id is null", ids.tvdb_id);
        assertNotNull(funcName + "ids imdb_id is null", ids.imdb_id);
        assertNotNull(funcName + "ids tvrage_id is null", ids.tvrage_id);
    }
    
    @Test
    public void test_images() {
        final String funcName = "test_images ";
        Images images = getManager().tvService().images(TestData.TVSHOW_ID, null);

        assertNotNull(funcName + "images is null", images);
        assertNotNull(funcName + "images id is null", images.id);
        assertTrue(funcName + "images id is != " + TestData.TVSHOW_ID, images.id == TestData.TVSHOW_ID);
        assertImages(funcName, images.backdrops);
        assertImages(funcName, images.posters);
    }
    
    @Test
    public void test_keywords() {
        final String funcName = "test_keywords ";
        TvKeywords keywords = getManager().tvService().keywords(TestData.TVSHOW_ID);

        assertNotNull(funcName + "keywords is null", keywords);
        assertNotNull(funcName + "keywords id is null", keywords.id);
        assertTrue(funcName + "keywords id is != " + TestData.TVSHOW_ID, keywords.id == TestData.TVSHOW_ID);
        assertNotNull(funcName + "keywords results is null", keywords.results);
        assertTrue(funcName + "keywords results size < 1", keywords.results.size() > 0);
        assertNotNull(funcName + "keywords get(0) is null", keywords.results.get(0));
        assertNotNull(funcName + "keywords id is null", keywords.results.get(0).id);
        assertNotNull(funcName + "keywords name is null", keywords.results.get(0).name);
    }
    
    @Test
    public void test_similar() {
        final String funcName = "test_similar ";
        TvResultsPage tvResultsPage = getManager().tvService().similar(TestData.TVSHOW_ID, 1, null);

        assertNotNull(funcName + "tvResultsPage is null", tvResultsPage);
        assertNotNull(funcName + "tvResultsPage page is null", tvResultsPage.page);
        assertTrue(funcName + "tvResultsPage page is < 1", tvResultsPage.page > 0);
        assertNotNull(funcName + "tvResultsPage total_pages is null", tvResultsPage.total_pages);
        assertTrue(funcName + "tvResultsPage total_pages is < 1", tvResultsPage.total_pages > 0);
        assertNotNull(funcName + "tvResultsPage total_results is null", tvResultsPage.total_results);
        assertTrue(funcName + "tvResultsPage total_results is < 1", tvResultsPage.total_results > 0);

        assertNotNull(funcName + "tvResultsPage results is null", tvResultsPage.results);
        assertTrue(funcName + "tvResultsPage results is < 1", tvResultsPage.results.size() > 0);
        assertNotNull(funcName + "tvResultsPage id is null", tvResultsPage.results.get(0).id);
        assertNotNull(funcName + "tvResultsPage backdrop_path is null", tvResultsPage.results.get(0).backdrop_path);
        assertNotNull(funcName + "tvResultsPage name is null", tvResultsPage.results.get(0).name);
        assertNotNull(funcName + "tvResultsPage poster_path is null", tvResultsPage.results.get(0).poster_path);
        assertNotNull(funcName + "tvResultsPage popularity is null", tvResultsPage.results.get(0).popularity);
        assertNotNull(funcName + "tvResultsPage name is null", tvResultsPage.results.get(0).name);
        assertNotNull(funcName + "tvResultsPage vote_average is null", tvResultsPage.results.get(0).vote_average);
        assertTrue(funcName + "tvResultsPage vote_average is < 1", tvResultsPage.results.get(0).vote_average > 0);
        assertNotNull(funcName + "tvResultsPage vote_count is null", tvResultsPage.results.get(0).vote_count);
        assertTrue(funcName + "tvResultsPage vote_count is < 1", tvResultsPage.results.get(0).vote_count > 0);
    }
    
    @Test
    public void test_videos() {
        final String funcName = "test_videos ";
        VideoResults videoResults = getManager().tvService().videos(TestData.TVSHOW_ID, null);

        assertNotNull(funcName + "videoResults is null", videoResults);
        assertNotNull(funcName + "videoResults results is null", videoResults.results);
        assertTrue(funcName + "videoResults results is < 1", videoResults.results.size() > 0);
        assertNotNull(funcName + "videoResults results get(0) is null", videoResults.results.get(0));
        assertNotNull(funcName + "videoResults id is null", videoResults.results.get(0).getId());
        assertNotNull(funcName + "videoResults iso_639_1 is null", videoResults.results.get(0).getIso_639_1());
        assertNotNull(funcName + "videoResults key is null", videoResults.results.get(0).getKey());
        assertNotNull(funcName + "videoResults name is null", videoResults.results.get(0).getName());
        assertNotNull(funcName + "videoResults site is null", videoResults.results.get(0).getSite());
        assertTrue(funcName + "videoResults site is != 'YouTube'",
                videoResults.results.get(0).getSite().equals("YouTube"));
        assertNotNull(funcName + "videoResults site is null", videoResults.results.get(0).getSite());
        assertNotNull(funcName + "videoResults type is null", videoResults.results.get(0).getType());
        assertTrue(funcName + "videoResults type is != 'Trailer'",
                videoResults.results.get(0).getType().equals("Trailer"));
    }
    
    @Test
    public void test_latest() {
        final String funcName = "test_latest ";
        TvShowComplete show = getManager().tvService().latest();

        // Latest show might not have a complete TMDb entry, but at should least some basic properties.
        assertNotNull(funcName + "show is null", show);
        assertNotNull(funcName + "show id is null", show.id);
        assertTrue(funcName + "show id is < 1", show.id > 0);
        assertNotNull(funcName + "show name is null", show.name);
    }
    
    @Test
    public void test_onTheAir() {
        final String funcName = "test_onTheAir ";
        TvResultsPage results = getManager().tvService().onTheAir(null, null);

        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results results is null", results.results);
        assertTrue(funcName + "results results is < 1", results.results.size() > 0);
    }
    
    @Test
    public void test_airingToday() {
        final String funcName = "test_airingToday ";
        TvResultsPage results = getManager().tvService().airingToday(null, null);

        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results results is null", results.results);
        assertTrue(funcName + "results results is < 1", results.results.size() > 0);
    }
    
    @Test
    public void test_topRated() {
        final String funcName = "test_topRated ";
        TvResultsPage results = getManager().tvService().topRated(null, null);

        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results results is null", results.results);
        assertTrue(funcName + "results results is < 1", results.results.size() > 0);
    }
    
    @Test
    public void test_popular() {
        final String funcName = "test_popular ";
        TvResultsPage results = getManager().tvService().popular(null, null);

        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results results is null", results.results);
        assertTrue(funcName + "results results is < 1", results.results.size() > 0);
    }
    
    private void assertTvShow(String funcName, TvShowComplete show) {
        assertNotNull(funcName + "show is null", show);
        assertNotNull(funcName + "show first_air_date is null", show.first_air_date);
        assertNotNull(funcName + "show homepage is null", show.homepage);
        assertNotNull(funcName + "show id is null", show.id);
        assertNotNull(funcName + "show in_production is null", show.in_production);
        assertNotNull(funcName + "show languages is null", show.languages);
        assertNotNull(funcName + "show last_air_date is null", show.last_air_date);
        assertNotNull(funcName + "show name is null", show.name);
        assertNotNull(funcName + "show number_of_seasons is null", show.number_of_seasons);
        assertNotNull(funcName + "show original_language is null", show.original_language);
        assertNotNull(funcName + "show original_name is null", show.original_name);
        assertNotNull(funcName + "show overview is null", show.overview);
        assertNotNull(funcName + "show popularity is null", show.popularity);
        assertTrue(funcName + "show popularity is < 0", show.popularity >= 0);
        assertNotNull(funcName + "show poster_path is null", show.poster_path);
        assertNotNull(funcName + "show status is null", show.status);
        assertNotNull(funcName + "show type is null", show.type);
        assertNotNull(funcName + "show vote_average is null", show.vote_average);
        assertTrue(funcName + "show vote_average is < 1", show.vote_average > 0);
        assertNotNull(funcName + "show vote_count is null", show.vote_count);
        assertTrue(funcName + "show vote_count is < 1", show.vote_count > 0);
        assertNotNull(funcName + "show created_by is null", show.created_by);

        for(Person person : show.created_by) {
            assertNotNull(funcName + "person is null", person);
            assertNotNull(funcName + "person id is null", person.id);
            assertNotNull(funcName + "person name is null", person.name);
            assertNotNull(funcName + "person profile_path is null", person.profile_path);
        }

        assertNotNull(funcName + "show seasons is null", show.seasons);
        for(TvSeason company : show.seasons) {
            assertNotNull(funcName + "company is null", company);
            assertNotNull(funcName + "company id is null", company.id);
            assertNotNull(funcName + "company air_date is null", company.air_date);
            assertNotNull(funcName + "company episode_count is null", company.episode_count);
            assertNotNull(funcName + "company season_number is null", company.season_number);
        }
        
    }

    private void assertCrewCredits(String funcName, List<CrewMember> crew) {
        assertNotNull(funcName + "crew is null", crew);
        assertTrue(funcName + "crew size is < 1", crew.size() > 0);

        for (CrewMember member : crew) {
            assertNotNull(funcName + "member is null", member);
            assertNotNull(funcName + "member id is null", member.id);
            assertNotNull(funcName + "member credit_id is null", member.credit_id);
            assertNotNull(funcName + "member name is null", member.name);
            assertNotNull(funcName + "member department is null", member.department);
            assertNotNull(funcName + "member job is null", member.job);
        }
    }
    
    private void assertCastCredits(String funcName, List<CastMember> cast) {
        assertNotNull(funcName + "cast is null", cast);
        assertTrue(funcName + "cast size is < 1", cast.size() > 0);

        for (CastMember member : cast) {
            assertNotNull(funcName + "member is null", member);
            assertNotNull(funcName + "member id is null", member.id);
            assertNotNull(funcName + "member credit_id is null", member.credit_id);
            assertNotNull(funcName + "member name is null", member.name);
            assertNotNull(funcName + "member character is null", member.character);
            assertNotNull(funcName + "member order is null", member.order);
        }
    }

    private void assertImages(String funcName, List<Image> images){
        assertNotNull(funcName + "images is null", images);
        assertTrue(funcName + "images size is < 1", images.size() > 0);

        for(Image image : images) {
            assertNotNull(funcName + "image is null", image);
            assertNotNull(funcName + "member file_path is null", image.file_path);
            assertNotNull(funcName + "member width is null", image.width);
            assertNotNull(funcName + "member height is null", image.height);

            assertNotNull(funcName + "image aspect_ratio is null", image.aspect_ratio);
            assertTrue(funcName + "image aspect_ratio < 1", image.aspect_ratio > 0);
            assertNotNull(funcName + "image vote_average is null", image.vote_average);
            assertTrue(funcName + "image vote_average < 0", image.vote_average >= 0);
            assertNotNull(funcName + "image vote_count is null", image.vote_count);
            assertTrue(funcName + "image vote_count < 0", image.vote_count >= 0);
        }
    }

}
