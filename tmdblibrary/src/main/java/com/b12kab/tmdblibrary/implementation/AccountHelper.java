package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.NetworkHelper;
import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.AccountResponse;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.enumerations.AccountFetchType;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Response;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_API_ERR_MSG;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_ACCOUNT_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_API_KEY_INVALID;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_PAGE_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_SESSION_RELATED;

public class AccountHelper extends NetworkHelper {
    @FunctionalInterface
    interface GetAccountMovieInfo {
        Call<MovieResultsPage> initialMovies(Tmdb tmdb, int accountId, String session, int page, String sortBy, String language);
    }
    GetAccountMovieInfo fetchFavored = (t, a, se, p, so, l) -> t.accountService().getAccountFavoredMovies(a, se, p, so, l);
    GetAccountMovieInfo fetchRated = (t, a, se, p, so, l) -> t.accountService().getAccountRatedMovie(a, se, p, so, l);

    private int maxPageFetch = 100;

    public int getMaxPageFetch() {
        return maxPageFetch;
    }

    /***
     * This will likely only be used for unit tests
     * @param maxPageFetch
     */
    public void setMaxPageFetch(int maxPageFetch) {
        if (maxPageFetch > 1000) {
            this.maxPageFetch = 1000;
            return;
        }
        this.maxPageFetch = maxPageFetch;
    }

    /***
     * Get account information of the signed in user
     *
     * @param tmdb Tmdb
     * @param session API Key
     * @return AccountResponse
     * @throws IOException TmdbException
     */
    public AccountResponse ProcessAccountInfo(Tmdb tmdb, String session) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            throw new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
        }

        // Check userid / passwd
        if (session == null || StringUtils.isBlank(session)) {
            throw new TmdbException(TMDB_CODE_SESSION_RELATED, "You must provide a populated TMDb session");
        }

        AccountResponse movieFull = this.ObtainAccountInfo(tmdb, session);

        return movieFull;
    }

    /***
     * Get account information of the signed in user
     *
     * @param tmdb Tmdb
     * @param session API Key
     * @return AccountResponse
     * @throws IOException TmdbException
     */
    @Nullable
    private AccountResponse ObtainAccountInfo(@NonNull Tmdb tmdb, String session) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                AccountResponse accountResponse = this.GetAccountMovieInfoPage(tmdb, session);

                if (accountResponse != null) {
                    return accountResponse;
                }

                // result not success, retry - it can't hurt
                retry = true;
                retryTime = 2;
            } catch (Exception ex) {
                if (ex instanceof TmdbException)
                {
                    TmdbException tmdbException = (TmdbException) ex;
                    NetworkHelper.ExceptionCheckReturn checkReturn = this.CheckForNetworkRetry(tmdbException);
                    if (!checkReturn.retry)
                        throw ex;

                    retry = true;
                    retryTime = checkReturn.retryTime;
                } else {
                    throw ex;
                }
            }

            if (retry) {
                try {
                    Thread.sleep((int) ((retryTime + 0.5) * 1000));
                } catch (InterruptedException e) { }
            } else {
                break;
            }
        }

        return null;
    }

    /***
     * Get account information, especially the account id.
     *
     * @param tmdb Tmdb
     * @param session API Key
     * @return AccountResponse
     * @throws IOException TmdbException
     */
    private AccountResponse GetAccountMovieInfoPage(@NonNull Tmdb tmdb, String session) throws IOException {
        try {
            Call<AccountResponse> call = tmdb.accountService().getAccountInfo(session);
            Response<AccountResponse> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            this.ProcessError(response);
            // this will never return, but the compiler wants a return
            return null;
        } catch (Exception exception) {
            if (exception instanceof TmdbException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }

    /***
     * Perform the initial fetch movies pages
     *
     * @param tmdb Tmdb
     * @param fetchType Account data type to fetch
     * @param session API Key
     * @param accountId User's account #
     * @param sortBy <em>Optional.</em> sort by
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultsPage
     * @throws IOException TmdbException
     */
    public MovieResultsPage ProcessAccountMovieInfo(Tmdb tmdb, AccountFetchType fetchType, String session, int accountId, String sortBy, String language) throws IOException {
        GetAccountMovieInfo pass = null;

        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            throw new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
        }

        if (fetchType == AccountFetchType.Favored) {
            pass = fetchFavored;
        } else if (fetchType == AccountFetchType.Rated) {
            pass = fetchRated;
        } else {
            throw new TmdbException(TMDB_CODE_PAGE_RELATED, "Invalid fetch type");
        }

        // Check userid / passwd
        if (session == null || StringUtils.isBlank(session)) {
            throw new TmdbException(TMDB_CODE_SESSION_RELATED, "You must provide a populated TMDb session");
        }

        if (accountId < 1) {
            throw new TmdbException(TMDB_CODE_ACCOUNT_RELATED, "Invalid TMDb account");
        }

        MovieResultsPage movieFull = this.ObtainAccountMovieInfoPages(pass, tmdb, session, accountId, sortBy, language);

        return movieFull;
    }

    /***
     * Loop thru pages to fetch
     *
     * @param function Method to call
     * @param tmdb Tmdb
     * @param session API Key
     * @param accountId User's account #
     * @param sortBy <em>Optional.</em> sort by
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultsPage
     * @throws IOException TmdbException
     */
    private MovieResultsPage ObtainAccountMovieInfoPages(GetAccountMovieInfo function, @NonNull Tmdb tmdb, String session, int accountId, String sortBy, String language) throws IOException {
        MovieResultsPage results = null;
        boolean initResults = false;

        // TMDb pages start at 1
        for (int pageCount = 1; pageCount <= this.getMaxPageFetch(); pageCount++) {
            MovieResultsPage resultsPage = this.ObtainAccountMovieInfoPage(function, tmdb, session, accountId, pageCount, sortBy, language);

            if (resultsPage != null && resultsPage.results != null && resultsPage.results.size() > 0) {
                if (!initResults) {
                    results = resultsPage;
                    initResults = true;
                } else {
                    results.results.addAll(resultsPage.results);
                    results.page = pageCount;
                }

                if (pageCount >= results.total_pages) {
                    break;
                }
            }
        }

        return results;
    }

    /***
     * Get specific TMDb page
     *
     * @param function Method to call
     * @param tmdb Tmdb
     * @param session API Key
     * @param accountId User's account #
     * @param page tmdb page #
     * @param sortBy <em>Optional.</em> sort by
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultsPage
     * @throws IOException TmdbException
     */
    @Nullable
    private MovieResultsPage ObtainAccountMovieInfoPage(GetAccountMovieInfo function, @NonNull Tmdb tmdb, String session, int accountId, int page, String sortBy, String language) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                MovieResultsPage resultsPage = this.GetAccountMovieInfoPage(function, tmdb, session, accountId, page, sortBy, language);

                if (resultsPage != null) {
                    return resultsPage;
                }

                // result not success, retry - it can't hurt
                retry = true;
                retryTime = 2;
            } catch (Exception ex) {
                if (ex instanceof TmdbException)
                {
                    TmdbException tmdbException = (TmdbException) ex;
                    NetworkHelper.ExceptionCheckReturn checkReturn = this.CheckForNetworkRetry(tmdbException);
                    if (!checkReturn.retry)
                        throw ex;

                    retry = true;
                    retryTime = checkReturn.retryTime;
                } else {
                    throw ex;
                }
            }

            if (retry) {
                try {
                    Thread.sleep((int) ((retryTime + 0.5) * 1000));
                } catch (InterruptedException e) { }
            } else {
                break;
            }
        }

        return null;
    }

    /***
     * Get the account's movie information for a particular page
     *
     * @param function Method to call
     * @param tmdb Tmdb
     * @param session API Key
     * @param accountId User's account #
     * @param page tmdb page #
     * @param sortBy <em>Optional.</em> sort by
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultsPage
     * @throws IOException TmdbException
     */
    private MovieResultsPage GetAccountMovieInfoPage(GetAccountMovieInfo function, @NonNull Tmdb tmdb, String session, int accountId, int page, String sortBy, String language) throws IOException {
        try {
            Call<MovieResultsPage> call = function.initialMovies(tmdb, accountId, session, page, sortBy, language);
            Response<MovieResultsPage> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            this.ProcessError(response);
            // this will never return, but the compiler wants a return
            return null;
        } catch (Exception exception) {
            if (exception instanceof TmdbException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }





}
