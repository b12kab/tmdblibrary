/*
 * Copyright 2012 Uwe Trottmann
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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class MovieResultsPage extends BaseResultsPage {

    private List<MovieAbbreviated> results;

    public List<MovieAbbreviated> getResults() {
        return results;
    }

    public void setResults(List<MovieAbbreviated> results) {
        this.results = results;
    }

    /***
     * Create empty MovieResultsPage
     * @return MovieResultsPage
     */
    @NonNull
    public static MovieResultsPage build() {
        MovieResultsPage movieResultsPage = new MovieResultsPage();
        movieResultsPage.setResults(new ArrayList<>());
        movieResultsPage.setPage(0);
        movieResultsPage.setTotal_results(0);
        movieResultsPage.setTotal_pages(0);

        return movieResultsPage;
    }
}
