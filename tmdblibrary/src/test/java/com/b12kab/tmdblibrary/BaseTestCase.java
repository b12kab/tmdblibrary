package com.b12kab.tmdblibrary;

import com.b12kab.tmdblibrary.entities.AuthenticateSessionNewResponse;
import com.b12kab.tmdblibrary.entities.AuthenticateTokenValidateWithLoginResponse;
import com.b12kab.tmdblibrary.entities.CreateNewTokenResponse;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public abstract class BaseTestCase {
    private static final boolean DEBUG = true;
    private static String sessionId;
    private static final Tmdb manager = new Tmdb();
    private Object locker = new Object();

    @BeforeAll
    public static void setUpOnce() {
        manager.setApiKey(BuildConfig.TMDB_TEST_API_KEY);
        manager.setIsDebug(DEBUG);
    }

    protected final Tmdb getManager() {
        return manager;
    }

    public String createTmdbSession() throws IOException {
        synchronized (locker) {
            if (this.sessionId == null) {
                this.sleepSetup(5);

                CreateNewTokenResponse newToken = this.getManager().authenticationService().createToken().execute().body();
                AuthenticateTokenValidateWithLoginResponse authToken = this.getManager().authenticationService().authorizeTokenWithLogin(newToken.getRequestToken(), BuildConfig.TMDB_TEST_ID, BuildConfig.TMDB_TEST_GOOD_PSWD).execute().body();
                AuthenticateSessionNewResponse authSession = this.getManager().authenticationService().createSession(authToken.getRequestToken()).execute().body();

                if (!authSession.getSuccess()) {
                    throw new TmdbException(10, "authorization wasn't successful");
                }

                sessionId = authSession.getSessionId();
            }
            return sessionId;
        }
    }

    public void sleepSetup(int secs) {
        try {
            Thread.sleep(secs * 1000);
        } catch (InterruptedException e){
            System.out.println(e);
        }
    }
}
