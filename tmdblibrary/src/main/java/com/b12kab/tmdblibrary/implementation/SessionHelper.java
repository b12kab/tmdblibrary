package com.b12kab.tmdblibrary.implementation;

import android.os.NetworkOnMainThreadException;

import com.b12kab.tmdblibrary.NetworkHelper;
import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.AuthenticateSessionNewResponse;
import com.b12kab.tmdblibrary.entities.AuthenticateTokenValidateWithLoginResponse;
import com.b12kab.tmdblibrary.entities.CreateNewTokenResponse;
import com.b12kab.tmdblibrary.entities.Status;
import com.b12kab.tmdblibrary.exceptions.TmdbException;
import com.b12kab.tmdblibrary.exceptions.TmdbNetworkException;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Response;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_API_ERR_MSG;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_API_KEY_INVALID;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_ID_OR_PASSWORD_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_SESSION_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_TOKEN_RELATED;

public class SessionHelper extends NetworkHelper implements ISessionHelper {

    /***
     * This is a list of error status codes created by TMDb
     *
     * @return List<Integer>
     */
    public List<Integer> getAssocHelperTmdbErrorStatusCodes() {
        return Arrays.asList(
                6,  // invalid session logout
                7,  // invalid API key
                30, // invalid name / pw
                34  // missing resource
        );
    }

    /***
     * This is a list of error status codes created by the helper
     *
     * @return List<Integer>
     */
    public List<Integer> getAssocHelperNonTmdbErrorStatusCodes() {
        return Arrays.asList(
                TMDB_CODE_API_KEY_INVALID,
                TMDB_CODE_ID_OR_PASSWORD_RELATED
        );
    }

    /***
     * Log out of the Tmdb session, if possible
     *
     * @param tmdb Tmdb
     * @param session logged in TMDb session
     * @return true = worked, false = didn't work
     * @throws IOException TmdbException
     */
    public boolean destroyTmdbSession(Tmdb tmdb, String session) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        // Check userid / passwd
        if (session == null || StringUtils.isBlank(session)) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_SESSION_RELATED, "You must provide a populated TMDb session");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        Boolean worked = this.obtainLogout(tmdb, session);
        if (worked == null)
            return false;

        return worked;
    }

    /***
     * Try to log out of a logged in TMDb session
     * This will try to loop thru up to 3 times
     *
     * @param tmdb Tmdb
     * @param session logged in TMDb session
     * @return Boolean
     * @throws IOException TmdbException
     */
    @Nullable
    private Boolean obtainLogout(Tmdb tmdb, String session) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                Status status = this.processLogout(tmdb, session);

                if (status != null) {
                    if (status.getSuccess()) {
                        return status.getSuccess();
                    } else {
                        if (status.getStatusCode() != null && status.getStatusCode() == 6) {
                            return false;
                        }
                        // result not success, retry - it can't hurt
                        retry = true;
                        retryTime = 2;
                    }
                }
            // Note - TmdbNetworkException and any other exception are ignored and will bubble up
            } catch (TmdbException ex) {
                NetworkHelper.ExceptionCheckReturn checkReturn = CheckForNetworkRetry(ex);
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
     * Logout of the session
     *
     * @param tmdb Tmdb
     * @param session logged in TMDb session
     * @return Status
     * @throws IOException TmdbException
     */
    private Status processLogout(@NonNull Tmdb tmdb, String session) throws IOException {
        try {
            Call<Status> call = tmdb.authenticationService().logoutSession(session);
            Response<Status> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            this.ProcessError(response);
            // this will never return, but the compiler wants a return
            return null;
        } catch (Exception exception) {
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException || exception instanceof NetworkOnMainThreadException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }

    /***
     * Get the TMDb session
     * @param tmdb Tmdb
     * @param userId user id
     * @param passwd password
     * @throws IOException TmdbException
     */
    public String createTmdbSession(Tmdb tmdb, String userId, String passwd) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        // Check userid / passwd
        if ((userId == null || StringUtils.isBlank(userId)) && (passwd == null || StringUtils.isBlank(passwd))) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_ID_OR_PASSWORD_RELATED, "You must provide a username and a password.");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if (userId == null || StringUtils.isBlank(userId)) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_ID_OR_PASSWORD_RELATED, "You must provide a username.");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if (passwd == null || StringUtils.isBlank(passwd)) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_ID_OR_PASSWORD_RELATED, "You must provide a password.");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        String token = this.obtainToken(tmdb);
        if (token == null)
        {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_TOKEN_RELATED, "Failed to create a new token");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        boolean worked = this.obtainAssociation(tmdb, userId, passwd, token);
        if (!worked) {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_TOKEN_RELATED, "Failed to associate token with userid / password");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        String sessionId = this.obtainSession(tmdb, token);
        if (sessionId == null)
        {
            TmdbException tmdbException = new TmdbException(TMDB_CODE_SESSION_RELATED, "Failed to create a session");
            tmdbException.setUseMessage(TmdbException.UseMessage.Yes);
            throw tmdbException;
        }

        return sessionId;
    }

    /***
     * Try to create a new token
     * This will try to loop thru up to 3 times
     *
     * @param tmdb Tmdb
     * @return Token value or null
     * @throws IOException TmdbException
     */
    @Nullable
    private String obtainToken(@NonNull Tmdb tmdb) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                CreateNewTokenResponse authenticateToken = this.getToken(tmdb);

                if (authenticateToken != null) {
                    if (authenticateToken.getSuccess() &&
                            authenticateToken.getRequestToken() != null &&
                            !authenticateToken.getRequestToken().isEmpty()) {
                        return authenticateToken.getRequestToken();
                    } else {
                        // result not success, retry - it can't hurt
                        retry = true;
                        retryTime = 2;
                    }
                }
            // Note - TmdbNetworkException and any other exception are ignored and will bubble up
            } catch (TmdbException ex) {
                NetworkHelper.ExceptionCheckReturn checkReturn = CheckForNetworkRetry(ex);
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
     * Step 1 - Create new token, if possible.
     *
     * @param tmdb Tmdb
     * @return AuthenticateTokenNewResponse
     * @throws IOException TmdbException
     */
    private CreateNewTokenResponse getToken(@NonNull Tmdb tmdb) throws IOException {
        try {
            Call<CreateNewTokenResponse> call = tmdb.authenticationService().createToken();
            Response<CreateNewTokenResponse> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }

            this.ProcessError(response);
            // this will never return, but the compiler wants a return
            return null;
        } catch (Exception exception) {
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException || exception instanceof NetworkOnMainThreadException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }

    /***
     * Try to associate token with userid / password
     * This will try to loop thru up to 3 times
     *
     * @param tmdb tmdb
     * @param userId user id
     * @param passwd password
     * @param token Token created in step 1
     * @return true = success; false = failure
     * @throws IOException TmdbException
     */
    private boolean obtainAssociation(@NonNull Tmdb tmdb, String userId, String passwd, String token) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                AuthenticateTokenValidateWithLoginResponse associateToken = this.associateToken(tmdb, userId, passwd, token);

                if (associateToken != null) {
                    if (associateToken.getSuccess()) {
                        return associateToken.getSuccess();
                    } else {
                        // result not success, retry - it can't hurt
                        retry = true;
                        retryTime = 2;
                    }
                }
            // Note - TmdbNetworkException and any other exception are ignored and will bubble up
            } catch (TmdbException ex) {
                NetworkHelper.ExceptionCheckReturn checkReturn = CheckForNetworkRetry(ex);
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

        return false;
    }

    /***
     * Step 2 - Associate the token with the user ID & password
     *
     * @param tmdb Tmdb
     * @param userId user id
     * @param passwd password
     * @param token created token
     * @return AuthenticateTokenValidateWithLoginResponse
     * @throws IOException TmdbException
     */
    @Nullable
    private AuthenticateTokenValidateWithLoginResponse associateToken(@NonNull Tmdb tmdb, String userId, String passwd, String token) throws IOException {
        try {
            Call<AuthenticateTokenValidateWithLoginResponse> call = tmdb.authenticationService().authorizeTokenWithLogin(token, userId, passwd);
            Response<AuthenticateTokenValidateWithLoginResponse> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            this.ProcessError(response);
            // this will never return, but the compiler wants a return
            return null;
        } catch (Exception exception) {
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException || exception instanceof NetworkOnMainThreadException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }

    /***
     * Try to create session
     * This will try to loop thru up to 3 times
     *
     * @param tmdb Tmdb
     * @param token Token associated in step 2
     * @return Session Id
     * @throws IOException TmdbException
     */
    @Nullable
    private String obtainSession(@NonNull Tmdb tmdb, String token) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                AuthenticateSessionNewResponse createSession = this.createSessionFromToken(tmdb, token);

                if (createSession != null) {
                    if (createSession.getSuccess()) {
                        return createSession.getSessionId();
                    } else {
                        // result not success, retry - it can't hurt
                        retry = true;
                        retryTime = 2;
                    }
                }
            } catch (TmdbException ex) {
                NetworkHelper.ExceptionCheckReturn checkReturn = CheckForNetworkRetry(ex);
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
     * Step 3 - Create the new session from the token
     *
     * @param tmdb Tmdb
     * @param token Associated userid / password token from step 2 (and step 1)
     * @return AuthenticateSessionNewResponse
     * @throws IOException TmdbException
     */
    @Nullable
    private AuthenticateSessionNewResponse createSessionFromToken(@NonNull Tmdb tmdb, String token) throws IOException {
        try {
            Call<AuthenticateSessionNewResponse> call = tmdb.authenticationService().createSession(token);
            Response<AuthenticateSessionNewResponse> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            this.ProcessError(response);
            // this will never return, but the compiler wants a return
            return null;
        } catch (Exception exception) {
            if (exception instanceof TmdbException || exception instanceof TmdbNetworkException || exception instanceof NetworkOnMainThreadException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }
}
