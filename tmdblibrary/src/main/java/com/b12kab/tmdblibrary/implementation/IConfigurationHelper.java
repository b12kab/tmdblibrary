package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.Configuration;
import com.b12kab.tmdblibrary.entities.ConfigurationLanguages;

import java.io.IOException;
import java.util.List;

public interface IConfigurationHelper {
    List<Integer> getAssocHelperTmdbErrorStatusCodes();

    List<Integer> getAssocHelperNonTmdbErrorStatusCodes();

    Configuration processConfigApi(Tmdb tmdb) throws IOException;
    ConfigurationLanguages processConfigLanguage(Tmdb tmdb) throws IOException;
}
