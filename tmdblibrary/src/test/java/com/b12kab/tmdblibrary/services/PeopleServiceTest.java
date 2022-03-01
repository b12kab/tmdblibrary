/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Keith Beatty 2017 - Converted to jUnit 4.12
 * Keith Beatty 2019 - Converted to jUnit 5.x
 */

package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.BaseTestCase;
import com.b12kab.tmdblibrary.TestData;
import com.b12kab.tmdblibrary.entities.Image;
import com.b12kab.tmdblibrary.entities.Media;
import com.b12kab.tmdblibrary.entities.Person;
import com.b12kab.tmdblibrary.entities.PersonCastCredit;
import com.b12kab.tmdblibrary.entities.PersonCredits;
import com.b12kab.tmdblibrary.entities.PersonCrewCredit;
import com.b12kab.tmdblibrary.entities.PersonIds;
import com.b12kab.tmdblibrary.entities.PersonImages;
import com.b12kab.tmdblibrary.entities.PersonResultsPage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PeopleServiceTest extends BaseTestCase {
    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd");

    @Test
    public void test_summary() throws ParseException {
        final String funcName = "test_summary ";
        Person person = null;
        try {
            person = getManager().personService().summary(TestData.PERSON_ID).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(person, "Result was null.");
        assertEquals("Brad Pitt", person.name, "Person name does not match.");
        assertNull(person.homepage, "Person homepage was not null");
        assertNotNull(person.id, "Person TMDB ID was null.");
        assertNotNull(person.biography, "Person biography was null.");
        assertEquals(JSON_STRING_DATE.parse("1963-12-18"),
                person.birthday, "Person birthday does not match.");
        assertNull(person.deathday, "Person deathday does not match");
        assertNotNull(person.place_of_birth, "Person place of birth does not match");
        assertNotNull(person.profile_path, "Movie profile path was null.");
    }

    @Test
    public void test_movie_credits() {
        final String funcName = "test_movie_credits ";
        PersonCredits credits = null;

        try {
            credits = getManager().personService().movieCredits(TestData.PERSON_ID, null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(credits, funcName + "credits is null");
        assertNotNull(credits.id, funcName + "credits id is null");
        assertEquals((int) credits.id, TestData.PERSON_ID, funcName + "credits id is not the same as " + TestData.PERSON_ID);

        assertCastCredits(funcName, credits, false);
        assertCrewCredits(funcName, credits, false);

        for (PersonCastCredit credit : credits.cast) {
            assertNotNull(credit, funcName + "credit is null");
            assertNotNull(credit.original_title, funcName + "credit original_title is null");
            assertNotNull(credit.title, funcName + "credit title is null");
            assertFalse(credit.title.isEmpty(), funcName + "credit title is empty");
        }
    }

    @Test
    public void test_tv_credits() {
        final String funcName = "test_tv_credits ";
        PersonCredits credits = null;

        try {
            credits = getManager().personService().tvCredits(TestData.PERSON_ID, null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(credits, funcName + "credits is null");
        assertNotNull(credits.id, funcName + "credits id is null");
        assertEquals((int) credits.id, TestData.PERSON_ID, funcName + "credits id is not the same as " + TestData.PERSON_ID);

        assertCastCredits(funcName, credits, false);

        for (PersonCastCredit credit : credits.cast) {
            assertNotNull(credit.episode_count, funcName + "credit episode_count is null");
            assertNotNull(credit.episode_count < 0, funcName + "credit episode_count < 0");
            assertNotNull(credit.name, funcName + "credit name is null");
            assertFalse(credit.name.isEmpty(), funcName + "credit name is empty");
        }
    }

    @Test
    public void test_combined_credits() {
        final String funcName = "test_combined_credits ";
        PersonCredits credits = null;

        try {
            credits = getManager().personService().combinedCredits(TestData.PERSON_ID, null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(credits, funcName + "credits is null");
        assertNotNull(credits.id, funcName + "credits id is null");
        assertEquals((int) credits.id, TestData.PERSON_ID, funcName + "credits id is not the same as " + TestData.PERSON_ID);

        assertCastCredits(funcName, credits, true);
        assertCrewCredits(funcName, credits, true);
    }
    
    @Test
    public void test_external_ids() {
        final String funcName = "test_external_ids ";
        PersonIds ids = null;

        try {
            ids = getManager().personService().externalIds(TestData.PERSON_ID).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(ids, funcName + "ids is null");
        assertNotNull(ids.id, funcName + "ids id is null");
        assertEquals((int) ids.id, TestData.PERSON_ID, funcName + "ids id is not " + TestData.PERSON_ID);

        assertNotNull(ids.imdb_id, funcName + "ids IMDB ID is null");
        assertEquals("nm0000093", ids.imdb_id, funcName + "ids IMDB ID is not" + "nm0000093");

        assertNotNull(funcName + "ids FREEBASE MID is null", ids.freebase_mid );
        assertEquals("/m/0c6qh", ids.freebase_mid, funcName + "ids FREEBASE MID is not" + "/m/0c6qh");

        assertNotNull(funcName + "ids FREEBASE ID is null", ids.freebase_id );
        assertEquals("/en/brad_pitt", ids.freebase_id, funcName + "ids FREEBASE ID was null.");

        assertNotNull(ids.tvrage_id, funcName + "ids tvrage_id is null");
        assertEquals(59436, (int) ids.tvrage_id, funcName + "ids tvrage_id is not " + 59436);
    }
    
    @Test
    public void test_images() {
        final String funcName = "test_images ";
        PersonImages images = null;

        try {
            images = getManager().personService().images(TestData.PERSON_ID).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(images, funcName + "images is null");
        assertNotNull(images.id, funcName + "images id is null");
        assertEquals((int) images.id, TestData.PERSON_ID, funcName + "images id is not the same as " + TestData.PERSON_ID);

        for (Image image : images.profiles) {
            assertNotNull(image, funcName + "image is null");
            assertNotNull(image.file_path, funcName + "image file_path is null");
            assertFalse(image.file_path.isEmpty(), funcName + "image file_path is empty");
            assertNotNull(image.width, funcName + "image width is null");
            assertNotNull(image.height, funcName + "image height is null");
        }
    }
    
    @Test
    public void test_popular() {
        final String funcName = "test_popular ";
        PersonResultsPage popular = null;

        try {
            popular = getManager().personService().popular(null, null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(popular, funcName + "popular is null");
        assertNotNull(popular.results, funcName + "popular results is null");
        assertTrue(popular.results.size() > 0, funcName + "popular results size < 1");
        assertNotNull(popular.results.get(0), funcName + "popular results get(0) is null");
        assertNotNull(popular.results.get(0).id, funcName + "popular id is null");
        assertNotNull(popular.results.get(0).name, funcName + "popular name is null");
        assertNotNull(popular.results.get(0).popularity, funcName + "popular popularity is null");
        assertNotNull(popular.results.get(0).profile_path, funcName + "popular profile_path is null");
        assertNotNull(popular.results.get(0).adult, funcName + "popular adult is null");

        for (Media media : popular.results.get(0).known_for) {
            if (media.vote_average > 0.0) {
                assertNotNull(media, funcName + "media is null");
//            assertNotNull(media.adult, funcName + "media adult is null"); // This isn't on all entries
                assertNotNull(media.backdrop_path, funcName + "media backdrop_path is null");
                assertNotNull(media.id, funcName + "media id is null");
//            assertNotNull(media.original_title, funcName + "media original_title is null"); // This isn't on all entries
//            assertNotNull(media.release_date, funcName + "media release_date is null"); // This isn't on all entries
                assertNotNull(media.poster_path, funcName + "media poster_path is null");
//                assertNotNull( media.popularity, funcName + "media popularity is null");
//                assertTrue(media.popularity >= 0, funcName + "media popularity size < 0");
//            assertNotNull(media.title, funcName + "media title is null");

                assertNotNull(media.vote_average, funcName + "media vote_average is null");
                assertTrue(media.vote_average > 0, funcName + "media vote_average is < 1");
                assertNotNull(media.vote_count, funcName + "media vote_count is null");
                assertTrue(media.vote_count > 0, funcName + "media vote_count is < 1");

                assertNotNull(media.media_type, funcName + "media media_type is null");
            }
        }
    }
    
    @Test
    public void test_latest() throws ParseException {
        final String funcName = "test_latest ";
        Person person = null;

        try {
            person = getManager().personService().latest().execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        // Latest person might not have a complete TMDb entry, but at should least some basic properties.
        assertNotNull(person, funcName + "person is null");
        assertNotNull(person.name, funcName + "person name is null");
        assertNotNull(person.id, funcName + "person id is null");
    }

    private void assertCastCredits(String funcName, PersonCredits credits, boolean hasMediaType) {
        // assert cast credits
        assertNotNull(credits, funcName + "credits is null");
        assertNotNull(credits.cast, funcName + "credits cast is null");
        assertFalse(credits.cast.isEmpty(), funcName + "credits cast is empty");

        for (PersonCastCredit credit : credits.cast) {
            assertNotNull(credit, funcName + "credit is null");
            assertNotNull(credit.character, funcName + "credit character is null"); // may be empty

            if (hasMediaType) {
                assertNotNull(credit.media_type, funcName + "credit media_type is null");
                assertFalse(credit.media_type.isEmpty(), funcName + "credits media_type is empty");
            }
        }
    }

    private void assertCrewCredits(String funcName, PersonCredits credits, boolean hasMediaType) {
        // assert crew credits
        assertNotNull(credits, funcName + "credits is null");
        assertNotNull(credits.crew, funcName + "credits crew is null");
        assertFalse(credits.crew.isEmpty(), funcName + "credits crew is empty");

        for (PersonCrewCredit credit : credits.crew) {
            // may be empty, but should exist
            assertNotNull(credit, funcName + "credit is null");
            assertNotNull(credit.department, funcName + "credit department is null");
            assertNotNull(credit.job, funcName + "credit job is null");

            if (hasMediaType) {
                assertNotNull(credit.media_type, funcName + "credit media_type is null");
                assertFalse(credit.media_type.isEmpty(), funcName + "credits media_type is empty");
            }
        }
    }
}
