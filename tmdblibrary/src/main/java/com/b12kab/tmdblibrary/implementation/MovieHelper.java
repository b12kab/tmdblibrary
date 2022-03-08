package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.NetworkHelper;
import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.enumerations.MovieFetchType;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import java.io.IOException;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Response;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_API_ERR_MSG;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_API_KEY_INVALID;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_MOVIE_TYPE_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_PAGE_RELATED;

public class MovieHelper extends NetworkHelper {
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
     * Perform the initial fetch movies pages
     *
     * @param tmdb Tmdb
     * @param fetchType Movie type to fetch
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param region <em>Optional.</em> ISO 3166-1 code; must be uppercase
     * @param initialFetchPages number of pages to fetch
     * @return MovieResultsPage
     * @throws IOException TmdbException
     */
    public MovieResultsPage ProcessInitialMovies(Tmdb tmdb, MovieFetchType fetchType, String language, String region, int initialFetchPages) throws IOException {
        GetInitialMovieType pass = null;

        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            throw new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
        }

        if (initialFetchPages < 1) {
            throw new TmdbException(TMDB_CODE_PAGE_RELATED, "You must have at least 1 initial page");
        }

        // all pages max out at 1000
        if (initialFetchPages > 1000)
            initialFetchPages = 1000;

        if (fetchType == MovieFetchType.NowPlaying) {
            pass = nowPlaying;
        } else if (fetchType == MovieFetchType.Popular) {
            pass = popular;
        } else if (fetchType == MovieFetchType.TopRated) {
            pass = topRated;
        } else if (fetchType == MovieFetchType.Upcoming) {
            pass = upcoming;
        } else {
            throw new TmdbException(TMDB_CODE_PAGE_RELATED, "Invalid fetch type");
        }

        MovieResultsPage movieFull = this.ObtainInitialMoviePages(pass, tmdb, language, region, initialFetchPages);

        return movieFull;
    }

    /***
     * Loop thru pages to fetch
     *
     * @param function Method to call
     * @param tmdb Tmdb
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param region <em>Optional.</em> ISO 3166-1 code; must be uppercase
     * @param initialFetchPages number of pages to fetch
     * @return MovieResultsPage
     * @throws IOException TmdbException
     */
    private MovieResultsPage ObtainInitialMoviePages(GetInitialMovieType function, @NonNull Tmdb tmdb, String language, String region, int initialFetchPages) throws IOException {
        MovieResultsPage results = null;
        boolean initResults = false;

        // TMDb pages start at 1
        for (int pageCount = 1; pageCount <= initialFetchPages; pageCount++) {
            MovieResultsPage resultsPage = this.ObtainMoviePage(function, tmdb, language, region, pageCount);

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
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param region <em>Optional.</em> ISO 3166-1 code; must be uppercase
     * @param page tmdb page #
     * @return MovieResultsPage
     * @throws IOException TmdbException
     */
    private MovieResultsPage ObtainMoviePage(GetInitialMovieType function, @NonNull Tmdb tmdb, String language, String region, int page) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                MovieResultsPage movieFull = this.GetMoviePage(function, tmdb, language, region, page);

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
     * @throws IOException TmdbException
     */
    private MovieResultsPage GetMoviePage(GetInitialMovieType function, @NonNull Tmdb tmdb, String language, String region, int page) throws IOException {
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
            if (exception instanceof TmdbException)
                throw exception;

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
     * @param startPage TMDb movie start page
     * @param endPage TMDb movie end page
     * @return MovieResultsPage
     * @throws IOException TmdbException
     */
    public MovieResultsPage ProcessAdditionalMovies(Tmdb tmdb, MovieFetchType fetchType, String language, String region, int startPage, int endPage) throws IOException {
        GetInitialMovieType pass = null;

        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            throw new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
        }

        if (endPage < startPage) {
            throw new TmdbException(TMDB_CODE_PAGE_RELATED, "End page (" + endPage + ") must equal or be greater than the start page (" + startPage + ")");
        }

        if (endPage < 1) {
            throw new TmdbException(TMDB_CODE_PAGE_RELATED, "End page (" + endPage + ") must equal or be greater than 1");
        }

        if (startPage < 1) {
            throw new TmdbException(TMDB_CODE_PAGE_RELATED, "Start page (" + startPage + ") must equal or be greater than 1");
        }

        if (fetchType == MovieFetchType.NowPlaying) {
            pass = nowPlaying;
        } else if (fetchType == MovieFetchType.Popular) {
            pass = popular;
        } else if (fetchType == MovieFetchType.TopRated) {
            pass = topRated;
        } else if (fetchType == MovieFetchType.Upcoming) {
            pass = upcoming;
        } else {
            throw new TmdbException(TMDB_CODE_MOVIE_TYPE_RELATED, "Invalid type");
        }

        MovieResultsPage movieFull = this.ObtainAdditionalMovies(pass, tmdb, language, region, startPage, endPage);

        return movieFull;
    }

    /***
     * Loop thru start and end pages to fetch
     *
     * @param function Method to call
     * @param tmdb Tmdb
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param region <em>Optional.</em> ISO 3166-1 code; must be uppercase
     * @param startPage TMDb movie start page
     * @param endPage TMDb movie end page
     * @return MovieResultsPage
     * @throws IOException TmdbException
     */
    private MovieResultsPage ObtainAdditionalMovies(GetInitialMovieType function, @NonNull Tmdb tmdb, String language, String region, int startPage, int endPage) throws IOException {
        MovieResultsPage results = null;
        boolean initResults = false;

        for (int pageCount = startPage; pageCount <= endPage; pageCount++) {
            MovieResultsPage resultsPage = this.ObtainMoviePage(function, tmdb, language, region, pageCount);

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


}
