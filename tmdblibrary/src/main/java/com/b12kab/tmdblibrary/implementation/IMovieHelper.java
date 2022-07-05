package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.enumerations.MovieFetchType;

import java.util.List;

public interface IMovieHelper {
    List<Integer> getAssocHelperTmdbErrorStatusCodes();
    List<Integer> getAssocHelperNonTmdbErrorStatusCodes();
    MovieResultsPage processMoviePage(Tmdb tmdb, MovieFetchType fetchType, String language, String region, int page) throws Exception;
}
