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
 */

package com.b12kab.tmdblibrary.entities;

import java.util.Date;
import java.util.List;

public class TvShowComplete extends TvShow {

    public List<Person> created_by;
    public List<Integer> episode_run_time;
    public List<Genre> genres;
    public String homepage;
    public Boolean in_production;
    public List<String> languages;
    public Date last_air_date;
    public TvEpisode last_episode_to_air;
    public TvEpisode next_episode_to_air;
    public List<Network> networks;
    public Integer number_of_episodes;
    public Integer number_of_seasons;
    public List<ProductionCompany> production_companies;
    public List<ProductionCountry> production_countries;
    public List<TvSeason> seasons;
    public List<SpokenLanguage> spoken_languages;
    public String status;
    public String tagline;
    public String type;
}
