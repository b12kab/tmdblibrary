package com.b12kab.tmdblibrary;

import org.junit.jupiter.api.BeforeAll;

public abstract class BaseTestCase {
    // Do NOT use this API key in your application, it is solely for testing tmdb-java!
    private static final String API_KEY = "25da90e9f8f0b3892d8bdeb6c3d6267d";

    // This is the TMDB session key required to get the account_states
    // If they match, then the MoviesServiceTest will not perform the
    // assertNotNull check
    protected static final String TMDB_SESSION         = "123445673456745674567456";
    protected static final String TMDB_SESSION_INVALID = "123445673456745674567456";

    private static final boolean DEBUG = true;

    private static final Tmdb manager = new Tmdb();

    private static final String tmdbUserName = "addYourTmdbUserIdHere";
    private static final String tmdbUserPassword = "addYourTmdbUserPasswordHere";
    private static final String tmdbUserPasswordInvalid = "asdfadfasdf";

    @BeforeAll
    public static void setUpOnce() {
        manager.setApiKey(API_KEY);
        manager.setIsDebug(DEBUG);
    }

    protected final Tmdb getManager() {
        return manager;
    }
}
