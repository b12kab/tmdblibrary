package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.BuildConfig;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SessionHelperTest extends BaseTestCase {
    SessionHelper sessionHelper = null;

    SessionHelperTest() {
        sessionHelper = new SessionHelper();
    }

    @BeforeEach
    void init() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){
            System.out.println(e);
        }
    }

    @Test
    public void test_create_tmdb_is_null() {
        final String funcName = "test_create_tmdb_is_null ";

        try {
            sessionHelper.CreateTmdbSession(null, null, null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_id_null_pswd_null() {
        final String funcName = "test_id_null_pswd_null ";

        try {
            sessionHelper.CreateTmdbSession(this.getManager(), null, null);
            fail("Exception did not occur on " + funcName);
        } catch (Exception e) {
            if (!(e instanceof TmdbException)) {
                fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
            }
            TmdbException exception = (TmdbException)e;
            assertEquals(exception.getCode(), 26, funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("user"), funcName + "message does not contain user");
            assertTrue(exception.getMessage().contains("password"), funcName + "message does not contain user");
        }
    }

    @Test
    public void test_id_empty_pswd_null() {
        final String funcName = "test_id_empty_pswd_null ";

        try {
            sessionHelper.CreateTmdbSession(this.getManager(), "", null);
            fail("Exception did not occur on " + funcName);
        } catch (Exception e) {
            if (!(e instanceof TmdbException)) {
                fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
            }
            TmdbException exception = (TmdbException)e;
            assertEquals(exception.getCode(), 26, funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("user"), funcName + "message does not contain user");
            assertTrue(exception.getMessage().contains("password"), funcName + "message does not contain user");
        }
    }

    @Test
    public void test_id_empty_pswd_empty() {
        final String funcName = "test_id_empty_pswd_null ";

        try {
            sessionHelper.CreateTmdbSession(this.getManager(), "", "");
            fail("Exception did not occur on " + funcName);
        } catch (Exception e) {
            if (!(e instanceof TmdbException)) {
                fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
            }
            TmdbException exception = (TmdbException)e;
            assertEquals(exception.getCode(), 26, funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("user"), funcName + "message does not contain user");
            assertTrue(exception.getMessage().contains("password"), funcName + "message does not contain user");
        }
    }

    @Test
    public void test_id_valid_pswd_empty() {
        final String funcName = "test_id_valid_pswd_empty ";

        try {
            sessionHelper.CreateTmdbSession(this.getManager(), BuildConfig.TMDB_TEST_ID, "");
            fail("Exception did not occur on " + funcName);
        } catch (Exception e) {
            if (!(e instanceof TmdbException)) {
                fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
            }
            TmdbException exception = (TmdbException)e;
            assertEquals(exception.getCode(), 26, funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertFalse(exception.getMessage().contains("user"), funcName + "message does not contain user");
            assertTrue(exception.getMessage().contains("password"), funcName + "message does not contain user");
        }
    }

    @Test
    public void test_id_empty_pswd_invalid() {
        final String funcName = "test_id_empty_pswd_invalid ";

        try {
            sessionHelper.CreateTmdbSession(this.getManager(), "", BuildConfig.TMDB_TEST_BAD_PSWD);
            fail("Exception did not occur on " + funcName);
        } catch (Exception e) {
            if (!(e instanceof TmdbException)) {
                fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
            }
            TmdbException exception = (TmdbException)e;
            assertEquals(exception.getCode(), 26, funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("user"), funcName + "message does not contain user");
            assertFalse(exception.getMessage().contains("password"), funcName + "message does not contain user");
        }
    }

    @Test
    public void test_id_unknown_pswd_invalid() {
        final String funcName = "test_id_unknown_pswd_invalid ";

        try {
            sessionHelper.CreateTmdbSession(this.getManager(), "blah", BuildConfig.TMDB_TEST_BAD_PSWD);
            fail("Exception did not occur on " + funcName);
        } catch (Exception e) {
            if (!(e instanceof TmdbException)) {
                fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
            }
            TmdbException exception = (TmdbException)e;
            assertEquals(exception.getCode(), 30, funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("user"), funcName + "message does not contain user");
            assertTrue(exception.getMessage().contains("password"), funcName + "message does not contain user");
        }
    }

    @Test
    public void test_id_valid_pswd_invalid() {
        final String funcName = "test_id_valid_pswd_invalid ";

        try {
            sessionHelper.CreateTmdbSession(this.getManager(), BuildConfig.TMDB_TEST_ID, BuildConfig.TMDB_TEST_BAD_PSWD);
            fail("Exception did not occur on " + funcName);
        } catch (Exception e) {
            if (!(e instanceof TmdbException)) {
                fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
            }
            TmdbException exception = (TmdbException)e;
            assertEquals(exception.getCode(), 30, funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("user"), funcName + "message does not contain user");
            assertTrue(exception.getMessage().contains("password"), funcName + "message does not contain user");
        }
    }

    @Test
    public void test_id_valid_pswd_valid() {
        final String funcName = "test_id_valid_pswd_valid ";
        String sessionId = null;

        try {
            sessionId = sessionHelper.CreateTmdbSession(this.getManager(), BuildConfig.TMDB_TEST_ID, BuildConfig.TMDB_TEST_GOOD_PSWD);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(sessionId, funcName + "session is null");
        assertFalse(StringUtils.isEmpty(sessionId), funcName + "session is empty");
    }

    @Test
    public void test_destroy_tmdb_is_null() {
        final String funcName = "test_destroy_tmdb_is_null ";

        try {
            sessionHelper.DestroyTmdbSession(null, null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
            int a = 1 + 1;
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_null_session_logout() {
        final String funcName = "test_null_session_logout ";

        try {
            sessionHelper.DestroyTmdbSession(this.getManager(), null);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(exception.getCode(), 26, funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("TMDb session"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_empty_session_logout() {
        final String funcName = "test_empty_session_logout ";

        try {
            sessionHelper.DestroyTmdbSession(this.getManager(), "");
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            TmdbException exception = (TmdbException)e;
            assertEquals(exception.getCode(), 26, funcName + "code doesn't match");
            assertNotNull(exception.getMessage(), funcName + "message is null");
            assertTrue(exception.getMessage().contains("TMDb session"), funcName + "message does not contain user");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e.toString());
        }
    }

    @Test
    public void test_invalid_session_logout() {
        final String funcName = "test_invalid_session_logout ";
        boolean status = false;

        try {
            status = sessionHelper.DestroyTmdbSession(this.getManager(), "badsession");
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertFalse(status, funcName + "status success is false");
    }

    @Test
    public void test_valid_session_logout() {
        final String funcName = "test_valid_session_logout ";
        boolean status = false;

        try {
            status = sessionHelper.DestroyTmdbSession(this.getManager(), sessionHelper.CreateTmdbSession(this.getManager(), BuildConfig.TMDB_TEST_ID, BuildConfig.TMDB_TEST_GOOD_PSWD));
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertTrue(status, funcName + "status success is false");
    }
}
