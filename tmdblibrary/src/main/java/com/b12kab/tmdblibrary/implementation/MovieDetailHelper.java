package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.NetworkHelper;
import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.MovieFull;
import com.b12kab.tmdblibrary.enumerations.AppendToResponseItem;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Response;

import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.CREDITS;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.IMAGES;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.RELEASE_DATES;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.REVIEWS;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.ACCT_STATES;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.VIDEOS;

public class MovieDetailHelper extends NetworkHelper {

    /***
     * Get movie detail
     *
     * @param tmdb Tmdb
     * @param movieId TMDb movie id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param session <em>Optional.</em> TMDb session Id
     * @param additionalAppends <em>Optional.</em> AppendToResponse - already appends ACCT_STATES, CREDITS, IMAGES, REVIEWS, VIDEOS, RELEASE_DATES
     * @return MovieFull
     * @throws IOException
     */
    public MovieFull ProcessMovieDetail(Tmdb tmdb, int movieId, String language, String session, AppendToResponse additionalAppends) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        MovieFull movieFull = this.ObtainMovieDetail(tmdb, movieId, language, session, additionalAppends);

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
     * @throws IOException
     */
    @Nullable
    private MovieFull ObtainMovieDetail(@NonNull Tmdb tmdb, int movieId, String language, String session, AppendToResponse additionalAppends) throws IOException {
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
                MovieFull movieFull = this.GetMovieDetail(tmdb, movieId, language, session, mergedResponse);

                if (movieFull != null) {
                    return movieFull;
                }

                // result not success, retry - it can't hurt
                retry = true;
                retryTime = 2;
            } catch (Exception ex) {
                if (ex instanceof TmdbException)
                {
                    TmdbException tmdbException = (TmdbException) ex;
                    NetworkHelper.ExceptionCheckReturn checkReturn = CheckForNetworkRetry(tmdbException);
                    if (!checkReturn.retry)
                        throw ex;

                    retry = true;
                    retryTime = checkReturn.retryTime;
                }
            }

            if (retry) {
                try {
                    Thread.sleep((int) ((retryTime + 0.5) * 1000));
                } catch (InterruptedException e) { }
                retry = false;
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
     * @throws IOException
     */
    private MovieFull GetMovieDetail(@NonNull Tmdb tmdb, int movieId, String language, String session, AppendToResponse appendToResponse) throws IOException {
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
        } catch (Exception exception) {
            if (exception instanceof TmdbException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }
}
