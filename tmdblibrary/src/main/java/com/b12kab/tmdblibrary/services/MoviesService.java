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
import com.b12kab.tmdblibrary.entities.MovieFull;
import com.b12kab.tmdblibrary.entities.MovieKeywords;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.ReleaseResults;
import com.b12kab.tmdblibrary.entities.ReviewResultsPage;
import com.b12kab.tmdblibrary.entities.Translations;
import com.b12kab.tmdblibrary.entities.VideoResults;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MoviesService {

    /**
     * Get the full movie information for a specific movie id.
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param appendToResponse <em>Optional.</em> extra requests to append to the result.
     * @return MovieFull
     */
    @GET("/movie/{id}")
    MovieFull summary(
            @Path("id") int tmdbId,
            @Query("language") String language,
            @Query("append_to_response") AppendToResponse appendToResponse
    );

    /***
     * If you are logged in, use this one.
     * Get the full movie information for a specific movie id.
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param session TMDB session Id
     * @param appendToResponse <em>Optional.</em> extra requests to append to the result.
     * @return MovieFull
     */
    @GET("/movie/{id}")
    MovieFull summary(
            @Path("id") int tmdbId,
            @Query("language") String language,
            @Query("session_id") String session,
            @Query("append_to_response") AppendToResponse appendToResponse
    );

    /**
     * Get the alternative titles for a specific movie id.
     *
     * @param tmdbId TMDb id.
     * @param country <em>Optional.</em> ISO 3166-1 code.
     * @return MovieAlternativeTitles
     */
    @GET("/movie/{id}/alternative_titles")
    MovieAlternativeTitles alternativeTitles(
            @Path("id") int tmdbId,
            @Query("country") String country
    );

    /**
     * Get the cast and crew information for a specific movie id.
     *
     * @param tmdbId TMDb id.
     * @return CreditResults
     */
    @GET("/movie/{id}/credits")
    CreditResults credits(
            @Path("id") int tmdbId
    );

    /**
     * Get the images (posters and backdrops) for a specific movie id.
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return Images
     */
    @GET("/movie/{id}/images")
    Images images(
            @Path("id") int tmdbId,
            @Query("language") String language
    );

    /**
     * Get the plot keywords for a specific movie id.
     *
     * @param tmdbId TMDb id.
     * @return MovieKeywords
     */
    @GET("/movie/{id}/keywords")
    MovieKeywords keywords(
            @Path("id") int tmdbId
    );

    /**
     * Get the release date and certification information by country for a specific movie id.
     *
     * @param tmdbId TMDb id.
     * @return ReleaseDateResults
     */
    @GET("/movie/{id}/release_dates")
    ReleaseResults releases(
            @Path("id") int tmdbId
    );

    /**
     * Get the videos (trailers, teasers, clips, etc...) for a specific movie id.
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return VideoResults
     */
    @GET("/movie/{id}/videos")
    VideoResults videos(
            @Path("id") int tmdbId,
            @Query("language") String language
    );

    /**
     * Get the translations for a specific movie id.
     *
     * @param tmdbId TMDb id.
     * @param appendToResponse <em>Optional.</em> extra requests to append to the result.
     * @return Translations
     */
    @GET("/movie/{id}/translations")
    Translations translations(
            @Path("id") int tmdbId,
            @Query("append_to_response") AppendToResponse appendToResponse
    );

    /**
     * Get the similar movies for a specific movie id.
     *
     * @param tmdbId TMDb id.
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultsPage
     */
    @GET("/movie/{id}/similar")
    MovieResultsPage similar(
            @Path("id") int tmdbId,
            @Query("page") Integer page,
            @Query("language") String language
    );

    /**
     * Get the reviews for a particular movie id.
     *
     * @param tmdbId TMDb id.
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return ReviewResultsPage
     */
    @GET("/movie/{id}/reviews")
    ReviewResultsPage reviews(
            @Path("id") int tmdbId,
            @Query("page") Integer page,
            @Query("language") String language
    );

    /**
     * Get the lists that the movie belongs to.
     *
     * @param tmdbId TMDb id.
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return ListResultsPage
     */
    @GET("/movie/{id}/lists")
    ListResultsPage lists(
            @Path("id") int tmdbId,
            @Query("page") Integer page,
            @Query("language") String language
    );

    /**
     * Get the latest movie id.
     *
     * @return MovieAbbreviated
     */
    @GET("/movie/latest")
    MovieAbbreviated latest();

    /**
     * Get the list of upcoming movies. This list refreshes every day. The maximum number of items this list will
     * include is 100.
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultsPage
     */
    @GET("/movie/upcoming")
    MovieResultsPage upcoming(
            @Query("page") Integer page,
            @Query("language") String language
    );

    /**
     * Get the list of movies playing in theaters. This list refreshes every day. The maximum number of items this list
     * will include is 100.
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultsPage
     */
    @GET("/movie/now_playing")
    MovieResultsPage nowPlaying(
            @Query("page") Integer page,
            @Query("language") String language
    );

    /**
     * Get the list of popular movies on The Movie Database. This list refreshes every day.
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultsPage
     */
    @GET("/movie/popular")
    MovieResultsPage popular(
            @Query("page") Integer page,
            @Query("language") String language
    );

    /**
     * Get the list of top rated movies. By default, this list will only include movies that have 10 or more votes. This
     * list refreshes every day.
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultsPage
     */
    @GET("/movie/top_rated")
    MovieResultsPage topRated(
            @Query("page") Integer page,
            @Query("language") String language
    );

}
