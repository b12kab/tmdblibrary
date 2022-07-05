package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.assertations.MovieAsserts;
import com.b12kab.tmdblibrary.entities.MovieAbbreviated;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.enumerations.MovieFetchType;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_INVALID_TYPE_ERR_MSG;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_LOWER_PAGE_ERR_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MovieHelperTest extends BaseTestCase {
    private final MovieHelper helper;

    public MovieHelperTest() {
        helper = new MovieHelper();
    }

    @Test
    public void test_movie_tmdb_error_status_cd() {
        final String funcName = "test_movie_tmdb_error_status_cd ";

        List<Integer> codes = helper.getAssocHelperTmdbErrorStatusCodes();
        assertNotNull(codes, funcName + "codes is null");
        assertNotEquals(0, codes.size(), "codes size = 0");
    }

    @Test
    public void test_movie_non_tmdb_error_status_cd() {
        final String funcName = "test_movie_non_tmdb_error_status_cd ";

        List<Integer> codes = helper.getAssocHelperNonTmdbErrorStatusCodes();
        assertNotNull(codes, funcName + "codes is null");
        assertEquals(0, codes.size(), "codes size = 0");
    }

    @Test
    public void test_movie_additional_tmdb_is_null() {
        final String funcName = "test_movie_additional_tmdb_is_null ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processMoviePage(null, null, null, null, 0);
        } catch (NullPointerException ignored) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_additional_start_page_is_minus_1() {
        final String funcName = "test_movie_additional_start_page_is_minus ";

        try {
            helper.processMoviePage(this.getManager(), MovieFetchType.Upcoming, null, null, -1);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertNotNull(e.getErrorKind(), funcName + " error kind is null");
            assertEquals(TmdbException.RetrofitErrorKind.None, e.getErrorKind(), funcName + " error kind does not match None");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_LOWER_PAGE_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_additional_start_page_0() {
        final String funcName = "test_movie_additional_start_page_is_0 ";

        try {
            helper.processMoviePage(this.getManager(), MovieFetchType.Upcoming, null, null, 0);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertNotNull(e.getErrorKind(), funcName + " error kind is null");
            assertEquals(TmdbException.RetrofitErrorKind.None, e.getErrorKind(), funcName + " error kind does not match None");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_LOWER_PAGE_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_additional_type_is_null() {
        final String funcName = "test_movie_additional_type_is_null ";

        try {
            helper.processMoviePage(this.getManager(), null, null, null, 2);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertNotNull(e.getErrorKind(), funcName + " error kind is null");
            assertEquals(TmdbException.RetrofitErrorKind.None, e.getErrorKind(), funcName + " error kind does not match None");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains(TMDB_INVALID_TYPE_ERR_MSG), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    /*****************/
    /* upcoming      */
    /*****************/

    @Test
    public void test_movie_additional_upcoming_start_page_2_end_page_2() {
        final String funcName = "test_movie_additional_upcoming_start_page_2_end_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processMoviePage(this.getManager(), MovieFetchType.Upcoming, null, null, 2);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        assertNotNull(resultsPage.page, funcName + "results page page is null");
        assertEquals(2, resultsPage.page, funcName + "results page page not 2");

        assertNotNull(resultsPage.total_pages, funcName + "results page total_pages is null");
        assertTrue(resultsPage.total_pages > 2, funcName + "results page pages <= 2");

        assertNotNull(resultsPage.total_results, funcName + "results page total_results is null");
        assertTrue(resultsPage.total_results > 40, funcName + "results page pages <= 40");

        assertNotNull(resultsPage.getResults(), funcName + "results page results is null");
        assertTrue(resultsPage.getResults().size() > 0, funcName + "results page results size = 0");
        assertEquals(20, resultsPage.getResults().size(), funcName + "results page results size != 20");
        for (MovieAbbreviated movie: resultsPage.getResults()) {
            MovieAsserts.assertMovieAbbr(movie, true, false);
        }
    }

    @Test
    public void test_movie_additional_upcoming_start_page_2() {
        final String funcName = "test_movie_additional_upcoming_start_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processMoviePage(this.getManager(), MovieFetchType.Upcoming, null, null, 2);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        assertNotNull(resultsPage.page, funcName + "results page page is null");
        assertEquals(2, resultsPage.page, funcName + "results page page not 2");

        assertNotNull(resultsPage.total_pages, funcName + "results page total_pages is null");
        assertTrue(resultsPage.total_pages > 4, funcName + "results page pages <= 4");

        assertNotNull(resultsPage.total_results, funcName + "results page total_results is null");
        assertTrue(resultsPage.total_results > 60, funcName + "results page pages <= 60");

        assertNotNull(resultsPage.getResults(), funcName + "results page results is null");
        assertTrue(resultsPage.getResults().size() > 0, funcName + "results page results size = 0");
        for (MovieAbbreviated movie: resultsPage.getResults()) {
            MovieAsserts.assertMovieAbbr(movie, true, false);
        }
    }

    /*****************/
    /* now playing   */
    /*****************/

    @Test
    public void test_movie_additional_nowplaying_start_page_2() {
        final String funcName = "test_movie_additional_nowplaying_start_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processMoviePage(this.getManager(), MovieFetchType.NowPlaying, null, null, 2);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        assertNotNull(resultsPage.page, funcName + "results page page is null");
        assertEquals(2, resultsPage.page, funcName + "results page page not 2");

        assertNotNull(resultsPage.total_pages, funcName + "results page total_pages is null");
        assertTrue(resultsPage.total_pages > 2, funcName + "results page pages <= 2");

        assertNotNull(resultsPage.total_results, funcName + "results page total_results is null");
        assertTrue(resultsPage.total_results > 40, funcName + "results page pages <= 40");

        assertNotNull(resultsPage.getResults(), funcName + "results page results is null");
        assertTrue(resultsPage.getResults().size() > 0, funcName + "results page results size = 0");
        assertEquals(20, resultsPage.getResults().size(), funcName + "results page results size != 20");
        for (MovieAbbreviated movie: resultsPage.getResults()) {
            MovieAsserts.assertMovieAbbr(movie, true, false);
        }
    }


    /*****************/
    /* popular       */
    /*****************/

    @Test
    public void test_movie_additional_popular_start_page_2() {
        final String funcName = "test_movie_additional_popular_start_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processMoviePage(this.getManager(), MovieFetchType.Popular, null, null, 2);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        assertNotNull(resultsPage.page, funcName + "results page page is null");
        assertEquals(2, resultsPage.page, funcName + "results page page not 2");

        assertNotNull(resultsPage.total_pages, funcName + "results page total_pages is null");
        assertTrue(resultsPage.total_pages > 2, funcName + "results page pages <= 2");

        assertNotNull(resultsPage.total_results, funcName + "results page total_results is null");
        assertTrue(resultsPage.total_results > 40, funcName + "results page pages <= 40");

        assertNotNull(resultsPage.getResults(), funcName + "results page results is null");
        assertTrue(resultsPage.getResults().size() > 0, funcName + "results page results size = 0");
        assertEquals(20, resultsPage.getResults().size(), funcName + "results page results size != 20");
        for (MovieAbbreviated movie: resultsPage.getResults()) {
            MovieAsserts.assertMovieAbbr(movie, true, false);
        }
    }

    /*****************/
    /* top rated     */
    /*****************/

    @Test
    public void test_top_rated_movie_additional_toprated_start_page_2() {
        final String funcName = "test_top_rated_movie_additional_toprated_start_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processMoviePage(this.getManager(), MovieFetchType.TopRated, null, null, 2);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        assertNotNull(resultsPage.page, funcName + "results page page is null");
        assertEquals(2, resultsPage.page, funcName + "results page page not 2");

        assertNotNull(resultsPage.total_pages, funcName + "results page total_pages is null");
        assertTrue(resultsPage.total_pages > 2, funcName + "results page pages <= 2");

        assertNotNull(resultsPage.total_results, funcName + "results page total_results is null");
        assertTrue(resultsPage.total_results > 40, funcName + "results page pages <= 40");

        assertNotNull(resultsPage.getResults(), funcName + "results page results is null");
        assertTrue(resultsPage.getResults().size() > 0, funcName + "results page results size = 0");
        assertEquals(20, resultsPage.getResults().size(), funcName + "results page results size != 20");
        for (MovieAbbreviated movie: resultsPage.getResults()) {
            MovieAsserts.assertMovieAbbr(movie, false, false);
        }
    }

}
