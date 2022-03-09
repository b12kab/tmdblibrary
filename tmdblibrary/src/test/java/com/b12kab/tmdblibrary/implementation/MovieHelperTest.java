package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.assertations.MovieAsserts;
import com.b12kab.tmdblibrary.entities.MovieAbbreviated;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.enumerations.MovieFetchType;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import org.junit.jupiter.api.Test;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_MOVIE_TYPE_RELATED;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_PAGE_RELATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MovieHelperTest extends BaseTestCase {
    private final MovieHelper helper;

    public MovieHelperTest() {
        helper = new MovieHelper();
    }

    @Test
    public void test_movie_tmdb_is_null() {
        final String funcName = "test_movie_tmdb_is_null ";

        try {
            helper.processInitialMovies(null, MovieFetchType.Upcoming, null, null, 0);
            fail("Exception did not occur on " + funcName);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_initial_page_is_too_low() {
        final String funcName = "test_movie_initial_page_is_too_low ";

        try {
            helper.processInitialMovies(this.getManager(), MovieFetchType.Upcoming, null, null, 0);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_PAGE_RELATED, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains("page"), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_initial_null_movie_type() {
        final String funcName = "test_movie_initial_null_movie_type ";

        try {
            helper.processInitialMovies(this.getManager(), null, null, null, 1111);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_PAGE_RELATED, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains("Invalid fetch type"), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_initial_upcoming_page_1() {
        final String funcName = "test_movie_initial_upcoming_page_1 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processInitialMovies(this.getManager(), MovieFetchType.Upcoming, null, null, 1);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }
        MovieAsserts.assertMovieResultsPage(resultsPage, false, false);
    }

    @Test
    public void test_movie_initial_upcoming_page_18() {
        final String funcName = "test_movie_initial_upcoming_page_18 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processInitialMovies(this.getManager(), MovieFetchType.Upcoming, null, null, 18);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }
        MovieAsserts.assertMovieResultsPage(resultsPage, true, false);
    }

    @Test
    public void test_movie_initial_popular_page_2() {
        final String funcName = "test_movie_initial_popular_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processInitialMovies(this.getManager(), MovieFetchType.Popular, null, null, 2);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }
        MovieAsserts.assertMovieResultsPage(resultsPage, false, false);
    }

    @Test
    public void test_movie_initial_now_playing_page_2() {
        final String funcName = "test_movie_initial_now_playing_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processInitialMovies(this.getManager(), MovieFetchType.NowPlaying, null, null, 2);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }
        MovieAsserts.assertMovieResultsPage(resultsPage, true, false);
    }

    @Test
    public void test_movie_initial_now_top_rated_page_2() {
        final String funcName = "test_movie_initial_now_playing_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processInitialMovies(this.getManager(), MovieFetchType.TopRated, null, null, 2);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }
        MovieAsserts.assertMovieResultsPage(resultsPage, false, false);
    }

    @Test
    public void test_movie_additional_tmdb_is_null() {
        final String funcName = "test_movie_additional_tmdb_is_null ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processAdditionalMovies(null, null, null, null, 0, 0);
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Non NullPointerException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_additional_start_page_greater_than_end_page() {
        final String funcName = "test_movie_additional_start_page_greater_than_end_page ";

        try {
            helper.processAdditionalMovies(this.getManager(), MovieFetchType.Upcoming, null, null, 2, 1);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_PAGE_RELATED, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains("greater than the start"), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_additional_start_page_is_minus_1_end_page_0() {
        final String funcName = "test_movie_additional_start_page_is_minus_1_end_page_0 ";

        try {
            helper.processAdditionalMovies(this.getManager(), MovieFetchType.Upcoming, null, null, -1, 0);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_PAGE_RELATED, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains("greater than 1"), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_additional_start_page_0_end_page_1() {
        final String funcName = "test_movie_additional_start_page_is_minus_1_end_page_0 ";

        try {
            helper.processAdditionalMovies(this.getManager(), MovieFetchType.Upcoming, null, null, 0, 1);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_PAGE_RELATED, e.getCode(),funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains("greater than 1"), funcName + "message incorrect");
        } catch (Exception e) {
            fail("Non TmdbException exception occurred on " + funcName + ": " + e);
        }
    }

    @Test
    public void test_movie_additional_type_is_null() {
        final String funcName = "test_movie_additional_start_page_is_minus_1_end_page_0 ";

        try {
            helper.processAdditionalMovies(this.getManager(), null, null, null, 2, 2);
            fail("Exception did not occur on " + funcName);
        } catch (TmdbException e) {
            assertEquals(TMDB_CODE_MOVIE_TYPE_RELATED, e.getCode(), funcName + "code doesn't match");
            assertNotNull(e.getMessage(), funcName + "message is null");
            assertTrue(e.getMessage().contains("Invalid type"), funcName + "message incorrect");
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
            resultsPage = helper.processAdditionalMovies(this.getManager(), MovieFetchType.Upcoming, null, null, 2, 2);
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

    @Test
    public void test_movie_additional_upcoming_start_page_2_end_page_4() {
        final String funcName = "test_movie_additional_upcoming_start_page_2_end_page_4 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processAdditionalMovies(this.getManager(), MovieFetchType.Upcoming, null, null, 2, 4);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        assertNotNull(resultsPage.page, funcName + "results page page is null");
        assertEquals(4, resultsPage.page, funcName + "results page page not 2");

        assertNotNull(resultsPage.total_pages, funcName + "results page total_pages is null");
        assertTrue(resultsPage.total_pages > 4, funcName + "results page pages <= 4");

        assertNotNull(resultsPage.total_results, funcName + "results page total_results is null");
        assertTrue(resultsPage.total_results > 60, funcName + "results page pages <= 60");

        assertNotNull(resultsPage.getResults(), funcName + "results page results is null");
        assertTrue(resultsPage.getResults().size() > 0, funcName + "results page results size = 0");
        // 2 -> 4 = 3 pages * 20 = 60
        assertEquals(60, resultsPage.getResults().size(), funcName + "results page results size != 240");
        for (MovieAbbreviated movie: resultsPage.getResults()) {
            MovieAsserts.assertMovieAbbr(movie, true, false);
        }
    }

    /*****************/
    /* now playing   */
    /*****************/

    @Test
    public void test_movie_additional_nowplaying_start_page_2_end_page_2() {
        final String funcName = "test_movie_additional_nowplaying_start_page_2_end_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processAdditionalMovies(this.getManager(), MovieFetchType.NowPlaying, null, null, 2, 2);
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
    public void test_movie_additional_nowplaying_start_page_2_end_page_4() {
        final String funcName = "test_movie_additional_nowplaying_start_page_2_end_page_4 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processAdditionalMovies(this.getManager(), MovieFetchType.NowPlaying, null, null, 2, 4);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        assertNotNull(resultsPage.page, funcName + "results page page is null");
        assertEquals(4, resultsPage.page, funcName + "results page page not 2");

        assertNotNull(resultsPage.total_pages, funcName + "results page total_pages is null");
        assertTrue(resultsPage.total_pages > 4, funcName + "results page pages <= 4");

        assertNotNull(resultsPage.total_results, funcName + "results page total_results is null");
        assertTrue(resultsPage.total_results > 60, funcName + "results page pages <= 60");

        assertNotNull(resultsPage.getResults(), funcName + "results page results is null");
        assertTrue(resultsPage.getResults().size() > 0, funcName + "results page results size = 0");
        // 2 -> 4 = 3 pages * 20 = 60
        assertEquals(60, resultsPage.getResults().size(), funcName + "results page results size != 240");
        for (MovieAbbreviated movie: resultsPage.getResults()) {
            MovieAsserts.assertMovieAbbr(movie, true, false);
        }
    }


    /*****************/
    /* popular       */
    /*****************/

    @Test
    public void test_movie_additional_popular_start_page_2_end_page_2() {
        final String funcName = "test_movie_additional_popular_start_page_2_end_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processAdditionalMovies(this.getManager(), MovieFetchType.Popular, null, null, 2, 2);
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
    public void test_movie_additional_popular_start_page_2_end_page_4() {
        final String funcName = "test_movie_additional_popular_start_page_2_end_page_4 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processAdditionalMovies(this.getManager(), MovieFetchType.Popular, null, null, 2, 4);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        assertNotNull(resultsPage.page, funcName + "results page page is null");
        assertEquals(4, resultsPage.page, funcName + "results page page not 2");

        assertNotNull(resultsPage.total_pages, funcName + "results page total_pages is null");
        assertTrue(resultsPage.total_pages > 4, funcName + "results page pages <= 4");

        assertNotNull(resultsPage.total_results, funcName + "results page total_results is null");
        assertTrue(resultsPage.total_results > 60, funcName + "results page pages <= 60");

        assertNotNull(resultsPage.getResults(), funcName + "results page results is null");
        assertTrue(resultsPage.getResults().size() > 0, funcName + "results page results size = 0");
        // 2 -> 4 = 3 pages * 20 = 60
        assertEquals(60, resultsPage.getResults().size(), funcName + "results page results size != 240");
        for (MovieAbbreviated movie: resultsPage.getResults()) {
            MovieAsserts.assertMovieAbbr(movie, true, true);
        }
    }


    /*****************/
    /* top rated     */
    /*****************/

    @Test
    public void test_movie_additional_toprated_start_page_2_end_page_2() {
        final String funcName = "test_movie_additional_toprated_start_page_2_end_page_2 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processAdditionalMovies(this.getManager(), MovieFetchType.TopRated, null, null, 2, 2);
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

    @Test
    public void test_movie_additional_toprated_start_page_2_end_page_4() {
        final String funcName = "test_movie_additional_toprated_start_page_2_end_page_4 ";
        MovieResultsPage resultsPage = null;

        try {
            resultsPage = helper.processAdditionalMovies(this.getManager(), MovieFetchType.TopRated, null, null, 2, 4);
        } catch (Exception e) {
            fail("Exception occurred on " + funcName + ": " + e);
        }

        assertNotNull(resultsPage.page, funcName + "results page page is null");
        assertEquals(4, resultsPage.page, funcName + "results page page not 2");

        assertNotNull(resultsPage.total_pages, funcName + "results page total_pages is null");
        assertTrue(resultsPage.total_pages > 4, funcName + "results page pages <= 4");

        assertNotNull(resultsPage.total_results, funcName + "results page total_results is null");
        assertTrue(resultsPage.total_results > 60, funcName + "results page pages <= 60");

        assertNotNull(resultsPage.getResults(), funcName + "results page results is null");
        assertTrue(resultsPage.getResults().size() > 0, funcName + "results page results size = 0");
        // 2 -> 4 = 3 pages * 20 = 60
        assertEquals(60, resultsPage.getResults().size(), funcName + "results page results size != 240");
        for (MovieAbbreviated movie: resultsPage.getResults()) {
            MovieAsserts.assertMovieAbbr(movie, false, false);
        }
    }

}
