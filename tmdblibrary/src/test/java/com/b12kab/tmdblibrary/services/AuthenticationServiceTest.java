package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.BuildConfig;
import com.b12kab.tmdblibrary.NetworkTestHelper;
import com.b12kab.tmdblibrary.entities.AuthenticateSessionNewResponse;
import com.b12kab.tmdblibrary.entities.AuthenticateTokenValidateWithLoginResponse;
import com.b12kab.tmdblibrary.entities.CreateNewTokenResponse;
import com.b12kab.tmdblibrary.entities.Status;
import com.b12kab.tmdblibrary.implementation.SessionHelper;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AuthenticationServiceTest extends BaseTestCase {
    public NetworkTestHelper networkTestHelper;
    SessionHelper sessionHelper;

    AuthenticationServiceTest() {
        networkTestHelper = new NetworkTestHelper();
        sessionHelper = new SessionHelper();
    }

    @BeforeEach
    void init() {
        this.sleepSetup(3);
    }

    @Test
    public void test_get_token_step_1_only() {
        final String funcName = "test_get_token_step_1_only ";

        CreateNewTokenResponse newToken = null;
        try {
            newToken = this.getManager().authenticationService().createToken().execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on test_get_token_step_1_only: " + e);
        }

        assertNotNull(newToken, funcName + "newToken is null");
        assertNotNull(newToken.getSuccess(), funcName + "newToken success is null");
        assertTrue(newToken.getSuccess(), funcName + "newToken success is false");
        assertFalse(newToken.getRequestToken().isEmpty(), funcName + "newToken overview string length is empty");
        assertNotNull(newToken.getRequestToken(), funcName + "newToken getRequestToken is null");
        assertFalse(newToken.getRequestToken().isEmpty(), funcName + "newToken getRequestToken string is empty");
    }

    @Test
    public void test_associate_tmdb_id_with_invalid_pw_step_2() {
        final String funcName = "test_associate_tmdb_id_with_invalid_pw_step_2 ";

        CreateNewTokenResponse newToken = null;
        try {
            newToken = this.getManager().authenticationService().createToken().execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on test_associate_tmdb_id_with_invalid_pw_step_2 - create token: " + e);
        }

        assertNotNull(newToken, funcName + "newToken is null");
        assertNotNull(newToken.getSuccess(), funcName + "newToken success is null");
        assertTrue(newToken.getSuccess(), funcName + "newToken success is false");
        assertFalse(newToken.getRequestToken().isEmpty(), funcName + "newToken overview string length is empty");
        assertNotNull(newToken.getRequestToken(), funcName + "newToken getRequestToken is null");
        assertFalse(newToken.getRequestToken().isEmpty(), funcName + "newToken getRequestToken string is empty");

        AuthenticateTokenValidateWithLoginResponse authToken = null;
        ResponseBody errorBody = null;
        Response<AuthenticateTokenValidateWithLoginResponse> response = null;
        try {
            Call<AuthenticateTokenValidateWithLoginResponse> call = this.getManager().authenticationService().authorizeTokenWithLogin(newToken.getRequestToken(), BuildConfig.TMDB_TEST_ID, BuildConfig.TMDB_TEST_BAD_PSWD);
            response = call.execute();
            authToken = response.body();
            errorBody = response.errorBody();
        }
        catch (Exception e)
        {
            fail("Exception occurred on test_associate_tmdb_id_with_invalid_pw_step_2 - associate token: " + e);
        }

        assertNull(authToken, funcName + "authToken is not null");
        assertNotNull(errorBody, funcName + "errorBody is null");

        String errors = null;

        try {
            errors = networkTestHelper.ObtainResponseError(errorBody);
        }
        catch (IOException e)
        {
            fail("Exception response error conversion: " + e);
        }

        Status status = null;
        if (errors != null) {
            status = networkTestHelper.ConvertTmdbError(errors);
        }

        assertNotNull(status, funcName + "Status is null");
        assertFalse(status.getSuccess(), funcName + "status success is false");
        assertEquals(status.getStatusCode(), 30, funcName + "status code is not 34");
        assertNotNull(status.getStatusMessage(), funcName + "status message is null");
    }

    @Test
    public void test_associate_tmdb_id_with_valid_pw_step2() {
        final String funcName = "test_associate_tmdb_id_with_valid_pw_step2 ";

        CreateNewTokenResponse newToken = null;
        try {
            newToken = this.getManager().authenticationService().createToken().execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on test_associate_tmdb_id_with_valid_pw_step2 - create token: " + e);
        }

        assertNotNull(newToken, funcName + "newToken is null");
        assertNotNull(newToken.getSuccess(), funcName + "newToken success is null");
        assertTrue(newToken.getSuccess(), funcName + "newToken success is false");
        assertFalse(newToken.getRequestToken().isEmpty(), funcName + "newToken overview string length is empty");
        assertNotNull(newToken.getRequestToken(), funcName + "newToken getRequestToken is null");
        assertFalse(newToken.getRequestToken().isEmpty(), funcName + "newToken getRequestToken string is empty");

        AuthenticateTokenValidateWithLoginResponse authToken = null;
        ResponseBody errorBody = null;
        Response<AuthenticateTokenValidateWithLoginResponse> response = null;

        try {
            Call<AuthenticateTokenValidateWithLoginResponse> call = this.getManager().authenticationService().authorizeTokenWithLogin(newToken.getRequestToken(), BuildConfig.TMDB_TEST_ID, BuildConfig.TMDB_TEST_GOOD_PSWD);
            response = call.execute();
            authToken = response.body();
            errorBody = response.errorBody();
        }
        catch (Exception e)
        {
            fail("Exception occurred on test_associate_tmdb_id_with_valid_pw_step2 - associate token: " + e);
        }

        assertNotNull(authToken, funcName + "authToken is null");
        assertNull(errorBody, funcName + "errorBody is not null");

        assertTrue(authToken.getSuccess(), funcName + "Success is false");
    }

    @Test
    public void test_associate_tmdb_id_with_valid_pw_step3() {
        final String funcName = "test_associate_tmdb_id_with_valid_pw_step3 ";

        CreateNewTokenResponse newToken = null;
        try {
            newToken = this.getManager().authenticationService().createToken().execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on test_associate_tmdb_id_with_valid_pw_step3 - create token: " + e);
        }

        assertNotNull(newToken, funcName + "newToken is null");
        assertNotNull(newToken.getSuccess(), funcName + "newToken success is null");
        assertTrue(newToken.getSuccess(), funcName + "newToken success is false");
        assertFalse(newToken.getRequestToken().isEmpty(), funcName + "newToken overview string length is empty");
        assertNotNull(newToken.getRequestToken(), funcName + "newToken getRequestToken is null");
        assertFalse(newToken.getRequestToken().isEmpty(), funcName + "newToken getRequestToken string is empty");

        AuthenticateTokenValidateWithLoginResponse authToken = null;

        try {
            authToken = this.getManager().authenticationService().authorizeTokenWithLogin(newToken.getRequestToken(), BuildConfig.TMDB_TEST_ID, BuildConfig.TMDB_TEST_GOOD_PSWD).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on test_associate_tmdb_id_with_valid_pw_step3 - associate token: " + e);
        }

        assertNotNull(authToken, funcName + "authToken is null");
        assertTrue(authToken.getSuccess(), funcName + "Success is false");
        assertNotNull(authToken.getRequestToken(), funcName + "Request token is null");
        assertFalse(StringUtils.isEmpty(authToken.getRequestToken()), funcName + "Request token is empty");

        Call<AuthenticateSessionNewResponse> call = null;
        ResponseBody errorBody = null;
        Response<AuthenticateSessionNewResponse> response = null;
        AuthenticateSessionNewResponse authSession = null;

        try {
            call = this.getManager().authenticationService().createSession(authToken.getRequestToken());
            response = call.execute();
            authSession = response.body();
            errorBody = response.errorBody();

        } catch (Exception e) {
            fail("Exception occurred on test_associate_tmdb_id_with_valid_pw_step3 - authenticateSession token: " + e);
        }

        assertNotNull(authSession, funcName + "authSession is null");
        assertNull(errorBody, funcName + "errorBody is not null");

        assertTrue(authSession.getSuccess(), funcName + "Success is false");
        assertNotNull(authSession.getSessionId(), funcName + "Session id is null");
        assertFalse(StringUtils.isEmpty(authSession.getSessionId()), funcName + "Session id is empty");
    }

    @Test
    public void test_tmdb_logout() throws Exception {
        final String funcName = "test_movie_logout ";

        String session = sessionHelper.createTmdbSession(this.getManager(), BuildConfig.TMDB_TEST_ID, BuildConfig.TMDB_TEST_GOOD_PSWD);

        Call<Status> call = null;
        ResponseBody errorBody = null;
        Response<Status> response = null;
        Status logout = null;

        try {
            call = this.getManager().authenticationService().logoutSession(session);
            response = call.execute();
            logout = response.body();
            errorBody = response.errorBody();

        } catch (Exception e) {
            fail("Exception occurred on test_tmdb_logout - " + e);
        }

        assertNotNull(logout, funcName + "logout is null");
        assertNull(errorBody, funcName + "errorBody is not null");

        assertNotNull(logout.getSuccess(), "success is null");
        assertTrue(logout.getSuccess(), "success is false");
    }
}
