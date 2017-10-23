/*
 * Copyright 2014 Uwe Trottmann
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
import com.b12kab.tmdblibrary.entities.Images;
import com.b12kab.tmdblibrary.entities.TvEpisode;
import com.b12kab.tmdblibrary.entities.TvSeason;
import com.b12kab.tmdblibrary.entities.Video;
import com.b12kab.tmdblibrary.entities.VideoResults;
import com.b12kab.tmdblibrary.enumerations.AppendToResponseItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class TvSeasonsServiceTest extends BaseTestCase {

    @Test
    public void test_season() {
        final String funcName = "test_season ";
        TvSeason season = getManager().tvSeasonsService().season(TestData.TVSHOW_ID, 1, null, null);
        assertTvSeason(funcName, season);
    }

    @Test
    public void test_season_with_append_to_response() {
        final String funcName = "test_season_with_append_to_response ";
        TvSeason season = getManager().tvSeasonsService().season(TestData.TVSHOW_ID, 1, null,
                new AppendToResponse(AppendToResponseItem.IMAGES,
                        AppendToResponseItem.EXTERNAL_IDS,
                        AppendToResponseItem.CREDITS));
        assertTvSeason(funcName, season);

        // credits
        assertNotNull(funcName + "season is null", season);
        assertNotNull(funcName + "season credits is null", season.credits);

        assertCrewCredits(funcName, season.credits.getCrew());
        assertCastCredits(funcName, season.credits.getCast());

        // images
        assertNotNull(funcName + "season images is null", season.images);
        assertImages(funcName, season.images.posters);

        // external ids
        assertNotNull(funcName + "season external_ids is null", season.external_ids);
        assertNotNull(funcName + "season freebase_id is null", season.external_ids.freebase_id);
        assertNotNull(funcName + "season freebase_mid is null", season.external_ids.freebase_mid);
        assertNotNull(funcName + "season tvdb_id is null", season.external_ids.tvdb_id);
    }
    
    @Test
    public void test_credits() {
        final String funcName = "test_credits ";
        CreditResults creditResults = getManager().tvSeasonsService().credits(TestData.TVSHOW_ID, 1);

        assertNotNull(funcName + "credits is null", creditResults);
        assertNotNull(funcName + "credits id is null", creditResults.getId());

        assertCrewCredits(funcName, creditResults.getCrew());
        assertCastCredits(funcName, creditResults.getCast());
    }
    
    @Test
    public void test_externalIds() {
        final String funcName = "test_externalIds ";
        ExternalIds ids = getManager().tvSeasonsService().externalIds(TestData.TVSHOW_ID, 1, null);

        assertNotNull(funcName + "ids is null", ids);
        assertNotNull(funcName + "ids id is null", ids.id);
        assertNotNull(funcName + "ids freebase_id is null", ids.freebase_id);
        assertNotNull(funcName + "ids freebase_mid is null", ids.freebase_mid);
        assertNotNull(funcName + "ids tvdb_id is null", ids.tvdb_id);
    }
    
    @Test
    public void test_images() {
        final String funcName = "test_images ";
        Images images = getManager().tvSeasonsService().images(TestData.TVSHOW_ID, 1, null);

        assertNotNull(funcName + "images is null", images);
        assertNotNull(funcName + "images id is null", images.id);
        assertImages(funcName, images.posters);
    }
    
    @Test
    public void test_videos() {
        final String funcName = "test_videos ";
        VideoResults videoResults = getManager().tvSeasonsService().videos(TestData.TVSHOW_ID, 1, null);
        assertNotNull(funcName + "videoResults is null", videoResults);
        assertNotNull(funcName + "videoResults results is null", videoResults.results);

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

    private void assertTvSeason(String funcName, TvSeason season) {
        assertNotNull(funcName + "season is null", season);
        assertNotNull(funcName + "season air_date is null", season.air_date);
        assertNotNull(funcName + "season name is null", season.name);
        assertTrue(funcName + "season name is != 'Season 1'",
                season.name.equals("Season 1"));
        assertNotNull(funcName + "season overview is null", season.overview);
        assertNotNull(funcName + "season id is null", season.id);
        assertNotNull(funcName + "season poster_path is null", season.poster_path);
        assertNotNull(funcName + "season season_number is null", season.season_number);
        assertTrue(funcName + "season season_number is != 1", season.season_number == 1);
        assertNotNull(funcName + "season episodes is null", season.episodes);

        for (TvEpisode episode : season.episodes) {
            assertNotNull(funcName + "episode is null", episode);
            assertNotNull(funcName + "episode crew is null", episode.getCrew());

            assertCrewCredits(funcName, episode.getCrew());
            assertCastCredits(funcName, episode.getGuest_stars());

            assertNotNull(funcName + "episode air_date is null", episode.air_date);
            assertNotNull(funcName + "episode episode_number is null", episode.episode_number);
            assertTrue(funcName + "episode episode_number < 1", episode.episode_number > 0);
            assertNotNull(funcName + "episode name is null", episode.name);
            assertNotNull(funcName + "episode overview is null", episode.overview);
            assertNotNull(funcName + "episode id is null", episode.id);
            assertNotNull(funcName + "episode season_number is null", episode.season_number);
            assertNotNull(funcName + "episode still_path is null", episode.still_path);

            assertNotNull(funcName + "episode vote_average is null", episode.vote_average);
            assertTrue(funcName + "episode vote_average is < 1", episode.vote_average > 0);
            assertNotNull(funcName + "episode vote_count is null", episode.vote_count);
            assertTrue(funcName + "episode vote_count is < 1", episode.vote_count > 0);
        }
    }
    
    private void assertCrewCredits(String funcName, List<CrewMember> crew) {
        assertNotNull(funcName + "crew is null", crew);

        for (CrewMember member : crew) {
            assertNotNull(funcName + "member is null", member);
            assertNotNull(funcName + "member id is null", member.id);
            assertNotNull(funcName + "member credit_id is null", member.credit_id);
            assertNotNull(funcName + "member name is null", member.name);
            assertNotNull(funcName + "member department is null", member.department);
            assertNotNull(funcName + "member job is null", member.job);
        }
    }
    
    private void assertCastCredits(String funcName, List<CastMember> cast) {
        assertNotNull(funcName + "cast is null", cast);

        for (CastMember member : cast) {
            assertNotNull(funcName + "member is null", member);
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

        for(Image image : images) {
            assertNotNull(funcName + "image is null", image);
            assertNotNull(funcName + "image file_path is null", image.file_path);
            assertNotNull(funcName + "image width is null", image.width);
            assertNotNull(funcName + "image height is null", image.height);
            assertNotNull(funcName + "image aspect_ratio is null", image.aspect_ratio);
            assertTrue(funcName + "image aspect_ratio < 1", image.aspect_ratio > 0);

            assertNotNull(funcName + "image vote_average is null", image.vote_average);
            assertTrue(funcName + "image vote_average is < 0", image.vote_average >= 0);
            assertNotNull(funcName + "image vote_count is null", image.vote_count);
            assertTrue(funcName + "image vote_count is < 0", image.vote_count >= 0);
        }
    }

}
