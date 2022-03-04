package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.assertations.MovieAsserts;
import com.b12kab.tmdblibrary.entities.MovieAbbreviated;
import com.b12kab.tmdblibrary.entities.MovieFull;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.enumerations.MovieFetchType;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MovieHelperTest extends BaseTestCase {
    private MovieHelper helper;

    public MovieHelperTest() {
        helper = new MovieHelper();
    }

    @Test
    public void test_movie_tmdb_is_null() {
        final String funcName = "test_movie_tmdb_is_null ";

        try {
            helper.ProcessInitialMovies(null, MovieFetchType.Upcoming, null, null, 0);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_movie_initial_page_is_too_low() {
        final String funcName = "test_movie_initial_page_is_too_low ";

        try {
            helper.ProcessInitialMovies(this.getManager(), MovieFetchType.Upcoming, null, null, 0);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(e.getCode(), 26, funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains("page"), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_movie_initial_null_movie_type() {
        final String funcName = "test_movie_initial_page_is_too_low ";

        try {
            helper.ProcessInitialMovies(this.getManager(), null, null, null, 0);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(e.getCode(), 26, funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_movie_initial_upcoming_page_1() throws ParseException {
        final String funcName = "test_movie_initial_upcoming_page_1 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.ProcessInitialMovies(this.getManager(), MovieFetchType.Upcoming, null, null, 1);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }
        MovieAsserts.assertMovieResultsPage(resultsPage, false);
    }

    @Test
    public void test_movie_initial_upcoming_page_18() throws ParseException {
        final String funcName = "test_movie_initial_upcoming_page_18 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.ProcessInitialMovies(this.getManager(), MovieFetchType.Upcoming, null, null, 18);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }
        MovieAsserts.assertMovieResultsPage(resultsPage, true);
    }

    @Test
    public void test_movie_initial_popular_page_2() throws ParseException {
        final String funcName = "test_movie_initial_popular_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.ProcessInitialMovies(this.getManager(), MovieFetchType.Popular, null, null, 2);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }
        MovieAsserts.assertMovieResultsPage(resultsPage, false);
    }

    @Test
    public void test_movie_initial_now_playing_page_2() throws ParseException {
        final String funcName = "test_movie_initial_now_playing_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.ProcessInitialMovies(this.getManager(), MovieFetchType.NowPlaying, null, null, 2);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }
        MovieAsserts.assertMovieResultsPage(resultsPage, true);
    }

    @Test
    public void test_movie_initial_now_top_rated_page_2() throws ParseException {
        final String funcName = "test_movie_initial_now_playing_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.ProcessInitialMovies(this.getManager(), MovieFetchType.TopRated, null, null, 2);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }
        MovieAsserts.assertMovieResultsPage(resultsPage, false);
    }

}
