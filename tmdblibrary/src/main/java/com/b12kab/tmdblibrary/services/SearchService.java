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

import com.b12kab.tmdblibrary.entities.CollectionResultsPage;
import com.b12kab.tmdblibrary.entities.CompanyResultsPage;
import com.b12kab.tmdblibrary.entities.KeywordResultsPage;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.PersonResultsPage;
import com.b12kab.tmdblibrary.entities.TvResultsPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchService {

    /**
     * Search for companies by name.
     * @see <a href="https://developers.themoviedb.org/3/search/search-companies">Documentation</a>
     *
     * @param query CGI escaped string
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @return CompanyResultsPage
     */
    @GET("search/company")
    Call<CompanyResultsPage> company(
            @Query("query") String query,
            @Query("page") Integer page
    );
    
    /**
     * Search for collections by name.
     * @see <a href="https://developers.themoviedb.org/3/search/search-collections">Documentation</a>
     *
     * @param query CGI escaped string
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return CollectionResultsPage
     */
    @GET("search/collection")
    Call<CollectionResultsPage> collection(
            @Query("query") String query,
            @Query("page") Integer page,
            @Query("language") String language
    );
    
    /**
     * Search for keywords by name.
     * @see <a href="https://developers.themoviedb.org/3/search/search-keywords">Documentation</a>
     *
     * @param query CGI escaped string
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @return KeywordResultsPage
     */
    @GET("search/keyword")
    Call<KeywordResultsPage> keyword(
            @Query("query") String query,
            @Query("page") Integer page
    );
    
    /**
     * Search for movies by title.
     * @see <a href="https://developers.themoviedb.org/3/search/search-movies">Documentation</a>
     *
     * @param query CGI escaped string
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param includeAdult <em>Optional.</em> Toggle the inclusion of adult titles. Expected value is: true or false
     * @param year <em>Optional.</em> Filter the results release dates to matches that include this value.
     * @param primaryReleaseYear <em>Optional.</em> Filter the results so that only the primary release dates have this
     * value.
     * @return MovieResultsPage
     */
    @GET("search/movie")
    Call<MovieResultsPage> movie(
            @Query("query") String query,
            @Query("page") Integer page,
            @Query("language") String language,
            @Query("include_adult") Boolean includeAdult,
            @Query("year") Integer year,
            @Query("primary_release_year") Integer primaryReleaseYear
    );
    
    /**
     * Search for people by name.
     * @see <a href="https://developers.themoviedb.org/3/search/search-people">Documentation</a>
     *
     * @param query CGI escaped string
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param includeAdult <em>Optional.</em> Toggle the inclusion of adult titles. Expected value is: true or false
     * @param region <em>Optional.</em> ISO 3166-1 code. Must be uppercase
     * @return PersonResultsPage
     */
    @GET("search/person")
    Call<PersonResultsPage> person(
            @Query("query") String query,
            @Query("page") Integer page,
            @Query("language") String language,
            @Query("include_adult") Boolean includeAdult,
            @Query("region") String region
    );

    /**
     * Search for TV shows by title.
     * @see <a href="https://developers.themoviedb.org/3/search/search-tv-shows">Documentation</a>
     *
     * @param query CGI escaped string
     * @param page Minimum 1, maximum 1000.
     * @param language ISO 639-1 code.
     * @param firstAirDateYear Filter the results to only match shows that have an air date with this value.
     * @param includeAdult <em>Optional.</em> Toggle the inclusion of adult titles. Expected value is: true or false
     * @return TvResultsPage
     */
    @GET("search/tv")
    Call<TvResultsPage> tv(
            @Query("query") String query,
            @Query("page") Integer page,
            @Query("language") String language,
            @Query("first_air_date_year") Integer firstAirDateYear,
            @Query("include_adult") Boolean includeAdult
    );
}
