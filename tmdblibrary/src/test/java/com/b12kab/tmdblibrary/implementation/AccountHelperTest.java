package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.assertations.MovieAsserts;
import com.b12kab.tmdblibrary.entities.AccountResponse;
import com.b12kab.tmdblibrary.entities.MovieAbbreviated;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.enumerations.AccountFetchType;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import org.junit.jupiter.api.Test;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_ACCOUNT_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_PAGE_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_SESSION_RELATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AccountHelperTest extends BaseTestCase {
    AccountHelper helper;

    public AccountHelperTest() {
        helper = new AccountHelper();
    }

//    @BeforeEach
//    void init() {
//        this.sleepSetup(2);
//    }

    @Test
    public void test_account_movie_tmdb_is_null() {
        final String funcName = "test_account_movie_tmdb_is_null ";

        try {
            helper.ProcessAccountMovieInfo(null, null, null, 0, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_invalid_fetch() {
        final String funcName = "test_account_movie_invalid_fetch ";

        try {
            helper.ProcessAccountMovieInfo(this.getManager(), null, null, 0, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_PAGE_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("fetch"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_account_movie_null_session() {
        final String funcName = "test_account_movie_invalid_session ";

        try {
            helper.ProcessAccountMovieInfo(this.getManager(), AccountFetchType.Rated, null, 0, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_SESSION_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("TMDb session"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_account_movie_invalid_account() {
        final String funcName = "test_account_movie_invalid_account ";

        try {
            helper.ProcessAccountMovieInfo(this.getManager(), AccountFetchType.Rated, "xxx", 0, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_ACCOUNT_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("TMDb account"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_account_movie_valid_session_favored() {
        final String funcName = "test_account_movie_valid_session_favored ";
        MovieResultsPage resultsPage = null;

        try {
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            resultsPage = helper.ProcessAccountMovieInfo(this.getManager(), AccountFetchType.Favored, session, accountResponse.getId(), null, null);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }
        MovieAsserts.assertMovieResultsPage(resultsPage, true, false);
        // Note: there's no current favorite indicator other than being on this list
    }

    @Test
    public void test_account_movie_valid_session_rated() {
        final String funcName = "test_account_movie_valid_session_rated ";
        MovieResultsPage resultsPage = null;

        try {
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            resultsPage = helper.ProcessAccountMovieInfo(this.getManager(), AccountFetchType.Rated, session, accountResponse.getId(), null, null);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovieResultsPage(resultsPage, true, false);
        for (MovieAbbreviated movie: resultsPage.getResults()) {
            assertNotNull(movie.getRating(), "results page rating is null");
        }
    }

    @Test
    public void test_account_movie_valid_session_rated_page_limiter() {
        final String funcName = "test_account_movie_valid_session_rated_page_limiter ";
        MovieResultsPage resultsPage = null;

        try {
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);
            helper.setMaxPageFetch(1);

            resultsPage = helper.ProcessAccountMovieInfo(this.getManager(), AccountFetchType.Rated, session, accountResponse.getId(), null, null);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        MovieAsserts.assertMovieResultsPage(resultsPage, true, false);
        for (MovieAbbreviated movie: resultsPage.getResults()) {
            assertNotNull(movie.getRating(), "results page rating is null");
        }

        assertEquals(20, resultsPage.getResults().size(), "results page results size != 20");
        assertTrue(resultsPage.total_results > 20, "results page results size <= 20");
    }

    @Test
    public void test_account_info_tmdb_is_null() {
        final String funcName = "test_account_info_tmdb_is_null ";

        try {
            helper.ProcessAccountInfo(null, null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_info_null_session() {
        final String funcName = "test_account_info_invalid_session ";

        try {
            helper.ProcessAccountInfo(this.getManager(), null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_SESSION_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("TMDb session"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_account_info_invalid_session() {
        final String funcName = "test_account_info_invalid_session ";

        try {
            helper.ProcessAccountInfo(this.getManager(), "xxx");
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(3, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_account_info_valid_session() {
        final String funcName = "test_account_info_valid_session ";

        AccountResponse accountResponse = null;

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();
            accountResponse = helper.ProcessAccountInfo(this.getManager(), session);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(accountResponse, funcName + "response is null");
        assertNotNull(accountResponse.getId(), funcName + "response id is null");
    }


}
