package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.entities.RegionResultsPage;
import com.b12kab.tmdblibrary.entities.TvShowComplete;
import com.b12kab.tmdblibrary.entities.WatchProviders;
import com.b12kab.tmdblibrary.entities.WatchResultsPage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class WatchServiceTest extends BaseTestCase {

    @Test
    public void test_regions() {
        final String funcName = "test_regions ";
        RegionResultsPage resultsPage = null;
        try {
            resultsPage = getManager().watchService().regions(null).execute().body();
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(resultsPage.results, funcName + "results is null");
        assertTrue(resultsPage.results.size() > 0, funcName + "results size = 0");
        assertNotNull(resultsPage.results.get(0), funcName + "results size element 0 is null");
        assertNotNull(resultsPage.results.get(0).english_name, funcName + "results size element 0 english name is null");
        assertNotNull(resultsPage.results.get(0).iso_3166_1, funcName + "results size element 0 iso 3166 is null");
        assertNotNull(resultsPage.results.get(0).native_name, funcName + "results size element 0 native name is null");
    }

    @Test
    public void test_region_movie() {
        final String funcName = "test_region_movie ";
        WatchResultsPage resultsPage = null;
        try {
            resultsPage = getManager().watchService().regionMovie(null, null).execute().body();
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(resultsPage.results, funcName + "results is null");
        assertTrue(resultsPage.results.size() > 0, funcName + "results size = 0");
        assertNotNull(resultsPage.results.get(0), funcName + "results size element 0 is null");
        assertNotNull(resultsPage.results.get(0).display_priority, funcName + "results size element 0 display priority is null");
        assertNotNull(resultsPage.results.get(0).logo_path, funcName + "results size element 0 logo path is null");
        assertNotNull(resultsPage.results.get(0).provider_id, funcName + "results size element 0 provider id is null");
        assertNotNull(resultsPage.results.get(0).provider_name, funcName + "results size element 0 provider name is null");
    }

    @Test
    public void test_region_movie_region_language() {
        final String funcName = "test_region_movie_region_language ";
        WatchResultsPage resultsPage = null;
        try {
            resultsPage = getManager().watchService().regionMovie("US", "en").execute().body();
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(resultsPage.results, funcName + "results is null");
        assertTrue(resultsPage.results.size() > 0, funcName + "results size = 0");
        assertNotNull(resultsPage.results.get(0), funcName + "results size element 0 is null");
        assertNotNull(resultsPage.results.get(0).display_priority, funcName + "results size element 0 display priority is null");
        assertNotNull(resultsPage.results.get(0).logo_path, funcName + "results size element 0 logo path is null");
        assertNotNull(resultsPage.results.get(0).provider_id, funcName + "results size element 0 provider id is null");
        assertNotNull(resultsPage.results.get(0).provider_name, funcName + "results size element 0 provider name is null");
    }

    @Test
    public void test_region_tv() {
        final String funcName = "test_region_tv ";
        WatchResultsPage resultsPage = null;
        try {
            resultsPage = getManager().watchService().regionTv(null, null).execute().body();
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(resultsPage.results, funcName + "results is null");
        assertTrue(resultsPage.results.size() > 0, funcName + "results size = 0");
        assertNotNull(resultsPage.results.get(0), funcName + "results size element 0 is null");
        assertNotNull(resultsPage.results.get(0).display_priority, funcName + "results size element 0 display priority is null");
        assertNotNull(resultsPage.results.get(0).logo_path, funcName + "results size element 0 logo path is null");
        assertNotNull(resultsPage.results.get(0).provider_id, funcName + "results size element 0 provider id is null");
        assertNotNull(resultsPage.results.get(0).provider_name, funcName + "results size element 0 provider name is null");
    }

    @Test
    public void test_region_tv_region_language() {
        final String funcName = "test_region_tv_region_language ";
        WatchResultsPage resultsPage = null;
        try {
            resultsPage = getManager().watchService().regionTv("US", "en").execute().body();
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(resultsPage.results, funcName + "results is null");
        assertTrue(resultsPage.results.size() > 0, funcName + "results size = 0");
        assertNotNull(resultsPage.results.get(0), funcName + "results size element 0 is null");
        assertNotNull(resultsPage.results.get(0).display_priority, funcName + "results size element 0 display priority is null");
        assertNotNull(resultsPage.results.get(0).logo_path, funcName + "results size element 0 logo path is null");
        assertNotNull(resultsPage.results.get(0).provider_id, funcName + "results size element 0 provider id is null");
        assertNotNull(resultsPage.results.get(0).provider_name, funcName + "results size element 0 provider name is null");
    }}
