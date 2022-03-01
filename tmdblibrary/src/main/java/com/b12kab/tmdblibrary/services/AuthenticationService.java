package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.entities.AuthenticateSessionNewResponse;
import com.b12kab.tmdblibrary.entities.AuthenticateTokenValidateWithLoginResponse;
import com.b12kab.tmdblibrary.entities.CreateNewTokenResponse;
import com.b12kab.tmdblibrary.entities.Status;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthenticationService {
    /***
     * This is part 1 to sign in - create a new token
     * @see <a href="https://developers.themoviedb.org/3/authentication/create-session">Documentation</a>
     *
     * @return AuthenticateTokenNewResponse
     */
    @GET("authentication/token/new")
    Call<CreateNewTokenResponse> createToken();

    /***
     * This is part 2 to authorize the token with the sign in credentials
     * @see <a href="https://developers.themoviedb.org/3/authentication/validate-request-token">Documentation</a>
     *
     * @param requestToken obtained in part 1
     * @param username TMDB username
     * @param password TMDB password
     * @return AuthenticateTokenValidateWithLoginResponse
     */
    @GET("authentication/token/validate_with_login")
    Call<AuthenticateTokenValidateWithLoginResponse> authorizeTokenWithLogin(
            @Query("request_token") String requestToken,
            @Query("username") String username,
            @Query("password") String password);

    /***
     * This is part 3 (final) to create the session with the associated logged token
     * @see <a href="https://developers.themoviedb.org/3/authentication/create-session">Documentation</a>
     *
     * @param requestToken obtained in part 1
     * @return AuthenticateSessionNewResponse
     */
    @GET("authentication/session/new")
    Call<AuthenticateSessionNewResponse> createSession(@Query("request_token") String requestToken);

    /***
     * Logout from a valid session
     * @see <a href="https://developers.themoviedb.org/3/authentication/delete-session">Documentation</a>
     *
     * @param sessionId Obtained with authenticateSession
     * @return Status
     */
    @DELETE("authentication/session")
    Call<Status> logoutSession(@Query("session_id") String sessionId);

}
