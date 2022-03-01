package com.b12kab.tmdblibrary.services;

import com.b12kab.tmdblibrary.entities.Certifications;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CertificationService {
    /**
     * Get the Certifications for Movies.
     */
    @GET("certification/movie/list")
    Call<Certifications> movie();

    /**
     * Get the Certifications for TV Shows.
     */
    @GET("certification/tv/list")
    Call<Certifications> tv();
}
