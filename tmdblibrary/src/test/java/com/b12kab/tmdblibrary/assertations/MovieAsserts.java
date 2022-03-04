package com.b12kab.tmdblibrary.assertations;

import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.entities.BaseMember;
import com.b12kab.tmdblibrary.entities.BaseMovie;
import com.b12kab.tmdblibrary.entities.CastMember;
import com.b12kab.tmdblibrary.entities.CreditResults;
import com.b12kab.tmdblibrary.entities.CrewMember;
import com.b12kab.tmdblibrary.entities.Image;
import com.b12kab.tmdblibrary.entities.MovieAbbreviated;
import com.b12kab.tmdblibrary.entities.MovieFull;
import com.b12kab.tmdblibrary.entities.ProductionCountry;
import com.b12kab.tmdblibrary.entities.ReleaseDateResults;
import com.b12kab.tmdblibrary.entities.ReleaseDates;
import com.b12kab.tmdblibrary.entities.Review;
import com.b12kab.tmdblibrary.entities.Video;
import com.b12kab.tmdblibrary.enumerations.StatusState;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MovieAsserts {
    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd");

    public static void assertBaseMovie(BaseMovie movie) {
        assertNotNull(movie, "movie");
        assertNotNull(movie.isAdult(), "movie adult");
        assertNotNull(movie.getBackdrop_path(), "movie backdrop");
        assertNotNull(movie.getOriginal_language(), "movie original language");
        assertNotNull(movie.getOverview(), "movie overview");
        assertNotNull(movie.getPopularity(), "movie popularity");
        assertNotNull(movie.getPoster_path(), "movie poster");
        assertNotNull(movie.getRelease_date(), "movie release");
        assertNotNull(movie.getTitle(), "movie title");
        assertNotNull(movie.isVideo(), "movie video");
        assertNotNull(movie.getVote_average(), "movie average");
        assertNotNull(movie.getVote_count(), "movie count");
        assertTrue(movie.getVote_average() >= 0, "movie average < 0");
        assertTrue(movie.getVote_count() >= 0, "movie count < 0");
    }

    public static void assertMovieAbbr(MovieAbbreviated movie) {
        assertBaseMovie(movie);
        assertNotNull(movie.getGenre_ids(), "movie genre id");
        assertTrue(movie.getGenre_ids().size() > 0, "movie genre id size");
    }

    public static void assertMovie(MovieFull movie, boolean collectionPopulated) {
        assertBaseMovie(movie);
        if (collectionPopulated)
            assertNotNull(movie.getBelongs_to_collection(), "movie belongs to collection");
        assertNotNull(movie.getBudget(), "movie budget");
        assertNotNull(movie.getGenres(), "movie genre");
        assertTrue(movie.getGenres().size() > 0, "movie genre size");
        assertNotNull(movie.getHomepage(), "movie homepage");
        assertNotNull(movie.getImdb_id(), "movie imdb page");
        assertNotNull(movie.getProduction_companies(), "movie production companies");
        assertTrue(movie.getProduction_companies().size() > 0, "movie production companies size");
        assertNotNull(movie.getProduction_countries(), "movie production countries");
        assertTrue(movie.getProduction_countries().size() > 0, "movie production countries size");
        assertNotNull(movie.getRevenue(), "movie revenue");
        assertNotNull(movie.getRuntime(), "movie runtime");
        assertNotNull(movie.getSpoken_languages(), "movie languages");
        assertNotNull(movie.getStatus(), "movie status");
        assertNotNull(movie.getTagline(), "movie tagline");
    }


    public static void assertMovieTestData(MovieFull movie, String funcName) throws ParseException {
        final int movieBudget = 63000000;
        final int movieRevenue = 100853753;
        final int movieRuntime = 139;
        final String movieReleaseDate = "1999-10-15";

        assertMovie(movie, false);
        assertNull(movie.getBelongs_to_collection(), funcName + "movie collection is not null");
        assertEquals(false, movie.isAdult(), funcName + "movie id is adult");
        assertEquals(TestData.MOVIE_ID, movie.getId(), funcName + "movie id is not " + TestData.MOVIE_ID);
        assertEquals(TestData.MOVIE_TITLE, movie.getTitle(), funcName + "movie title is not " + TestData.MOVIE_TITLE);
        assertEquals(TestData.MOVIE_TITLE, movie.getOriginal_title(), funcName + "movie original_title is not " + TestData.MOVIE_TITLE);
        assertEquals(movieBudget, movie.getBudget(), funcName + "movie budget is not equal to " + movieBudget);
        assertEquals(movieRevenue, movie.getRevenue(), funcName + "movie revenue not equal to " + movieRevenue);
        assertEquals(movieRuntime, movie.getRuntime(), funcName + "movie runtime not equal to " + movieRuntime);
        assertEquals(TestData.MOVIE_IMDB, movie.getImdb_id(), funcName + "movie imdb_id is not equal to " + TestData.MOVIE_IMDB);
        assertEquals(JSON_STRING_DATE.parse(movieReleaseDate), movie.getRelease_date(),
                funcName + "movie release_date is not equal to " + movieReleaseDate);
        assertEquals(StatusState.RELEASED.toString(), movie.getStatus(), funcName + "movie status not equal to " + StatusState.RELEASED);

        boolean foundCountry = false;
        for (ProductionCountry country: movie.getProduction_countries()) {
            if (country.iso_3166_1.equals("US")) {
                foundCountry = true;
                assertEquals("United States of America", country.name, funcName + "movie production country name mismatch");
            }
        }

        if (!foundCountry) {
            fail(funcName + "Failed to find US as one of the production countries ");
        }

        assertEquals("en", movie.getSpoken_languages().get(0).iso_639_1, funcName + "movie spoken language 639 mismatch");
        assertEquals("English", movie.getSpoken_languages().get(0).name, funcName + "movie spoken language name mismatch");
    }

    public static void assertMovieTestDataAppended(MovieFull movie, String funcName,
                                                   boolean appendAccount,
                                                   boolean appendCredit,
                                                   boolean appendImages,
                                                   boolean appendRelease,
                                                   boolean appendVideos,
                                                   boolean appendReviews,
                                                   boolean appendSimilar) throws ParseException {
        if (appendAccount) {
            assertNotNull(movie.account_states, funcName + "movie appended states is null");
            assertNotNull(movie.account_states.getRated(), funcName + "movie appended states rating is null");
            assertNotNull(movie.account_states.isWatchlist(), funcName + "movie appended states watchlist is null");
        } else {
            assertNull(movie.account_states, funcName + "movie appended states is not null");
        }

        if (appendCredit) {
            assertNotNull(movie.credits, funcName + "movie credits is null");
            // According to the schema, id should be here, but ¯\_(ツ)_/¯
            // assertNotNull(movie.credits.getId(), funcName + "movie credits Id is null");
            assertNotNull(movie.credits.getCast(), funcName + "movie credits cast is null");
            assertTrue(movie.credits.getCast().size() > 0, funcName + "movie credits cast size is 0");
            assertNotNull(movie.credits.getCrew(), funcName + "movie credits crew is null");
            assertTrue(movie.credits.getCrew().size() > 0, funcName + "movie credits crew size is 0");
            assertCredits(movie.credits, funcName);
        } else {
            assertNull(movie.credits, funcName + "movie credits is not null");
        }

        if (appendImages) {
            assertNotNull(movie.images, funcName + "movie images is null");
            // According to the schema, id should be here, but ¯\_(ツ)_/¯
            // assertNotNull(movie.images.id, funcName + "movie images id is null");
            assertNotNull(movie.images.backdrops, funcName + "movie images backdrops is null");
            assertTrue(movie.images.backdrops.size() > 0, funcName + "movie images backdrops is 0");
            assertImageType(movie.images.backdrops, funcName, "backdrops");
            assertNotNull(movie.images.posters, funcName + "movie images posters is null");
            assertTrue(movie.images.posters.size() > 0, funcName + "movie images posters is 0");
            assertImageType(movie.images.posters, funcName, "posters");
            assertNotNull(movie.images.logos, funcName + "movie images stills is null");
            assertTrue(movie.images.logos.size() > 0, funcName + "movie images stills is 0");
            assertImageType(movie.images.logos, funcName, "logos");
        } else {
            assertNull(movie.images, funcName + "movie images is not null");
        }

        if (appendRelease) {
            assertNotNull(movie.release_dates, funcName + "movie release dates is null");
            // According to the schema, id should be here, but ¯\_(ツ)_/¯
            // assertNotNull(movie.release_dates.getId(), funcName + "movie release dates id is null");
            assertNotNull(movie.release_dates.getResults(), funcName + "movie release dates results is null");
            assertTrue(movie.release_dates.getResults().size() > 0, funcName + "movie release dates results is 0");
            assertReleaseDateResults(movie.release_dates.getResults(), funcName);
        } else {
            assertNull(movie.release_dates, funcName + "movie release dates is not null");
        }

        if (appendVideos) {
            assertNotNull(movie.videos, funcName + "movie videos is null");
            // According to the schema, id should be here, but ¯\_(ツ)_/¯
            // assertNotNull(movie.videos.id, funcName + "movie videos id is null");
            assertNotNull(movie.videos.getResults(), funcName + "movie videos results is null");
            assertTrue(movie.videos.getResults().size() > 0, funcName + "movie videos results size is 0");
            assertVideos(movie.videos.getResults(), funcName);
        } else {
            assertNull(movie.videos, funcName + "movie videos is not null");
        }

        if (appendReviews) {
            assertNotNull(movie.reviews, funcName + "movie reviews is null");
            // According to the schema, id should be here, but ¯\_(ツ)_/¯
            // assertNotNull(movie.reviews.id, funcName + "movie reviews id is null");
            assertNotNull(movie.reviews.page, funcName + "movie reviews page is null");
            assertNotNull(movie.reviews.total_pages, funcName + "movie reviews total pages is null");
            assertNotNull(movie.reviews.total_results, funcName + "movie reviews total results is null");
            assertNotNull(movie.reviews.results, funcName + "movie reviews results list is null");
            assertTrue(movie.reviews.results.size() > 0, funcName + "movie reviews results list is 0");
            assertReviews(movie.reviews.results, funcName);
        } else {
            assertNull(movie.reviews, funcName + "movie reviews is not null");
        }

        if (appendSimilar) {
            assertNotNull(movie.similar, funcName + "movie similar is null");
            assertNotNull(movie.similar.page, funcName + "movie similar page is null");
            assertNotNull(movie.similar.total_pages, funcName + "movie similar total pages is null");
            assertNotNull(movie.similar.total_results, funcName + "movie similar total results is null");
            assertNotNull(movie.similar.results, funcName + "movie similar results list is null");
            assertTrue(movie.similar.results.size() > 0, funcName + "movie similar results list is 0");
        } else {
            assertNull(movie.similar, funcName + "movie similar is not null");
        }
    }

    public static void assertReviews(List<Review> reviews, String funcName) throws ParseException {
        assertNotNull(reviews, funcName + "reviews list is null");
        assertTrue(reviews.size() > 0, funcName + "reviews list is 0");
        for (Review item: reviews) {
            assertNotNull(item, funcName + "review is null");
            assertNotNull(item.id, funcName + "review id is null");
            assertNotNull(item.author, funcName + "review author is null");
            assertNotNull(item.author_details, funcName + "review author detail is null");
            assertNotNull(item.author_details.username, funcName + "review author detail is null");
            assertNotNull(item.content, funcName + "review content is null");
            assertNotNull(item.created_at, funcName + "review created_at is null");
            assertNotNull(item.updated_at, funcName + "review updated_at is null");
            assertNotNull(item.url, funcName + "review updated_at is null");
        }
    }

    public static void assertVideos(List<Video> list, String funcName) throws ParseException {
        assertNotNull(list, funcName + "Video list is null");
        assertTrue(list.size() > 0, funcName + "Video list is 0");
        for (Video item: list) {
            assertNotNull(item, funcName + "video is null");
            assertNotNull(item.getId(), funcName + "video id is null");
            assertNotNull(item.getIso_639_1(), funcName + "video 639 is null");
            assertNotNull(item.getIso_3166_1(), funcName + "video 3166 is null");
            assertNotNull(item.getName(), funcName + "video name is null");
            assertNotNull(item.getKey(), funcName + "video key is null");
            assertNotNull(item.getSite(), funcName + "video site is null");
            assertNotNull(item.getSize(), funcName + "video size is null");
            assertNotNull(item.getType(), funcName + "video type is null");
            assertNotNull(item.getOfficial(), funcName + "video official is null");
            assertNotNull(item.getPublished_at(), funcName + "video published is null");

        }
    }

    public static void assertReleaseDateResults(List<ReleaseDateResults> list, String funcName) throws ParseException {
        assertNotNull(list, funcName + "release date list is null");
        assertTrue(list.size() > 0, funcName + "release date results list is 0");
        for (ReleaseDateResults item: list) {
            assertNotNull(item, funcName + "release date results is null");
            assertNotNull(item.getIso_3166_1(), funcName + "release date results 3166 is null");
            assertNotNull(item.getRelease_dates(), funcName + "release date dates is null");
            for (ReleaseDates release: item.getRelease_dates()) {
                assertNotNull(release.getCertification(), funcName + "release date key is null");
                assertNotNull(release.getIso_639_1(), funcName + "release date 639 is null");
                assertNotNull(release.getRelease_date(), funcName + "release date date is null");
                assertNotNull(release.getType(), funcName + "release date type is null");
            }
        }
    }

    public static void assertImageType(List<Image> items, String funcName, String imgType) throws ParseException {
        assertNotNull(items, funcName + "image " + imgType + " image");
        assertTrue(items.size() > 0, funcName + "image " + imgType + " image list is 0");
        for (Image item: items) {
            assertNotNull(item, funcName + "image " + imgType + " image is null");
            assertNotNull(item.aspect_ratio, funcName + "image " + imgType + " image aspect ratio is null");
            assertNotNull(item.height, funcName + "image " + imgType + " image height is null");
            // assertNotNull(item.iso_639_1, "image " + imgType + " image 639 is null");
            assertNotNull(item.file_path, "image " + imgType + " image path is null");
            assertNotNull(item.vote_average, funcName + "image " + imgType + " image vote average is null");
            assertNotNull(item.vote_count, funcName + "image " + imgType + " image vote count is null");
            assertNotNull(item.width, funcName + "image " + imgType + " image width is null");
        }
    }

    public static void assertCredits(CreditResults creditResults, String funcName) throws ParseException {
        assertNotNull(creditResults, funcName + "credit is null");
        assertNotNull(creditResults.getCast(), funcName + "credit cast is null");
        assertTrue(creditResults.getCast().size() > 0, funcName + "credit cast size is 0");
        for (CastMember item: creditResults.getCast()) {
            assertBase(item, funcName, "credit cast item base");

            assertNotNull(item.getCharacter(), funcName + "credit cast item character is null");
            assertNotNull(item.getOrder(), funcName + "credit cast item order is null");
            assertNotNull(item.getCast_id(), funcName + "credit cast item cast id is null");
        }

        assertNotNull(creditResults.getCrew(), funcName + "credit crew is null");
        assertTrue(creditResults.getCrew().size() > 0, funcName + "credit crew size is 0");
        for (CrewMember item: creditResults.getCrew()) {
            assertBase(item, funcName, "credit crew item base");

            assertNotNull(item.getDepartment(), funcName + "credit crew orig name is null");
            assertNotNull(item.getJob(), funcName + "credit crew item job is null");
        }
    }

    public static void assertBase(BaseMember item, String funcName, String type) throws ParseException {
        assertNotNull(item, funcName +  " " + type + " member is null");
        assertNotNull(item.getAdult(), funcName +  " " + type + " adult is null");
        assertNotNull(item.getGender(), funcName +  " " + type + " gender is null");
        assertNotNull(item.getId(), funcName +  " " + type + " id is null");
        assertNotNull(item.getKnown_for_department(), funcName +  " " + type +  " known 4 is null");
        assertNotNull(item.getName(), funcName +  " " + type + " name is null");
        assertNotNull(item.getOriginal_name(), funcName +  " " + type + " orig name is null");
        assertNotNull(item.getPopularity(), funcName +  " " + type +  " popularity is null");
        // assertNotNull(item.getProfile_path(), funcName +  " " + type + " profile path is null");
        assertNotNull(item.getCredit_id(), funcName + " " + type + " credit id is null");

    }

}
