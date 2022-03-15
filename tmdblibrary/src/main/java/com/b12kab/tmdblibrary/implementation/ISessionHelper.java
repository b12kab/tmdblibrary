package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.Tmdb;

import java.io.IOException;
import java.util.List;

public interface ISessionHelper {
    List<Integer> getAssocHelperTmdbErrorStatusCodes();
    List<Integer> getAssocHelperNonTmdbErrorStatusCodes();
    boolean destroyTmdbSession(Tmdb tmdb, String session) throws IOException;
    String createTmdbSession(Tmdb tmdb, String userId, String passwd) throws IOException;
}
