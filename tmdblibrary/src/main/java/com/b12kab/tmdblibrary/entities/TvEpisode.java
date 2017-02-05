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

package com.b12kab.tmdblibrary.entities;

import java.util.Date;
import java.util.List;

public class TvEpisode {

    public Integer id;

    public Date air_date;
    public List<CrewMember> crew;
    public Integer episode_number;
    public List<CastMember> guest_stars;
    public String name;
    public String overview;
    public String production_code;
    public Integer season_number;
    public String still_path;
    public Double vote_average;
    public Integer vote_count;
    public Images images;
    public ExternalIds external_ids;
    // Note: this if using .tvSeasonsService().season, the data will be in the above crew & guest_stars
    public CreditResults credits;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getAir_date() {
        return air_date;
    }

    public void setAir_date(Date air_date) {
        this.air_date = air_date;
    }

    public List<CrewMember> getCrew() {
        return crew;
    }

    public void setCrew(List<CrewMember> crew) {
        this.crew = crew;
    }

    public Integer getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(Integer episode_number) {
        this.episode_number = episode_number;
    }

    public List<CastMember> getGuest_stars() {
        return guest_stars;
    }

    public void setGuest_stars(List<CastMember> guest_stars) {
        this.guest_stars = guest_stars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getProduction_code() {
        return production_code;
    }

    public void setProduction_code(String production_code) {
        this.production_code = production_code;
    }

    public Integer getSeason_number() {
        return season_number;
    }

    public void setSeason_number(Integer season_number) {
        this.season_number = season_number;
    }

    public String getStill_path() {
        return still_path;
    }

    public void setStill_path(String still_path) {
        this.still_path = still_path;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public ExternalIds getExternal_ids() {
        return external_ids;
    }

    public void setExternal_ids(ExternalIds external_ids) {
        this.external_ids = external_ids;
    }

    public CreditResults getCredits() {
        return credits;
    }

    public void setCredits(CreditResults credits) {
        this.credits = credits;
    }
}
