package com.b12kab.tmdblibrary;

import com.b12kab.tmdblibrary.entities.AccountResponse;
import com.b12kab.tmdblibrary.entities.AuthenticateSessionNewResponse;
import com.b12kab.tmdblibrary.entities.AuthenticateTokenValidateWithLoginResponse;
import com.b12kab.tmdblibrary.entities.CreateNewTokenResponse;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class BaseTestCase {
    private static final boolean DEBUG = true;
    private static String sessionId;
    private static final Tmdb manager = new Tmdb();
    private final Object sessionLocker = new Object();
    private final Object accountLocker = new Object();
    private AccountResponse accountResponse;

    @BeforeAll
    public static void setUpOnce() {
        manager.setApiKey(BuildConfig.TMDB_TEST_API_KEY);
        manager.setIsDebug(DEBUG);
    }

    protected final Tmdb getManager() {
        return manager;
    }

    public String createTmdbSession() throws IOException {
        synchronized (sessionLocker) {
            if (sessionId == null) {
                this.sleepSetup(4);

                CreateNewTokenResponse newToken = this.getManager().authenticationService().createToken().execute().body();
                AuthenticateTokenValidateWithLoginResponse authToken = this.getManager().authenticationService().authorizeTokenWithLogin(newToken.getRequestToken(), BuildConfig.TMDB_TEST_ID, BuildConfig.TMDB_TEST_GOOD_PSWD).execute().body();
                AuthenticateSessionNewResponse authSession = this.getManager().authenticationService().createSession(authToken.getRequestToken()).execute().body();

                if (!authSession.getSuccess()) {
                    throw new TmdbException(810, "authorization wasn't successful");
                }
                sessionId = authSession.getSessionId();
            }
            return sessionId;
        }
    }

    public AccountResponse getAccount(String session) throws IOException {
        Call<AccountResponse> call = null;
        ResponseBody errorBody = null;
        Response<AccountResponse> response = null;
        AccountResponse acctResp = null;

        synchronized (accountLocker) {
            if (this.accountResponse == null) {
                try {
                    this.sleepSetup(3);

                    call = this.getManager().accountService().getAccountInfo(session);
                    response = call.execute();
                    acctResp = response.body();
                    errorBody = response.errorBody();
                } catch (Exception e) {
                    fail("Exception occurred on getAccount - " + e.toString());
                    throw e;
                }

                if (!response.isSuccessful()) {
                    throw new TmdbException(811, "response wasn't successful");
                }
                this.accountResponse = acctResp;
            }
            return this.accountResponse;
        }
    }

    public void sleepSetup(int secs) {
        try {
            Thread.sleep(secs * 1000L);
        } catch (InterruptedException e){
            System.out.println(e);
        }
    }
}
