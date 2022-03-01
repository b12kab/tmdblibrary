package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.entities.Review;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;

import retrofit2.Call;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReviewsServiceTest extends BaseTestCase {

    @Test
    public void test_getDetails() throws IOException {
        Call<Review> call = getManager().reviewsService().getDetails(
                TestData.REVIEW_ID
        );

        Review review = call.execute().body();

        assertReviewDataIntegrity(review);
    }

    public static void assertReview(Review review, boolean extensive) {
        assertNotNull(review.author);
        assertNotNull(review.content);
        assertNotNull(review.id);
        assertNotNull(review.url);
        if (!extensive)
            return;

        assertNotNull(review.iso_639_1);
        assertNotNull(review.media_id);
        assertNotNull(review.media_title);
        assertNotNull(review.media_type);
    }

    public static void assertReviewDataIntegrity(Review review) {
        assertReview(review, true);

        assertEquals("movie", review.media_type);
        assertEquals(TestData.REVIEW_ID, review.id);
        assertTrue(StringUtils.isNotBlank(review.content));
        assertTrue(StringUtils.isNotBlank(review.author));

    }
}
