/*
 * Copyright (c) 2019 by Keith Beatty.
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
 */

package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.entities.AccountFavorite;
import com.b12kab.tmdblibrary.entities.AccountResponse;
import com.b12kab.tmdblibrary.entities.AccountState;
import com.b12kab.tmdblibrary.entities.AuthenticateSessionNewResponse;
import com.b12kab.tmdblibrary.entities.CreateNewTokenResponse;
import com.b12kab.tmdblibrary.entities.AuthenticateTokenValidateWithLoginResponse;
import com.b12kab.tmdblibrary.entities.ListResultsPage;
import com.b12kab.tmdblibrary.entities.MovieRatingValue;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.Status;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountService {
    /***
     * This will get the user's information
     * @see <a href="https://developers.themoviedb.org/3/account/get-account-details">Documentation</a>
     *
     * @param sessionId Obtained with authenticateSession
     * @return AccountResponse
     */
    @GET("account")
    Call<AccountResponse> getAccountInfo(@Query("session_id") String sessionId);

    /**
     * Get all of the lists created by an account. Will include private lists if you are the owner.
     * @see <a href="https://developers.themoviedb.org/3/account/get-created-lists">Documentation</a>
     *
     * @param userId <em>Optional.</em> Obtained from /account
     * @param sessionId Obtained with authenticateSession
     * @param page <em>Optional.</em> Minimum 1, maximum 1000.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return ListResultsPage
     */
    @GET("account/{account_id}/lists")
    Call<ListResultsPage> getAccountLists(@Path("account_id") Integer userId,
                                          @Query("session_id") String sessionId,
                                          @Query("page") Integer page,
                                          @Query("language") String language);

    /***
     * This will get a list of all the user's movie ratings
     * @see <a href="https://developers.themoviedb.org/3/account/get-rated-movies">Documentation</a>
     *
     * @param userId <em>Optional.</em> Obtained from /account
     * @param sessionId Obtained with authenticateSession
     * @param page <em>Optional.</em> Minimum 1, maximum 1000.
     * @param sortBy <em>Optional.</em> Use only created_at.desc or created_at.asc
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultPage
     */
    @GET("account/{account_id}/rated/movies")
    Call<MovieResultsPage> getAccountRatedMovie(@Path("account_id") Integer userId,
                                                @Query("session_id") String sessionId,
                                                @Query("page") Integer page,
                                                @Query("sort_by") String sortBy,
                                                @Query("language") String language);

    /***
     * This will get a list of the user's favorite movie's
     * @see <a href="https://developers.themoviedb.org/3/account/get-favorite-movies">Documentation</a>
     *
     * @param userId <em>Optional.</em> Obtained from /account
     * @param sessionId Obtained with authenticateSession
     * @param page <em>Optional.</em> Minimum 1, maximum 1000.
     * @param sortBy <em>Optional.</em> Use only created_at.desc or created_at.asc
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return MovieResultPage
     */
    @GET("account/{account_id}/favorite/movies")
    Call<MovieResultsPage> getFavoritedMovies(@Path("account_id") Integer userId,
                                                 @Query("session_id") String sessionId,
                                                 @Query("page") Integer page,
                                                 @Query("sort_by") String sortBy,
                                                 @Query("language") String language);

    /***
     * This will set the movie / TV favorite
     * @see <a href="https://developers.themoviedb.org/3/account/mark-as-favorite">Documentation</a>
     *
     * @param userId <em>Optional.</em> Obtained from /account
     * @param sessionId Obtained with authenticateSession
     * @param favorite AccountFavorite
     * @return Status
     */
    @POST("account/{account_id}/favorite")
    @Headers("Content-Type: application/json;charset=utf-8")
    Call<Status> setFavorite(@Path("account_id") Integer userId,
                             @Query("session_id") String sessionId,
                             @Body AccountFavorite favorite);

    /***
     * This will get the movie's account state.
     * @see <a href="https://developers.themoviedb.org/3/movies/get-movie-account-states">Documentation</a>
     *
     * @param movieId movie id for account status
     * @param sessionId Obtained with authenticateSession
     * @param guestSessionId <em>Optional.</em>
     * @return AccountState
     */
    @GET("movie/{movie_id}/account_states")
    Call<AccountState> getMovieAccountState(@Path("movie_id") int movieId,
                                            @Query("session_id") String sessionId,
                                            @Query("guest_session_id") String guestSessionId );

    /***
     * Set the user's rating for the movie.
     * @see <a href="https://developers.themoviedb.org/3/movies/rate-movie">Documentation</a>
     *
     * @param movieId movie id for account status
     * @param sessionId Obtained with authenticateSession
     * @param movieRatingValue movie rating value
     * @param guestSessionId <em>Optional.</em>
     * @return Status account status of call
     */
    @POST("movie/{movie_id}/rating")
    @Headers("Content-Type: application/json;charset=utf-8")
    Call<Status> setMovieRating(@Path("movie_id") int movieId,
                                @Query("session_id") String sessionId,
                                @Query("guest_session_id") String guestSessionId,
                                @Body MovieRatingValue movieRatingValue);

    /***
     * Remove the user's rating for the movie.
     * @see <a href="https://developers.themoviedb.org/3/movies/delete-movie-rating">Documentation</a>
     *
     * @param movieId movie id for account status
     * @param sessionId <em>Quasi-Optional.</em> Obtained with authenticateSession
     * @param guestSessionId <em>Optional.</em>
     * @return Status account status of call
     */
    @DELETE("movie/{movie_id}/rating")
    @Headers("Content-Type: application/json;charset=utf-8")
    Call<Status> removeMovieRating(@Path("movie_id") int movieId,
                                   @Query("session_id") String sessionId,
                                   @Query("guest_session_id") String guestSessionId);
}
