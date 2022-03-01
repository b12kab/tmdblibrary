package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.entities.GenreResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenresService {

    /**
     * Get the list of movie genres.
     * @see <a href="https://developers.themoviedb.org/3/genres/get-movie-list">Documentation</a>
     *
     * @param language <em>Optional.</em> ISO 639-1 code.
     */
    @GET("genre/movie/list")
    Call<GenreResults> movie(
            @Query("language") String language
    );

    /**
     * Get the list of TV genres.
     * @see <a href="https://developers.themoviedb.org/3/genres/get-tv-list>Documentation</a>
     *
     * @param language <em>Optional.</em> ISO 639-1 code.
     */
    @GET("genre/tv/list")
    Call<GenreResults> tv(
            @Query("language") String language
    );

}
