package com.b12kab.tmdblibrary.implementation;

import android.util.Log;

import com.b12kab.tmdblibrary.NetworkHelper;
import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.enumerations.MovieFetchType;
import com.b12kab.tmdblibrary.exceptions.TmdbException;
import com.b12kab.tmdblibrary.exceptions.TmdbNetworkException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Response;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_API_ERR_MSG;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_LOWER_PAGE_ERR_MSG;

public class MovieHelper extends NetworkHelper implements IMovieHelper {
    private static final String TAG = MovieHelper.class.getSimpleName();
    //https://stackoverflow.com/questions/2186931/java-pass-method-as-parameter
    @FunctionalInterface
    interface GetInitialMovieType {
        Call<MovieResultsPage> initialMovies(Tmdb tmdb, String language, String region, int page);
    }

    GetInitialMovieType nowPlaying = (t, l, r, p) -> t.moviesService().nowPlaying(p,l,r);
    GetInitialMovieType popular = (t, l, r, p) -> t.moviesService().popular(p,l,r);
    GetInitialMovieType topRated = (t, l, r, p) -> t.moviesService().topRated(p,l,r);
    GetInitialMovieType upcoming = (t, l, r, p) -> t.moviesService().upcoming(p,l,r);

    /***
     * This is a list of error status codes created by TMDb
     *
     * @return List<Integer>
     */
    public List<Integer> getAssocHelperTmdbErrorStatusCodes() {
        return Arrays.asList(
                7,  // invalid API key
                34  // missing resource
        );
    }

    /***
     * This is a list of error status codes created by the helper
     *
     * @return List<Integer>
     */
    public List<Integer> getAssocHelperNonTmdbErrorStatusCodes() {
        return Collections.emptyList();
    }

    /***
     * Get specific TMDb page
     * This will try to loop thru up to 3 times
     *
     * @param function Method to call
     * @param tmdb Tmdb
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param region <em>Optional.</em> ISO 3166-1 code; must be uppercase
     * @param page tmdb page #
     * @return MovieResultsPage
     * @throws Exception Exception
     */
    @Nullable
    private MovieResultsPage obtainMoviePage(GetInitialMovieType function, @NonNull Tmdb tmdb, String language, String region, int page) throws Exception {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                MovieResultsPage movieFull = this.getMoviePage(function, tmdb, language, region, page);

                if (movieFull != null) {
                    return movieFull;
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
                retry = false;
            } else {
                break;
            }
        }

        return null;
    }

    /***
     * Get a specific movie page
     *
     * @param function Method to call
     * @param tmdb Tmdb
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param region <em>Optional.</em> ISO 3166-1 code; must be uppercase
     * @param page tmdb page #
     * @return MovieResultsPage
     * @throws Exception Exception
     */
    private MovieResultsPage getMoviePage(GetInitialMovieType function, @NonNull Tmdb tmdb, String language, String region, int page) throws Exception {
        try {
            Call<MovieResultsPage> call = function.initialMovies(tmdb, language, region, page);
            Response<MovieResultsPage> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            this.ProcessError(response);
            // this will never return, but the compiler wants a return
            return null;
        } catch (Exception exception) {
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException)
                throw exception;

            Log.d( TAG,"getMoviePage - exception: " + exception.getMessage());
            throw this.GetFailure(exception);
        }
    }

    /***
     * Get one or more page or pages after initial fetch is completed
     *
     * @param tmdb Tmdb
     * @param fetchType Movie type to fetch
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param region <em>Optional.</em> ISO 3166-1 code; must be uppercase
     * @param page TMDb movie start page
     * @return List<MovieResultsPage>
     * @throws Exception Exception
     */
    public MovieResultsPage processMoviePage(Tmdb tmdb, MovieFetchType fetchType, String language, String region, int page) throws Exception {
        GetInitialMovieType pass = null;

        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            TmdbException tmdbException = new TmdbException(TMDB_API_ERR_MSG);
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            tmdbException.setErrorKind(TmdbException.RetrofitErrorKind.None);
            throw tmdbException;
        }

        if (page < 1) {
            TmdbException tmdbException = new TmdbException(TMDB_LOWER_PAGE_ERR_MSG);
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            tmdbException.setErrorKind(TmdbException.RetrofitErrorKind.None);
            throw tmdbException;
        }

        pass = this.assignServiceEndpoint(fetchType);

        MovieResultsPage resultsPage = this.obtainMoviePage(pass, tmdb, language, region, page);

        return resultsPage;
    }

    /***
     * Assign the service endpoint to use
     *
     * @param fetchType Movie type to fetch
     * @return GetInitialMovieType
     * @throws TmdbException Exception
     */
    private GetInitialMovieType assignServiceEndpoint(MovieFetchType fetchType) throws TmdbException {

        if (fetchType == MovieFetchType.NowPlaying) {
            return nowPlaying;
        } else if (fetchType == MovieFetchType.Popular) {
            return popular;
        } else if (fetchType == MovieFetchType.TopRated) {
            return topRated;
        } else if (fetchType == MovieFetchType.Upcoming) {
            return upcoming;
        } else {
            TmdbException tmdbException = new TmdbException("Invalid fetch type");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            tmdbException.setErrorKind(TmdbException.RetrofitErrorKind.None);
            throw tmdbException;
        }
    }
}
