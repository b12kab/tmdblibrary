package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.entities.FindResults;
import com.b12kab.tmdblibrary.enumerations.ExternalSource;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FindService {

    /**
     * The find method makes it easy to search for objects in our database by an external id.
     * For instance, an IMDB ID. This will search all objects (movies, TV shows and people)
     * and return the results in a single response. TV season and TV episode searches
     * will be supported shortly.
     * @see <a href="https://developers.themoviedb.org/3/find/find-by-id">Documentation</a>
     *
     * @param externalId external Id
     * @param source external source value
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @return FindResults
     */
    @GET("find/{id}")
    Call<FindResults> find(
            @Path("id") String externalId,
            @Query("external_source") ExternalSource source,
            @Query("language") String language
    );
}
