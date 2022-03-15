package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.AppendToResponse;
import com.b12kab.tmdblibrary.entities.MovieFull;

import java.io.IOException;
import java.util.List;

public interface IMovieDetailHelper {
    List<Integer> getAssocHelperTmdbErrorStatusCodes();

    List<Integer> getAssocHelperNonTmdbErrorStatusCodes();

    MovieFull processMovieDetail(Tmdb tmdb, int movieId, String language, String session, AppendToResponse additionalAppends) throws IOException;

}
