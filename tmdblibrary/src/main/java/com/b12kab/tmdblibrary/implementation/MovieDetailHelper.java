package com.b12kab.tmdblibrary.implementation;

import android.os.NetworkOnMainThreadException;

import com.b12kab.tmdblibrary.NetworkHelper;
import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.MovieFull;
import com.b12kab.tmdblibrary.exceptions.TmdbException;
import com.b12kab.tmdblibrary.exceptions.TmdbNetworkException;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Response;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_API_ERR_MSG;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_API_KEY_INVALID;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.CREDITS;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.IMAGES;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.RELEASE_DATES;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.REVIEWS;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.ACCT_STATES;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.VIDEOS;

public class MovieDetailHelper extends NetworkHelper implements IMovieDetailHelper {

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
        return Collections.singletonList(
                TMDB_CODE_API_KEY_INVALID
        );
    }

    /***
     * Get movie detail
     *
     * @param tmdb Tmdb
     * @param movieId TMDb movie id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param session <em>Optional.</em> TMDb session Id
     * @param additionalAppends <em>Optional.</em> AppendToResponse - already appends ACCT_STATES, CREDITS, IMAGES, REVIEWS, VIDEOS, RELEASE_DATES
     * @return MovieFull MovieFull
     * @throws IOException TmdbException
     */
    public MovieFull processMovieDetail(Tmdb tmdb, int movieId, String language, String session, AppendToResponse additionalAppends) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        MovieFull movieFull = this.obtainMovieDetail(tmdb, movieId, language, session, additionalAppends);

        return movieFull;
    }

    /***
     * Get movie detail
     * This will try to loop thru up to 3 times
     *
     * @param tmdb Tmdb
     * @param movieId TMDb movie id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param session <em>Optional.</em> TMDb session Id
     * @param additionalAppends AppendToResponse - additional AppendToResponseItems to be merged
     * @return MovieFull
     * @throws IOException TmdbException
     */
    @Nullable
    private MovieFull obtainMovieDetail(@NonNull Tmdb tmdb, int movieId, String language, String session, AppendToResponse additionalAppends) throws IOException {
        boolean retry;
        int retryTime = 0;

        AppendToResponse appendToResponse = new AppendToResponse(
                ACCT_STATES, CREDITS, IMAGES, REVIEWS, VIDEOS, RELEASE_DATES);

        AppendToResponse mergedResponse = null;

        if (additionalAppends != null && additionalAppends.size() > 0) {
            mergedResponse = appendToResponse.merge(additionalAppends);
        } else {
            mergedResponse = appendToResponse;
        }

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                MovieFull movieFull = this.getMovieDetail(tmdb, movieId, language, session, mergedResponse);

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
            } else {
                break;
            }
        }

        return null;
    }

    /***
     * Try to get the full movie detail
     *
     * @param tmdb Tmdb
     * @param movieId TMDb movie id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param session TMDb session Id
     * @param appendToResponse AppendToResponse - merged appendResponse
     * @return MovieFull
     * @throws IOException TmdbException
     */
    private MovieFull getMovieDetail(@NonNull Tmdb tmdb, int movieId, String language, String session, AppendToResponse appendToResponse) throws IOException {
        try {
            Call<MovieFull> call;
            if (session != null && !StringUtils.isBlank(session)) {
                call = tmdb.moviesService().summary(movieId, language, appendToResponse, session);
            } else {
                call = tmdb.moviesService().summary(movieId, language, appendToResponse);
            }
            Response<MovieFull> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            this.ProcessError(response);
            // this will never return, but the compiler wants a return
            return null;
        // Note - TmdbNetworkException and any other exception are ignored and will bubble up
        } catch (Exception exception) {
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException || exception instanceof NetworkOnMainThreadException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }
}
