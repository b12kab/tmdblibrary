package com.b12kab.tmdblibrary.implementation;

import android.os.NetworkOnMainThreadException;

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
import com.b12kab.tmdblibrary.exceptions.TmdbNetworkException;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

public class AccountHelper extends NetworkHelper implements IAccountHelper {
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
     * This is a list of error status codes created by TMDb
     *
     * @return List<Integer>
     */
    public List<Integer> getAssocHelperTmdbErrorStatusCodes() {
        return Arrays.asList(
                1,  // success
                3,  // authentication failed
                7,  // invalid API key
                12, // item updated OK
                13, // item deleted OK
                34  // missing resource
        );
    }

    /***
     * This is a list of error status codes created by the helper
     *
     * @return List<Integer>
     */
    public List<Integer> getAssocHelperNonTmdbErrorStatusCodes() {
        return Arrays.asList(
                TMDB_CODE_API_KEY_INVALID,
                TMDB_CODE_PAGE_RELATED,
                TMDB_CODE_SESSION_RELATED,
                TMDB_CODE_ACCOUNT_RELATED,
                TMDB_CODE_MOVIE_ID_RELATED,
                TMDB_CODE_FAVORITE_RELATED,
                TMDB_CODE_RATING_RELATED
        );
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
    public AccountResponse processAccountInfo(Tmdb tmdb, String session) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        // Check userid / passwd
        if (session == null || StringUtils.isBlank(session)) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_SESSION_RELATED, "You must provide a populated TMDb session");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        AccountResponse movieFull = this.obtainAccountInfo(tmdb, session);

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
    private AccountResponse obtainAccountInfo(@NonNull Tmdb tmdb, String session) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                AccountResponse accountResponse = this.getAccountInfo(tmdb, session);

                if (accountResponse != null) {
                    return accountResponse;
                }

                // result not success, retry - it can't hurt
                retry = true;
                retryTime = 2;
            // Note - TmdbNetworkException and any other exception are ignored and will bubble up
            } catch (TmdbException ex) {
                NetworkHelper.ExceptionCheckReturn checkReturn = this.CheckForNetworkRetry(ex);
                if (!checkReturn.retry)
                    throw ex;

                retry = true;
                retryTime = checkReturn.retryTime;
            }

            if (retry) {
                try {
                    Thread.sleep((int) ((retryTime + 0.5) * 1000));
                } catch (InterruptedException ignored) { }
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
    private AccountResponse getAccountInfo(@NonNull Tmdb tmdb, String session) throws IOException {
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
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException || exception instanceof NetworkOnMainThreadException)
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
    @Nullable
    public MovieResultsPage processAccountMovieInfo(Tmdb tmdb, AccountFetchType fetchType, String session, int accountId, String sortBy, String language) throws IOException {
        GetAccountMovieInfo pass = null;

        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if (fetchType == AccountFetchType.Favored) {
            pass = fetchFavored;
        } else if (fetchType == AccountFetchType.Rated) {
            pass = fetchRated;
        } else {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_PAGE_RELATED, "Invalid fetch type");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        // Check userid / passwd
        if (session == null || StringUtils.isBlank(session)) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_SESSION_RELATED, "You must provide a populated TMDb session");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if (accountId < 1) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_ACCOUNT_RELATED, "Invalid TMDb account");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        MovieResultsPage movieFull = this.obtainAccountMovieInfoPages(pass, tmdb, session, accountId, sortBy, language);

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
    @Nullable
    private MovieResultsPage obtainAccountMovieInfoPages(GetAccountMovieInfo function, @NonNull Tmdb tmdb, String session, int accountId, String sortBy, String language) throws IOException {
        MovieResultsPage results = null;
        boolean initResults = false;

        // TMDb pages start at 1
        for (int pageCount = 1; pageCount <= this.getMaxPageFetch(); pageCount++) {
            MovieResultsPage resultsPage = this.obtainAccountMovieInfoPage(function, tmdb, session, accountId, pageCount, sortBy, language);

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
     * This will try to loop thru up to 3 times
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
    private MovieResultsPage obtainAccountMovieInfoPage(GetAccountMovieInfo function, @NonNull Tmdb tmdb, String session, int accountId, int page, String sortBy, String language) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                MovieResultsPage resultsPage = this.getAccountMovieInfoPage(function, tmdb, session, accountId, page, sortBy, language);

                if (resultsPage != null) {
                    return resultsPage;
                }

                // result not success, retry - it can't hurt
                retry = true;
                retryTime = 2;
            // Note - TmdbNetworkException and any other exception are ignored and will bubble up
            } catch (TmdbException ex) {
                NetworkHelper.ExceptionCheckReturn checkReturn = this.CheckForNetworkRetry(ex);
                if (!checkReturn.retry)
                    throw ex;

                retry = true;
                retryTime = checkReturn.retryTime;
            }

            if (retry) {
                try {
                    Thread.sleep((int) ((retryTime + 0.5) * 1000));
                } catch (InterruptedException ignored) { }
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
    private MovieResultsPage getAccountMovieInfoPage(GetAccountMovieInfo function, @NonNull Tmdb tmdb, String session, int accountId, int page, String sortBy, String language) throws IOException {
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
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException || exception instanceof NetworkOnMainThreadException)
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
    @Nullable
    public AccountState processAccountMovieInfoDetail(Tmdb tmdb, int movieId, String session, String guestSessionId) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if (movieId < 1) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_MOVIE_ID_RELATED, "Invalid TMDb movie id");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if ((session == null || StringUtils.isBlank(session) && (guestSessionId == null || StringUtils.isBlank(guestSessionId)))) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_SESSION_RELATED, "You must provide a populated session or guest session");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        AccountState accountState = this.obtainAccountMovieInfoDetail(tmdb, movieId, session, guestSessionId);

        return accountState;
    }

    /***
     * Get the account movie information
     * This will try to loop thru up to 3 times
     *
     * @param tmdb Tmdb
     * @param movieId Tmdb movie id
     * @param session session Key
     * @param guestSessionId guest session
     * @return AccountState
     * @throws IOException TmdbException
     */
    @Nullable
    private AccountState obtainAccountMovieInfoDetail(@NonNull Tmdb tmdb, int movieId, String session, String guestSessionId) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                AccountState accountState = this.getAccountMovieInfoDetail(tmdb, movieId, session, guestSessionId);

                if (accountState != null) {
                    return accountState;
                }

                // result not success, retry - it can't hurt
                retry = true;
                retryTime = 2;
            // Note - TmdbNetworkException and any other exception are ignored and will bubble up
            } catch (TmdbException ex) {
                NetworkHelper.ExceptionCheckReturn checkReturn = this.CheckForNetworkRetry(ex);
                if (!checkReturn.retry)
                    throw ex;

                retry = true;
                retryTime = checkReturn.retryTime;
            }

            if (retry) {
                try {
                    Thread.sleep((int) ((retryTime + 0.5) * 1000));
                } catch (InterruptedException ignored) { }
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
    private AccountState getAccountMovieInfoDetail(@NonNull Tmdb tmdb, int movieId, String session, String guestSessionId) throws IOException {
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
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException || exception instanceof NetworkOnMainThreadException)
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
    @Nullable
    public Status processAccountFavorite(Tmdb tmdb, String session, int accountId, AccountFavorite favorite) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        // Check userid / passwd
        if (session == null || StringUtils.isBlank(session)) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_SESSION_RELATED, "You must provide a populated TMDb session");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if (accountId < 1) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_ACCOUNT_RELATED, "Invalid TMDb account id");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if (favorite == null) {
            throw new NullPointerException("AccountFavorite is null");
        } else {
            if (favorite.getMediaType() == null || StringUtils.isBlank(favorite.getMediaType())) {
                TmdbException tmdbException = new TmdbException(TMDB_CODE_FAVORITE_RELATED, "Empty favorite media type");
                tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
                throw tmdbException;
            } else if (!(favorite.getMediaType().equals(MediaType.MOVIE.toString()) || favorite.getMediaType().equals(MediaType.TV.toString()))) {
                TmdbException tmdbException = new TmdbException(TMDB_CODE_FAVORITE_RELATED, "Favorite media type must be either " + MediaType.MOVIE.toString() + " or " + MediaType.TV.toString());
                tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
                throw tmdbException;
            }
            if (favorite.getId() == null) {
                TmdbException tmdbException = new TmdbException(TMDB_CODE_FAVORITE_RELATED, "Empty favorite media id");
                tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
                throw tmdbException;
            } else if (favorite.getId() < 1) {
                TmdbException tmdbException = new TmdbException(TMDB_CODE_FAVORITE_RELATED, "Invalid favorite media id");
                tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
                throw tmdbException;
            }
            if (favorite.getFavorite() == null) {
                TmdbException tmdbException = new TmdbException(TMDB_CODE_FAVORITE_RELATED, "Empty favorite setting");
                tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
                throw tmdbException;
            }
        }

        Status status = this.tryAccountFavorite(tmdb, session, accountId, favorite);

        return status;
    }

    /***
     * Try to set the favorite on the user's account
     * This will try to loop thru up to 3 times
     *
     * @param tmdb Tmdb
     * @param session session Key
     * @param accountId User's account #
     * @param favorite AccountFavorite
     * @return Status Set status
     * @throws IOException TmdbException
     */
    @Nullable
    private Status tryAccountFavorite(@NonNull Tmdb tmdb, String session, int accountId, AccountFavorite favorite) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                Status state = this.setAccountFavorite(tmdb, session, accountId, favorite);

                if (state != null) {
                    return state;
                }

                // result not success, retry - it can't hurt
                retry = true;
                retryTime = 2;
            // Note - TmdbNetworkException and any other exception are ignored and will bubble up
            } catch (TmdbException ex) {
                NetworkHelper.ExceptionCheckReturn checkReturn = this.CheckForNetworkRetry(ex);
                if (!checkReturn.retry)
                    throw ex;

                retry = true;
                retryTime = checkReturn.retryTime;
            }

            if (retry) {
                try {
                    Thread.sleep((int) ((retryTime + 0.5) * 1000));
                } catch (InterruptedException ignored) { }
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
    private Status setAccountFavorite(@NonNull Tmdb tmdb, String session, int accountId, AccountFavorite favorite) throws IOException {
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
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException || exception instanceof NetworkOnMainThreadException)
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
    @Nullable
    public Status addMovieRating(Tmdb tmdb, int movieId, String session, String guestSessionId, RatingValue ratingValue) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if (movieId < 1) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_MOVIE_ID_RELATED, "Invalid TMDb movie id");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if ((session == null || StringUtils.isBlank(session) && (guestSessionId == null || StringUtils.isBlank(guestSessionId)))) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_SESSION_RELATED, "You must provide a populated session or guest session");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if (ratingValue == null) {
            throw new NullPointerException("RatingValue is null");
        } else {
            if (ratingValue.getValue() < 0.5 || ratingValue.getValue() > 10) {
                TmdbException tmdbException = new TmdbException(TMDB_CODE_RATING_RELATED, "The rating value is expected to be between 0.5 and 10.0.");
                tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
                throw tmdbException;
            }
        }

        Status status = this.attachMovieRating(tmdb, movieId, session, guestSessionId, ratingValue);

        return status;
    }

    /***
     * Try to set the movie rating on the user's account
     * This will try to loop thru up to 3 times
     *
     * @param tmdb Tmdb
     * @param movieId Tmdb movie id
     * @param session session Key
     * @param guestSessionId guest session
     * @param ratingValue RatingValue
     * @return Status
     * @throws IOException TmdbException
     */
    @Nullable
    private Status attachMovieRating(@NonNull Tmdb tmdb, int movieId, String session, String guestSessionId, RatingValue ratingValue) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                Status state = this.postMovieRating(tmdb, movieId, session, guestSessionId, ratingValue);

                if (state != null) {
                    return state;
                }

                // result not success, retry - it can't hurt
                retry = true;
                retryTime = 2;
            // Note - TmdbNetworkException and any other exception are ignored and will bubble up
            } catch (TmdbException ex) {
                NetworkHelper.ExceptionCheckReturn checkReturn = this.CheckForNetworkRetry(ex);
                if (!checkReturn.retry)
                    throw ex;

                retry = true;
                retryTime = checkReturn.retryTime;
            }

            if (retry) {
                try {
                    Thread.sleep((int) ((retryTime + 0.5) * 1000));
                } catch (InterruptedException ignored) { }
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
    private Status postMovieRating(@NonNull Tmdb tmdb, int movieId, String session, String guestSessionId, RatingValue ratingValue) throws IOException {
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
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException || exception instanceof NetworkOnMainThreadException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }

    /***
     * Remove the movie rating on the user's account
     *
     * @param tmdb Tmdb
     * @param movieId Tmdb movie id
     * @param session session Key
     * @param guestSessionId guest session
     * @return Status
     * @throws IOException TmdbException
     */
    @Nullable
    public Status removeMovieRating(Tmdb tmdb, int movieId, String session, String guestSessionId) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if (movieId < 1) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_MOVIE_ID_RELATED, "Invalid TMDb movie id");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if ((session == null || StringUtils.isBlank(session) && (guestSessionId == null || StringUtils.isBlank(guestSessionId)))) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_SESSION_RELATED, "You must provide a populated session or guest session");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        Status status = this.detachMovieRating(tmdb, movieId, session, guestSessionId);

        return status;
    }

    /***
     * Try to remove the movie rating on the user's account
     * This will try to loop thru up to 3 times
     *
     * @param tmdb Tmdb
     * @param movieId Tmdb movie id
     * @param session session Key
     * @param guestSessionId guest session
     * @return Status
     * @throws IOException TmdbException
     */
    @Nullable
    private Status detachMovieRating(@NonNull Tmdb tmdb, int movieId, String session, String guestSessionId) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                Status state = this.deleteMovieRating(tmdb, movieId, session, guestSessionId);

                if (state != null) {
                    return state;
                }

                // result not success, retry - it can't hurt
                retry = true;
                retryTime = 2;
            // Note - TmdbNetworkException and any other exception are ignored and will bubble up
            } catch (TmdbException ex) {
                NetworkHelper.ExceptionCheckReturn checkReturn = this.CheckForNetworkRetry(ex);
                if (!checkReturn.retry)
                    throw ex;

                retry = true;
                retryTime = checkReturn.retryTime;
            }

            if (retry) {
                try {
                    Thread.sleep((int) ((retryTime + 0.5) * 1000));
                } catch (InterruptedException ignored) { }
            } else {
                break;
            }
        }

        return null;
    }

    /***
     * Remove the movie rating
     *
     * @param tmdb Tmdb
     * @param movieId Tmdb movie id
     * @param session session Key
     * @param guestSessionId guest session
     * @return Status
     * @throws IOException TmdbException
     */
    private Status deleteMovieRating(@NonNull Tmdb tmdb, int movieId, String session, String guestSessionId) throws IOException {
        try {
            Call<Status> call = tmdb.accountService().removeMovieRating(movieId, session, guestSessionId);
            Response<Status> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            this.ProcessError(response);
            // this will never return, but the compiler wants a return
            return null;
        } catch (Exception exception) {
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException || exception instanceof NetworkOnMainThreadException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }

}
