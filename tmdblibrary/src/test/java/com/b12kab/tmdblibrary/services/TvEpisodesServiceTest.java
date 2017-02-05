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
import com.b12kab.tmdblibrary.entities.TvEpisode;
import com.b12kab.tmdblibrary.entities.Images;
import com.b12kab.tmdblibrary.entities.VideoResults;
import com.b12kab.tmdblibrary.entities.VideoResults.Video;
import com.b12kab.tmdblibrary.enumerations.AppendToResponseItem;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TvEpisodesServiceTest extends BaseTestCase {

    @Test
    public void test_episode() {
        final String funcName = "test_episode ";
        TvEpisode episode = getManager().tvEpisodesService().episode(TestData.TVSHOW_ID, 1, 1, null, null);
        assertTvEpisode(funcName, episode);
    }

    @Test
    public void test_episode_with_append_to_response() {
        final String funcName = "test_episode_with_append_to_response ";
        TvEpisode episode = getManager().tvEpisodesService().episode(TestData.TVSHOW_ID, 1, 1, null,
                new AppendToResponse(
                        AppendToResponseItem.CREDITS,
                        AppendToResponseItem.IMAGES,
                        AppendToResponseItem.EXTERNAL_IDS));

        assertTvEpisode(funcName, episode);

        // credits
        assertNotNull(funcName + "episode credits is null", episode.credits);
        assertNotNull(funcName + "episode crew is null", episode.credits.getCrew());
        assertTrue(funcName + "episode crew List < 1", episode.credits.getCrew().size() > 0);
        assertNotNull(funcName + "episode guest_stars is null", episode.credits.getGuest_stars());
        assertTrue(funcName + "episode guest_stars List < 1", episode.credits.getGuest_stars().size() > 0);
        assertNotNull(funcName + "episode cast is null", episode.credits.getCast());
        assertTrue(funcName + "episode cast List < 1", episode.credits.getCast().size() > 0);
        assertCrewCredits(funcName, episode.credits.getCrew());
        assertCastCredits(funcName, episode.credits.getCast());

        // images
        assertNotNull(funcName + "episode images is null", episode.images);
        assertImages(funcName,episode.images.stills);

        // external ids
        assertNotNull(funcName + "episode external_ids is null", episode.external_ids);
//        assertNotNull(funcName + "episode freebase_id is null", episode.external_ids.freebase_id);
        assertNotNull(funcName + "episode freebase_mid is null", episode.external_ids.freebase_mid);
        assertNotNull(funcName + "episode tvdb_id is null", episode.external_ids.tvdb_id);
        assertNotNull(funcName + "episode imdb_id is null", episode.external_ids.imdb_id);
        assertNotNull(funcName + "episode tvrage_id is null", episode.external_ids.tvrage_id);
    }

    @Test
    public void test_credits() {
        final String funcName = "test_credits ";
        CreditResults creditResults = getManager().tvEpisodesService().credits(TestData.TVSHOW_ID, 1, 1);

        assertNotNull(funcName + "credits is null", creditResults);
        assertNotNull(funcName + "credits id is null", creditResults.getId());
        assertNotNull(funcName + "credits crew is null", creditResults.getCrew());
        assertNotNull(funcName + "credits cast is null", creditResults.getCast());
        assertNotNull(funcName + "credits guest_stars is null", creditResults.getGuest_stars());
    }

    @Test
    public void test_externalIds() {
        final String funcName = "test_externalIds ";
        ExternalIds ids = getManager().tvEpisodesService().externalIds(TestData.TVSHOW_ID, 1, 1);

        assertNotNull(funcName + "ids is null", ids);
        assertNotNull(funcName + "ids id is null", ids.id);
//        assertNotNull(funcName + "ids freebase_id is null", ids.freebase_id);
        assertNotNull(funcName + "ids freebase_mid is null", ids.freebase_mid);
        assertNotNull(funcName + "ids tvdb_id is null", ids.tvdb_id);
        assertNotNull(funcName + "ids imdb_id is null", ids.imdb_id);
        assertNotNull(funcName + "ids tvrage_id is null", ids.tvrage_id);
    }

    @Test
    public void test_images() {
        final String funcName = "test_images ";
        Images images = getManager().tvEpisodesService().images(TestData.TVSHOW_ID, 1, 1);
        assertNotNull(funcName + "images is null", images);
        assertNotNull(funcName + "images id is null", images.id);

        assertImages(funcName, images.stills);
    }

    @Test
    public void test_videos() {
        final String funcName = "test_videos ";
        VideoResults videoResults = getManager().tvEpisodesService().videos(TestData.TVSHOW_ID, 1, 1);

        assertNotNull(funcName + "videoResults is null", videoResults);

        for (Video video : videoResults.results) {
            assertNotNull(funcName + "video is null", video);
            assertNotNull(funcName + "video id is null", video.getId());
            assertNotNull(funcName + "video iso_639_1 is null", video.getIso_639_1());
            assertNotNull(funcName + "video key is null", video.getKey());
            assertNotNull(funcName + "video name is null", video.getName());
            assertNotNull(funcName + "video site is null", video.getSite());
            assertNotNull(funcName + "video size is null", video.getSize());
            assertNotNull(funcName + "video type is null", video.getType());
        }
    }

    private void assertTvEpisode(String funcName, TvEpisode episode) {
        assertNotNull(funcName + "episode is null", episode);
        assertNotNull(funcName + "episode air_date is null", episode.air_date);
        assertNotNull(funcName + "episode episode_number is null", episode.episode_number);
        assertNotNull(funcName + "episode name is null", episode.name);
        assertNotNull(funcName + "episode overview is null", episode.overview);
        assertNotNull(funcName + "episode id is null", episode.id);
        assertNotNull(funcName + "episode season_number is null", episode.season_number);
        assertTrue(funcName + "episode season_number != 1", episode.season_number == 1);
        assertNotNull(funcName + "episode still_path is null", episode.still_path);
        assertNotNull(funcName + "episode vote_average is null", episode.vote_average);
        assertTrue(funcName + "episode vote_average is < 1", episode.vote_average > 0);
        assertNotNull(funcName + "episode vote_count is null", episode.vote_count);
        assertTrue(funcName + "episode vote_count is < 1", episode.vote_count > 0);
    }

    private void assertCrewCredits(String funcName, List<CrewMember> crew) {
        assertNotNull(funcName + "crew is null", crew);
        assertTrue(funcName + "crew List size < 1", crew.size() > 0);

        for (CrewMember member : crew) {
            assertNotNull(funcName + "member id is null", member.id);
            assertNotNull(funcName + "member credit_id is null", member.credit_id);
            assertNotNull(funcName + "member name is null", member.name);
            assertNotNull(funcName + "member department is null", member.department);
            assertNotNull(funcName + "member job is null", member.job);
        }
    }

    private void assertCastCredits(String funcName, List<CastMember> cast) {
        assertNotNull(funcName + "cast is null", cast);
        assertTrue(funcName + "cast List size < 1", cast.size() > 0);

        for (CastMember member : cast) {
            assertNotNull(funcName + "member id is null", member.id);
            assertNotNull(funcName + "member credit_id is null", member.credit_id);
            assertNotNull(funcName + "member name is null", member.name);
            assertNotNull(funcName + "member character is null", member.character);
            assertNotNull(funcName + "member order is null", member.order);
        }
    }

    private void assertImages(String funcName, List<Image> images){
        assertNotNull(funcName + "images is null", images);
        assertTrue(funcName + "images List size < 1", images.size() > 0);

        for (Image image : images) {
            assertNotNull(funcName + "image file_path is null", image.file_path);
            assertNotNull(funcName + "image width is null", image.width);
            assertNotNull(funcName + "image height is null", image.height);
            assertNotNull(funcName + "image aspect_ratio is null", image.aspect_ratio);
            assertTrue(funcName + "image aspect_ratio < 1", image.aspect_ratio > 0);
            assertNotNull(funcName + "image vote_average is null", image.vote_average);
            assertTrue(funcName + "image vote_average < 0", image.vote_average >= 0);
            assertNotNull(funcName + "image vote_count is null", image.vote_count);
            assertTrue(funcName + "image vote_count < 0", image.vote_count >= 0);
        }
    }
}
