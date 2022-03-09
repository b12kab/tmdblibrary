/*
 * Copyright 2013 Uwe Trottmann
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
 */

package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.entities.Configuration;
import com.b12kab.tmdblibrary.entities.ConfigurationCountries;
import com.b12kab.tmdblibrary.entities.ConfigurationLanguages;
import com.b12kab.tmdblibrary.entities.Jobs;
import com.b12kab.tmdblibrary.entities.Timezones;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ConfigurationService {

    /**
     * Get the system wide configuration information for images. Some elements of the API require
     * some knowledge of the configuration data which can be found here. The purpose of this is to
     * try and keep the actual API responses as light as possible. It is recommended you store this
     * data within your application and check for updates every so often.<br> <br> To build an image
     * URL, you will need 3 pieces of data. The base_url, size and file_path. Simply combine them
     * all and you will have a fully qualified URL. Here is an example URL:<br> <br> <a href=
     * "http://cf2.imgobject.com/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg"
     * >http://cf2.imgobject.com/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg</a>
     * @see <a href="https://developers.themoviedb.org/3/configuration">Documentation</a>
     *
     * @return Configuration
     */
    @GET("configuration")
    Call<Configuration> configuration();

    /***
     * Get the list of countries (ISO 3166-1 tags) used throughout TMDB.
     * @see <a href="https://developers.themoviedb.org/3/configuration/get-countries">Documentation</a>
     *
     * @return List<ConfigurationCountries>
     */
    @GET("configuration/countries")
    Call<List<ConfigurationCountries>> countries();

    /**
     * Get a list of the jobs and departments we use on TMDb.
     * @see <a href="https://developers.themoviedb.org/3/configuration/get-jobs">Documentation</a>
     *
     * @return List<Jobs>
     */
    @GET("configuration/jobs")
    Call<List<Jobs>> jobs();

    /***
     * Get the list of languages (ISO 639-1 tags) used throughout TMDB.
     * @see <a href="https://developers.themoviedb.org/3/configuration/get-languages">Documentation</a>
     *
     * @return ConfigurationLanguages
     */
    @GET("configuration/languages")
    Call<ConfigurationLanguages> languages();

    /***
     * Get a list of the officially supported translations on TMDB.
     * @see <a href="https://developers.themoviedb.org/3/configuration/get-primary-translations">Documentation</a>
     *
     * @return List<String>
     */
    @GET("configuration/primary_translations")
    Call<List<String>> translations();

    /***
     * Get the list of timezones used throughout TMDB.
     * @see <a href="https://developers.themoviedb.org/3/configuration/get-timezones">Documentation</a>
     *
     * @return Call<List<Timezones>>
     */
    @GET("configuration/timezones")
    Call<Timezones> timezones();
}
