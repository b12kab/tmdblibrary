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
 */

package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.CreditResults;
import com.b12kab.tmdblibrary.entities.ExternalIds;
import com.b12kab.tmdblibrary.entities.Images;
import com.b12kab.tmdblibrary.entities.TvSeason;
import com.b12kab.tmdblibrary.entities.VideoResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TvSeasonsService {

    /**
     * Get the primary information about a TV season by its season number.
     * @see <a href="https://developers.themoviedb.org/3/tv-seasons/get-tv-season-details">Documentation</a>
     *
     * @param showId A themoviedb id.
     * @param seasonNumber season
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param appendToResponse <em>Optional.</em> extra requests to append to the result.
     * @return TvSeason
     */
    @GET("tv/{id}/season/{season_number}")
    Call<TvSeason> season(
            @Path("id") int showId,
            @Path("season_number") int seasonNumber,
            @Query("language") String language,
            @Query("append_to_response") AppendToResponse appendToResponse
    );
    
    /**
     * Get the cast and crew credits for a TV season by season number.
     * @see <a href="https://developers.themoviedb.org/3/tv-seasons/get-tv-season-credits">Documentation</a>
     *
     * @param showId A themoviedb id.
     * @param seasonNumber season
     * @return CreditResults
     */
    @GET("tv/{id}/season/{season_number}/credits")
    Call<CreditResults> credits(
            @Path("id") int showId,
            @Path("season_number") int seasonNumber
    );
    
    /**
     * Get the external ids that we have stored for a TV season by season number.
     * @see <a href="https://developers.themoviedb.org/3/tv-seasons/get-tv-season-external-ids">Documentation</a>
     *
     * @param showId A themoviedb id.
     * @param seasonNumber season
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return ExternalIds
     */
    @GET("tv/{id}/season/{season_number}/external_ids")
    Call<ExternalIds> externalIds(
            @Path("id") int showId,
            @Path("season_number") int seasonNumber,
            @Query("language") String language
    );
    
    /**
     * Get the images (posters) that we have stored for a TV season by season number.
     * @see <a href="https://developers.themoviedb.org/3/tv-seasons/get-tv-season-images">Documentation</a>
     *
     * @param showId A themoviedb id.
     * @param seasonNumber season
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return Images
     */
    @GET("tv/{id}/season/{season_number}/images")
    Call<Images> images(
            @Path("id") int showId,
            @Path("season_number") int seasonNumber,
            @Query("language") String language
    );
    
    /**
     * Get the videos that have been added to a TV season (trailers, teasers, etc...)
     * @see <a href="https://developers.themoviedb.org/3/tv-seasons/get-tv-season-videos">Documentation</a>
     *
     * @param showId A themoviedb id.
     * @param seasonNumber season
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return VideoResults
     */
    @GET("tv/{id}/season/{season_number}/videos")
    Call<VideoResults> videos(
            @Path("id") int showId,
            @Path("season_number") int seasonNumber,
            @Query("language") String language
    );
}
