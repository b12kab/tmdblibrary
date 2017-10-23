package com.b12kab.tmdblibrary;

import org.junit.Before;

public abstract class BaseTestCase {
    // Do NOT use this API key in your application, it is solely for testing tmdb-java!
    private static final String API_KEY = "25da90e9f8f0b3892d8bdeb6c3d6267d";

    private static final boolean DEBUG = true;

    private static final Tmdb manager = new Tmdb();

    private static final String tmdbUserName = "addYourTmdbUserIdHere";
    private static final String tmdbUserPassword = "addYourTmdbUserPasswordHere";
    private static final String tmdbUserPasswordInvalid = "asdfadfasdf";

    @Before
    public void setUpOnce() {
        manager.setApiKey(API_KEY);
    }
//        manager.setIsDebug(DEBUG);

    protected final Tmdb getManager() {
        return manager;
    }
}
