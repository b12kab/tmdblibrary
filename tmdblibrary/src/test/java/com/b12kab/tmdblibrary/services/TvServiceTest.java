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
 * Keith Beatty 2019 - Converted to jUnit 5.x
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

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.in;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TvServiceTest extends BaseTestCase {

    @BeforeEach
    void init() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){
            System.out.println(e.toString());
        }
    }

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
        assertNotNull(show.credits, funcName + "show credits is null");

        assertCrewCredits(funcName, show.credits.getCrew());
        assertCastCredits(funcName, show.credits.getCast());

        // images
        assertNotNull(show.images, funcName + "show images is null");
        assertImages(funcName, show.images.backdrops);
        assertImages(funcName, show.images.posters);

        // external ids
        assertNotNull(show.external_ids, funcName + "show external_ids is null");
        assertNotNull(show.external_ids.freebase_id, funcName + "show freebase_id is null");
        assertNotNull(show.external_ids.freebase_mid, funcName + "show freebase_mid is null");
        assertNotNull(show.external_ids.tvdb_id, funcName + "show tvdb_id is null");
        assertNotNull(show.external_ids.imdb_id, funcName + "show imdb_id is null");
        assertNotNull(show.external_ids.tvrage_id, funcName + "show tvrage_id is null");
    }
    
    @Test
    public void test_alternative_titles() {
        final String funcName = "test_alternative_titles ";
        TvAlternativeTitles titles = getManager().tvService().alternativeTitles(TestData.TVSHOW_ID);

        assertNotNull(titles, funcName + "titles is null");
        assertNotNull(titles.id, funcName + "titles id is null");
        assertEquals(TestData.TVSHOW_ID, (int) titles.id, funcName + "titles id is != " + TestData.TVSHOW_ID);
        assertNotNull(titles.results, funcName + "titles results is null");
        assertTrue(titles.results.size() > 0, funcName + "titles results size < 1");
        assertNotNull(titles.results.get(0), funcName + "titles results get(0) is null");
        assertNotNull(titles.results.get(0).iso_3166_1, funcName + "titles iso_3166_1 is null");
        assertNotNull(titles.results.get(0).title, funcName + "titles title is null");
    }
    
    @Test
    public void test_credits() {
        final String funcName = "test_credits ";
        CreditResults credits = getManager().tvService().credits(TestData.TVSHOW_ID, null);

        assertNotNull(credits, funcName + "credits is null");
        assertCrewCredits(funcName, credits.getCrew());
        assertCastCredits(funcName, credits.getCast());
    }
    
    @Test
    public void test_externalIds() {
        final String funcName = "test_externalIds ";
        ExternalIds ids = getManager().tvService().externalIds(TestData.TVSHOW_ID, null);

        assertNotNull(ids, funcName + "ids is null");
        assertNotNull(ids.id, funcName + "ids id is null");
        assertNotNull(ids.freebase_id, funcName + "ids freebase_id is null");
        assertNotNull(ids.freebase_mid, funcName + "ids freebase_mid is null");
        assertNotNull(ids.tvdb_id, funcName + "ids tvdb_id is null");
        assertNotNull(ids.imdb_id, funcName + "ids imdb_id is null");
        assertNotNull(ids.tvrage_id, funcName + "ids tvrage_id is null");
    }
    
    @Test
    public void test_images() {
        final String funcName = "test_images ";
        Images images = getManager().tvService().images(TestData.TVSHOW_ID, null);

        assertNotNull(images, funcName + "images is null");
        assertNotNull(images.id, funcName + "image s id is null");
        assertEquals((int) images.id, TestData.TVSHOW_ID, funcName + "images id is != " + TestData.TVSHOW_ID);
        assertImages(funcName, images.backdrops);
        assertImages(funcName, images.posters);
    }
    
    @Test
    public void test_keywords() {
        final String funcName = "test_keywords ";
        TvKeywords keywords = getManager().tvService().keywords(TestData.TVSHOW_ID);

        assertNotNull(keywords, funcName + "keywords is null");
        assertNotNull(keywords.id, funcName + "keywords id is null");
        assertEquals((int) keywords.id, TestData.TVSHOW_ID, funcName + "keywords id is != " + TestData.TVSHOW_ID);
        assertNotNull(keywords.results, funcName + "keywords results is null" );
        assertTrue(keywords.results.size() > 0, funcName + "keywords results size < 1");
        assertNotNull(keywords.results.get(0), funcName + "keywords get(0) is null");
        assertNotNull(keywords.results.get(0).id, funcName + "keywords id is null");
        assertNotNull(keywords.results.get(0).name, funcName + "keywords name is null");
    }
    
    @Test
    public void test_similar() {
        final String funcName = "test_similar ";
        TvResultsPage tvResultsPage = getManager().tvService().similar(TestData.TVSHOW_ID, 1, null);

        assertNotNull(tvResultsPage, funcName + "tvResultsPage is null");
        assertNotNull(tvResultsPage.page, funcName + "tvResultsPage page is null");
        assertTrue(tvResultsPage.page > 0, funcName + "tvResultsPage page is < 1" );
        assertNotNull(tvResultsPage.total_pages, funcName + "tvResultsPage total_pages is null");
        assertTrue(tvResultsPage.total_pages > 0, funcName + "tvResultsPage total_pages is < 1");
        assertNotNull(tvResultsPage.total_results, funcName + "tvResultsPage total_results is null");
        assertTrue(tvResultsPage.total_results > 0, funcName + "tvResultsPage total_results is < 1" );

        assertNotNull(tvResultsPage.results, funcName + "tvResultsPage results is null");
        assertTrue(tvResultsPage.results.size() > 0, funcName + "tvResultsPage results is < 1");
        assertNotNull(tvResultsPage.results.get(0).id, funcName + "tvResultsPage id is null");
        assertNotNull(tvResultsPage.results.get(0).backdrop_path, funcName + "tvResultsPage backdrop_path is null");
        assertNotNull(tvResultsPage.results.get(0).name, funcName + "tvResultsPage name is null");
        assertNotNull(tvResultsPage.results.get(0).poster_path, funcName + "tvResultsPage poster_path is null");
        assertNotNull(tvResultsPage.results.get(0).popularity, funcName + "tvResultsPage popularity is null");
        assertNotNull(tvResultsPage.results.get(0).name, funcName + "tvResultsPage name is null");
        assertNotNull(tvResultsPage.results.get(0).vote_average, funcName + "tvResultsPage vote_average is null");
        assertTrue(tvResultsPage.results.get(0).vote_average > 0, funcName + "tvResultsPage vote_average is < 1");
        assertNotNull(tvResultsPage.results.get(0).vote_count, funcName + "tvResultsPage vote_count is null");
        assertTrue(tvResultsPage.results.get(0).vote_count > 0, funcName + "tvResultsPage vote_count is < 1");
    }
    
    @Test
    public void test_videos() {
        final String funcName = "test_videos ";
        VideoResults videoResults = getManager().tvService().videos(TestData.TVSHOW_ID, null);

        assertNotNull(videoResults, funcName + "videoResults is null");
        assertNotNull(videoResults.results, funcName + "videoResults results is null");
        assertTrue(videoResults.results.size() > 0, funcName + "videoResults results is < 1");
        assertNotNull(videoResults.results.get(0), funcName + "videoResults results get(0) is null");
        assertNotNull(videoResults.results.get(0).getId(), funcName + "videoResults id is null");
        assertNotNull(videoResults.results.get(0).getIso_639_1(), funcName + "videoResults iso_639_1 is null");
        assertNotNull(videoResults.results.get(0).getKey(), funcName + "videoResults key is null");
        assertNotNull(videoResults.results.get(0).getName(), funcName + "videoResults name is null");
        assertNotNull(videoResults.results.get(0).getSite(), funcName + "videoResults site is null");
        assertEquals("YouTube", videoResults.results.get(0).getSite(), funcName + "videoResults site is != 'YouTube'");
        assertNotNull(videoResults.results.get(0).getSite(), funcName + "videoResults site is null");
        String videoType = videoResults.results.get(0).getType();
        assertNotNull(videoType, funcName + "videoResults type is null");
        assertThat(videoType, anyOf(in(new String[]{ "Trailer", "Clip", "Featurette"}))); //, funcName + "videoResults type is != 'Trailer'");
    }
    
    @Test
    public void test_latest() {
        final String funcName = "test_latest ";
        TvShowComplete show = getManager().tvService().latest();

        // Latest show might not have a complete TMDb entry, but at should least some basic properties.
        assertNotNull(show, funcName + "show is null");
        assertNotNull(show.id, funcName + "show id is null");
        assertTrue(show.id > 0, funcName + "show id is < 1");
        assertNotNull(show.name, funcName + "show name is null");
    }
    
    @Test
    public void test_onTheAir() {
        final String funcName = "test_onTheAir ";
        TvResultsPage results = getManager().tvService().onTheAir(null, null);

        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.results, funcName + "results results is null");
        assertTrue(results.results.size() > 0, funcName + "results results is < 1");
    }
    
    @Test
    public void test_airingToday() {
        final String funcName = "test_airingToday ";
        TvResultsPage results = getManager().tvService().airingToday(null, null);

        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.results, funcName + "results results is null");
        assertTrue(results.results.size() > 0, funcName + "results results is < 1");
    }
    
    @Test
    public void test_topRated() {
        final String funcName = "test_topRated ";
        TvResultsPage results = getManager().tvService().topRated(null, null);

        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.results, funcName + "results results is null");
        assertTrue(results.results.size() > 0, funcName + "results results is < 1");
    }
    
    @Test
    public void test_popular() {
        final String funcName = "test_popular ";
        TvResultsPage results = getManager().tvService().popular(null, null);

        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.results, funcName + "results results is null");
        assertTrue(results.results.size() > 0, funcName + "results results is < 1");
    }
    
    private void assertTvShow(String funcName, TvShowComplete show) {
        assertNotNull(show, funcName + "show is null");
        assertNotNull(show.first_air_date, funcName + "show first_air_date is null");
        assertNotNull(show.homepage, funcName + "show homepage is null");
        assertNotNull(show.id, funcName + "show id is null");
        assertNotNull(show.languages, funcName + "show languages is null");
        assertNotNull(show.last_air_date, funcName + "show last_air_date is null");
        assertNotNull(show.name, funcName + "show name is null");
        assertNotNull(show.number_of_seasons, funcName + "show number_of_seasons is null");
        assertNotNull(show.original_language, funcName + "show original_language is null");
        assertNotNull(show.original_name, funcName + "show original_name is null");
        assertNotNull(show.overview, funcName + "show overview is null");
        assertNotNull(show.popularity, funcName + "show popularity is null");
        assertTrue(show.popularity >= 0, funcName + "show popularity is < 0");
        assertNotNull(show.poster_path, funcName + "show poster_path is null");
        assertNotNull(show.status, funcName + "show status is null");
        assertNotNull(show.type, funcName + "show type is null");
        assertNotNull(show.vote_average, funcName + "show vote_average is null");
        assertTrue(show.vote_average > 0, funcName + "show vote_average is < 1");
        assertNotNull(show.vote_count, funcName + "show vote_count is null");
        assertTrue(show.vote_count > 0, funcName + "show vote_count is < 1");
        assertNotNull(show.created_by, funcName + "show created_by is null");

        for(Person person : show.created_by) {
            assertNotNull(person, funcName + "person is null");
            assertNotNull(person.id, funcName + "person id is null");
            assertNotNull(person.name, funcName + "person name is null");
            assertNotNull(person.profile_path, funcName + "person profile_path is null");
        }

        assertNotNull(show.seasons, funcName + "show seasons is null");
        for(TvSeason company : show.seasons) {
            assertNotNull(company, funcName + "company is null");
            assertNotNull(company.id, funcName + "company id is null");
            assertNotNull(company.air_date, funcName + "company air_date is null");
            assertNotNull(company.episode_count, funcName + "company episode_count is null");
            assertNotNull(company.season_number, funcName + "company season_number is null");
        }
        
    }

    private void assertCrewCredits(String funcName, List<CrewMember> crew) {
        assertNotNull(crew, funcName + "crew is null");
        assertTrue(crew.size() > 0, funcName + "crew size is < 1");

        for (CrewMember member : crew) {
            assertNotNull(member, funcName + "member is null");
            assertNotNull(member.id, funcName + "member id is null");
            assertNotNull(member.credit_id, funcName + "member credit_id is null");
            assertNotNull(member.name, funcName + "member name is null");
            assertNotNull(member.department, funcName + "member department is null");
            assertNotNull(member.job, funcName + "member job is null");
        }
    }
    
    private void assertCastCredits(String funcName, List<CastMember> cast) {
        assertNotNull(cast, funcName + "cast is null");
        assertTrue(cast.size() > 0, funcName + "cast size is < 1");

        for (CastMember member : cast) {
            assertNotNull(member, funcName + "member is null");
            assertNotNull(member.id, funcName + "member id is null");
            assertNotNull(member.credit_id, funcName + "member credit_id is null");
            assertNotNull(member.name, funcName + "member name is null");
            assertNotNull(member.character, funcName + "member character is null");
            assertNotNull(member.order, funcName + "member order is null");
        }
    }

    private void assertImages(String funcName, List<Image> images){
        assertNotNull(images, funcName + "images is null");
        assertTrue(images.size() > 0, funcName + "images size is < 1");

        for(Image image : images) {
            assertNotNull(image, funcName + "image is null");
            assertNotNull(image.file_path, funcName + "member file_path is null");
            assertNotNull(image.width, funcName + "member width is null");
            assertNotNull(image.height, funcName + "member height is null");

            assertNotNull(image.aspect_ratio, funcName + "image aspect_ratio is null");
            assertTrue(image.aspect_ratio > 0, funcName + "image aspect_ratio < 1");
            assertNotNull(image.vote_average, funcName + "image vote_average is null");
            assertTrue(image.vote_average >= 0, funcName + "image vote_average < 0");
            assertNotNull(image.vote_count, funcName + "image vote_count is null");
            assertTrue(image.vote_count >= 0, funcName + "image vote_count < 0");
        }
    }
}
