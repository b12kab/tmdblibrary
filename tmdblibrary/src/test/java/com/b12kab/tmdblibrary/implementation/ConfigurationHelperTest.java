package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.assertations.ConfigurationAsserts;
import com.b12kab.tmdblibrary.entities.Configuration;
import com.b12kab.tmdblibrary.entities.ConfigurationLanguages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class ConfigurationHelperTest extends BaseTestCase {
    ConfigurationHelper helper;

    public ConfigurationHelperTest() {
        helper = new ConfigurationHelper();
    }

    @Test
    public void test_configuration_api_null_tmdb() {
        final String funcName = "test_configuration_api_null_tmdb ";

        try {
            helper.processConfigApi(null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_configuration_api_valid() {
        final String funcName = "test_configuration_api_valid ";

        Configuration config = null;

        try {
            config = helper.processConfigApi(getManager());
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        ConfigurationAsserts.assertApiConfiguration(config);
    }

    @Test
    public void test_configuration_language_null_tmdb() {
        final String funcName = "test_configuration_language_null_tmdb ";

        try {
            helper.processConfigLanguage(null);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_configuration_language_valid() {
        final String funcName = "test_configuration_language_valid ";

        ConfigurationLanguages config = null;

        try {
            config = helper.processConfigLanguage(getManager());
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        ConfigurationAsserts.assertConfigLanguage(config);
    }

}
