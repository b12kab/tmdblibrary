/*
 * Copyright 2014 Chris Banes
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

import com.b12kab.tmdblibrary.entities.Person;
import com.b12kab.tmdblibrary.entities.PersonCredits;
import com.b12kab.tmdblibrary.entities.PersonIds;
import com.b12kab.tmdblibrary.entities.PersonImages;
import com.b12kab.tmdblibrary.entities.PersonResultsPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PeopleService {

    /**
     * Get the general person information for a specific id.
     * @see <a href="https://developers.themoviedb.org/3/people/get-person-details">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @return Person
     */
    @GET("person/{id}")
    Call<Person> summary(
            @Path("id") int tmdbId
    );

    /**
     * Get the movie credits for a specific person id.
     * @see <a href="https://developers.themoviedb.org/3/people/get-person-movie-credits">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return PersonCredits
     */
    @GET("person/{id}/movie_credits")
    Call<PersonCredits> movieCredits(
            @Path("id") int tmdbId,
            @Query("language") String language
    );

    /**
     * Get the TV credits for a specific person id.
     * @see <a href="https://developers.themoviedb.org/3/people/get-person-tv-credits">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return PersonCredits
     */
    @GET("person/{id}/tv_credits")
    Call<PersonCredits> tvCredits(
            @Path("id") int tmdbId,
            @Query("language") String language
    );

    /**
     * Get the movie and TV credits for a specific person id.
     * @see <a href="https://developers.themoviedb.org/3/people/get-person-combined-credits">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return PersonCredits
     */
    @GET("person/{id}/combined_credits")
    Call<PersonCredits> combinedCredits(
            @Path("id") int tmdbId,
            @Query("language") String language
    );

    /**
     * Get the external ids for a specific person id.
     * @see <a href="https://developers.themoviedb.org/3/people/get-person-external-ids">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @return PersonIds
     */
    @GET("person/{id}/external_ids")
    Call<PersonIds> externalIds(
            @Path("id") int tmdbId
    );

    /**
     * Get the images for a specific person id.
     * @see <a href="https://developers.themoviedb.org/3/people/get-person-images">Documentation</a>
     *
     * @param tmdbId TMDb id.
     * @return PersonImages
     */
    @GET("person/{id}/images")
    Call<PersonImages> images(
            @Path("id") int tmdbId
    );

    /**
     * Get the list of popular people on The Movie Database. This list refreshes every day.
     * @see <a href="https://developers.themoviedb.org/3/people/get-popular-people">Documentation</a>
     *
     * @param page page
     * @param language ISO 639-1 value, min length 2 digits
     * @return PersonResultsPage
     */
    @GET("person/popular")
    Call<PersonResultsPage> popular(
            @Query("page") Integer page,
            @Query("language") String language
    );

    /**
     * Get the latest person id.
     * @see <a href="https://developers.themoviedb.org/3/people/get-latest-person">Documentation</a>
     *
     * @return Person
     */
    @GET("person/latest")
    Call<Person> latest();
}
