package com.b12kab.tmdblibrary.implementation;

import com.b12kab.tmdblibrary.NetworkHelper;
import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.entities.Configuration;
import com.b12kab.tmdblibrary.entities.ConfigurationLanguages;
import com.b12kab.tmdblibrary.exceptions.TmdbException;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Response;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_API_ERR_MSG;
import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_API_KEY_INVALID;

public class ConfigurationHelper extends NetworkHelper {
    /***
     * Get the configuration API
     *
     * @param tmdb Tmdb
     * @return Configuration
     * @throws IOException TmdbException
     */
    @Nullable
    public Configuration processConfigApi(Tmdb tmdb) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            throw new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
        }

        Configuration configuration = this.obtainConfigApi(tmdb);

        return configuration;
    }

    /***
     * Obtain the configuration API
     *
     * @param tmdb Tmdb
     * @return Configuration
     * @throws IOException TmdbException
     */
    @Nullable
    private Configuration obtainConfigApi(@NonNull Tmdb tmdb) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                Configuration apiConfiguration = this.getConfigApi(tmdb);

                if (apiConfiguration != null) {
                    return apiConfiguration;
                }

                // result not success, retry - it can't hurt
                retry = true;
                retryTime = 2;
            } catch (Exception ex) {
                if (ex instanceof TmdbException)
                {
                    TmdbException tmdbException = (TmdbException) ex;
                    NetworkHelper.ExceptionCheckReturn checkReturn = this.CheckForNetworkRetry(tmdbException);
                    if (!checkReturn.retry)
                        throw ex;

                    retry = true;
                    retryTime = checkReturn.retryTime;
                } else {
                    throw ex;
                }
            }

            if (retry) {
                try {
                    Thread.sleep((int) ((retryTime + 0.5) * 1000));
                } catch (InterruptedException e) { }
            } else {
                break;
            }
        }

        return null;
    }

    /***
     * Get the API configuration
     *
     * @param tmdb Tmdb
     * @return Configuration
     * @throws IOException TmdbException
     */
    private Configuration getConfigApi(@NonNull Tmdb tmdb) throws IOException {
        try {

            Call<Configuration> call = tmdb.configurationService().configuration();
            Response<Configuration> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            this.ProcessError(response);
            // this will never return, but the compiler wants a return
            return null;
        } catch (Exception exception) {
            if (exception instanceof TmdbException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }

    /***
     * Get the configuration languages
     *
     * @param tmdb Tmdb
     * @return ConfigurationLanguages
     * @throws IOException TmdbException
     */
    @Nullable
    public ConfigurationLanguages processConfigLanguage(Tmdb tmdb) throws IOException {
        if (tmdb == null) {
            throw new NullPointerException("Tmdb is null");
        }

        if (!tmdb.checkTmdbAPIKeyPopulated()) {
            throw new TmdbException(TMDB_CODE_API_KEY_INVALID, TMDB_API_ERR_MSG);
        }

        ConfigurationLanguages configuration = this.obtainConfigLanguage(tmdb);

        return configuration;
    }

    /***
     * Obtain the configuration languages
     *
     * @param tmdb Tmdb
     * @return ConfigurationLanguages
     * @throws IOException TmdbException
     */
    @Nullable
    private ConfigurationLanguages obtainConfigLanguage(@NonNull Tmdb tmdb) throws IOException {
        boolean retry;
        int retryTime = 0;

        for (int loopCount = 0; loopCount < 3; loopCount++) {
            retry = false;
            try {
                ConfigurationLanguages configLanguages = this.getConfigLanguages(tmdb);

                if (configLanguages != null) {
                    return configLanguages;
                }

                // result not success, retry - it can't hurt
                retry = true;
                retryTime = 2;
            } catch (Exception ex) {
                if (ex instanceof TmdbException)
                {
                    TmdbException tmdbException = (TmdbException) ex;
                    NetworkHelper.ExceptionCheckReturn checkReturn = this.CheckForNetworkRetry(tmdbException);
                    if (!checkReturn.retry)
                        throw ex;

                    retry = true;
                    retryTime = checkReturn.retryTime;
                } else {
                    throw ex;
                }
            }

            if (retry) {
                try {
                    Thread.sleep((int) ((retryTime + 0.5) * 1000));
                } catch (InterruptedException e) { }
            } else {
                break;
            }
        }

        return null;
    }

    /***
     * Get the configuration languages
     *
     * @param tmdb Tmdb
     * @return ConfigurationLanguages
     * @throws IOException TmdbException
     */
    private ConfigurationLanguages getConfigLanguages(@NonNull Tmdb tmdb) throws IOException {
        try {

            Call<ConfigurationLanguages> call = tmdb.configurationService().languages();
            Response<ConfigurationLanguages> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            this.ProcessError(response);
            // this will never return, but the compiler wants a return
            return null;
        } catch (Exception exception) {
            if (exception instanceof TmdbException)
                throw exception;

            throw this.GetFailure(exception);
        }
    }
}