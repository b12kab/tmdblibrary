/*
 * Copyright 2013 Uwe Trottmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.CreditResults;
import com.b12kab.tmdblibrary.entities.Images;
import com.b12kab.tmdblibrary.entities.ListResultsPage;
import com.b12kab.tmdblibrary.entities.MovieAbbreviated;
import com.b12kab.tmdblibrary.entities.MovieAlternativeTitles;
import com.b12kab.tmdblibrary.entities.MovieExternalIds;
import com.b12kab.tmdblibrary.entities.MovieFull;
import com.b12kab.tmdblibrary.entities.MovieKeywords;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.ReleaseResults;
import com.b12kab.tmdblibrary.entities.ReviewResultsPage;
import com.b12kab.tmdblibrary.entities.Translations;
import com.b12kab.tmdblibrary.entities.VideoResults;
import com.b12kab.tmdblibrary.entities.WatchProviders;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MoviesService {

    /**
     * Get the full movie information for a specific movie id.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-details">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param appendToResponse <em>Optional.</em> extra requests to append to the result.
     * @return MovieFull
     */
    @GET("movie/{id}")
    Call<MovieFull> summary(
            @Path("id") int tmdbId,
            @Query("language") String language,
            @Query("append_to_response") AppendToResponse appendToResponse
    );

    /***
     * Get the full movie information for a specific movie id.
     * ++++++If you are logged in, use this method+++++++++++
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-details">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param appendToResponse <em>Optional.</em> extra requests to append to the result.
     * @param session TMDB session Id
     * @return MovieFull
     */
    @GET("movie/{id}")
    Call<MovieFull> summary(
            @Path("id") int tmdbId,
            @Query("language") String language,
            @Query("append_to_response") AppendToResponse appendToResponse,
            @Query("session_id") String session
    );

    /**
     * Get the alternative titles for a specific movie id.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-alternative-titles">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param country <em>Optional.</em> ISO 3166-1 code.
     * @return MovieAlternativeTitles
     */
    @GET("movie/{id}/alternative_titles")
    Call<MovieAlternativeTitles> alternativeTitles(
            @Path("id") int tmdbId,
            @Query("country") String country
    );

    /**
     * Get the cast and crew information for a specific movie id.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-credits">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param language  <em>Optional.</em> ISO 639-1 code.
     * @return CreditResults
     */
    @GET("movie/{id}/credits")
    Call<CreditResults> credits(
            @Path("id") int tmdbId,
            @Query("language") String language
    );

    /**
     * Get the external ids that we have stored for a movie.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-external-ids">Documentation</a>
     *
     * @param movieId A Movie TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     */
    @GET("movie/{movie_id}/external_ids")
    Call<MovieExternalIds> externalIds(
            @Path("movie_id") int movieId,
            @Query("language") String language
    );

    /**
     * Get the images (posters and backdrops) for a specific movie id.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-images">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return Images
     */
    @GET("movie/{id}/images")
    Call<Images> images(
            @Path("id") int tmdbId,
            @Query("language") String language
    );

    /**
     * Get the plot keywords for a specific movie id.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-keywords">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @return MovieKeywords
     */
    @GET("movie/{id}/keywords")
    Call<MovieKeywords> keywords(
            @Path("id") int tmdbId
    );

    /**
     * Get the lists that the movie belongs to.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-lists">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return ListResultsPage
     */
    @GET("movie/{id}/lists")
    Call<ListResultsPage> lists(
            @Path("id") int tmdbId,
            @Query("page") Integer page,
            @Query("language") String language
    );

    /**
     * Get the latest movie id.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-latest-movie">Documentation</a>
     *
     * @return MovieAbbreviated
     */
    @GET("movie/latest")
    Call<MovieAbbreviated> latest();

    /**
     * Get the release date and certification information by country for a specific movie id.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-release-dates">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @return ReleaseDateResults
     */
    @GET("movie/{id}/release_dates")
    Call<ReleaseResults> releases(
            @Path("id") int tmdbId
    );

    /**
     * Get the reviews for a particular movie id.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-reviews">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return ReviewResultsPage
     */
    @GET("movie/{id}/reviews")
    Call<ReviewResultsPage> reviews(
            @Path("id") int tmdbId,
            @Query("page") Integer page,
            @Query("language") String language
    );

    /**
     * Get the similar movies for a specific movie id.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-similar-movies">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultsPage
     */
    @GET("movie/{id}/similar")
    Call<MovieResultsPage> similar(
            @Path("id") int tmdbId,
            @Query("page") Integer page,
            @Query("language") String language
    );

    /**
     * Get the videos (trailers, teasers, clips, etc...) for a specific movie id.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-videos">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return VideoResults
     */
    @GET("movie/{id}/videos")
    Call<VideoResults> videos(
            @Path("id") int tmdbId,
            @Query("language") String language
    );

    /**
     * Get the translations for a specific movie id.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-translations">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param appendToResponse <em>Optional.</em> extra requests to append to the result.
     * @return Translations
     */
    @GET("movie/{id}/translations")
    Call<Translations> translations(
            @Path("id") int tmdbId,
            @Query("append_to_response") AppendToResponse appendToResponse
    );

    /**
     * Get the list of upcoming movies. This list refreshes every day.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-upcoming">Documentation</a>
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param region <em>Optional.</em> ISO 3166-1 code. Must be upper case!
     * @return MovieResultsPage
     */
    @GET("movie/upcoming")
    Call<MovieResultsPage> upcoming(
            @Query("page") Integer page,
            @Query("language") String language,
            @Query("region") String region
    );

    /**
     * Get the list of movies playing in theaters. This list refreshes every day.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-now-playing">Documentation</a>
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param region <em>Optional.</em> ISO 3166-1 code. Must be upper case!
     * @return MovieResultsPage
     */
    @GET("movie/now_playing")
    Call<MovieResultsPage> nowPlaying(
            @Query("page") Integer page,
            @Query("language") String language,
            @Query("region") String region
    );

    /**
     * Get the list of popular movies on The Movie Database. This list refreshes every day.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-popular-movies">Documentation</a>
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param region <em>Optional.</em> ISO 3166-1 code. Must be upper case!
     * @return MovieResultsPage
     */
    @GET("movie/popular")
    Call<MovieResultsPage> popular(
            @Query("page") Integer page,
            @Query("language") String language,
            @Query("region") String region
    );

    /**
     * Get the list of top rated movies. By default, this list will only include movies that have 10 or more votes.
     * This list refreshes every day.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-top-rated-movies">Documentation</a>
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param region <em>Optional.</em> ISO 3166-1 code. Must be upper case!
     * @return MovieResultsPage
     */
    @GET("movie/top_rated")
    Call<MovieResultsPage> topRated(
            @Query("page") Integer page,
            @Query("language") String language,
            @Query("region") String region
    );

    /**
     * Get a list of the availabilities per country by provider.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-watch-providers">Documentation</a>
     *
     * Please note: In order to use this data you must attribute the source of the data as JustWatch.
     * @param tmdbId TMDb id.
     * @return WatchProviders
     */
    @GET("movie/{id}/watch/providers")
    Call<WatchProviders> watchProviders(
            @Path("id") int tmdbId
    );
}
