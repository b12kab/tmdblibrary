package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.CreditResults;
import com.b12kab.tmdblibrary.entities.ExternalIds;
import com.b12kab.tmdblibrary.entities.Images;
import com.b12kab.tmdblibrary.entities.TvAlternativeTitles;
import com.b12kab.tmdblibrary.entities.TvKeywords;
import com.b12kab.tmdblibrary.entities.TvResultsPage;
import com.b12kab.tmdblibrary.entities.TvShowComplete;
import com.b12kab.tmdblibrary.entities.VideoResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TvService {
    
    /**
     * Get the primary information about a TV series by id.
     * @see <a href="https://developers.themoviedb.org/3/tv/get-tv-details">Documentation</a>
     *
     * @param tmdbId A themoviedb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param appendToResponse <em>Optional.</em> extra requests to append to the result.
     * @return TvShowComplete
     */
    @GET("tv/{id}")
    Call<TvShowComplete> tv(
            @Path("id") int tmdbId,
            @Query("language") String language,
            @Query("append_to_response") AppendToResponse appendToResponse
    );
    
    /**
     * Get the alternative titles for a specific show ID.
     * @see <a href="https://developers.themoviedb.org/3/tv/get-tv-alternative-titles">Documentation</a>
     *
     * @param tmdbId A themoviedb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return TvAlternativeTitles
     */
    @GET("tv/{id}/alternative_titles")
    Call<TvAlternativeTitles> alternativeTitles(
            @Path("id") int tmdbId,
            @Query("language") String language
    );

    /**
     * Get the cast and crew information about a TV series. Just like the website, we pull this information from the
     * last season of the series.
     * @see <a href="https://developers.themoviedb.org/3/tv/get-tv-credits">Documentation</a>
     *
     * @param tmdbId A themoviedb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return CreditResults
     */
    @GET("tv/{id}/credits")
    Call<CreditResults> credits(
            @Path("id") int tmdbId,
            @Query("language") String language
    );

    /**
     * Get the external ids that we have stored for a TV series.
     * @see <a href="https://developers.themoviedb.org/3/tv/get-tv-external-ids">Documentation</a>
     *
     * @param tmdbId A themoviedb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return ExternalIds
     */
    @GET("tv/{id}/external_ids")
    Call<ExternalIds> externalIds(
            @Path("id") int tmdbId,
            @Query("language") String language
    );
    
    /**
     * Get the images (posters and backdrops) for a TV series.
     * @see <a href="https://developers.themoviedb.org/3/tv/get-tv-images">Documentation</a>
     *
     * @param tmdbId A themoviedb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return Images
     */
    @GET("tv/{id}/images")
    Call<Images> images(
            @Path("id") int tmdbId,
            @Query("language") String language
    );
    
    /**
     * Get the plot keywords for a specific TV show id.
     * @see <a href="https://developers.themoviedb.org/3/tv/get-tv-keywords">Documentation</a>
     *
     * @param tmdbId A themoviedb id.
     * @return TvKeywords
     */
    @GET("tv/{id}/keywords")
    Call<TvKeywords> keywords(
            @Path("id") int tmdbId
    );
    
    /**
     * Get the similar TV shows for a specific tv id.
     * @see <a href="https://developers.themoviedb.org/3/tv/get-similar-tv-shows">Documentation</a>
     *
     * @param tmdbId A themoviedb id.
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return TvResultsPage
     */
    @GET("tv/{id}/similar")
    Call<TvResultsPage> similar(
            @Path("id") int tmdbId,
            @Query("page") Integer page,
            @Query("language") String language
    );
    
    /**
     * Get the videos that have been added to a TV series (trailers, opening credits, etc...)
     * @see <a href="https://developers.themoviedb.org/3/tv/get-tv-videos">Documentation</a>
     *
     * @param tmdbId A themoviedb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return VideoResults
     */
    @GET("tv/{id}/videos")
    Call<VideoResults> videos(
            @Path("id") int tmdbId,
            @Query("language") String language
    );
    
    /**
     * Get the latest TV show id.
     * @see <a href="https://developers.themoviedb.org/3/tv/get-latest-tv">Documentation</a>
     *
     * @return TvShowComplete
     */
    @GET("tv/latest")
    Call<TvShowComplete> latest();
    
    /**
     * Get the list of TV shows that are currently on the air.
     * This query looks for any TV show that has an episode with an air date in the next 7 days.
     * @see <a href="https://developers.themoviedb.org/3/tv/get-tv-on-the-air">Documentation</a>
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return TvResultsPage
     */
    @GET("tv/on_the_air")
    Call<TvResultsPage> onTheAir(
            @Query("page") Integer page,
            @Query("language") String language
    );
    
    /**
     * Get the list of TV shows that air today. Without a specified timezone, 
     * this query defaults to EST (Eastern Time UTC-05:00).
     * @see <a href="https://developers.themoviedb.org/3/tv/get-tv-airing-today">Documentation</a>
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return TvResultsPage
     */
    @GET("tv/airing_today")
    Call<TvResultsPage> airingToday(
            @Query("page") Integer page,
            @Query("language") String language
    );
    
    /**
     * Get the list of top rated TV shows. By default, this list will only include TV
     * shows that have 2 or more votes. This list refreshes every day.
     * @see <a href="https://developers.themoviedb.org/3/tv/get-top-rated-tv">Documentation</a>
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return TvResultsPage
     */
    @GET("tv/top_rated")
    Call<TvResultsPage> topRated(
            @Query("page") Integer page,
            @Query("language") String language
    );
    
    /**
     * Get the list of popular TV shows. This list refreshes every day.
     * @see <a href="https://developers.themoviedb.org/3/tv/get-popular-tv-shows">Documentation</a>
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return TvResultsPage
     */
    @GET("tv/popular")
    Call<TvResultsPage> popular(
            @Query("page") Integer page,
            @Query("language") String language
    );
}
