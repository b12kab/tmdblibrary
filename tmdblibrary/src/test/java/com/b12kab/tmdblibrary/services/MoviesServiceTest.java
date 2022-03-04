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
import com.b12kab.tmdblibrary.assertations.MovieAsserts;
import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.CreditResults;
import com.b12kab.tmdblibrary.entities.Images;
import com.b12kab.tmdblibrary.entities.ListResultsPage;
import com.b12kab.tmdblibrary.entities.MovieAbbreviated;
import com.b12kab.tmdblibrary.entities.MovieAlternativeTitles;
import com.b12kab.tmdblibrary.entities.MovieExternalIds;
import com.b12kab.tmdblibrary.entities.MovieFull;
import com.b12kab.tmdblibrary.entities.MovieKeywords;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.ReleaseResults;
import com.b12kab.tmdblibrary.entities.ReviewResultsPage;
import com.b12kab.tmdblibrary.entities.Translations;
import com.b12kab.tmdblibrary.entities.VideoResults;
import com.b12kab.tmdblibrary.entities.WatchProviders;
import com.b12kab.tmdblibrary.enumerations.AppendToResponseItem;

import java.text.ParseException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.b12kab.tmdblibrary.assertations.MovieAsserts.assertImageType;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoviesServiceTest extends BaseTestCase {

    @Test
    public void test_summary() throws ParseException {
        final String funcName = "test_summary ";
        MovieFull movie = null;
        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovieTestData(movie, funcName);
    }

    @Test
    public void test_summary_language() {
        final String funcName = "test_summary_language ";
        final String movieTitle = "Clube da Luta";
        MovieFull movie = null;
        try
        {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, "pt-BR", null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, false);
        assertEquals(movieTitle, movie.getTitle(),
                funcName + "movie original_title is not " + movieTitle);
    }

    @Test
    public void test_summary_with_collection() throws Exception {
        final String funcName = "test_summary_with_collection ";
        final int movieCollectionId = 1241;
        final String movieCollectionName = "Harry Potter Collection";
        MovieFull movie = null;

        try {
            movie = this.getManager().moviesService().summary(TestData.MOVIE_WITH_COLLECTION_ID, null, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, true);
        assertEquals(movie.getTitle(), TestData.MOVIE_WITH_COLLECTION_TITLE, funcName + "movie title is not " + TestData.MOVIE_WITH_COLLECTION_TITLE);
        assertNotNull(movie.getBelongs_to_collection(), funcName + "movie belongs_to_collection is null");
        assertNotNull(movie.getBelongs_to_collection().id, funcName + "movie belongs_to_collection.id is null");
        assertEquals((int) movie.getBelongs_to_collection().id, movieCollectionId, funcName + "movie belongs_to_collection.id is not " + movieCollectionId);
        assertNotNull(movie.getBelongs_to_collection().name, funcName + "movie belongs_to_collection.name is null");
        assertEquals(movie.getBelongs_to_collection().name, movieCollectionName, funcName + "movie belongs_to_collection.name is not " + movieCollectionName);
    }

    @Test
    public void test_summary_append_credits() throws ParseException {
        final String funcName = "test_summary_append_credits ";
        MovieFull movie = null;
        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                new AppendToResponse( AppendToResponseItem.CREDITS)).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, false);
        assertNotNull(movie.credits.getCrew(), funcName + "movie credits crew is null");
        assertTrue(movie.credits.getCrew().size() > 0, funcName + "movie credits crew size is 0");
        MovieAsserts.assertCredits(movie.credits, funcName);
    }

    @Test
    public void test_summary_append_images() throws ParseException {
        final String funcName = "test_summary_append_images ";
        MovieFull movie = null;

        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                    new AppendToResponse( AppendToResponseItem.IMAGES)).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, false);
        assertNotNull(movie.images.backdrops, funcName + "movie images backdrops is null");
        assertTrue(movie.images.backdrops.size() > 0, funcName + "movie images backdrops is 0");
        MovieAsserts.assertImageType(movie.images.backdrops, funcName, "backdrops");
        assertNotNull(movie.images.posters, funcName + "movie images posters is null");
        assertTrue(movie.images.posters.size() > 0, funcName + "movie images posters is 0");
        assertImageType(movie.images.posters, funcName, "posters");
        assertNotNull(movie.images.logos, funcName + "movie images stills is null");
        assertTrue(movie.images.logos.size() > 0, funcName + "movie images stills is 0");
        assertImageType(movie.images.logos, funcName, "logos");
    }

    @Test
    public void test_summary_append_releases() throws ParseException {
        final String funcName = "test_summary_append_releases ";
        MovieFull movie = null;

        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                new AppendToResponse( AppendToResponseItem.RELEASE_DATES)).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, false);
        assertNotNull(movie.release_dates.getResults(), funcName + "movie release dates results is null");
        assertTrue(movie.release_dates.getResults().size() > 0, funcName + "movie release dates results is 0");
        MovieAsserts.assertReleaseDateResults(movie.release_dates.getResults(), funcName);
    }

    @Test
    public void test_summary_append_videos() throws ParseException {
        final String funcName = "test_summary_append_videos ";
        MovieFull movie = null;
        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                new AppendToResponse(AppendToResponseItem.VIDEOS)).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, false);
        assertNotNull(movie.videos.getResults(), funcName + "movie videos results is null");
        assertTrue(movie.videos.getResults().size() > 0, funcName + "movie videos results size is 0");
        MovieAsserts.assertVideos(movie.videos.getResults(), funcName);
    }

    @Test
    public void test_summary_append_reviews() throws ParseException {
        final String funcName = "test_summary_append_videos ";
        MovieFull movie = null;
        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                    new AppendToResponse(AppendToResponseItem.REVIEWS)).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, false);
        assertNotNull(movie.reviews.results, funcName + "movie reviews results list is null");
        assertTrue(movie.reviews.results.size() > 0, funcName + "movie reviews results list is 0");
        MovieAsserts.assertReviews(movie.reviews.results, funcName);
    }

    @Test
    public void test_summary_append_similar() throws ParseException {
        final String funcName = "test_summary_append_similar ";
        MovieFull movie = null;

        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                new AppendToResponse( AppendToResponseItem.SIMILAR)).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, false);
        MovieAsserts.assertMovieTestDataAppended(movie, funcName,
        false,
                false,
                false,
                false,
                false,
                false,
        true);
        }

    @Test
    public void test_summary_append_states_no_session() {
        final String funcName = "test_summary_append_states_no_session ";
        AppendToResponse atr = new AppendToResponse( AppendToResponseItem.ACCT_STATES);
        MovieFull movie = null;

        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                atr ).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, false);
        assertNull(movie.getAccount_states(),funcName + "movie similar is not null");
    }

    @Test
    public void test_summary_append_states_null_session() {
        final String funcName = "test_summary_append_states_no_session ";
        AppendToResponse atr = new AppendToResponse( AppendToResponseItem.ACCT_STATES);
        MovieFull movie = null;

        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                    atr, null ).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, false);
        assertNull(movie.getAccount_states(),funcName + "movie similar is not null");
    }

    @Test
    public void test_summary_append_states_valid_session() throws ParseException {
        final String funcName = "test_summary_append_states ";
        AppendToResponse atr = new AppendToResponse( AppendToResponseItem.ACCT_STATES);
        MovieFull movie = null;

        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                atr, this.createTmdbSession()).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, false);
        assertNotNull(movie.getAccount_states(),funcName + "movie similar is not null");
        MovieAsserts.assertMovieTestDataAppended(movie, funcName,
                true,
                false,
                false,
                false,
                false,
                false,
                false);
    }

    @Test
    public void test_summary_append_all() throws ParseException {
        final String funcName = "test_summary_append_all ";
        MovieFull movie = null;

        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                    new AppendToResponse(
                            AppendToResponseItem.CREDITS,
                            AppendToResponseItem.IMAGES,
                            AppendToResponseItem.RELEASE_DATES,
                            AppendToResponseItem.VIDEOS,
                            AppendToResponseItem.REVIEWS,
                            AppendToResponseItem.SIMILAR)).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovie(movie, false);
        MovieAsserts.assertMovieTestDataAppended(movie, funcName,
                false,
                true,
                true,
                true,
                true,
                true,
                true);
    }

    @Test
    public void test_summary_append_all_session() {
        final String funcName = "test_summary_append_all_session ";
        AppendToResponse atr = new AppendToResponse(
                AppendToResponseItem.CREDITS,
                AppendToResponseItem.IMAGES,
                AppendToResponseItem.RELEASE_DATES,
                AppendToResponseItem.VIDEOS,
                AppendToResponseItem.REVIEWS,
                AppendToResponseItem.SIMILAR,
                AppendToResponseItem.LISTS);

        MovieFull movie = null;

        try {
            movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                    atr).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(movie, funcName + "movie is null");
        assertNotNull(movie.getCredits(),funcName + "movie credits is null");
        assertNotNull(movie.getImages(),funcName + "movie images is null");
        assertNotNull(movie.getVideos(), funcName + "movie videos is null");
        assertNotNull(movie.getReviews(),funcName + "movie reviews is null");
        assertNotNull(movie.getSimilar(),funcName + "movie similar is null");
        assertNotNull(movie.getRelease_dates(),funcName + "movie release_dates is null");
    }

    @Test
    public void test_alternative_titles() {
        final String funcName = "test_alternative_titles ";
        final String movieLanguage = "PL";
        final String movieTitle = "Podziemny krąg";
        MovieAlternativeTitles titles = null;

        try {
            titles = getManager().moviesService().alternativeTitles(TestData.MOVIE_ID, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(titles, funcName + "titles is null");
        assertNotNull(titles.id, funcName + "titles id is null");
        assertEquals((int) titles.id, TestData.MOVIE_ID, funcName + "titles id is not equal to " + TestData.MOVIE_ID);
        assertNotNull(titles.titles, funcName + "titles titles is null");
        assertTrue(titles.titles.size() > 0, funcName + "titles titles List < 1");
        assertNotNull(titles.titles.get(0), funcName + "titles titles get(0) is null");
        assertNotNull(titles.titles.get(0).iso_3166_1, funcName + "titles iso_3166_1 is null");
        assertEquals(2, titles.titles.get(0).iso_3166_1.length(), funcName + "titles iso_3166_1 length not equal 2");
        assertNotNull( titles.titles.get(0).title, funcName + "titles title is null");
    }

    @Test
    public void test_credits() {
        final String funcName = "test_credits ";
        final String creditName = "Edward Norton";
        CreditResults credits = null;
        try {
            credits = getManager().moviesService().credits(TestData.MOVIE_ID, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(credits, funcName + "credits is null");
        assertNotNull(credits.getId(), funcName + "credits id is null");
        assertEquals((int) credits.getId(), TestData.MOVIE_ID, funcName + "credits id is not equal to " + TestData.MOVIE_ID);
        assertNotNull(credits.getCast(), funcName + "credits cast is null");
        assertTrue(credits.getCast().size() > 0, funcName + "credits cast List < 1" );
        assertNotNull(credits.getCast().get(0), funcName + "credits cast get(0) is null");
        assertNotNull(credits.getCast().get(0).name, funcName + "credits name is null");
        assertEquals(credits.getCast().get(0).name, creditName, funcName + "credits name not equal " + creditName);
        assertNotNull(credits.getCrew(), funcName + "credits crew is null");
    }

    @Test
    public void test_externalIds() {
        final String funcName = "test_externalIds ";

        MovieExternalIds externalIds = null;

        try {
            externalIds = getManager().moviesService().externalIds(TestData.MOVIE_ID, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(externalIds, funcName + "externalIds is null");
        assertNotNull(externalIds.id, funcName + "externalIds id is null");
        assertNotNull(externalIds.imdb_id, funcName + "externalIds imdb is null");
    }

    @Test
    public void test_images() {
        final String funcName = "test_images ";
        Images images = null;
        try {
            images = getManager().moviesService().images(TestData.MOVIE_ID, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(images,funcName + "images is null");
        assertNotNull(images.id, funcName + "images id is null");
        assertEquals((int) images.id, TestData.MOVIE_ID, funcName + "images id is not equal to " + TestData.MOVIE_ID);

        assertNotNull(images.backdrops, funcName + "images backdrops is null");
        assertTrue(images.backdrops.size() > 0, funcName + "images backdrops List < 1");
        assertNotNull(images.backdrops.get(0), funcName + "images backdrops get(0) is null");
        assertNotNull(images.backdrops.get(0).file_path, funcName + "images backdrops file_path is null");
        assertFalse(images.backdrops.get(0).file_path.isEmpty(), funcName + "images backdrops file_path is empty");
        assertEquals((int) images.id, TestData.MOVIE_ID, funcName + "images id is not equal to " + TestData.MOVIE_ID);
        assertNotNull(images.backdrops.get(0).width, funcName + "Images backdrops width is null");
        assertTrue(images.backdrops.get(0).width > 1200, funcName + "Images backdrops width not > 1200");
        assertNotNull(images.backdrops.get(0).height, funcName + "Images backdrops height is null");
        assertTrue(images.backdrops.get(0).height > 600, funcName + "Images backdrops height not > 600");
//        assertNotNull(funcName + "Images backdrops iso_639_1 is null", images.backdrops.get(0).iso_639_1);
//        assertEquals(funcName + "Images backdrops iso_639_1 is not "+ "en",
//                images.backdrops.get(0).iso_639_1.equals("en"));
        assertNotNull(images.backdrops.get(0).aspect_ratio, funcName + "Images backdrops aspect_ratio is null");
        assertTrue(images.backdrops.get(0).aspect_ratio > 1.7F, funcName + "Images backdrops aspect_ratio < 1.7F");
        assertNotNull(images.backdrops.get(0).vote_average, funcName + "Images backdrops vote_average is null");
        assertTrue(images.backdrops.get(0).vote_average > 0, funcName + "Images backdrops vote_average < 1");
        assertNotNull(images.backdrops.get(0).vote_count, funcName + "Images backdrops vote_count is null");
        assertTrue(images.backdrops.get(0).vote_count > 0, funcName + "Images backdrops vote_count < 1");

        assertNotNull(images.posters, funcName + "images posters is null");
        assertTrue(images.posters.size() > 0, funcName + "images posters List < 1");
        assertNotNull(images.posters.get(0), funcName + "images posters get(0) is null");
        assertNotNull(images.posters.get(0).file_path, funcName + "images posters file_path is null");
        assertFalse(images.posters.get(0).file_path.isEmpty(), funcName + "images posters file_path is empty");
        assertNotNull(images.posters.get(0).width, funcName + "Images posters width is null");
        assertTrue(images.posters.get(0).width < 1200, funcName + "Images posters width < 1200");
        assertNotNull(images.posters.get(0).height, funcName + "Images posters height is null");
        assertFalse(images.posters.get(0).height > 1800, funcName + "Images posters height not > 1800");
        assertNotNull(images.posters.get(0).iso_639_1, funcName + "Images posters iso_639_1 is null");
        assertEquals(2, images.posters.get(0).iso_639_1.length(), funcName + "Images posters iso_639_1 length is not 2");
        assertNotNull(images.posters.get(0).aspect_ratio, funcName + "Images posters aspect_ratio is null");
        assertTrue(images.posters.get(0).aspect_ratio > 0.6f, funcName + "Images posters aspect_ratio < 0.6f");
        assertNotNull(images.posters.get(0).vote_average, funcName + "Images posters vote_average is null");
        assertTrue(images.posters.get(0).vote_average > 0, funcName + "Images posters vote_average < 1");
        assertNotNull(images.posters.get(0).vote_count, funcName + "Images posters vote_count is null");
        assertTrue(images.posters.get(0).vote_count > 0, funcName + "Images posters vote_count < 1");
    }

    @Test
    public void test_keywords() {
        final String funcName = "test_keywords ";
        MovieKeywords keywords = null;

        try {
            keywords = getManager().moviesService().keywords(TestData.MOVIE_ID).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(keywords, funcName + "keywords is null");
        assertNotNull(keywords.id, funcName + "keywords id is null");
        assertEquals((int) keywords.id, TestData.MOVIE_ID, funcName + "keywords id is != " + TestData.MOVIE_ID);
        assertTrue(keywords.keywords.size() > 0, funcName + "keywords keywords size is 0");
        assertNotNull(keywords.keywords.get(0), funcName + "keywords keywords is null");
        assertNotNull(keywords.keywords.get(0).id, funcName + "keywords keywords id is null");
        boolean itemFound = false;
        for (int i = 0; i < keywords.keywords.size(); i++) {
            int id = keywords.keywords.get(i).id;
            if (id == 825) {
                assertNotNull(keywords.keywords.get(i).name, funcName + "keywords keywords name is null");
                assertEquals("support group", keywords.keywords.get(i).name, funcName + "keywords keywords name is != 'support group'");
                itemFound = true;
                break;
            }
        }
    }

    @Test
    public void test_releases() {
        final String funcName = "test_releases ";
        ReleaseResults releases = null;
        try {
            releases = getManager().moviesService().releases(TestData.MOVIE_ID).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(releases, funcName + "releases is null");
        assertNotNull(releases.getResults(), funcName + "releases results is null");
        assertTrue(releases.getResults().size() > 0, funcName + "releases results List < 1");
        assertNotNull(releases.getResults().get(0), funcName + "releases results get(0) is null");

        assertNotNull(releases.getResults().get(0).getIso_3166_1(), funcName + "releases iso_3166_1 is null");
//        assertEquals(releases.getResults().get(0).getIso_3166_1(), "US", funcName + "releases iso_3166_1 != 'US'" );
        assertNotNull(releases.getResults().get(0).getRelease_dates(), funcName + "releases release_dates is null" );
        assertTrue(releases.getResults().get(0).getRelease_dates().size() > 0, funcName + "releases release_dates List < 1");
        assertNotNull(releases.getResults().get(0).getRelease_dates().get(0), funcName + "releases get(0) is null" );
        assertNotNull(releases.getResults().get(0).getRelease_dates().get(0).getCertification(),
                funcName + "releases certification is null");
//      assertEquals(releases.getResults().get(0).getRelease_dates().get(0).getCertification(), "R",
//        funcName + "releases certification != 'R' " );
        assertNotNull(releases.getResults().get(0).getRelease_dates().get(0).getIso_639_1(),
                funcName + "releases iso_639_1 is null");
//        assertEquals(releases.getResults().get(0).getRelease_dates().get(0).getIso_639_1(), "en",
//        funcName + "releases certification != 'en' ");
        assertNotNull(releases.getResults().get(0).getRelease_dates().get(0).getRelease_date(),
                funcName + "releases release_date is null");
//        assertEquals(releases.getResults().get(0).getRelease_dates().get(0).getRelease_date(), "1999-10-14"
//        funcName + "releases release_date != '1999-10-14' ");
    }

    @Test
    public void test_videos() {
        final String funcName = "test_videos ";
        VideoResults videos = null;

        try {
            videos = getManager().moviesService().videos(TestData.MOVIE_ID, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(videos, funcName + "videos is null");
        assertNotNull(videos.getResults(), funcName + "videos results is null");
        assertTrue(videos.getResults().size() > 0, funcName + "videos results List < 1");
        assertNotNull(videos.getResults().get(0), funcName + "videos get(0) is null");
        assertNotNull(videos.getResults().get(0).getId(), funcName + "videos id is null");
        assertNotNull(videos.getResults().get(0).getIso_639_1(), funcName + "videos iso_639_1 is null");
        assertNotNull(videos.getResults().get(0).getKey(), funcName + "videos key is null");
        assertNotNull(videos.getResults().get(0).getName(), funcName + "videos name is null");
        assertNotNull(videos.getResults().get(0).getSite(), funcName + "videos site is null");
        assertEquals("YouTube", videos.getResults().get(0).getSite(), funcName + "videos site != 'YouTube'");
        assertNotNull(videos.getResults().get(0).getSize(), funcName + "videos size is null");
        assertNotNull(videos.getResults().get(0).getType(), funcName + "videos type is null");
        assertEquals(videos.getResults().get(0).getType(), "Trailer", funcName + "videos type != 'Trailer'");
    }

    @Test
    public void test_translations() {
        final String funcName = "test_translations ";
        Translations translations = null;

        try {
            translations = getManager().moviesService().translations(TestData.MOVIE_ID, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(translations, funcName + "translations is null");
        assertNotNull(translations.id, funcName + "translations id is null");
        assertEquals((Integer) translations.id, (Integer) TestData.MOVIE_ID, funcName + "translations id != " + TestData.MOVIE_ID);
        assertNotNull(translations.translations,funcName + "translations translations is null");

        for (Translations.Translation translation : translations.translations) {
            assertNotNull(translation, funcName + "translation is null");
            assertNotNull(translation.name, funcName + "translation id is null");
            assertNotNull(translation.iso_639_1, funcName + "translation iso_639_1 is null");
            assertNotNull(translation.english_name, funcName + "translation english_name is null");
        }
    }

    @Test
    public void test_similar() {
        final String funcName = "test_similar ";
        MovieResultsPage results = null;

        try {
            results = getManager().moviesService().similar(TestData.MOVIE_ID, 3, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(results,funcName + "results is null");
        assertNotNull(results.page, funcName + "results page is null");
        assertTrue(results.page > 0, funcName + "results page < 1");
        assertNotNull(results.total_pages, funcName + "results total_pages is null");
        assertTrue(results.total_pages > 0, funcName + "results total_pages < 1");
        assertNotNull(results.total_results, funcName + "results total_results is null");
        assertTrue(results.total_results > 0, funcName + "results total_results < 1");
        assertNotNull(results.results, funcName + "results results is null");
        assertTrue(results.results.size() > 0, funcName + "results results List size < 1");
        assertNotNull(results.results.get(0), funcName + "results results get(0) is null");
        assertFalse(results.results.get(0).isAdult(), funcName + "results adult != false ");
        assertNotNull(results.results.get(0).getBackdrop_path(), funcName + "results backdrop_path is null");
        assertTrue(results.results.get(0).getId() > 0, funcName + "results id < 1");
        assertNotNull(results.results.get(0).getOriginal_title(), funcName + "results original_title is null");
        assertNotNull(results.results.get(0).getRelease_date(), funcName + "results release_date is null");
        assertNotNull(results.results.get(0).getPoster_path(), funcName + "results poster_path is null");
        assertNotNull(results.results.get(0).getPopularity(), funcName + "results popularity is null");
        assertTrue(results.results.get(0).getPopularity() > 0, funcName + "results popularity < 1");
        assertNotNull(results.results.get(0).getTitle(), funcName + "results title is null");
        assertNotNull(results.results.get(0).getVote_average(), funcName + "results vote_average is null");
        assertTrue(results.results.get(0).getVote_average() > 0, funcName + "results vote_average < 1");
        assertTrue(results.results.get(0).getVote_count() > 0, funcName + "results vote_count < 1");
    }

    @Test
    public void test_reviews() {
        final String funcName = "test_similar ";
        ReviewResultsPage results = null;

        try {
            results = getManager().moviesService().reviews(TestData.MOVIE_ID, 1, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.id, funcName + "results id is null");
        assertNotNull(results.total_pages, funcName + "results total_pages is null");
        assertTrue(results.total_pages > 0, funcName + "results total_pages < 1");
        assertNotNull(results.total_results, funcName + "results total_results is null");
        assertTrue(results.total_results > 0, funcName + "results total_results < 1");
        assertNotNull(results.results, funcName + "results results is null");
        assertTrue(results.results.size() > 0, funcName + "results results List size < 1");
        assertNotNull(results.results.get(0), funcName + "results results get(0) is null");
        assertNotNull(results.results.get(0).id, funcName + "results id is null");
        assertNotNull(results.results.get(0).author, funcName + "results author is null");
        assertNotNull(results.results.get(0).content, funcName + "results content is null");
        assertNotNull(results.results.get(0).author_details, funcName + "author detail is null");
    }

    @Test
    public void test_lists() {
        final String funcName = "test_lists ";
        ListResultsPage results = null;

        try {
            results = getManager().moviesService().lists(49026, 1, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.id, funcName + "results id is null");
        assertNotNull(results.total_pages, funcName + "results total_pages is null");
        assertTrue(results.total_pages > 0, funcName + "results total_pages < 1");
        assertNotNull(results.total_results, funcName + "results total_results is null");
        assertTrue(results.total_results > 0, funcName + "results total_results < 1");
        assertNotNull(results.results, funcName + "results results is null");
        assertTrue(results.results.size() > 0, funcName + "results results List size < 1");
        assertNotNull(results.results.get(0), funcName + "results results get(0) is null");
        assertNotNull(results.results.get(0).id, funcName + "results id is null");
        assertNotNull(results.results.get(0).description, funcName + "results description is null" );
        assertNotNull(results.results.get(0).favorite_count, funcName + "results favorite_count is null");
        assertNotNull(results.results.get(0).item_count, funcName + "results item_count is null");
        assertTrue(results.results.get(0).item_count > 0, funcName + "results item_count List size < 1");
        assertNotNull(results.results.get(0).iso_639_1, funcName + "results iso_639_1 is null");
        assertNotNull(results.results.get(0).name, funcName + "results name is null");
//        assertNotNull( results.results.get(0).poster_path, funcName + "results poster_path is null");
    }

    @Test
    public void test_latest() {
        final String funcName = "test_latest ";
        MovieAbbreviated movie = null;
        try {
            movie = getManager().moviesService().latest().execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        // Latest movie might not have a complete TMDb entry, but should at least some basic properties.
        assertNotNull(movie, funcName + "movie is null");
        assertNotNull(movie.getTitle(), funcName + "movie title is null");
    }

    @Test
    public void test_upcoming() {
        final String funcName = "test_upcoming ";
        MovieResultsPage page = null;
        try {
            page = getManager().moviesService().upcoming(null, null, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(page, funcName + "page is null");
        assertNotNull(page.getResults(), funcName + "page results is null");
        assertTrue(page.getResults().size() > 0, funcName + "page results List size < 1");
    }

    @Test
    public void test_nowPlaying() {
        final String funcName = "test_nowPlaying ";
        MovieResultsPage page = null;

        try {
            page = getManager().moviesService().nowPlaying(null, null, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(page, funcName + "page is null");
        assertNotNull(page.getResults(), funcName + "page results is null");
        assertTrue(page.getResults().size() > 0, funcName + "page results List size < 1");
    }

    @Test
    public void test_popular() {
        final String funcName = "test_popular ";
        MovieResultsPage page = null;

        try {
            page = getManager().moviesService().popular(null, null, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(page, funcName + "page is null");
        assertNotNull(page.getResults(), funcName + "page results is null");
        assertTrue(page.getResults().size() > 0, funcName + "page results List size < 1");
    }

    @Test
    public void test_topRated() {
        final String funcName = "test_topRated ";
        MovieResultsPage page = null;
        try {
            page = getManager().moviesService().topRated(null, null, null).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(page, funcName + "page is null");
        assertNotNull(page.getResults(), funcName + "page results is null");
        assertTrue(page.getResults().size() > 0, funcName + "page results List size < 1");
    }

    @Test
    public void test_watchProviders() {
        final String funcName = "test_watchProviders ";
        WatchProviders providers = null;

        try {
            providers = getManager().moviesService().watchProviders(TestData.MOVIE_ID).execute().body();
        }
        catch (Exception e)
        {
            Assertions.fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(providers, funcName + "providers is null");
        assertNotNull(providers.id, funcName + "id is null");
        assertTrue(providers.results.size() > 0, funcName + "providers results List size < 1");
    }
}
