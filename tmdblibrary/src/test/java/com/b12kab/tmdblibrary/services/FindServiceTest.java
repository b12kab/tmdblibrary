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
import com.b12kab.tmdblibrary.entities.FindResults;
import com.b12kab.tmdblibrary.entities.MovieResult;
import com.b12kab.tmdblibrary.entities.Person;
import com.b12kab.tmdblibrary.entities.TvEpisode;
import com.b12kab.tmdblibrary.entities.TvSeason;
import com.b12kab.tmdblibrary.entities.TvShow;
import com.b12kab.tmdblibrary.enumerations.ExternalSource;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    @Test
    public void test_find_movie() throws ParseException {
        final String funcName = "test_find_movie ";
        FindResults results = getManager().findService().find(ImdbMovieId, ExternalSource.IMDB_ID, null);
        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results movie_results is null", results.movie_results);
        assertTrue(funcName + "results movie_results < 1", results.movie_results.size() > 0);

        MovieResult movie = results.movie_results.get(0);
        assertNotNull(funcName + "movie is null", movie);
        assertFalse(funcName + "movie adult is not false", movie.isAdult());

        assertNotNull(funcName + "movie getBackdrop_path() is null", movie.getBackdrop_path());
        assertTrue(funcName + "movie id is < 1", movie.getId() > 0);

        assertNotNull(funcName + "movie getOriginal_title() is null", movie.getOriginal_title());
        assertEquals(funcName + "movie getOriginal_title() is not " + movieTitle, movie.getOriginal_title(), movieTitle);

        assertNotNull(funcName + "movie getRelease_date() is null", movie.getRelease_date());
        assertEquals(funcName + "movie getRelease_date() is not 2009-08-21",
                JSON_STRING_DATE.parse("2009-08-18"), movie.getRelease_date());

        assertNotNull(funcName + "movie getPoster_path() is null", movie.getPoster_path());

        assertNotNull(funcName + "movie getPopularity() is null", movie.getPopularity());

        assertNotNull(funcName + "movie getTitle() is null", movie.getTitle());
        assertEquals(funcName + "movie getTitle() is not " + movieTitle, movie.getTitle(), movieTitle);

        assertNotNull(funcName + "movie vote_average is null", movie.getVote_average());
        assertTrue(funcName + "movie vote_average is < 1", movie.getVote_average() > 0);

        assertNotNull(funcName + "movie vote_count is null", movie.getVote_count());
        assertTrue(funcName + "movie vote_count is < 1", movie.getVote_count() > 0);
    }
    
    @Test
    public void test_find_people() {
        final String funcName = "test_find_people ";
        FindResults results = getManager().findService().find(ImdbPersonId, ExternalSource.IMDB_ID, null);
        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results List person_results is null", results.person_results);
        assertTrue(funcName + "results List person_results size < 1", results.person_results.size() > 0);

        Person person = results.person_results.get(0);
        assertNotNull(funcName + "person is null", person);
        assertNotNull(funcName + "person id is null", person.id);
        assertNotNull(funcName + "person name is null", person.name);
        assertTrue(funcName + "person name is not the same as "+ TestData.PERSON_NAME, person.name.equals(TestData.PERSON_NAME));
        assertNotNull(funcName + "person profile_path is null", person.profile_path);
    }
    
    @Test
    public void test_find_tv_show() {
        final String funcName = "test_find_tv_show ";
        FindResults results = getManager().findService().find(ImdbTvId, ExternalSource.IMDB_ID, null);
        assertNotNull(funcName + "results is null", results );
        assertNotNull(funcName + "results tv_results is null", results.tv_results);
        assertTrue(funcName + "results tv_results is < 1", results.tv_results.size() > 0);

        TvShow show = results.tv_results.get(0);
        assertNotNull(funcName + "show is null", show);
        assertNotNull(funcName + "show id is null", show.id);
        assertNotNull(funcName + "show original_name is null", show.original_name);
        assertTrue(funcName + "show original_name is not" + ImdbTvIdName, show.original_name.equals(ImdbTvIdName));
        assertTrue(funcName + "show name is not" + ImdbTvIdName, show.name.equals(ImdbTvIdName));
        assertNotNull(funcName + "show first_air_date is null", show.first_air_date);
        assertNotNull(funcName + "show backdrop_path is null", show.backdrop_path);
        assertNotNull(funcName + "show poster_path is null", show.poster_path);
        assertNotNull(funcName + "show popularity is null", show.popularity);
        assertTrue(funcName + "show popularity is < 1", show.popularity > 0);
        assertNotNull(funcName + "show vote_average is null", show.vote_average);
        assertTrue(funcName + "show vote_average is < 1", show.vote_average > 0);
        assertNotNull(funcName + "show vote_count is null", show.vote_count);
        assertTrue(funcName + "show vote_count is < 1", show.vote_count > 0);
    }
    
    @Test
    public void test_find_tv_season() {
        final String funcName = "test_find_tv_season ";
        FindResults results = getManager().findService().find(ImdbTvSeasonId, ExternalSource.TVDB_ID, null);
        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results tv_season_results is null", results.tv_season_results);
        assertTrue(funcName + "results tv_season_results List < 1", results.tv_season_results.size() > 0);

        TvSeason season = results.tv_season_results.get(0);
        assertNotNull(funcName + "season is null", season);
        assertNotNull(funcName + "season air_date is null", season.air_date);
        assertNotNull(funcName + "season name is null", season.name);
//        assertTrue(funcName + "season name is not equal to " + ImdbTvSeasonName, season.name.equals(ImdbTvSeasonName));
        assertNotNull(funcName + "season id is null", season.id);
//        assertNotNull(funcName + "season poster_path is null", season.poster_path);
        assertNotNull(funcName + "season season_number is null", season.season_number);
//        assertTrue(funcName + "season season_number is != 1", season.season_number == 1);
    }
    
    @Test
    public void test_find_tv_episode() {
        final String funcName = "test_find_tv_episode ";
        FindResults results = getManager().findService().find(ImdbTvEpisodeId, ExternalSource.IMDB_ID, null);
        assertNotNull(funcName + "results is null", results);
        assertNotNull(funcName + "results List tv_episode_results is null", results.tv_episode_results);
        assertTrue(funcName + "results List tv_episode_results size < 1", results.tv_episode_results.size() > 0);

        TvEpisode episode = results.tv_episode_results.get(0);
        assertNotNull(funcName + "episode is null", episode);
        assertNotNull(funcName + "episode air_date is null", episode.air_date);
        assertNotNull(funcName + "episode episode_number is null", episode.episode_number);
        assertTrue(funcName + "episode episode_number < 1", episode.episode_number > 0);
        assertNotNull(funcName + "episode name is null", episode.name);
        assertNotNull(funcName + "episode id is null", episode.id);
        assertNotNull(funcName + "episode season_number is null", episode.season_number);
        assertTrue(funcName + "episode season_number < 1", episode.season_number == 1);
        assertNotNull(funcName + "episode still_path is null", episode.still_path);
        assertNotNull(funcName + "episode vote_average is null", episode.vote_average);
        assertTrue(funcName + "episode vote_average is < 1", episode.vote_average > 0);
        assertNotNull(funcName + "episode vote_count is null", episode.vote_count);
        assertTrue(funcName + "episode vote_count is < 1", episode.vote_count > 0);
    }

}
