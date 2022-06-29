package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.Tmdb;

import java.util.List;

public interface ISessionHelper {
    List<Integer> getAssocHelperTmdbErrorStatusCodes();
    List<Integer> getAssocHelperNonTmdbErrorStatusCodes();
    boolean destroyTmdbSession(Tmdb tmdb, String session) throws Exception;
    String createTmdbSession(Tmdb tmdb, String userId, String passwd) throws Exception;
}
