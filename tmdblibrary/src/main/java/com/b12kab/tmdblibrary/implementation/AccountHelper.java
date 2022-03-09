package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.NetworkHelper;
import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.AccountFavorite;
import com.b12kab.tmdblibrary.entities.AccountResponse;
import com.b12kab.tmdblibrary.entities.AccountState;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.RatingValue;
import com.b12kab.tmdblibrary.entities.Status;
import com.b12kab.tmdblibrary.enumerations.AccountFetchType;
import com.b12kab.tmdblibrary.enumerations.MediaType;
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
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_FAVORITE_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_MOVIE_ID_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_PAGE_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_RATING_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_SESSION_RELATED;

public class AccountHelper extends NetworkHelper {
    @FunctionalInterface
    interface GetAccountMovieInfo {
        Call<MovieResultsPage> initialMovies(Tmdb tmdb, String session, int accountId, int page, String sortBy, String language);
    }
    GetAccountMovieInfo fetchFavored = (t, se, a, p, so, l) -> t.accountService().getAccountFavoredMovies(a, se, p, so, l);
    GetAccountMovieInfo fetchRated = (t, se, a, p, so, l) -> t.accountService().getAccountRatedMovie(a, se, p, so, l);

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
     * @param session session Key
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
     * @param session session Key
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
     * @param session session Key
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
     * @param accountId User's account #
     * @param session session Key
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
     * @param session session Key
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
     * @param session session Key
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
     * @param session session Key
     * @param accountId User's account #
     * @param page tmdb page #
     * @param sortBy <em>Optional.</em> sort by
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultsPage
     * @throws IOException TmdbException
     */
    private MovieResultsPage GetAccountMovieInfoPage(GetAccountMovieInfo function, @NonNull Tmdb tmdb, String session, int accountId, int page, String sortBy, String language) throws IOException {
        try {
            Call<MovieResultsPage> call = function.initialMovies(tmdb, session, accountId, page, sortBy, language);
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

    /***
     * Get the account movie information for the signed in user or guest user session
     *
     * @param tmdb Tmdb
     * @param movieId Tmdb movie id
     * @param session session Key
     * @param guestSessionId guest session
     * @return AccountState
     * @throws IOException TmdbException
     */
    public AccountState ProcessAccountMovieInfoDetail(Tmdb tmdb, int movieId, String session, String guestSessionId) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            throw new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
        }

        if (movieId < 1) {
            throw new TmdbException(TMDB_CODE_MOVIE_ID_RELATED, "Invalid TMDb movie id");
        }

        if ((session == null || StringUtils.isBlank(session) && (guestSessionId == null || StringUtils.isBlank(guestSessionId)))) {
            throw new TmdbException(TMDB_CODE_SESSION_RELATED, "You must provide a populated session or guest session");
        }

        AccountState accountState = this.ObtainAccountMovieInfoDetail(tmdb, movieId, session, guestSessionId);

        return accountState;
    }

    /***
     * Get the account movie information
     *
     * @param tmdb Tmdb
     * @param movieId Tmdb movie id
     * @param session session Key
     * @param guestSessionId guest session
     * @return AccountState
     * @throws IOException TmdbException
     */
    private AccountState ObtainAccountMovieInfoDetail(@NonNull Tmdb tmdb, int movieId, String session, String guestSessionId) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                AccountState accountState = this.GetAccountMovieInfoDetail(tmdb, movieId, session, guestSessionId);

                if (accountState != null) {
                    return accountState;
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
     * Get the account movie information
     *
     * @param tmdb Tmdb
     * @param movieId Tmdb movie id
     * @param session session Key
     * @param guestSessionId guest session
     * @return AccountState
     * @throws IOException TmdbException
     */
    private AccountState GetAccountMovieInfoDetail(@NonNull Tmdb tmdb, int movieId, String session, String guestSessionId) throws IOException {
        try {
            Call<AccountState> call = tmdb.accountService().getMovieAccountState(movieId, session, guestSessionId);
            Response<AccountState> response = call.execute();
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
     * Set the user's favorite on their account
     *
     * @param tmdb Tmdb
     * @param session session Key
     * @param accountId User's account #
     * @param favorite AccountFavorite
     * @return Status Set status
     * @throws IOException TmdbException
     */
    public Status ProcessAccountFavorite(Tmdb tmdb, String session, int accountId, AccountFavorite favorite) throws IOException {
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

        if (accountId < 1) {
            throw new TmdbException(TMDB_CODE_ACCOUNT_RELATED, "Invalid TMDb account id");
        }

        if (favorite == null) {
            throw new NullPointerException("AccountFavorite is null");
        } else {
            if (favorite.getMediaType() == null || StringUtils.isBlank(favorite.getMediaType())) {
                throw new TmdbException(TMDB_CODE_FAVORITE_RELATED, "Empty favorite media type");
            } else if (!(favorite.getMediaType().equals(MediaType.MOVIE.toString()) || favorite.getMediaType().equals(MediaType.TV.toString()))) {
                throw new TmdbException(TMDB_CODE_FAVORITE_RELATED, "Favorite media type must be either " + MediaType.MOVIE.toString() + " or " + MediaType.TV.toString());
            }
            if (favorite.getId() == null) {
                throw new TmdbException(TMDB_CODE_FAVORITE_RELATED, "Empty favorite media id");
            } else if (favorite.getId() < 1) {
                throw new TmdbException(TMDB_CODE_FAVORITE_RELATED, "Invalid favorite media id");
            }
            if (favorite.getFavorite() == null) {
                throw new TmdbException(TMDB_CODE_FAVORITE_RELATED, "Empty favorite setting");
            }
        }

        Status status = this.TryAccountFavorite(tmdb, session, accountId, favorite);

        return status;
    }

    /***
     * Try to set the favorite on the user's account
     *
     * @param tmdb Tmdb
     * @param session session Key
     * @param accountId User's account #
     * @param favorite AccountFavorite
     * @return Status Set status
     * @throws IOException TmdbException
     */
    private Status TryAccountFavorite(@NonNull Tmdb tmdb, String session, int accountId, AccountFavorite favorite) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                Status state = this.SetAccountFavorite(tmdb, session, accountId, favorite);

                if (state != null) {
                    return state;
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
     * Set the account favorite
     *
     * @param tmdb Tmdb
     * @param session session Key
     * @param accountId User's account #
     * @param favorite AccountFavorite
     * @return Status Set status
     * @throws IOException TmdbException
     */
    private Status SetAccountFavorite(@NonNull Tmdb tmdb, String session, int accountId, AccountFavorite favorite) throws IOException {
        try {
            Call<Status> call = tmdb.accountService().setAccountFavorite(accountId, session, favorite);
            Response<Status> response = call.execute();
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
     * Set the user's movie rating on their account
     *
     * @param tmdb Tmdb
     * @param movieId Tmdb movie id
     * @param session session Key
     * @param guestSessionId guest session
     * @param ratingValue RatingValue
     * @return Status
     * @throws IOException TmdbException
     */
    public Status ProcessMovieRating(Tmdb tmdb, int movieId, String session, String guestSessionId, RatingValue ratingValue) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            throw new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
        }

        if (movieId < 1) {
            throw new TmdbException(TMDB_CODE_MOVIE_ID_RELATED, "Invalid TMDb movie id");
        }

        if ((session == null || StringUtils.isBlank(session) && (guestSessionId == null || StringUtils.isBlank(guestSessionId)))) {
            throw new TmdbException(TMDB_CODE_SESSION_RELATED, "You must provide a populated session or guest session");
        }

        if (ratingValue == null) {
            throw new NullPointerException("RatingValue is null");
        } else {
            if (ratingValue.getValue() < 0.5 || ratingValue.getValue() > 10) {
                throw new TmdbException(TMDB_CODE_RATING_RELATED, "The rating value is expected to be between 0.5 and 10.0.");
            }
        }

        Status status = this.TryMovieRating(tmdb, movieId, session, guestSessionId, ratingValue);

        return status;
    }

    /***
     * Try to set the movie rating on the user's account
     *
     * @param tmdb Tmdb
     * @param movieId Tmdb movie id
     * @param session session Key
     * @param guestSessionId guest session
     * @param ratingValue RatingValue
     * @return Status
     * @throws IOException TmdbException
     */
    private Status TryMovieRating(@NonNull Tmdb tmdb, int movieId, String session, String guestSessionId, RatingValue ratingValue) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                Status state = this.SetMovieRating(tmdb, movieId, session, guestSessionId, ratingValue);

                if (state != null) {
                    return state;
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
     * Set the movie rating
     *
     * @param tmdb Tmdb
     * @param movieId Tmdb movie id
     * @param session session Key
     * @param guestSessionId guest session
     * @param ratingValue RatingValue
     * @return Status
     * @throws IOException TmdbException
     */
    private Status SetMovieRating(@NonNull Tmdb tmdb, int movieId, String session, String guestSessionId, RatingValue ratingValue) throws IOException {
        try {
            Call<Status> call = tmdb.accountService().setMovieRating(movieId, session, guestSessionId, ratingValue);
            Response<Status> response = call.execute();
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
