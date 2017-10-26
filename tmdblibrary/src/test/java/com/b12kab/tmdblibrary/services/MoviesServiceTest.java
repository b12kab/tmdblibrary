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
import com.b12kab.tmdblibrary.entities.CreditResults;
import com.b12kab.tmdblibrary.entities.Images;
import com.b12kab.tmdblibrary.entities.ListResultsPage;
import com.b12kab.tmdblibrary.entities.MovieAbbreviated;
import com.b12kab.tmdblibrary.entities.MovieAlternativeTitles;
import com.b12kab.tmdblibrary.entities.MovieFull;
import com.b12kab.tmdblibrary.entities.MovieKeywords;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.ReleaseResults;
import com.b12kab.tmdblibrary.entities.ReviewResultsPage;
import com.b12kab.tmdblibrary.entities.Translations;
import com.b12kab.tmdblibrary.entities.VideoResults;
import com.b12kab.tmdblibrary.enumerations.AppendToResponseItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class MoviesServiceTest extends BaseTestCase {
    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd");

    @Test
    public void test_summary() throws ParseException {
        final String funcName = "test_summary ";
        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, null, null);
        assertMovie(funcName, movie);
        assertNotNull(funcName + "movie original_title is null", movie.getOriginal_title());
        assertTrue(funcName + "movie original_title is not " + TestData.MOVIE_TITLE,
                movie.getOriginal_title().equals(TestData.MOVIE_TITLE));
    }

    @Test
    public void test_summary_language() throws ParseException {
        final String funcName = "test_summary_language ";
        final String movieTitle = "Clube da Luta";
        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, "pt-BR", null);
        assertNotNull(funcName + "movie is null", movie);

        assertNotNull(funcName + "movie title is null", movie.getTitle());
        assertEquals(funcName + "movie original_title is not " + movieTitle,
                movie.getTitle(), movieTitle);
    }

    @Test
    public void test_summary_with_collection() throws ParseException {
        final String funcName = "test_summary_with_collection ";
        final int movieCollectionId = 1241;
        final String movieCollectionName = "Harry Potter Collection";
        MovieFull movie = this.getManager().moviesService().summary(TestData.MOVIE_WITH_COLLECTION_ID, null, null);

        assertNotNull(funcName + "movie is null", movie);
        assertNotNull(funcName + "movie title is null", movie.getTitle());
        assertTrue(funcName + "movie title is not " + TestData.MOVIE_WITH_COLLECTION_TITLE,
                movie.getTitle().equals(TestData.MOVIE_WITH_COLLECTION_TITLE));
        assertNotNull(funcName + "movie belongs_to_collection is null", movie.getBelongs_to_collection());
        assertNotNull(funcName + "movie belongs_to_collection.id is null", movie.getBelongs_to_collection().id);
        assertTrue(funcName + "movie belongs_to_collection.id is not " + movieCollectionId,
                movie.getBelongs_to_collection().id == movieCollectionId);
        assertNotNull(funcName + "movie belongs_to_collection.name is null", movie.getBelongs_to_collection().name);
        assertTrue(funcName + "movie belongs_to_collection.name is not "+ movieCollectionName,
                movie.getBelongs_to_collection().name.equals(movieCollectionName));
    }

    private void assertMovie(String funcName, MovieFull movie) throws ParseException {
        final int movieBudget = 63000000;
        final int movieRevenue = 100853753;
        final int movieRuntime = 139;
        final String movieReleaseDate = "1999-10-15";

        assertNotNull(funcName + "movie is null", movie);
        assertNotNull(funcName + "movie id is null", movie.getId());
        assertTrue(funcName + "movie id is not " + TestData.MOVIE_ID, movie.getId() == TestData.MOVIE_ID);
        assertNotNull(funcName + "movie title is null", movie.getTitle());
        assertTrue(funcName + "movie title is not "+ TestData.MOVIE_TITLE,
                movie.getTitle().equals(TestData.MOVIE_TITLE));
        assertNotNull(funcName + "movie overview is null", movie.getOverview());
        assertFalse(funcName + "movie overview is empty", movie.getOverview().isEmpty());
        assertNotNull(funcName + "movie tagline is null", movie.getTagline());
        assertFalse(funcName + "movie tagline is empty", movie.getTagline().isEmpty());
        assertTrue(funcName + "movie adult is false", movie.isAdult() == false);
        assertNotNull(funcName + "movie backdrop_path is null", movie.getBackdrop_path());
        assertFalse(funcName + "movie backdrop_path is empty", movie.getBackdrop_path().isEmpty());
        assertTrue(funcName + "movie budget is not equal to " + movieBudget, movie.getBudget() == movieBudget);
        assertNotNull(funcName + "movie imdb_id is null", movie.getImdb_id());
        assertTrue(funcName + "movie imdb_id is not equal to " + TestData.MOVIE_IMDB,
                movie.getImdb_id().equals(TestData.MOVIE_IMDB));
        assertNotNull(funcName + "movie poster_path is null", movie.getPoster_path());
        assertNotNull(funcName + "movie release_date is null", movie.getRelease_date());
        assertEquals(funcName + "movie release_date is not equal to " + movieReleaseDate,
                movie.getRelease_date(), JSON_STRING_DATE.parse(movieReleaseDate));
        assertTrue(funcName + "movie revenue is not equal to " + movieRevenue,
                movie.getRevenue() == movieRevenue);
        assertTrue(funcName + "movie runtime is not equal to " + movieRuntime,
                movie.getRuntime() == movieRuntime);

        assertNotNull(funcName + "movie vote_average is null", movie.getVote_average());
        assertTrue(funcName + "movie vote_average is < 1", movie.getVote_average() > 0);
        assertNotNull(funcName + "movie vote_count is null", movie.getVote_count());
        assertTrue(funcName + "movie vote_count is < 1", movie.getVote_count() > 0);
    }

    @Test
    public void test_summary_append_credits() {
        final String funcName = "test_summary_append_credits ";
        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                new AppendToResponse( AppendToResponseItem.CREDITS));

        assertNotNull(funcName + "movie is null", movie);
        assertNotNull(funcName + "movie credits is null", movie.getCredits());
    }

    @Test
    public void test_summary_append_images() {
        final String funcName = "test_summary_append_images ";
        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                new AppendToResponse( AppendToResponseItem.IMAGES));

        assertNotNull(funcName + "movie is null", movie);
        assertNotNull(funcName + "movie images is null", movie.getImages());
    }

    @Test
    public void test_summary_append_releases() {
        final String funcName = "test_summary_append_releases ";
        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                new AppendToResponse( AppendToResponseItem.RELEASES));

        assertNotNull(funcName + "movie is null", movie);
        assertNotNull(funcName + "movie release_dates is null", movie.getRelease_dates());
    }

    @Test
    public void test_summary_append_videos() {
        final String funcName = "test_summary_append_videos ";
        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                new AppendToResponse(AppendToResponseItem.VIDEOS));

        assertNotNull(funcName + "movie is null", movie);
        assertNotNull(funcName + "movie videos is null", movie.getVideos());
    }

    @Test
    public void test_summary_append_reviews() {
        final String funcName = "test_summary_append_videos ";
        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                new AppendToResponse(AppendToResponseItem.REVIEWS));

        assertNotNull(funcName + "movie is null", movie);
        assertNotNull(funcName + "movie reviews is null", movie.getReviews());
    }

    @Test
    public void test_summary_append_similar() {
        final String funcName = "test_summary_append_similar ";
        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                new AppendToResponse( AppendToResponseItem.SIMILAR));

        assertNotNull(funcName + "movie is null", movie);
        assertNotNull(funcName + "movie similar is null", movie.getSimilar());
    }

    @Test
    public void test_summary_append_states_no_session() {
        final String funcName = "test_summary_append_states_no_session ";
        AppendToResponse atr = new AppendToResponse( AppendToResponseItem.STATES);
        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                atr );

        assertNotNull(funcName + "movie is null", movie);
        assertNull(funcName + "movie similar is not null", movie.getAccount_states());
    }

    @Test
    public void test_summary_append_states_session() {
        final String funcName = "test_summary_append_states ";
        AppendToResponse atr = new AppendToResponse( AppendToResponseItem.STATES);
        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                TMDB_SESSION,
                atr);

        assertNotNull(funcName + "movie is null", movie);
        if (!TMDB_SESSION.equals(TMDB_SESSION_INVALID)) {
            assertNotNull(funcName + "movie similar is null", movie.getAccount_states());
        }
    }

    @Test
    public void test_summary_append_all() {
        final String funcName = "test_summary_append_all ";
        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                new AppendToResponse(
                        AppendToResponseItem.CREDITS,
                        AppendToResponseItem.IMAGES,
                        AppendToResponseItem.RELEASES,
                        AppendToResponseItem.VIDEOS,
                        AppendToResponseItem.REVIEWS,
                        AppendToResponseItem.SIMILAR));

        assertNotNull(funcName + "movie is null", movie);
        assertNotNull(funcName + "movie credits is null", movie.getCredits());
        assertNotNull(funcName + "movie images is null", movie.getImages());
        assertNotNull(funcName + "movie videos is null", movie.getVideos());
        assertNotNull(funcName + "movie reviews is null", movie.getReviews());
        assertNotNull(funcName + "movie similar is null", movie.getSimilar());
        assertNotNull(funcName + "movie release_dates is null", movie.getRelease_dates());
    }

    @Test
    public void test_summary_append_all_session() {
        final String funcName = "test_summary_append_all_session ";
        AppendToResponse atr = new AppendToResponse(
                AppendToResponseItem.CREDITS,
                AppendToResponseItem.IMAGES,
                AppendToResponseItem.RELEASES,
                AppendToResponseItem.VIDEOS,
                AppendToResponseItem.REVIEWS,
                AppendToResponseItem.SIMILAR,
                AppendToResponseItem.STATES);

        MovieFull movie = getManager().moviesService().summary(TestData.MOVIE_ID, null,
                TMDB_SESSION,
                atr);

        assertNotNull(funcName + "movie is null", movie);
        assertNotNull(funcName + "movie credits is null", movie.getCredits());
        assertNotNull(funcName + "movie images is null", movie.getImages());
        assertNotNull(funcName + "movie videos is null", movie.getVideos());
        assertNotNull(funcName + "movie reviews is null", movie.getReviews());
        assertNotNull(funcName + "movie similar is null", movie.getSimilar());
        assertNotNull(funcName + "movie release_dates is null", movie.getRelease_dates());
        if (!TMDB_SESSION.equals(TMDB_SESSION_INVALID)) {
            assertNotNull(funcName + "movie account states is null", movie.getAccount_states());
        }
    }

    @Test
    public void test_alternative_titles() {
        final String funcName = "test_alternative_titles ";
        final String movieLanguage = "PL";
        final String movieTitle = "Podziemny krąg";

        MovieAlternativeTitles titles = getManager().moviesService().alternativeTitles(TestData.MOVIE_ID, null);

        assertNotNull(funcName + "titles is null", titles);
        assertNotNull(funcName + "titles id is null", titles.id);
        assertTrue(funcName + "titles id is not equal to " + TestData.MOVIE_ID,
                titles.id.equals(TestData.MOVIE_ID));
        assertNotNull(funcName + "titles titles is null", titles.titles);
        assertTrue(funcName + "titles titles List < 1", titles.titles.size() > 0);
        assertNotNull(funcName + "titles titles get(0) is null", titles.titles.get(0));
        assertNotNull(funcName + "titles iso_3166_1 is null", titles.titles.get(0).iso_3166_1);
        assertTrue(funcName + "titles iso_3166_1 length not equal 2",
                titles.titles.get(0).iso_3166_1.length() == 2);
        assertNotNull(funcName + "titles title is null", titles.titles.get(0).title);
    }

    @Test
    public void test_credits() {
        final String funcName = "test_credits ";
        final String creditName = "Edward Norton";
        CreditResults credits = getManager().moviesService().credits(TestData.MOVIE_ID);

        assertNotNull(funcName + "credits is null", credits);
        assertNotNull(funcName + "credits id is null", credits.getId());
        assertTrue(funcName + "credits id is not equal to " + TestData.MOVIE_ID,
                credits.getId().equals(TestData.MOVIE_ID));
        assertNotNull(funcName + "credits cast is null", credits.getCast());
        assertTrue(funcName + "credits cast List < 1", credits.getCast().size() > 0);
        assertNotNull(funcName + "credits cast get(0) is null", credits.getCast().get(0));
        assertNotNull(funcName + "credits name is null", credits.getCast().get(0).name);
        assertTrue(funcName + "credits name not equal "+ creditName,
                credits.getCast().get(0).name.equals(creditName));
        assertNotNull(funcName + "credits crew is null", credits.getCrew());
    }

    @Test
    public void test_images() {
        final String funcName = "test_images ";
        Images images = getManager().moviesService().images(TestData.MOVIE_ID, null);
        assertNotNull(funcName + "images is null", images);
        assertNotNull(funcName + "images id is null", images.id);
        assertTrue(funcName + "images id is not equal to " + TestData.MOVIE_ID,
                images.id.equals(TestData.MOVIE_ID));

        assertNotNull(funcName + "images backdrops is null", images.backdrops);
        assertTrue(funcName + "images backdrops List < 1", images.backdrops.size() > 0);
        assertNotNull(funcName + "images backdrops get(0) is null", images.backdrops.get(0));
        assertNotNull(funcName + "images backdrops file_path is null", images.backdrops.get(0).file_path);
        assertFalse(funcName + "images backdrops file_path is empty",
                images.backdrops.get(0).file_path.isEmpty());
        assertTrue(funcName + "images id is not equal to " + TestData.MOVIE_ID,
                images.id.equals(TestData.MOVIE_ID));
        assertNotNull(funcName + "Images backdrops width is null", images.backdrops.get(0).width);
        assertTrue(funcName + "Images backdrops width not > 1200",
                images.backdrops.get(0).width > 1200);
        assertNotNull(funcName + "Images backdrops height is null", images.backdrops.get(0).height);
        assertTrue(funcName + "Images backdrops height not > 600",
                images.backdrops.get(0).height > 600);
//        assertNotNull(funcName + "Images backdrops iso_639_1 is null", images.backdrops.get(0).iso_639_1);
//        assertEquals(funcName + "Images backdrops iso_639_1 is not "+ "en",
//                images.backdrops.get(0).iso_639_1.equals("en"));
        assertNotNull(funcName + "Images backdrops aspect_ratio is null", images.backdrops.get(0).aspect_ratio);
        assertTrue(funcName + "Images backdrops aspect_ratio < 1.7F", images.backdrops.get(0).aspect_ratio > 1.7F);
        assertNotNull(funcName + "Images backdrops vote_average is null", images.backdrops.get(0).vote_average);
        assertTrue(funcName + "Images backdrops vote_average < 1", images.backdrops.get(0).vote_average > 0);
        assertNotNull(funcName + "Images backdrops vote_count is null", images.backdrops.get(0).vote_count);
        assertTrue(funcName + "Images backdrops vote_count < 1", images.backdrops.get(0).vote_count > 0);

        assertNotNull(funcName + "images posters is null", images.posters);
        assertTrue(funcName + "images posters List < 1", images.posters.size() > 0);
        assertNotNull(funcName + "images posters get(0) is null", images.posters.get(0));
        assertNotNull(funcName + "images posters file_path is null", images.posters.get(0).file_path);
        assertFalse(funcName + "images posters file_path is empty",
                images.posters.get(0).file_path.isEmpty());
        assertNotNull(funcName + "Images posters width is null", images.posters.get(0).width);
        assertFalse(funcName + "Images posters width < 1200",
                images.posters.get(0).width < 1200);
        assertNotNull(funcName + "Images posters height is null", images.posters.get(0).height);
        assertTrue(funcName + "Images posters height not > 1800",
                images.posters.get(0).height > 1800);
        assertNotNull(funcName + "Images posters iso_639_1 is null", images.posters.get(0).iso_639_1);
        assertTrue(funcName + "Images posters iso_639_1 length is not 2",
                images.posters.get(0).iso_639_1.length() == 2);
        assertNotNull(funcName + "Images posters aspect_ratio is null", images.posters.get(0).aspect_ratio);
        assertTrue(funcName + "Images posters aspect_ratio < 0.6f", images.posters.get(0).aspect_ratio > 0.6f);
        assertNotNull(funcName + "Images posters vote_average is null", images.posters.get(0).vote_average);
        assertTrue(funcName + "Images posters vote_average < 1", images.posters.get(0).vote_average > 0);
        assertNotNull(funcName + "Images posters vote_count is null", images.posters.get(0).vote_count);
        assertTrue(funcName + "Images posters vote_count < 1", images.posters.get(0).vote_count > 0);

        /* this is new test */
//        assertNotNull(funcName + "images stills is null", images.stills);
//        assertTrue(funcName + "images stills List < 1", images.stills.size() > 0);
//        assertNotNull(funcName + "images stills get(0) is null", images.stills.get(0));
//        assertNotNull(funcName + "images stills file_path is null", images.stills.get(0).file_path);
//        assertFalse(funcName + "images stills file_path is empty",
//                images.stills.get(0).file_path.isEmpty());
//        assertNotNull(funcName + "Images stills width is null", images.stills.get(0).width);
//        assertEquals(funcName + "Images stills width is not 1000",
//                (long) images.stills.get(0).width, (long) 1000);
//        assertNotNull(funcName + "Images stills height is null", images.stills.get(0).height);
//        assertEquals(funcName + "Images stills height is not 1500",
//                (long) images.stills.get(0).height, (long) 1500);
//        assertNotNull(funcName + "Images stills iso_639_1 is null", images.stills.get(0).iso_639_1);
//        assertEquals(funcName + "Images stills iso_639_1 length is not 2",
//                images.stills.get(0).iso_639_1.length() == 2);
//        assertNotNull(funcName + "Images stills aspect_ratio is null", images.stills.get(0).aspect_ratio);
//        assertTrue(funcName + "Images stills aspect_ratio < 0.6f", images.stills.get(0).aspect_ratio > 0.6f);
//        assertNotNull(funcName + "Images stills vote_average is null", images.stills.get(0).vote_average);
//        assertTrue(funcName + "Images stills vote_average < 1", images.stills.get(0).vote_average > 0);
//        assertNotNull(funcName + "Images stills vote_count is null", images.stills.get(0).vote_count);
//        assertTrue(funcName + "Images stills vote_count < 1", images.stills.get(0).vote_count > 0);
    }

    @Test
    public void test_keywords() {
        final String funcName = "test_keywords ";
        MovieKeywords keywords = getManager().moviesService().keywords(TestData.MOVIE_ID);

        assertNotNull(funcName + "keywords is null", keywords);
        assertNotNull(funcName + "keywords id is null", keywords.id);
        assertTrue(funcName + "keywords id is != " + TestData.MOVIE_ID, keywords.id == TestData.MOVIE_ID);
        assertNotNull(funcName + "keywords keywords is null", keywords.keywords.get(0));
        assertNotNull(funcName + "keywords keywords id is null", keywords.keywords.get(0).id);
        assertTrue(funcName + "keywords keywords id is != 825" , keywords.keywords.get(0).id == 825);
        assertNotNull(funcName + "keywords keywords name is null", keywords.keywords.get(0).name);
        assertTrue(funcName + "keywords keywords name is != 'support group'" ,
                keywords.keywords.get(0).name.equals("support group"));
    }

    @Test
    public void test_releases() {
        final String funcName = "test_releases ";
        ReleaseResults releases = getManager().moviesService().releases(TestData.MOVIE_ID);

        assertNotNull(funcName + "releases is null", releases);
        assertNotNull(funcName + "releases results is null", releases.getResults());
        assertTrue(funcName + "releases results List < 1", releases.getResults().size() > 0);
        assertNotNull(funcName + "releases results get(0) is null", releases.getResults().get(0));

        assertNotNull(funcName + "releases iso_3166_1 is null", releases.getResults().get(0).getIso_3166_1());
//        assertEquals(funcName + "releases iso_3166_1 != 'US' ", releases.getResults().get(0).getIso_3166_1(), "US");
        assertNotNull(funcName + "releases release_dates is null", releases.getResults().get(0).getRelease_dates() );
        assertTrue(funcName + "releases release_dates List < 1", releases.getResults().get(0).getRelease_dates().size() > 0);
        assertNotNull(funcName + "releases get(0) is null", releases.getResults().get(0).getRelease_dates().get(0) );
        assertNotNull(funcName + "releases certification is null",
                releases.getResults().get(0).getRelease_dates().get(0).getCertification() );
//        assertEquals(funcName + "releases certification != 'R' ",
//                releases.getResults().get(0).getRelease_dates().get(0).getCertification(), "R");
        assertNotNull(funcName + "releases iso_639_1 is null",
                releases.getResults().get(0).getRelease_dates().get(0).getIso_639_1() );
//        assertEquals(funcName + "releases certification != 'en' ",
//                releases.getResults().get(0).getRelease_dates().get(0).getIso_639_1(), "en");
        assertNotNull(funcName + "releases release_date is null",
                releases.getResults().get(0).getRelease_dates().get(0).getRelease_date() );
//        assertEquals(funcName + "releases release_date != '1999-10-14' ",
//                releases.getResults().get(0).getRelease_dates().get(0).getRelease_date(), "1999-10-14");
    }

    @Test
    public void test_videos() {
        final String funcName = "test_videos ";
        VideoResults videos = getManager().moviesService().videos(TestData.MOVIE_ID, null);

        assertNotNull(funcName + "videos is null", videos);
        assertNotNull(funcName + "videos results is null", videos.getResults());
        assertTrue(funcName + "videos results List < 1", videos.getResults().size() > 0);
        assertNotNull(funcName + "videos get(0) is null", videos.getResults().get(0));
        assertNotNull(funcName + "videos id is null", videos.getResults().get(0).getId());
        assertNotNull(funcName + "videos iso_639_1 is null", videos.getResults().get(0).getIso_639_1());
        assertNotNull(funcName + "videos key is null", videos.getResults().get(0).getKey());
        assertNotNull(funcName + "videos name is null", videos.getResults().get(0).getName());
        assertNotNull(funcName + "videos site is null", videos.getResults().get(0).getSite());
        assertTrue(funcName + "videos site != 'YouTube'",
                videos.getResults().get(0).getSite().equals("YouTube"));
        assertNotNull(funcName + "videos size is null", videos.getResults().get(0).getSize());
        assertNotNull(funcName + "videos type is null", videos.getResults().get(0).getType());
        assertEquals(funcName + "videos type != 'Trailer'",
                videos.getResults().get(0).getType(), "Trailer");
    }

    @Test
    public void test_translations() {
        final String funcName = "test_translations ";
        Translations translations = getManager().moviesService().translations(TestData.MOVIE_ID, null);

        assertNotNull(funcName + "translations is null", translations);
        assertNotNull(funcName + "translations id is null", translations.id);
        assertEquals(funcName + "translations id != " + TestData.MOVIE_ID,
                (Integer) translations.id, (Integer) TestData.MOVIE_ID);
        assertNotNull(funcName + "translations translations is null", translations.translations);

        for (Translations.Translation translation : translations.translations) {
            assertNotNull(funcName + "translation is null", translation);
            assertNotNull(funcName + "translation id is null", translation.name);
            assertNotNull(funcName + "translation iso_639_1 is null", translation.iso_639_1);
            assertNotNull(funcName + "translation english_name is null", translation.english_name);
        }
    }

    @Test
    public void test_similar() {
        final String funcName = "test_similar ";
        MovieResultsPage results = getManager().moviesService().similar(TestData.MOVIE_ID, 3, null);

        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results page is null", results.page);
        assertTrue(funcName + "results page < 1", results.page > 0);
        assertNotNull(funcName + "results total_pages is null", results.total_pages);
        assertTrue(funcName + "results total_pages < 1", results.total_pages > 0);
        assertNotNull(funcName + "results total_results is null", results.total_results);
        assertTrue(funcName + "results total_results < 1", results.total_results > 0);
        assertNotNull(funcName + "results results is null", results.results);
        assertTrue(funcName + "results results List size < 1", results.results.size() > 0);
        assertNotNull(funcName + "results results get(0) is null", results.results.get(0));
        assertTrue(funcName + "results adult != false ", results.results.get(0).isAdult() == false);
        assertNotNull(funcName + "results backdrop_path is null", results.results.get(0).getBackdrop_path());
        assertTrue(funcName + "results id < 1", results.results.get(0).getId() > 0);
        assertNotNull(funcName + "results original_title is null", results.results.get(0).getOriginal_title());
        assertNotNull(funcName + "results release_date is null", results.results.get(0).getRelease_date());
        assertNotNull(funcName + "results poster_path is null", results.results.get(0).getPoster_path());
        assertNotNull(funcName + "results popularity is null", results.results.get(0).getPopularity());
        assertTrue(funcName + "results popularity < 1", results.results.get(0).getPopularity() > 0);
        assertNotNull(funcName + "results title is null", results.results.get(0).getTitle());
        assertNotNull(funcName + "results vote_average is null", results.results.get(0).getVote_average());
        assertTrue(funcName + "results vote_average < 1", results.results.get(0).getVote_average() > 0);
        assertNotNull(funcName + "results vote_count is null", results.results.get(0).getVote_count());
        assertTrue(funcName + "results vote_count < 1", results.results.get(0).getVote_count() > 0);
    }

    @Test
    public void test_reviews() {
        final String funcName = "test_similar ";
        ReviewResultsPage results = getManager().moviesService().reviews(49026, 1, null);

        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results id is null", results.id);
        assertNotNull(funcName + "results total_pages is null", results.total_pages);
        assertTrue(funcName + "results total_pages < 1", results.total_pages > 0);
        assertNotNull(funcName + "results total_results is null", results.total_results);
        assertTrue(funcName + "results total_results < 1", results.total_results > 0);
        assertNotNull(funcName + "results results is null", results.results);
        assertTrue(funcName + "results results List size < 1", results.results.size() > 0);
        assertNotNull(funcName + "results results get(0) is null", results.results.get(0));
        assertNotNull(funcName + "results id is null", results.results.get(0).id);
        assertNotNull(funcName + "results author is null", results.results.get(0).author);
        assertNotNull(funcName + "results content is null", results.results.get(0).content);
        assertNotNull(funcName + "results url is null", results.results.get(0).url);
    }

    @Test
    public void test_lists() {
        final String funcName = "test_lists ";
        ListResultsPage results = getManager().moviesService().lists(49026, 1, null);

        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results id is null", results.id);
        assertNotNull(funcName + "results total_pages is null", results.total_pages);
        assertTrue(funcName + "results total_pages < 1", results.total_pages > 0);
        assertNotNull(funcName + "results total_results is null", results.total_results);
        assertTrue(funcName + "results total_results < 1", results.total_results > 0);
        assertNotNull(funcName + "results results is null", results.results);
        assertTrue(funcName + "results results List size < 1", results.results.size() > 0);
        assertNotNull(funcName + "results results get(0) is null", results.results.get(0));
        assertNotNull(funcName + "results id is null", results.results.get(0).id);
        assertNotNull(funcName + "results description is null", results.results.get(0).description);
        assertNotNull(funcName + "results favorite_count is null", results.results.get(0).favorite_count);
        assertNotNull(funcName + "results item_count is null", results.results.get(0).item_count);
        assertTrue(funcName + "results item_count List size < 1", results.results.get(0).item_count > 0);
        assertNotNull(funcName + "results iso_639_1 is null", results.results.get(0).iso_639_1);
        assertNotNull(funcName + "results name is null", results.results.get(0).name);
//        assertNotNull(funcName + "results poster_path is null", results.results.get(0).poster_path);
    }

    @Test
    public void test_latest() {
        final String funcName = "test_latest ";
        MovieAbbreviated movie = getManager().moviesService().latest();

        // Latest movie might not have a complete TMDb entry, but should at least some basic properties.
        assertNotNull(funcName + "movie is null", movie);
        assertNotNull(funcName + "movie id is null", movie.getId());
        assertNotNull(funcName + "movie title is null", movie.getTitle());
    }

    @Test
    public void test_upcoming() {
        final String funcName = "test_upcoming ";
        MovieResultsPage page = getManager().moviesService().upcoming(null, null);

        assertNotNull(funcName + "page is null", page);
        assertNotNull(funcName + "page results is null", page.getResults());
        assertTrue(funcName + "page results List size < 1", page.getResults().size() > 0);
    }

    @Test
    public void test_nowPlaying() {
        final String funcName = "test_nowPlaying ";
        MovieResultsPage page = getManager().moviesService().nowPlaying(null, null);

        assertNotNull(funcName + "page is null", page);
        assertNotNull(funcName + "page results is null", page.getResults());
        assertTrue(funcName + "page results List size < 1", page.getResults().size() > 0);
    }

    @Test
    public void test_popular() {
        final String funcName = "test_popular ";
        MovieResultsPage page = getManager().moviesService().popular(null, null);

        assertNotNull(funcName + "page is null", page);
        assertNotNull(funcName + "page results is null", page.getResults());
        assertTrue(funcName + "page results List size < 1", page.getResults().size() > 0);
    }

    @Test
    public void test_topRated() {
        final String funcName = "test_topRated ";
        MovieResultsPage page = getManager().moviesService().topRated(null, null);

        assertNotNull(funcName + "page is null", page);
        assertNotNull(funcName + "page results is null", page.getResults());
        assertTrue(funcName + "page results List size < 1", page.getResults().size() > 0);
    }

}
