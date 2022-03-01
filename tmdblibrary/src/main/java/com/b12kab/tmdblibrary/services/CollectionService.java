/*
 * Copyright 2015 Manuel Laggner
 *
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
 */
package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.Collection;
import com.b12kab.tmdblibrary.entities.Images;
import com.b12kab.tmdblibrary.entities.Timezones;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CollectionService {
    /**
     * Get the basic collection information for a specific collection id.
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param appendToResponse <em>Optional.</em> extra requests to append to the result.
     * @return Collection
     */
    @GET("collection/{id}")
    Call<Collection> summary(@Path("id") int tmdbId, @Query("language") String language, @Query("append_to_response") AppendToResponse appendToResponse);

    /**
     * Get the images (posters and backdrops) for a specific collection id.
     *
     * @param tmdbId TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return Images
     */
    @GET("collection/{id}/images")
    Call<Images> images(@Path("id") int tmdbId, @Query("language") String language);


    /**
     * Get the list of supported timezones on TMDb.
     * @see <a href="https://developers.themoviedb.org/3/configuration/get-timezones">Documentation</a>
     */
    @GET("configuration/timezones")
    Call<Timezones> timezones();
}
