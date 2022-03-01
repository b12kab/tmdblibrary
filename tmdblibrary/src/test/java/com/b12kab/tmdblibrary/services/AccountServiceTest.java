package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.entities.AccountFavorite;
import com.b12kab.tmdblibrary.entities.AccountResponse;
import com.b12kab.tmdblibrary.entities.AccountState;
import com.b12kab.tmdblibrary.entities.ListResultsPage;
import com.b12kab.tmdblibrary.entities.MovieRatingValue;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.Status;
import com.b12kab.tmdblibrary.enumerations.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AccountServiceTest extends BaseTestCase {

    @BeforeEach
    void init() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){
            System.out.println(e);
        }
    }

    @Test
    public void test_account() throws IOException {
        final String funcName = "test_account ";

        String session = this.createTmdbSession();

        Call<AccountResponse> call = null;
        ResponseBody errorBody = null;
        Response<AccountResponse> response = null;
        AccountResponse acctResp = null;

        try {
            call = this.getManager().accountService().getAccountInfo(session);
            response = call.execute();
            acctResp = response.body();
            errorBody = response.errorBody();
        } catch (Exception e) {
            fail("Exception occurred on test_account - " + e.toString());
        }

        assertNull(errorBody, funcName + "error body is null");
        assertNotNull(acctResp, funcName + "AccountResponse is null");

        assertNotNull(acctResp.getActualName(), funcName + "actual name is null");
        assertNotNull(acctResp.getAvatar(), funcName + "avatar is null");
        assertNotNull(acctResp.getAvatar().getGravatar(), funcName + "avatar's gravitar is null");
        assertNotNull(acctResp.getAvatar().getGravatar().getHash(), funcName + "avatar's gravitar's hash is null");
        assertNotNull(acctResp.getCountryCodes(), funcName + "country code is null");
        assertNotNull(acctResp.getId(), funcName + "Id is null");
        assertNotNull(acctResp.getLanguageRepresentation(), funcName + "language is null");
        assertNotNull(acctResp.getUserName(), funcName + "username is null");
        assertNotNull(acctResp.isIncludeAdult(), funcName + "include adult is null");
    }

    @Test
    public void test_account_lists() throws IOException {
        final String funcName = "test_account_lists ";

        String session = this.createTmdbSession();
        Integer acctId = getAccountId(session);

        Call<ListResultsPage> call = null;
        ResponseBody errorBody = null;
        Response<ListResultsPage> response = null;
        ListResultsPage listResultsPage = null;

        try {
            call = this.getManager().accountService().getAccountLists(acctId, session, 1, null );
            response = call.execute();
            listResultsPage = response.body();
            errorBody = response.errorBody();
        } catch (Exception e) {
            fail("Exception occurred on test_account_lists - " + e.toString());
        }

        assertNull(errorBody, funcName + "error body is null");
        assertNotNull(listResultsPage, funcName + "list results is null");

//        assertNotNull(listResultsPage.id, funcName + "id is null");
        assertNotNull(listResultsPage.page, funcName + "page is null");
        assertNotNull(listResultsPage.total_results, funcName + "total results is null");
        assertNotNull(listResultsPage.total_pages, funcName + "total pages is null");
        assertNotNull(listResultsPage.results, funcName + "results is null");
//        assertTrue(listResultsPage.results.size() > 0, funcName + "results size is 0");
    }

    @Test
    public void test_rated_movies() throws IOException {
        final String funcName = "test_rated_movies ";

        String session = this.createTmdbSession();
        Integer acctId = getAccountId(session);

        Call<MovieResultsPage> call = null;
        ResponseBody errorBody = null;
        Response<MovieResultsPage> response = null;
        MovieResultsPage movieResultsPage = null;

        try {
            call = this.getManager().accountService().getAccountRatedMovie(acctId, session, 1, null, null );
            response = call.execute();
            movieResultsPage = response.body();
            errorBody = response.errorBody();
        } catch (Exception e) {
            fail("Exception occurred on test_rated_movies - " + e.toString());
        }

        assertNull(errorBody, funcName + "error body is null");
        assertNotNull(movieResultsPage, funcName + "movie results is null");

        assertNotNull(movieResultsPage.page, funcName + "page is null");
        assertNotNull(movieResultsPage.total_results, funcName + "total results is null");
        assertNotNull(movieResultsPage.total_pages, funcName + "total pages is null");
        assertNotNull(movieResultsPage.results, funcName + "results is null");
    }

    @Test
    public void test_favorited_movies() throws IOException {
        final String funcName = "test_favorited_movies ";

        String session = this.createTmdbSession();
        Integer acctId = getAccountId(session);

        Call<MovieResultsPage> call = null;
        ResponseBody errorBody = null;
        Response<MovieResultsPage> response = null;
        MovieResultsPage movieResultsPage = null;

        try {
            call = this.getManager().accountService().getFavoritedMovies(acctId, session, 1, null, null );
            response = call.execute();
            movieResultsPage = response.body();
            errorBody = response.errorBody();
        } catch (Exception e) {
            fail("Exception occurred on test_favorited_movies - " + e.toString());
        }

        assertNull(errorBody, funcName + "error body is null");
        assertNotNull(movieResultsPage, funcName + "movie results is null");

        assertNotNull(movieResultsPage.page, funcName + "page is null");
        assertNotNull(movieResultsPage.total_results, funcName + "total results is null");
        assertNotNull(movieResultsPage.total_pages, funcName + "total pages is null");
        assertNotNull(movieResultsPage.results, funcName + "results is null");
    }

    @Test
    public void test_make_favorite_movie() throws IOException {
        final String funcName = "test_make_favorite_movie ";

        String session = this.createTmdbSession();
        Integer acctId = getAccountId(session);

        Call<Status> call = null;
        ResponseBody errorBody = null;
        Response<Status> response = null;
        Status status = null;

        AccountFavorite accountFavorite = new AccountFavorite();
        accountFavorite.setMediaType(MediaType.MOVIE.toString());
        accountFavorite.setFavorite(true);
        accountFavorite.setId(TestData.MOVIE_ID);

        try {
            call = this.getManager().accountService().setFavorite(acctId, session, accountFavorite );
            response = call.execute();
            status = response.body();
            errorBody = response.errorBody();
        } catch (Exception e) {
            fail("Exception occurred on test_make_favorite_movie - " + e.toString());
        }

        assertNull(errorBody, funcName + "error body is null");
        assertNotNull(status, funcName + "status is null");

        assertNotNull(status.getStatusCode(), funcName + "status code is null");
        assertNotNull(status.getStatusMessage(), funcName + "status message is null");
    }

    @Test
    public void test_movie_acct_state() throws IOException {
        final String funcName = "test_movie_acct_state ";

        String session = this.createTmdbSession();

        Call<AccountState> call = null;
        ResponseBody errorBody = null;
        Response<AccountState> response = null;
        AccountState acctState = null;

        try {
            call = this.getManager().accountService().getMovieAccountState(TestData.MOVIE_ID, session, null );
            response = call.execute();
            acctState = response.body();
            errorBody = response.errorBody();
        } catch (Exception e) {
            fail("Exception occurred on test_movie_acct_state - " + e.toString());
        }

        assertNull(errorBody, funcName + "error body is null");
        assertNotNull(acctState, funcName + "account state is null");

        assertEquals(acctState.getId(), TestData.MOVIE_ID, "movie id does not match");
//        assertNotNull(acctState.getRated(), funcName + "rating is null");
        assertNotNull(acctState.isWatchlist(), funcName + "watch list is null");
    }

    @Test
    public void test_movie_set_movie_rating() throws IOException {
        final String funcName = "test_movie_set_movie_rating ";

        String session = this.createTmdbSession();

        Call<Status> call = null;
        ResponseBody errorBody = null;
        Response<Status> response = null;
        Status status = null;

        MovieRatingValue movieRatingValue = new MovieRatingValue();
        movieRatingValue.setValue(8.5F);

        try {
            call = this.getManager().accountService().setMovieRating(TestData.MOVIE_ID, session, null,  movieRatingValue);
            response = call.execute();
            status = response.body();
            errorBody = response.errorBody();
        } catch (Exception e) {
            fail("Exception occurred on test_movie_set_movie_rating - " + e.toString());
        }

        assertNull(errorBody, funcName + "error body is null");
        assertNotNull(status, funcName + "status is null");

        assertNotNull(status.getStatusCode(), funcName + "status code is null");
        assertNotNull(status.getStatusMessage(), funcName + "status message is null");
    }

    @Test
    public void test_movie_remove_movie_rating() throws IOException {
        final String funcName = "test_movie_remove_movie_rating ";

        String session = this.createTmdbSession();

        Call<Status> call = null;
        ResponseBody errorBody = null;
        Response<Status> response = null;
        Status status = null;

        MovieRatingValue movieRatingValue = new MovieRatingValue();
        movieRatingValue.setValue(8.5F);

        try {
            Status setStatus = this.getManager().accountService().setMovieRating(TestData.MOVIE_ID, session, null,  movieRatingValue).execute().body();
            assertNotNull(setStatus, funcName + "set status is null");
            assertTrue(setStatus.getSuccess(), funcName + "set status success is false");

            call = this.getManager().accountService().removeMovieRating(TestData.MOVIE_ID, session, null);
            response = call.execute();
            status = response.body();
            errorBody = response.errorBody();
        } catch (Exception e) {
            fail("Exception occurred on test_movie_remove_movie_rating - " + e.toString());
        }

        assertNull(errorBody, funcName + "error body is null");
        assertNotNull(status, funcName + "status is null");

        assertNotNull(status.getStatusCode(), funcName + "status code is null");
        assertNotNull(status.getStatusMessage(), funcName + "status message is null");
    }

    public Integer getAccountId(String session) throws IOException {
        AccountResponse acctResp = this.getManager().accountService().getAccountInfo(session).execute().body();
        if (acctResp != null && acctResp.getId() != null) {
            return acctResp.getId();
        }

        return null;
    }
}
