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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PeopleServiceTest extends BaseTestCase {

    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd");

    @Test
    public void test_summary() throws ParseException {
        Person person = getManager().personService().summary(TestData.PERSON_ID);
        assertNotNull("Result was null.", person);
        assertEquals("Person name does not match.", "Brad Pitt", person.name);
        assertNotNull("Person homepage was null", person.homepage);
        assertNotNull("Person TMDB ID was null.", person.id);
        assertNotNull("Person biography was null.", person.biography);
        assertEquals("Person birthday does not match.", JSON_STRING_DATE.parse("1963-12-18"),
                person.birthday);
        assertNull("Person deathday does not match", person.deathday);
        assertNotNull("Person place of birth does not match", person.place_of_birth);
        assertNotNull("Movie profile path was null.", person.profile_path);
    }

    @Test
    public void test_movie_credits() {
        final String funcName = "test_movie_credits ";
        PersonCredits credits = getManager().personService().movieCredits(TestData.PERSON_ID, null);
        assertNotNull(funcName + "credits is null", credits);
        assertNotNull(funcName + "credits id is null", credits.id);
        assertTrue(funcName + "credits id is not the same as "+ TestData.PERSON_ID, credits.id == TestData.PERSON_ID);

        assertCastCredits(funcName, credits, false);
        assertCrewCredits(funcName, credits, false);

        for (PersonCastCredit credit : credits.cast) {
            assertNotNull(funcName + "credit is null", credit);
            assertNotNull(funcName + "credit original_title is null", credit.original_title);
            assertNotNull(funcName + "credit title is null", credit.title);
            assertFalse(funcName + "credit title is empty", credit.title.isEmpty());
        }
    }

    @Test
    public void test_tv_credits() {
        final String funcName = "test_tv_credits ";
        PersonCredits credits = getManager().personService().tvCredits(TestData.PERSON_ID, null);

        assertNotNull(funcName + "credits is null", credits);
        assertNotNull(funcName + "credits id is null", credits.id);
        assertTrue(funcName + "credits id is not " + TestData.PERSON_ID, credits.id == TestData.PERSON_ID);
        assertCastCredits(funcName, credits, false);

        for (PersonCastCredit credit : credits.cast) {
            assertNotNull(funcName + "credit episode_count is null", credit.episode_count);
            assertNotNull(funcName + "credit episode_count < 0", credit.episode_count < 0);
            assertNotNull(funcName + "credit name is null", credit.name);
            assertFalse(funcName + "credit name is empty", credit.name.isEmpty());
        }
    }

    @Test
    public void test_combined_credits() {
        final String funcName = "test_combined_credits ";
        PersonCredits credits = getManager().personService().combinedCredits(TestData.PERSON_ID, null);

        assertNotNull(funcName + "credits is null", credits);
        assertNotNull(funcName + "credits id is null", credits.id);
        assertTrue(funcName + "credits id is not " + TestData.PERSON_ID, credits.id == TestData.PERSON_ID);

        assertCastCredits(funcName, credits, true);
        assertCrewCredits(funcName, credits, true);
    }
    
    @Test
    public void test_external_ids() {
        final String funcName = "test_external_ids ";
        PersonIds ids = getManager().personService().externalIds(TestData.PERSON_ID);

        assertNotNull(funcName + "ids is null", ids);
        assertNotNull(funcName + "ids id is null", ids.id);
        assertTrue(funcName + "ids id is not " + TestData.PERSON_ID, ids.id == TestData.PERSON_ID);

        assertNotNull(funcName + "ids IMDB ID is null", ids.imdb_id );
        assertTrue(funcName + "ids IMDB ID is not" + "nm0000093", ids.imdb_id.equals("nm0000093") );

        assertNotNull(funcName + "ids FREEBASE MID is null", ids.freebase_mid );
        assertTrue(funcName + "ids FREEBASE MID is not" + "/m/0c6qh", ids.freebase_mid.equals("/m/0c6qh") );

        assertNotNull(funcName + "ids FREEBASE ID is null", ids.freebase_id );
        assertTrue(funcName + "ids FREEBASE ID was null.", ids.freebase_id.equals("/en/brad_pitt"));

        assertNotNull(funcName + "ids tvrage_id is null", ids.tvrage_id);
        assertTrue(funcName + "ids tvrage_id is not " + 59436, ids.tvrage_id == 59436);
    }
    
    @Test
    public void test_images() {
        final String funcName = "test_images ";
        PersonImages images = getManager().personService().images(TestData.PERSON_ID);
        assertNotNull(funcName + "images is null", images);
        assertNotNull(funcName + "images id is null", images.id);
        assertTrue(funcName + "images id is not the same as "+ TestData.PERSON_ID, images.id == TestData.PERSON_ID);

        for (Image image : images.profiles) {
            assertNotNull(funcName + "image is null", image);
            assertNotNull(funcName + "image file_path is null", image.file_path);
            assertFalse(funcName + "image file_path is empty", image.file_path.isEmpty());
            assertNotNull(funcName + "image width is null", image.width);
            assertNotNull(funcName + "image height is null", image.height);
            assertNotNull(funcName + "image aspect_ratio is null", image.aspect_ratio);
        }
    }
    
    @Test
    public void test_popular() {
        final String funcName = "test_popular ";
        PersonResultsPage popular = getManager().personService().popular(null);

        assertNotNull(funcName + "popular is null", popular);
        assertNotNull(funcName + "popular results is null", popular.results);
        assertTrue(funcName + "popular results size < 1", popular.results.size() > 0);
        assertNotNull(funcName + "popular results get(0) is null", popular.results.get(0));
        assertNotNull(funcName + "popular id is null", popular.results.get(0).id);
        assertNotNull(funcName + "popular name is null", popular.results.get(0).name);
        assertNotNull(funcName + "popular popularity is null", popular.results.get(0).popularity);
        assertNotNull(funcName + "popular profile_path is null", popular.results.get(0).profile_path);
        assertNotNull(funcName + "popular adult is null", popular.results.get(0).adult);

        for (Media media : popular.results.get(0).known_for) {
            assertNotNull(funcName + "media is null", media);
//            assertNotNull(funcName + "media adult is null", media.adult); // This isn't on all entries
            assertNotNull(funcName + "media backdrop_path is null", media.backdrop_path);
            assertNotNull(funcName + "media id is null", media.id);
//            assertNotNull(funcName + "media original_title is null", media.original_title); // This isn't on all entries
//            assertNotNull(funcName + "media release_date is null", media.release_date); // This isn't on all entries
            assertNotNull(funcName + "media poster_path is null", media.poster_path);
            assertNotNull(funcName + "media popularity is null", media.popularity);
            assertTrue(funcName + "media popularity size < 0", media.popularity >= 0);
//            assertNotNull(funcName + "media title is null", media.title);

            assertNotNull(funcName + "media vote_average is null", media.vote_average);
            assertTrue(funcName + "media vote_average is < 1", media.vote_average > 0);
            assertNotNull(funcName + "media vote_count is null", media.vote_count);
            assertTrue(funcName + "media vote_count is < 1", media.vote_count > 0);

            assertNotNull(funcName + "media media_type is null", media.media_type);
        }
    }
    
    @Test
    public void test_latest() throws ParseException {
        final String funcName = "test_latest ";
        Person person = getManager().personService().latest();
        // Latest person might not have a complete TMDb entry, but at should least some basic properties.
        assertNotNull(funcName + "person is null", person);
        assertNotNull(funcName + "person name is null", person.name);
        assertNotNull(funcName + "person id is null", person.id);
    }

    private void assertCastCredits(String funcName, PersonCredits credits, boolean hasMediaType) {
        // assert cast credits
        assertNotNull(funcName + "credits is null", credits);
        assertNotNull(funcName + "credits cast is null", credits.cast);
        assertFalse(funcName + "credits cast is empty", credits.cast.isEmpty());

        for (PersonCastCredit credit : credits.cast) {
            assertNotNull(funcName + "credit is null", credit);
            assertNotNull(funcName + "credit character is null", credit.character); // may be empty

            if (hasMediaType) {
                assertNotNull(funcName + "credit media_type is null", credit.media_type);
                assertFalse(funcName + "credits media_type is empty", credit.media_type.isEmpty());
            }
        }
    }

    private void assertCrewCredits(String funcName, PersonCredits credits, boolean hasMediaType) {
        // assert crew credits
        assertNotNull(funcName + "credits is null", credits);
        assertNotNull(funcName + "credits crew is null", credits.crew);
        assertFalse(funcName + "credits crew is empty", credits.crew.isEmpty());

        for (PersonCrewCredit credit : credits.crew) {
            // may be empty, but should exist
            assertNotNull(funcName + "credit is null", credit);
            assertNotNull(funcName + "credit department is null", credit.department);
            assertNotNull(funcName + "credit job is null", credit.job);

            if (hasMediaType) {
                assertNotNull(funcName + "credit media_type is null", credit.media_type);
                assertFalse(funcName + "credits media_type is empty", credit.media_type.isEmpty());
            }
        }
    }

}
