package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.entities.RegionResultsPage;
import com.b12kab.tmdblibrary.entities.WatchResultsPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WatchService {
    /***
     * Region list
     * @see <a href="https://developers.themoviedb.org/3/watch-providers/get-available-regions">Documentation</a>
     *
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return RegionResultsPage
     */
    @GET("watch/providers/regions")
    Call<RegionResultsPage> regions(@Query("language") String language);

    /***
     * Get movie providers for a region
     * @see <a href="https://developers.themoviedb.org/3/watch-providers/get-movie-providers">Documentation</a>
     *
     * @param watchRegion <em>Optional.</em> ISO-3166-1 code
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return WatchProviders
     */
    @GET("watch/providers/movie")
    Call<WatchResultsPage> regionMovie(@Query("watch_region") String watchRegion,
                                       @Query("language") String language);

    /***
     * Get tv providers for a region
     * @see <a href="https://developers.themoviedb.org/3/watch-providers/get-tv-providers">Documentation</a>
     *
     * @param watchRegion <em>Optional.</em> ISO-3166-1 code
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return WatchProviders
     */
    @GET("watch/providers/tv")
    Call<WatchResultsPage> regionTv(@Query("watch_region") String watchRegion,
                                    @Query("language") String language);
}
