/*
 * Copyright (c) 2016 by Keith Beatty.
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
 * 2017 Keith Beatty - Refactored
 */

package com.b12kab.tmdblibrary.entities;

import java.util.List;

public class MovieFull extends MovieBase {
    Collection belongs_to_collection;
    int budget;
    List<Genre> genres;
    String homepage;
    String imdb_id;
    List<ProductionCompany> production_companies;
    List<ProductionCountry> production_countries;
    int revenue;
    int runtime;
    List<SpokenLanguage> spoken_languages;
    String status;
    String tagline;

    // Following are used with append_to_response
    public CreditResults credits;
    public Images images;
    public ReleaseResults release_dates;
    public VideoResults videos;
    public ReviewResultsPage reviews;
    public MovieResultsPage similar;
    public AccountState account_states;

    public Collection getBelongs_to_collection() {
        return belongs_to_collection;
    }

    public void setBelongs_to_collection(Collection belongs_to_collection) {
        this.belongs_to_collection = belongs_to_collection;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public List<ProductionCompany> getProduction_companies() {
        return production_companies;
    }

    public void setProduction_companies(List<ProductionCompany> production_companies) {
        this.production_companies = production_companies;
    }

    public List<ProductionCountry> getProduction_countries() {
        return production_countries;
    }

    public void setProduction_countries(List<ProductionCountry> production_countries) {
        this.production_countries = production_countries;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public List<SpokenLanguage> getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(List<SpokenLanguage> spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public CreditResults getCredits() {
        return credits;
    }

    public void setCredits(CreditResults credits) {
        this.credits = credits;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public ReleaseResults getRelease_dates() {
        return release_dates;
    }

    public void setRelease_dates(ReleaseResults release_dates) {
        this.release_dates = release_dates;
    }

    public VideoResults getVideos() {
        return videos;
    }

    public void setVideos(VideoResults videos) {
        this.videos = videos;
    }

    public ReviewResultsPage getReviews() {
        return reviews;
    }

    public void setReviews(ReviewResultsPage reviews) {
        this.reviews = reviews;
    }

    public MovieResultsPage getSimilar() {
        return similar;
    }

    public void setSimilar(MovieResultsPage similar) {
        this.similar = similar;
    }

    public AccountState getAccount_states() {
        return account_states;
    }

    public void setAccount_states(AccountState account_states) {
        this.account_states = account_states;
    }

}
