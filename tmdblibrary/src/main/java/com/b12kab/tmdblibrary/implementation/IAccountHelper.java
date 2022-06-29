package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.AccountFavorite;
import com.b12kab.tmdblibrary.entities.AccountResponse;
import com.b12kab.tmdblibrary.entities.AccountState;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.RatingValue;
import com.b12kab.tmdblibrary.entities.Status;
import com.b12kab.tmdblibrary.enumerations.AccountFetchType;

import java.util.List;

public interface IAccountHelper {
    List<Integer> getAssocHelperTmdbErrorStatusCodes();

    List<Integer> getAssocHelperNonTmdbErrorStatusCodes();

    AccountResponse processAccountInfo(Tmdb tmdb, String session) throws Exception;

    MovieResultsPage processAccountMovieInfo(Tmdb tmdb, AccountFetchType fetchType, String session, int accountId, String sortBy, String language) throws Exception;

    AccountState processAccountMovieInfoDetail(Tmdb tmdb, int movieId, String session, String guestSessionId) throws Exception;

    Status processAccountFavorite(Tmdb tmdb, String session, int accountId, AccountFavorite favorite) throws Exception;

    Status addMovieRating(Tmdb tmdb, int movieId, String session, String guestSessionId, RatingValue ratingValue) throws Exception;

    Status removeMovieRating(Tmdb tmdb, int movieId, String session, String guestSessionId) throws Exception;
}
