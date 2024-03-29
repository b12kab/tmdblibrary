package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.assertations.MovieAsserts;
import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.MovieFull;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;

import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.SIMILAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class MovieDetailHelperTest extends BaseTestCase {
    private MovieDetailHelper helper;

    public MovieDetailHelperTest() {
        helper = new MovieDetailHelper();
    }

    @Test
    public void test_movie_detail_tmdb_error_status_cd() {
        final String funcName = "test_movie_detail_tmdb_error_status_cd ";

        List<Integer> codes = helper.getAssocHelperTmdbErrorStatusCodes();
        assertNotNull(codes, funcName + "codes is null");
        assertNotEquals(0, codes.size(), "codes size = 0");
    }

    @Test
    public void test_movie_detail_non_tmdb_error_status_cd() {
        final String funcName = "test_movie_detail_non_tmdb_error_status_cd ";

        List<Integer> codes = helper.getAssocHelperNonTmdbErrorStatusCodes();
        assertNotNull(codes, funcName + "codes is null");
        assertNotEquals(0, codes.size(), "codes size = 0");
    }

    @Test
    public void test_movie_detail_tmdb_is_null() {
        final String funcName = "test_movie_detail_tmdb_is_null ";

        try {
            helper.processMovieDetail(null, 0, null, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_movie_detail_movieId_missing_no_session() {
        final String funcName = "test_movie_detail_movieId_missing_no_session ";

        try {
            helper.processMovieDetail(this.getManager(), 0, null, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertNotNull(e.getErrorKind(), funcName + " error kind is null");
            assertEquals(TmdbException.RetrofitErrorKind.Tmdb, e.getErrorKind(), funcName + " error kind does not match Tmdb");
            assertEquals(34, e.getStatus().getStatusCode(), funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_movie_detail_movieId_missing_with_session() {
        final String funcName = "test_movie_detail_movieId_missing_with_session ";

        try {
            this.sleepSetup(3);
            helper.processMovieDetail(this.getManager(), 0, null, this.createTmdbSession(), null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertNotNull(e.getErrorKind(), funcName + " error kind is null");
            assertEquals(TmdbException.RetrofitErrorKind.Tmdb, e.getErrorKind(), funcName + " error kind does not match Tmdb");
            assertEquals(34, e.getStatus().getStatusCode(), funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_movie_detail_movieId_valid_no_session() throws ParseException {
        final String funcName = "test_movie_detail_movieId_valid_no_session ";

        MovieFull movie = null;

        try {
            movie = helper.processMovieDetail(this.getManager(), TestData.MOVIE_ID, null, null, null);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }
        MovieAsserts.assertMovieTestData(movie, funcName);
        MovieAsserts.assertMovieTestDataAppended(movie, funcName,
                false,
                true,
                true,
                true,
                true,
                true,
                false);
    }

    @Test
    public void test_movie_detail_movieId_valid_with_additional_response_session() throws ParseException {
        final String funcName = "test_movie_detail_movieId_valid_with_session ";
        AppendToResponse additionalResponse = new AppendToResponse(SIMILAR);

        MovieFull movie = null;

        try {
            this.sleepSetup(3);
            movie = helper.processMovieDetail(this.getManager(), TestData.MOVIE_ID, null, this.createTmdbSession(), additionalResponse);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }
        MovieAsserts.assertMovieTestData(movie, funcName);
        MovieAsserts.assertMovieTestDataAppended(movie, funcName,
                true,
                true,
                true,
                true,
                true,
                true,
                true);
    }

    @Test
    public void test_movie_detail_movieId_valid_with_additional_response_empty() throws ParseException {
        final String funcName = "test_movie_detail_movieId_valid_with_session ";
        AppendToResponse additionalResponse = new AppendToResponse();

        MovieFull movie = null;

        try {
            this.sleepSetup(3);
            movie = helper.processMovieDetail(this.getManager(), TestData.MOVIE_ID, null, this.createTmdbSession(), additionalResponse);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }
        MovieAsserts.assertMovieTestData(movie, funcName);
        MovieAsserts.assertMovieTestDataAppended(movie, funcName,
                true,
                true,
                true,
                true,
                true,
                true,
                false);
    }

}
