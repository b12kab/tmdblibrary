package com.b12kab.tmdblibrary.enumerations;

import com.b12kab.tmdblibrary.entities.AppendToResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.CREDITS;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.EXTERNAL_IDS;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.IMAGES;
import static com.b12kab.tmdblibrary.enumerations.AppendToResponseItem.VIDEOS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AppendToResponseTest {
    private AppendToResponse appendToResponse;
    private AppendToResponseItem otherResponseItem = CREDITS;
    private AppendToResponseItem[] appendToResponseItems = {IMAGES, EXTERNAL_IDS};
    private AppendToResponseItem[] appendToResponseOverlapItems = {EXTERNAL_IDS, otherResponseItem};
    private AppendToResponseItem[] appendToResponseNoOverlapItems = {otherResponseItem, VIDEOS};

    private String appendToResponseItemsString = IMAGES.toString() + "," + EXTERNAL_IDS.toString();

    @BeforeEach
    void init() {
        appendToResponse = new AppendToResponse(appendToResponseItems);
    }

    @Test
    void test_append_no_items() {
        appendToResponse = new AppendToResponse();
        assertEquals(0, appendToResponse.size(), "size doesn't match 0");
        assertNotNull(appendToResponse.toList(), "list conversion is null");
        assertEquals(0, appendToResponse.toList().size(), "list size doesn't match 0");
        assertNull(appendToResponse.toString(), "string doesn't match null");
    }

    @Test
    void test_append_has_one_item() {
        appendToResponse = new AppendToResponse(IMAGES);
        assertEquals(1, appendToResponse.size(), "size doesn't match 1");
        assertNotNull(appendToResponse.toList(), "list conversion is null");
        assertEquals(1, appendToResponse.toList().size(), "list size doesn't match 1");
        assertEquals(IMAGES.toString(), appendToResponse.toString(), "string doesn't match");
    }

    @Test
    void test_append_has_items() {
        assertEquals(2, appendToResponse.size(), "size isn't correct");
        assertNotNull(appendToResponse.toList(), "array conversion is null");
        assertEquals(2, appendToResponse.toList().size(), "list size isn't correct");
        assertEquals(appendToResponseItemsString, appendToResponse.toString(), "string doesn't match null");
    }

    @Test
    void test_append_merge_null_into_has_data() {
        AppendToResponse newReponse = appendToResponse.merge(null);

        assertEquals(appendToResponse.size(), newReponse.size(), "size doesn't match original");
        assertNotNull(newReponse.toList(), "list conversion is null");
        assertEquals(appendToResponse.toList().size(), newReponse.toList().size(), "list size doesn't match original");
        // this does a toString and compares the results
        assertEquals(appendToResponse, newReponse, "object doesn't equal");
    }

    @Test
    void test_append_merge_empty_into_empty() {
        AppendToResponse emptyReponse = new AppendToResponse();
        AppendToResponse otherEmptyReponse = new AppendToResponse();
        AppendToResponse newReponse = emptyReponse.merge(otherEmptyReponse);

        assertEquals(0, newReponse.size(), "size doesn't match 0");
        assertNotNull(newReponse.toList(), "list conversion is null");
        assertEquals(0, newReponse.toList().size(), "list size doesn't match 0");
        // this does a toString and compares the results
        assertEquals(emptyReponse, newReponse, "object doesn't equal");
    }

    @Test
    void test_append_merge_empty_into_has_data() {
        AppendToResponse emptyReponse = new AppendToResponse();
        AppendToResponse newReponse = emptyReponse.merge(appendToResponse);

        assertEquals(appendToResponse.size(), newReponse.size(), "size doesn't match");
        assertNotNull(newReponse.toList(), "list conversion is null");
        assertEquals(appendToResponse.toList().size(), newReponse.toList().size(), "list size doesn't match");
        // this does a toString and compares the results
        assertEquals(appendToResponse, newReponse, "object doesn't equal");
    }

    @Test
    void test_append_merge_has_data_into_empty() {
        AppendToResponse emptyReponse = new AppendToResponse();
        AppendToResponse newReponse = appendToResponse.merge(emptyReponse);

        assertEquals(appendToResponse.size(), newReponse.size(), "size doesn't match");
        assertNotNull(newReponse.toList(), "list conversion is null");
        assertEquals(appendToResponse.toList().size(), newReponse.toList().size(), "list size doesn't match");
        // this does a toString and compares the results
        assertEquals(appendToResponse, newReponse, "object doesn't equal");
    }

    @Test
    void test_append_merge_has_data_into_has_data_duplicate() {
        AppendToResponse duplicateReponse = new AppendToResponse(appendToResponseItems);
        AppendToResponse newReponse = appendToResponse.merge(duplicateReponse);

        assertEquals(appendToResponse.size(), newReponse.size(), "size doesn't match");
        assertNotNull(newReponse.toList(), "list conversion is null");
        assertEquals(appendToResponse.toList().size(), newReponse.toList().size(), "list size doesn't match");
        // this does a toString and compares the results
        assertEquals(appendToResponse, newReponse, "object doesn't equal");
    }

    @Test
    void test_append_merge_has_data_into_has_data_overlap() {
        AppendToResponse overlapReponse = new AppendToResponse(appendToResponseOverlapItems);
        AppendToResponse newReponse = appendToResponse.merge(overlapReponse);

        assertEquals(3, newReponse.size(), "size doesn't match");
        assertNotNull(newReponse.toList(), "list conversion is null");
        assertEquals(3, newReponse.toList().size(), "list size doesn't match");
        // this does a toString and compares the results
        assertEquals(appendToResponse, newReponse, "object equals");
    }

    @Test
    void test_append_merge_has_data_into_has_data_no_overlap() {
        AppendToResponse noOverlapReponse = new AppendToResponse(appendToResponseNoOverlapItems);
        AppendToResponse newReponse = appendToResponse.merge(noOverlapReponse);

        assertEquals(4, newReponse.size(), "size doesn't match");
        assertNotNull(newReponse.toList(), "list conversion is null");
        assertEquals(4, newReponse.toList().size(), "list size doesn't match");
        // this does a toString and compares the results
        assertEquals(appendToResponse, newReponse, "object equals");
    }
}
