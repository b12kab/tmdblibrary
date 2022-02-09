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
import com.b12kab.tmdblibrary.entities.FindResults;
import com.b12kab.tmdblibrary.entities.MovieAbbreviated;
import com.b12kab.tmdblibrary.entities.Person;
import com.b12kab.tmdblibrary.entities.TvEpisode;
import com.b12kab.tmdblibrary.entities.TvSeason;
import com.b12kab.tmdblibrary.entities.TvShow;
import com.b12kab.tmdblibrary.enumerations.ExternalSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindServiceTest extends BaseTestCase {
    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd");

    static final String movieTitle = "Inglourious Basterds";
    static final String ImdbMovieId = "tt0361748";
    static final String ImdbPersonId = "nm0000093";
    static final String ImdbTvId = "tt0903747";
    static final String ImdbTvIdName = "Breaking Bad";
    static final String ImdbTvSeasonId = "30272";
    static final String ImdbTvSeasonName = "Season 1";
    static final String ImdbTvEpisodeId = "tt0959621";

    @BeforeEach
    void init() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){
            System.out.println(e);
        }
    }

    @Test
    public void test_find_movie() throws ParseException {
        final String funcName = "test_find_movie ";
        FindResults results = getManager().findService().find(ImdbMovieId, ExternalSource.IMDB_ID, null);

        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.movie_results, funcName + "results movie_results is null");
        assertTrue(results.movie_results.size() > 0, funcName + "results movie_results < 1");

        MovieAbbreviated movie = results.movie_results.get(0);
        assertNotNull(movie, funcName + "movie is null");
        assertFalse(movie.isAdult(), funcName + "movie adult is not false");

        assertNotNull(movie.getBackdrop_path(), funcName + "movie getBackdrop_path() is null");
        assertTrue(movie.getId() > 0, funcName + "movie id is < 1");

        assertNotNull(movie.getOriginal_title(), funcName + "movie getOriginal_title() is null");
        assertEquals(movie.getOriginal_title(), movieTitle, funcName + "movie getOriginal_title() is not " + movieTitle);

        assertNotNull(movie.getRelease_date(), funcName + "movie getRelease_date() is null");
        assertEquals(JSON_STRING_DATE.parse("2009-08-19"), movie.getRelease_date(), funcName + "movie getRelease_date() is not 2009-08-21");

        assertNotNull(movie.getPoster_path(), funcName + "movie getPoster_path() is null");

        assertNotNull(movie.getPopularity(), funcName + "movie getPopularity() is null");

        assertNotNull(movie.getTitle(), funcName + "movie getTitle() is null");
        assertEquals(movie.getTitle(), movieTitle, funcName + "movie getTitle() is not " + movieTitle);

        assertNotNull( movie.getVote_average(), funcName + "movie vote_average is null");
        assertTrue(movie.getVote_average() > 0, funcName + "movie vote_average is < 1");

        assertNotNull(movie.getVote_count(), funcName + "movie vote_count is null");
        assertTrue(movie.getVote_count() > 0, funcName + "movie vote_count is < 1");
    }
    
    @Test
    public void test_find_people() {
        final String funcName = "test_find_people ";
        FindResults results = getManager().findService().find(ImdbPersonId, ExternalSource.IMDB_ID, null);

        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.person_results, funcName + "results List person_results is null");
        assertTrue(results.person_results.size() > 0, funcName + "results List person_results size < 1");

        Person person = results.person_results.get(0);
        assertNotNull(person, funcName + "person is null" );
        assertNotNull(person.id, funcName + "person id is null");
        assertNotNull(person.name, funcName + "person name is null");
        assertEquals(person.name, TestData.PERSON_NAME, funcName + "person name is not the same as " + TestData.PERSON_NAME);
        assertNotNull(funcName + "person profile_path is null", person.profile_path);
    }
    
    @Test
    public void test_find_tv_show() {
        final String funcName = "test_find_tv_show ";
        FindResults results = getManager().findService().find(ImdbTvId, ExternalSource.IMDB_ID, null);

        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.tv_results, funcName + "results tv_results is null");
        assertTrue(results.tv_results.size() > 0, funcName + "results tv_results is < 1");

        TvShow show = results.tv_results.get(0);
        assertNotNull(show, funcName + "show is null");
        assertNotNull(show.id, funcName + "show id is null");
        assertNotNull(show.original_name, funcName + "show original_name is null");
        assertEquals(show.original_name, ImdbTvIdName, funcName + "show original_name is not" + ImdbTvIdName);
        assertEquals(show.name, ImdbTvIdName, funcName + "show name is not" + ImdbTvIdName);
        assertNotNull(show.first_air_date, funcName + "show first_air_date is null");
        assertNotNull(show.backdrop_path, funcName + "show backdrop_path is null");
        assertNotNull(show.poster_path, funcName + "show poster_path is null");
        assertNotNull(show.popularity, funcName + "show popularity is null");
        assertTrue(show.popularity > 0, funcName + "show popularity is < 1");
        assertNotNull(show.vote_average, funcName + "show vote_average is null");
        assertTrue(show.vote_average > 0, funcName + "show vote_average is < 1");
        assertNotNull(show.vote_count, funcName + "show vote_count is null");
        assertTrue(show.vote_count > 0, funcName + "show vote_count is < 1");
    }
    
    @Test
    public void test_find_tv_season() {
        final String funcName = "test_find_tv_season ";
        FindResults results = getManager().findService().find(ImdbTvSeasonId, ExternalSource.TVDB_ID, null);

        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.tv_season_results, funcName + "results tv_season_results is null");
        assertTrue(results.tv_season_results.size() > 0, funcName + "results tv_season_results List < 1");

        TvSeason season = results.tv_season_results.get(0);
        assertNotNull(season, funcName + "season is null");
        assertNotNull(season.air_date, funcName + "season air_date is null");
        assertNotNull(season.name, funcName + "season name is null");
//        assertTrue(season.name.equals(ImdbTvSeasonName), funcName + "season name is not equal to " + ImdbTvSeasonName);
        assertNotNull(season.id, funcName + "season id is null");
//        assertNotNull(season.poster_path, funcName + "season poster_path is null");
        assertNotNull(season.season_number, funcName + "season season_number is null");
//        assertTrue(season.season_number == 1, funcName + "season season_number is != 1");
    }
    
    @Test
    public void test_find_tv_episode() {
        final String funcName = "test_find_tv_episode ";
        FindResults results = getManager().findService().find(ImdbTvEpisodeId, ExternalSource.IMDB_ID, null);

        assertNotNull(results, funcName + "results is null");
        assertNotNull(results.tv_episode_results, funcName + "results List tv_episode_results is null");
        assertTrue(results.tv_episode_results.size() > 0, funcName + "results List tv_episode_results size < 1");

        TvEpisode episode = results.tv_episode_results.get(0);
        assertNotNull(episode, funcName + "episode is null");
        assertNotNull(episode.air_date, funcName + "episode air_date is null");
        assertNotNull(episode.episode_number, funcName + "episode episode_number is null");
        assertTrue(episode.episode_number > 0, funcName + "episode episode_number < 1");
        assertNotNull(episode.name, funcName + "episode name is null");
        assertNotNull(episode.id, funcName + "episode id is null");
        assertNotNull(episode.season_number, funcName + "episode season_number is null");
        assertTrue(episode.season_number == 1, funcName + "episode season_number < 1");
        assertNotNull(episode.still_path, funcName + "episode still_path is null");
        assertNotNull(episode.vote_average, funcName + "episode vote_average is null");
        assertTrue(episode.vote_average > 0, funcName + "episode vote_average is < 1");
        assertNotNull(episode.vote_count, funcName + "episode vote_count is null");
        assertTrue(episode.vote_count > 0, funcName + "episode vote_count is < 1");
    }
}
