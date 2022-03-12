package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.BuildConfig;
import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.enumerations.MovieFetchType;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import static com.b12kab.tmdblibrary.BuildConfig.DEBUG;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_API_ERR_MSG;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_API_KEY_INVALID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class TmdbApiCheck {
    private final Tmdb tmdb;
    private final Tmdb tmdbApiNull;

    private final MovieHelper movieHelper;
    private final MovieDetailHelper detailHelper;
    private final SessionHelper sessionHelper;
    private final AccountHelper accountHelper;
    private final ConfigurationHelper configurationHelper;

    public TmdbApiCheck() {
        tmdb = new Tmdb();
        tmdbApiNull = new Tmdb();
        tmdb.setIsDebug(DEBUG);
        tmdb.setApiKey("");
        tmdbApiNull.setIsDebug(DEBUG);
        tmdbApiNull.setApiKey(null);
        movieHelper = new MovieHelper();
        detailHelper = new MovieDetailHelper();
        sessionHelper = new SessionHelper();
        accountHelper = new AccountHelper();
        configurationHelper = new ConfigurationHelper();
    }

    @Test
    public void test_movie_initial_invalid_api_key() {
        final String funcName = "test_movie_initial_invalid_api_key ";

        try {
            movieHelper.processInitialMovies(tmdb, MovieFetchType.Upcoming, null, null, 0);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_additional_invalid_api_key() {
        final String funcName = "test_movie_additional_invalid_api_key ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = movieHelper.processAdditionalMovies(tmdb, null, null, null, 0, 0);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_detail_tmdb_null_api_key() {
        final String funcName = "test_movie_detail_tmdb_null_api_key ";

        try {
            detailHelper.processMovieDetail(tmdbApiNull, 0, null, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_detail_tmdb_invalid_api_key() {
        final String funcName = "test_movie_detail_tmdb_invalid_api_key ";

        try {
            detailHelper.processMovieDetail(tmdb, 0, null, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_session_create_tmdb_invalid_api_key() {
        final String funcName = "test_session_create_tmdb_invalid_api_key ";

        try {
            sessionHelper.createTmdbSession(tmdb, "blah", BuildConfig.TMDB_TEST_BAD_PSWD);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_session_destroy_tmdb_invalid_api_key() {
        final String funcName = "test_destroy_tmdb_invalid_api_key ";

        try {
            sessionHelper.destroyTmdbSession(tmdb, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_info_invalid_api_key() {
        final String funcName = "test_account_movie_info_invalid_api_key ";

        try {
            accountHelper.processAccountMovieInfo(tmdb, null, null, 0, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_info_invalid_api_key() {
        final String funcName = "test_account_info_invalid_api_key ";

        try {
            accountHelper.processAccountInfo(tmdb, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_info_movieId_invalid_api_key() {
        final String funcName = "test_account_info_invalid_api_key ";

        try {
            accountHelper.processAccountMovieInfoDetail(tmdb,1,null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_invalid_api_key() {
        final String funcName = "test_account_favorite_invalid_api_key ";

        try {
            accountHelper.processAccountFavorite(tmdb, null, -1, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_add_rating_invalid_api_key() {
        final String funcName = "test_account_movie_add_rating_invalid_api_key ";

        try {
            accountHelper.addMovieRating(tmdb, -1, null, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_remove_rating_invalid_api_key() {
        final String funcName = "test_account_movie_remove_rating_invalid_api_key ";

        try {
            accountHelper.removeMovieRating(tmdb, -1, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_config_api_invalid_api_key() {
        final String funcName = "test_config_api_invalid_api_key ";

        try {
            configurationHelper.processConfigApi(tmdb);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_config_lang_invalid_api_key() {
        final String funcName = "test_config_lang_invalid_api_key ";

        try {
            configurationHelper.processConfigLanguage(tmdb);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_API_KEY_INVALID, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_API_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }
}