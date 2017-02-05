/*
 * Copyright (c) 2016 by Keith Beatty.
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

import android.support.annotation.Nullable;

import com.b12kab.tmdblibrary.entities.AccountResponse;
import com.b12kab.tmdblibrary.entities.AccountState;
import com.b12kab.tmdblibrary.entities.AccountStatusCodeReturn;
import com.b12kab.tmdblibrary.entities.AuthenticateSessionNewResponse;
import com.b12kab.tmdblibrary.entities.AuthenticateTokenNewResponse;
import com.b12kab.tmdblibrary.entities.AuthenticateTokenValidateWithLoginResponse;
import com.b12kab.tmdblibrary.entities.MovieRatingValue;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.TypedJsonString;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface AccountService {
    // https://www.themoviedb.org/documentation/api/sessions
    // Note: Async usage

    /***
     * This is part 1 to sign in
     *
     * @return AuthenticateTokenNewResponse
     */
    @GET("/authentication/token/new")
    AuthenticateTokenNewResponse authenticateTokenNewSync();

    /***
     * This is part 2 to sign in
     *
     * @param requestToken obtained in part 1
     * @param username TMDB username
     * @param password TMDB password
     * @return AuthenticateTokenValidateWithLoginResponse
     */
    @GET("/authentication/token/validate_with_login")
    AuthenticateTokenValidateWithLoginResponse authenticateTokenValidateWithLoginSync(
            @Query("request_token") String requestToken,
            @Query("username") String username,
            @Query("password") String password);

    /***
     * This is part 3 (final) to sign in
     *
     * @param requestToken obtained in part 1
     * @return AuthenticateSessionNewResponse
     */
    @GET("/authentication/session/new")
    AuthenticateSessionNewResponse authenticateSessionNewSync(@Query("request_token") String requestToken);

    /***
     * This will get the user's information Asynchronously
     *
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @param callback AccountResponse
     */
    @GET("/account")
    void getAccountInfoAsync(@Query("session_id") String sessionId,
                             Callback<AccountResponse> callback);

    /***
     * This will get the user's information Synchronously
     *
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @return AccountResponse
     */
    @GET("/account")
    AccountResponse getAccountInfoSync(@Query("session_id") String sessionId);

    /***
     * This will get a list of all the user's movie rating Synchronously
     * https://developers.themoviedb.org/3/account/get-rated-movies
     *
     * @param userId Obtained from /account
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @param page <Optional>Minimum 1, maximum 1000.</Optional>
     * @param sortBy <Optional>Use only created_at.desc or created_at.asc</Optional>
     * @param language <Optional>ISO 639-1 code.</Optional>
     * @return MovieResultPage
     */
    @GET("/account/{account_id}/rated/movies")
    MovieResultsPage getAccountRatingMovieSync(@Path("account_id") int userId,
                                              @Query("session_id") String sessionId,
                                              @Query("page") Integer page,
                                              @Query("sort_by") String sortBy,
                                              @Query("language") String language);


    /***
     * This will get a list of all the user's movie rating Asynchronously
     * https://developers.themoviedb.org/3/account/get-rated-movies
     *
     * @param userId Obtained from /account
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @param page <Optional>Minimum 1, maximum 1000.</Optional>
     * @param sortBy <Optional>Use only created_at.desc or created_at.asc</Optional>
     * @param language <Optional>ISO 639-1 code.</Optional>
     * @param callback MovieResultPage
     */
    @GET("/account/{account_id}/rated/movies")
    void getAccountRatingMovieAsync(@Path("account_id") int userId,
                                    @Query("session_id") String sessionId,
                                    @Query("page") Integer page,
                                    @Query("sort_by") String sortBy,
                                    @Query("language") String language,
                                    Callback<MovieResultsPage > callback);


    /***
     * This will get a list of the user's favorite movie's Asynchronously
     * https://developers.themoviedb.org/3/account/get-favorite-movies
     *
     * @param userId Obtained from /account
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @param page <Optional>Minimum 1, maximum 1000.</Optional>
     * @param sortBy <Optional>Use only created_at.desc or created_at.asc</Optional>
     * @param language <Optional>ISO 639-1 code.</Optional>
     * @param callback MovieResultPage
     */
    @GET("/account/{account_id}/favorite/movies")
    void getFavoriteMoviesAsync(@Path("account_id") int userId,
                                @Query("session_id") String sessionId,
                                @Nullable @Query("page") Integer page,
                                @Nullable @Query("sort_by") String sortBy,
                                @Nullable @Query("language") String language,
                                Callback<MovieResultsPage> callback);

    /***
     * This will get a list of the user's favorite movie's Synchronously
     * https://developers.themoviedb.org/3/account/get-favorite-movies
     *
     * @param userId Obtained from /account
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @param page <Optional>Minimum 1, maximum 1000.</Optional>
     * @param sortBy <Optional>Use only created_at.desc or created_at.asc</Optional>
     * @param language <Optional>ISO 639-1 code.</Optional>
     * @return MovieResultPage
     */
    @GET("/account/{account_id}/favorite/movies")
    MovieResultsPage getFavoriteMoviesSync(@Path("account_id") int userId,
                                           @Query("session_id") String sessionId,
                                           @Query("page") Integer page,
                                           @Query("sort_by") String sortBy,
                                           @Query("language") String language);

    /***
     * This will set the movie / TV favorite synchronously
     * https://developers.themoviedb.org/3/account/mark-as-favorite
     *
     * @param userId Obtained from /account
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @param favoriteSet translated JSON body
     *                    Note: I switched this from AccountFavoriteSet to TypedJsonString
     *                          as this has either worked or not worked, depending on
     *                          the whim of TMDb. Play it smart, so I moved it over to
     *                          TypedJsonString. See below URL.
     * @return AccountStatusCodeReturn
     */
//    https://stackoverflow.com/questions/21398598/how-to-post-raw-whole-json-in-the-body-of-a-retrofit-request#
//    @Headers({ "Content-type: application/json" })
    @POST("/account/{account_id}/favorite")
    AccountStatusCodeReturn setFavoriteSync(@Path("account_id") int userId,
                                            @Query("session_id") String sessionId,
                                            @Body TypedJsonString favoriteSet);
//                                            @Body AccountFavoriteSet favoriteSet);

    /***
     * This will get the movie's account state. Synchronous call
     * https://developers.themoviedb.org/3/movies/get-movie-account-states
     *
     * @param movieId movie id for account status
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @return AccountStates
     */
    @GET("/movie/{movie_id}/account_states")
    AccountState getAccountStateMovieSync(@Path("movie_id") int movieId,
                                          @Query("session_id") String sessionId);

    /***
     * Set the user's rating for the movie. Synchronous call
     * https://developers.themoviedb.org/3/movies/rate-movie
     *
     * @param movieId movie id for account status
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @param movieRatingValue movie rating value
     * @return AccountStatusCodeReturn account status of call
     */
    @POST("/movie/{movie_id}/rating")
    AccountStatusCodeReturn setMovieRatingSync(@Path("movie_id") int movieId,
                                               @Query("session_id") String sessionId,
                                               @Body MovieRatingValue movieRatingValue);

    /***
     * Set the user's rating for the movie. Asynchronous call
     * https://developers.themoviedb.org/3/movies/rate-movie
     *
     * @param movieId movie id for account status
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @param movieRatingValue movie rating value
     * @param callback AccountStatusCodeReturn
     */
    @POST("/movie/{movie_id}/rating")
    void setMovieRatingAsync(@Path("movie_id") int movieId,
                             @Query("session_id") String sessionId,
                             @Body MovieRatingValue movieRatingValue,
                             Callback<AccountStatusCodeReturn> callback);

    /***
     * Remove the user's rating for the movie. Synchronous call
     *
     * @param movieId movie id for account status
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @return AccountStatusCodeReturn account status of call
     */
    @DELETE("/movie/{movie_id}/rating")
    AccountStatusCodeReturn removeMovieRatingSync(@Path("movie_id") int movieId,
                                                  @Query("session_id") String sessionId);

    /***
     * Remove the user's rating for the movie. Asynchronous call
     *
     * @param movieId movie id for account status
     * @param sessionId Obtained with authenticateTokenValidateWithLoginSync
     * @param callback AccountStatusCodeReturn
     */
    @DELETE("/movie/{movie_id}/rating")
    void removeMovieRatingAsync(@Path("movie_id") int movieId,
                                @Query("session_id") String sessionId,
                                Callback<AccountStatusCodeReturn> callback);

}
