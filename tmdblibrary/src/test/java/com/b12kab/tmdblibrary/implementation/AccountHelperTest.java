package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.assertations.MovieAsserts;
import com.b12kab.tmdblibrary.entities.AccountFavorite;
import com.b12kab.tmdblibrary.entities.AccountResponse;
import com.b12kab.tmdblibrary.entities.AccountState;
import com.b12kab.tmdblibrary.entities.MovieAbbreviated;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.RatingValue;
import com.b12kab.tmdblibrary.entities.Status;
import com.b12kab.tmdblibrary.enumerations.AccountFetchType;
import com.b12kab.tmdblibrary.enumerations.MediaType;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_ACCOUNT_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_FAVORITE_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_MOVIE_ID_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_PAGE_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_RATING_RELATED;
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
            helper.processAccountMovieInfo(null, null, null, 0, null, null);
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
            helper.processAccountMovieInfo(this.getManager(), null, null, 0, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_PAGE_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("fetch"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_null_session() {
        final String funcName = "test_account_movie_invalid_session ";

        try {
            helper.processAccountMovieInfo(this.getManager(), AccountFetchType.Rated, null, 0, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_SESSION_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("TMDb session"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_invalid_account() {
        final String funcName = "test_account_movie_invalid_account ";

        try {
            helper.processAccountMovieInfo(this.getManager(), AccountFetchType.Rated, "xxx", 0, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_ACCOUNT_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("TMDb account"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_valid_session_favored() {
        final String funcName = "test_account_movie_valid_session_favored ";
        MovieResultsPage resultsPage = null;

        try {
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            resultsPage = helper.processAccountMovieInfo(this.getManager(), AccountFetchType.Favored, session, accountResponse.getId(), null, null);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
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

            resultsPage = helper.processAccountMovieInfo(this.getManager(), AccountFetchType.Rated, session, accountResponse.getId(), null, null);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
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

            resultsPage = helper.processAccountMovieInfo(this.getManager(), AccountFetchType.Rated, session, accountResponse.getId(), null, null);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
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
            helper.processAccountInfo(null, null);
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
            helper.processAccountInfo(this.getManager(), null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_SESSION_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("TMDb session"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_info_invalid_session() {
        final String funcName = "test_account_info_invalid_session ";

        try {
            helper.processAccountInfo(this.getManager(), "xxx");
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(3, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_info_valid_session() {
        final String funcName = "test_account_info_valid_session ";

        AccountResponse accountResponse = null;

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();
            accountResponse = helper.processAccountInfo(this.getManager(), session);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        assertNotNull(accountResponse, funcName + "response is null");
        assertNotNull(accountResponse.getId(), funcName + "response id is null");
    }

    @Test
    public void test_account_movie_info_tmdb_is_null() {
        final String funcName = "test_account_movie_info_tmdb_is_null ";

        try {
            helper.processAccountMovieInfoDetail(null, 0, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_info_movieId_invalid() {
        final String funcName = "test_account_movie_info_movieId_invalid ";

        try {
            helper.processAccountMovieInfoDetail(this.getManager(), 0, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_MOVIE_ID_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("movie id"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_info_null_session() {
        final String funcName = "test_account_movie_info_null_session ";

        try {
            helper.processAccountMovieInfoDetail(this.getManager(), 1, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_SESSION_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("session"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_info_invalid_session() {
        final String funcName = "test_account_movie_info_invalid_session ";

        try {
            helper.processAccountMovieInfoDetail(this.getManager(), TestData.MOVIE_ID,"xxx", null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(3, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_info_valid_session() {
        final String funcName = "test_account_movie_info_valid_session ";

        AccountState accountState = null;

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();
            accountState = helper.processAccountMovieInfoDetail(this.getManager(), TestData.MOVIE_ID, session, null);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        assertNotNull(accountState, funcName + "response is null");
        assertNotNull(accountState.getId(), funcName + "response id is null");
        assertEquals(TestData.MOVIE_ID, accountState.getId(), funcName + "response id != requested movie id");
        assertNotNull(accountState.getRated(), funcName + "response rating is null");
        assertTrue(accountState.getRated() > 0, funcName + "response rating is 0");
        assertTrue(accountState.isFavorite(), funcName + "response favorite is false");
    }

    @Test
    public void test_account_favorite_tmdb_is_null() {
        final String funcName = "test_account_favorite_tmdb_is_null ";

        try {
            helper.processAccountFavorite(null, null, -1, null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_null_session() {
        final String funcName = "test_account_favorite_null_session ";

        try {
            helper.processAccountFavorite(this.getManager(), null, 0, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_SESSION_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("session"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_invalid_accountId() {
        final String funcName = "test_account_favorite_invalid_accountId ";

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();

            helper.processAccountFavorite(this.getManager(), session, 0, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_ACCOUNT_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("account id"), funcName + "message does not contain account id");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_null_favorite() {
        final String funcName = "test_account_favorite_null_favorite ";

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            helper.processAccountFavorite(this.getManager(), session, accountResponse.getId(), null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_bad_favorite_type_null() {
        final String funcName = "test_account_favorite_bad_favorite_type_null ";
        AccountFavorite favorite = new AccountFavorite();
        favorite.setMediaType((String)null);

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            helper.processAccountFavorite(this.getManager(), session, accountResponse.getId(), favorite);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_FAVORITE_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("Empty favorite media type"), funcName + "message does not contain account id");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_bad_favorite_type_empty() {
        final String funcName = "test_account_favorite_bad_favorite_type_empty ";
        AccountFavorite favorite = new AccountFavorite();
        favorite.setMediaType("");

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            helper.processAccountFavorite(this.getManager(), session, accountResponse.getId(), favorite);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_FAVORITE_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("Empty favorite media type"), funcName + "message does not contain account id");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_bad_favorite_type_invalid() {
        final String funcName = "test_account_favorite_bad_favorite_type_invalid ";
        AccountFavorite favorite = new AccountFavorite();
        favorite.setMediaType("xxx");

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            helper.processAccountFavorite(this.getManager(), session, accountResponse.getId(), favorite);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_FAVORITE_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("Favorite media type must be either"), funcName + "message does not contain account id");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_bad_favorite_id_null() {
        final String funcName = "test_account_favorite_bad_favorite_id_null ";
        AccountFavorite favorite = new AccountFavorite();
        favorite.setMediaType(MediaType.TV);

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            helper.processAccountFavorite(this.getManager(), session, accountResponse.getId(), favorite);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_FAVORITE_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("Empty favorite media id"), funcName + "message does not contain account id");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_bad_favorite_id_invalid() {
        final String funcName = "test_account_favorite_bad_favorite_id_invalid ";
        AccountFavorite favorite = new AccountFavorite();
        favorite.setMediaType(MediaType.TV);
        favorite.setId(0);

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            helper.processAccountFavorite(this.getManager(), session, accountResponse.getId(), favorite);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_FAVORITE_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("Invalid favorite media id"), funcName + "message does not contain account id");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_bad_favorite_id_favorite() {
        final String funcName = "test_account_favorite_bad_favorite_id_favorite ";
        AccountFavorite favorite = new AccountFavorite();
        favorite.setMediaType(MediaType.TV);
        favorite.setId(TestData.TVSHOW_ID);

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            helper.processAccountFavorite(this.getManager(), session, accountResponse.getId(), favorite);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_FAVORITE_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("Empty favorite setting"), funcName + "message does not contain account id");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_invalid_session() {
        final String funcName = "test_account_favorite_invalid_session ";
        AccountFavorite favorite = new AccountFavorite();
        favorite.setFavorite(false);
        favorite.setMediaType(MediaType.TV);
        favorite.setId(TestData.TVSHOW_ID);

        try {
            this.sleepSetup(2);
            helper.processAccountFavorite(this.getManager(),"xxx", 1, favorite);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(3, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_favorite_valid_session() {
        final String funcName = "test_account_favorite_valid_session ";
        AccountFavorite favorite = new AccountFavorite();
        favorite.setFavorite(false);
        favorite.setMediaType(MediaType.TV);
        favorite.setId(TestData.TVSHOW_ID);
        Status status = null;

        try {
            this.sleepSetup(2);
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            status = helper.processAccountFavorite(this.getManager(), session, accountResponse.getId(), favorite);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }
        assertNotNull(status, funcName + "status is null");
        assertNotNull(status.getStatusCode(), funcName + "status status code is null");
        assertTrue(status.getSuccess(), funcName + "status success is false");
        assertNotNull(status.getStatusMessage(), funcName + "status status message is null");
        assertEquals(13, status.getStatusCode(), funcName + "status status code is not 12");
    }

    @Test
    public void test_account_movie_rating_tmdb_is_null() {
        final String funcName = "test_account_movie_rating_tmdb_is_null ";

        try {
            helper.processMovieRating(null, -1, null, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_rating_movieId_invalid() {
        final String funcName = "test_account_movie_rating_movieId_invalid ";

        try {
            helper.processMovieRating(this.getManager(), 0, null, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_MOVIE_ID_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("movie id"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_rating_null_session() {
        final String funcName = "test_account_movie_rating_null_session ";

        try {
            helper.processMovieRating(this.getManager(), 1, null, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_SESSION_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("populated session or guest session"), funcName + "message does not contain session");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_rating_empty_session() {
        final String funcName = "test_account_movie_rating_empty_session ";

        try {
            helper.processMovieRating(this.getManager(), 1, "", "", null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_SESSION_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("populated session or guest session"), funcName + "message does not contain session");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_rating_null_rating() {
        final String funcName = "test_account_movie_rating_null_rating ";

        try {
            helper.processMovieRating(this.getManager(), 1, "xxx", null, null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_rating_0_rating() {
        final String funcName = "test_account_movie_rating_0_rating ";

        RatingValue ratingValue = new RatingValue();

        try {
            helper.processMovieRating(this.getManager(), 1, "xxx", "", ratingValue);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_RATING_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("expected to be between 0.5 and 10.0"), funcName + "message does not contain 'expected to be between 0.5 and 10.0'");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_rating_10_rating() {
        final String funcName = "test_account_movie_rating_10_rating ";

        RatingValue ratingValue = new RatingValue();
        ratingValue.setValue(10.01F);

        try {
            helper.processMovieRating(this.getManager(), 1, "xxx", "", ratingValue);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(TMDB_CODE_RATING_RELATED, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("expected to be between 0.5 and 10.0"), funcName + "message does not contain 'expected to be between 0.5 and 10.0'");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_rating_invalid_session() {
        final String funcName = "test_account_movie_rating_invalid_session ";

        RatingValue ratingValue = new RatingValue();
        ratingValue.setValue(8);

        try {
            helper.processMovieRating(this.getManager(), 1, "xxx", "", ratingValue);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(3, exception.getCode(), funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_account_movie_rating_valid_session() {
        final String funcName = "test_account_movie_rating_valid_session ";

        MovieResultsPage resultsPage = null;
        RatingValue ratingValue = new RatingValue();

        try {
            this.sleepSetup(4);
            String session = this.createTmdbSession();
            AccountResponse accountResponse = this.getAccount(session);

            resultsPage = helper.processAccountMovieInfo(this.getManager(), AccountFetchType.Rated, session, accountResponse.getId(), null, null);
            Optional<MovieAbbreviated> item = resultsPage.getResults().stream().filter(c -> c.getId() == TestData.MOVIE_RATING_TEST_ID)
                    .findFirst();

            float value = 5;
            if (item.isPresent()) {
                value = item.get().getRating();
                value += 0.5;
            }
            if (value > 10) {
                value = 5;
            }
            ratingValue.setValue(value);

            Status status = helper.processMovieRating(this.getManager(), TestData.MOVIE_RATING_TEST_ID, session, null, ratingValue);

            assertNotNull(status, funcName + "status is null");
            assertNotNull(status.getStatusCode(), funcName + "status status code is null");
            assertTrue(status.getSuccess(), funcName + "status success is false");
            assertNotNull(status.getStatusMessage(), funcName + "status status message is null");
            assertTrue(status.getStatusMessage().contains("uccess"), funcName + "status status message is not success");
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }
    }


}
