/*
 * Copyright 2015 Miguel Teixeira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.CastMember;
import com.b12kab.tmdblibrary.entities.CreditResults;
import com.b12kab.tmdblibrary.entities.CrewMember;
import com.b12kab.tmdblibrary.entities.ExternalIds;
import com.b12kab.tmdblibrary.entities.Image;
import com.b12kab.tmdblibrary.entities.Images;
import com.b12kab.tmdblibrary.entities.TvEpisode;
import com.b12kab.tmdblibrary.entities.Video;
import com.b12kab.tmdblibrary.entities.VideoResults;
import com.b12kab.tmdblibrary.enumerations.AppendToResponseItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

public class TvEpisodesServiceTest extends BaseTestCase {

    @Test
    public void test_episode() {
        final String funcName = "test_episode ";
        TvEpisode episode = null;
        try {
            episode = getManager().tvEpisodesService().episode(TestData.TVSHOW_ID, 1, 1, null, null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertTvEpisode(funcName, episode);
    }

    @Test
    public void test_episode_with_append_to_response() {
        final String funcName = "test_episode_with_append_to_response ";
        TvEpisode episode = null;

        try {
            episode = getManager().tvEpisodesService().episode(TestData.TVSHOW_ID, 1, 1, null,
                    new AppendToResponse(
                            AppendToResponseItem.CREDITS,
                            AppendToResponseItem.IMAGES,
                            AppendToResponseItem.EXTERNAL_IDS)).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertTvEpisode(funcName, episode);

        // credits
        assertNotNull(episode.credits, funcName + "episode credits is null" );
        assertNotNull(episode.credits.getCrew(), funcName + "episode crew is null");
        assertTrue(episode.credits.getCrew().size() > 0,  funcName + "episode crew List < 1");
        assertNotNull(episode.getGuest_stars(), funcName + "episode guest_stars is null");
        assertTrue(episode.getGuest_stars().size() > 0, funcName + "episode guest_stars List < 1");
        assertNotNull(episode.credits.getCast(), funcName + "episode cast is null");
        assertTrue(episode.credits.getCast().size() > 0, funcName + "episode cast List < 1");
        assertCrewCredits(funcName, episode.credits.getCrew());
        assertCastCredits(funcName, episode.credits.getCast());

        // images
        assertNotNull(episode.images, funcName + "episode images is null");
        assertImages(funcName,episode.images.stills);

        // external ids
        assertNotNull(episode.external_ids, funcName + "episode external_ids is null");
//        assertNotNull(episode.external_ids.freebase_id, funcName + "episode freebase_id is null");
        assertNotNull(episode.external_ids.freebase_mid, funcName + "episode freebase_mid is null");
        assertNotNull(episode.external_ids.tvdb_id, funcName + "episode tvdb_id is null");
        assertNotNull(episode.external_ids.imdb_id, funcName + "episode imdb_id is null");
        assertNotNull(episode.external_ids.tvrage_id, funcName + "episode tvrage_id is null");
    }

    @Test
    public void test_credits() {
        final String funcName = "test_credits ";
        CreditResults creditResults = null;

        try {
            creditResults = getManager().tvEpisodesService().credits(TestData.TVSHOW_ID, 1, 1, null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(creditResults, funcName + "credits is null");
        assertNotNull(creditResults.getId(), funcName + "credits id is null");
        assertNotNull(creditResults.getCrew(), funcName + "credits crew is null");
        assertNotNull(creditResults.getCast(), funcName + "credits cast is null");
    }

    @Test
    public void test_externalIds() {
        final String funcName = "test_externalIds ";
        ExternalIds ids = null;
        try {
            ids = getManager().tvEpisodesService().externalIds(TestData.TVSHOW_ID, 1, 1).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(ids, funcName + "ids is null");
        assertNotNull(ids.id, funcName + "ids id is null");
//        assertNotNull(ids.freebase_id, funcName + "ids freebase_id is null");
        assertNotNull(ids.freebase_mid, funcName + "ids freebase_mid is null");
        assertNotNull(ids.tvdb_id, funcName + "ids tvdb_id is null");
        assertNotNull(ids.imdb_id, funcName + "ids imdb_id is null");
        assertNotNull(ids.tvrage_id, funcName + "ids tvrage_id is null");
    }

    @Test
    public void test_images() {
        final String funcName = "test_images ";
        Images images = null;

        try {
            images = getManager().tvEpisodesService().images(TestData.TVSHOW_ID, 1, 1).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(images, funcName + "images is null");
        assertNotNull(images.id, funcName + "images id is null");

        assertImages(funcName, images.stills);
    }

    @Test
    public void test_videos() {
        final String funcName = "test_videos ";
        VideoResults videoResults = null;

        try {
            videoResults = getManager().tvEpisodesService().videos(TestData.TVSHOW_ID, 1, 1, null).execute().body();
        }
        catch (Exception e)
        {
            fail("Exception occurred on " + funcName + ": " + e.toString());
        }

        assertNotNull(videoResults, funcName + "videoResults is null");

        for (Video video : videoResults.results) {
            assertNotNull(video, funcName + "video is null");
            assertNotNull(video.getId(), funcName + "video id is null");
            assertNotNull(video.getIso_639_1(), funcName + "video iso_639_1 is null");
            assertNotNull(video.getKey(), funcName + "video key is null");
            assertNotNull(video.getName(), funcName + "video name is null");
            assertNotNull(video.getSite(), funcName + "video site is null");
            assertNotNull(video.getSize(), funcName + "video size is null");
            assertNotNull(video.getType(), funcName + "video type is null");
        }
    }

    private void assertTvEpisode(String funcName, TvEpisode episode) {
        assertNotNull(episode, funcName + "episode is null");
        assertNotNull(episode.air_date, funcName + "episode air_date is null");
        assertNotNull(episode.episode_number, funcName + "episode episode_number is null");
        assertNotNull(episode.name, funcName + "episode name is null");
        assertNotNull(episode.overview, funcName + "episode overview is null");
        assertNotNull(episode.id, funcName + "episode id is null");
        assertNotNull(episode.season_number, funcName + "episode season_number is null");
        assertEquals(1, (int) episode.season_number, funcName + "episode season_number != 1");
        assertNotNull(episode.still_path, funcName + "episode still_path is null");
        assertNotNull(episode.vote_average, funcName + "episode vote_average is null");
        assertTrue(episode.vote_average > 0, funcName + "episode vote_average is < 1");
        assertNotNull(episode.vote_count, funcName + "episode vote_count is null");
        assertTrue(episode.vote_count > 0, funcName + "episode vote_count is < 1");
    }

    private void assertCrewCredits(String funcName, List<CrewMember> crew) {
        assertNotNull(crew, funcName + "crew is null");
        assertTrue(crew.size() > 0, funcName + "crew List size < 1");

        for (CrewMember member : crew) {
            assertNotNull(member.id, funcName + "member id is null");
            assertNotNull(member.credit_id, funcName + "member credit_id is null");
            assertNotNull(member.name, funcName + "member name is null");
            assertNotNull(member.department, funcName + "member department is null");
            assertNotNull(member.job, funcName + "member job is null");
        }
    }

    private void assertCastCredits(String funcName, List<CastMember> cast) {
        assertNotNull(cast, funcName + "cast is null");
        assertTrue(cast.size() > 0, funcName + "cast List size < 1");

        for (CastMember member : cast) {
            assertNotNull(member.id, funcName + "member id is null" );
            assertNotNull(member.credit_id, funcName + "member credit_id is null");
            assertNotNull(member.name, funcName + "member name is null");
            assertNotNull(member.character, funcName + "member character is null");
            assertNotNull(member.order, funcName + "member order is null");
        }
    }

    private void assertImages(String funcName, List<Image> images){
        assertNotNull(images, funcName + "images is null");
        assertTrue(images.size() > 0, funcName + "images List size < 1");

        for (Image image : images) {
            assertNotNull(image.file_path, funcName + "image file_path is null");
            assertNotNull(image.width, funcName + "image width is null");
            assertNotNull(image.height, funcName + "image height is null");
            assertNotNull(image.aspect_ratio, funcName + "image aspect_ratio is null");
            assertTrue(image.aspect_ratio > 0, funcName + "image aspect_ratio < 1");
            assertNotNull(image.vote_average, funcName + "image vote_average is null");
            assertTrue(image.vote_average >= 0, funcName + "image vote_average < 0");
            assertNotNull(image.vote_count, funcName + "image vote_count is null");
            assertTrue(image.vote_count >= 0, funcName + "image vote_count < 0");
        }
    }
}
