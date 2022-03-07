/*
 * Copyright 2013 Uwe Trottmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.b12kab.tmdblibrary;

import com.b12kab.tmdblibrary.services.AccountService;
import com.b12kab.tmdblibrary.services.AuthenticationService;
import com.b12kab.tmdblibrary.services.CertificationService;
import com.b12kab.tmdblibrary.services.CollectionService;
import com.b12kab.tmdblibrary.services.CompaniesService;
import com.b12kab.tmdblibrary.services.ConfigurationService;
import com.b12kab.tmdblibrary.services.DiscoverService;
import com.b12kab.tmdblibrary.services.FindService;
import com.b12kab.tmdblibrary.services.GenresService;
import com.b12kab.tmdblibrary.services.MoviesService;
import com.b12kab.tmdblibrary.services.PeopleService;
import com.b12kab.tmdblibrary.services.ReviewsService;
import com.b12kab.tmdblibrary.services.SearchService;
import com.b12kab.tmdblibrary.services.TvEpisodesService;
import com.b12kab.tmdblibrary.services.TvSeasonsService;
import com.b12kab.tmdblibrary.services.TvService;
import com.b12kab.tmdblibrary.services.WatchService;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Helper class for easy usage of the TMDB v3 API using retrofit.
 * <p>
 * Create an instance of this class, {@link #setApiKey(String)} and then call any of the service methods.
 * <p>
 * The service methods take care of constructing the required {@link Retrofit} and creating the service. You
 * can customize the {@link #retrofitBuilder()} by overriding {@link #okHttpClientBuilder()} and setting e.g.
 * your own HTTP client instance or thread executor.
 * <p>
 * Only one {@link Retrofit} instance is created upon the first and re-used for any consequent service
 * method call.
 */
public class Tmdb {

    /**
     * Tmdb API URL.
     */
    public static final String API_URL = "https://api.themoviedb.org/3/";

    /**
     * API key query parameter name.
     */
    public static final String PARAM_API_KEY = "api_key";

    private String apiKey;
    private boolean isDebug;
    private Retrofit retrofit;
    private HttpLoggingInterceptor logging;

    /**
     * Create a new manager instance.
     */
    public Tmdb() {
    }

    /**
     * Set the TMDB API key.
     * <p>
     * The next service method call will trigger a rebuild of the {@link Retrofit}. If you have cached any
     * service instances, get a new one from its service method.
     *
     * @param value Your TMDB API key.
     * @return Tmdb
     */
    public Tmdb setApiKey(String value) {
        this.apiKey = value;
        return this;
    }

    /***
     * Check to see if the TMDb API key is populated
     * @return true - populated, false - not populated
     */
    public boolean checkTmdbAPIKeyPopulated() {
        boolean apiPopulated = false;
        if (apiKey != null) {
            if (!StringUtils.isBlank(apiKey)) {
                apiPopulated = true;
            }
        }

        return apiPopulated;
    }

    /**
     * Set the default log level.
     *
     * @param isDebug If true, the log level is set to {@link HttpLoggingInterceptor.Level#BODY}.
     *                Otherwise {@link HttpLoggingInterceptor.Level#NONE}.
     * @return Tmdb
     */
    public Tmdb setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        if (logging != null) {
            logging.setLevel(isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        }
        return this;
    }

    /**
     * Creates a {@link Retrofit.Builder} that sets the base URL, adds a Gson converter and sets {@link
     * #okHttpClientBuilder()} as its client. <p> Override this to for example set your own call executor.
     *
     * @return
     * @see #okHttpClientBuilder()
     */
    protected Retrofit.Builder retrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(TmdbHelper.getGsonBuilder().create()))
                .client(okHttpClientBuilder().build());
    }

    /**
     * Creates a {@link OkHttpClient.Builder} for usage with {@link #retrofitBuilder()}. Adds interceptors to add auth
     * headers and to log requests. <p> Override this to for example add your own interceptors.
     *
     * @see #retrofitBuilder()
     */
    protected OkHttpClient.Builder okHttpClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request();

                HttpUrl.Builder urlBuilder = request.url().newBuilder();
                urlBuilder.addEncodedQueryParameter(PARAM_API_KEY, apiKey);

                Request.Builder builder = request.newBuilder();
                builder.url(urlBuilder.build());

                return chain.proceed(builder.build());
            }
        });
        if (isDebug) {
            logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        return builder;
    }

    /**
     * Return the current {@link Retrofit} instance. If none exists (first call, auth changed), builds a new one. <p/>
     * <p>When building, sets the base url and a custom client with an {@link Interceptor} which supplies authentication
     * data.
     */
    protected Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = retrofitBuilder().build();
        }
        return retrofit;
    }

    public AuthenticationService authenticationService() {
        return getRetrofit().create(AuthenticationService.class);
    }

    public AccountService accountService() {
        return getRetrofit().create(AccountService.class);
    }

    public CertificationService certificationService() {
        return getRetrofit().create(CertificationService.class);
    }

    public CompaniesService companyService() {
        return getRetrofit().create(CompaniesService.class);
    }

    public ConfigurationService configurationService() {
        return getRetrofit().create(ConfigurationService.class);
    }

    public FindService findService() {
        return getRetrofit().create(FindService.class);
    }

    public GenresService genresService() {
        return getRetrofit().create(GenresService.class);
    }

    public MoviesService moviesService() {
        return getRetrofit().create(MoviesService.class);
    }

    public PeopleService personService() {
        return getRetrofit().create(PeopleService.class);
    }

    public ReviewsService reviewsService() {
        return getRetrofit().create(ReviewsService.class);
    }

    public SearchService searchService() {
        return getRetrofit().create(SearchService.class);
    }

    public TvService tvService() {
        return getRetrofit().create(TvService.class);
    }

    public TvSeasonsService tvSeasonsService() {
        return getRetrofit().create(TvSeasonsService.class);
    }

    public TvEpisodesService tvEpisodesService() {
        return getRetrofit().create(TvEpisodesService.class);
    }

    public DiscoverService discoverService() {
        return getRetrofit().create(DiscoverService.class);
    }

    public CollectionService collectionService() {
        return getRetrofit().create(CollectionService.class);
    }

    public WatchService watchService() {
        return getRetrofit().create(WatchService.class);
    }
}
