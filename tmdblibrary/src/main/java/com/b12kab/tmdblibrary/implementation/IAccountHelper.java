package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.AccountFavorite;
import com.b12kab.tmdblibrary.entities.AccountResponse;
import com.b12kab.tmdblibrary.entities.AccountState;
import com.b12kab.tmdblibrary.entities.MovieResultsPage;
import com.b12kab.tmdblibrary.entities.RatingValue;
import com.b12kab.tmdblibrary.entities.Status;
import com.b12kab.tmdblibrary.enumerations.AccountFetchType;

import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;

public interface IAccountHelper {
    List<Integer> getAssocHelperTmdbErrorStatusCodes();

    List<Integer> getAssocHelperNonTmdbErrorStatusCodes();

    AccountResponse processAccountInfo(Tmdb tmdb, String session) throws IOException;

    MovieResultsPage processAccountMovieInfo(Tmdb tmdb, AccountFetchType fetchType, String session, int accountId, String sortBy, String language) throws IOException;

    AccountState processAccountMovieInfoDetail(Tmdb tmdb, int movieId, String session, String guestSessionId) throws IOException;

    Status processAccountFavorite(Tmdb tmdb, String session, int accountId, AccountFavorite favorite) throws IOException;

    Status addMovieRating(Tmdb tmdb, int movieId, String session, String guestSessionId, RatingValue ratingValue) throws IOException;

    Status removeMovieRating(Tmdb tmdb, int movieId, String session, String guestSessionId) throws IOException;
}
