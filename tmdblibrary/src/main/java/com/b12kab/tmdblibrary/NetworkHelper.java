package com.b12kab.tmdblibrary;

import com.b12kab.tmdblibrary.entities.Status;
import com.b12kab.tmdblibrary.exceptions.TmdbException;
import com.b12kab.tmdblibrary.exceptions.TmdbNetworkException;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Response;

public abstract class NetworkHelper {

    public class ExceptionCheckReturn {
        public boolean retry;
        public int retryTime;

        ExceptionCheckReturn() {
            this.retry = false;
            this.retryTime = -1;
        }
    }

    public static class TmdbCodes {
        public static int TMDB_CODE_API_KEY_INVALID = 700;
        public static int TMDB_CODE_ID_OR_PASSWORD_RELATED = 701;
        public static int TMDB_CODE_TOKEN_RELATED = 702;
        public static int TMDB_CODE_SESSION_RELATED = 703;
        public static int TMDB_CODE_MOVIE_ID_RELATED = 704;

        public static int TMDB_CODE_PAGE_RELATED = 710;
        public static int TMDB_CODE_MOVIE_TYPE_RELATED = 711;
        public static int TMDB_CODE_ACCOUNT_RELATED = 712;
        public static int TMDB_CODE_FAVORITE_RELATED = 713;
        public static int TMDB_CODE_RATING_RELATED = 714;

        public static int TMDB_CODE_EXCEPTION_UNKNOWN_CAUSE = 799;

        public static String TMDB_API_ERR_MSG = "API key was not set, call setApiKey() prior to this.";
        public static String TMDB_LOWER_PAGE_ERR_MSG = "Initial page must be at least 1";
        public static String TMDB_INVALID_TYPE_ERR_MSG = "Invalid fetch type";
        public static String TMDB_BAD_SESSION_ERR_MSG = "Populated TMDb session is required";
        public static String TMDB_INVALID_ACCOUNT_ERR_MSG = "Invalid TMDb account";
    }

    /***
     * Process a non-good error result
     *
     * @param response Response<?>
     * @throws TmdbException or TmdbNetworkException - IOException
     */
    public void ProcessError(@NonNull Response<?> response) throws IOException {
        TmdbException tmdbException = new TmdbException();
        boolean hasBody = false;
        int httpStatus = response.code();
        tmdbException.setHttpResponseCode(httpStatus);

        String errorBody = this.ObtainResponseError(response.errorBody());

        if (!StringUtils.isBlank(errorBody)) {
            hasBody = true;
            this.AddErrorInfo(errorBody, tmdbException);
        }

        if (httpStatus == 429) {
            this.CheckRetryTime(response.headers().toMultimap(), tmdbException);
        }

        if (hasBody) {
            throw tmdbException;
        }

        if (StringUtils.isBlank(response.message())) {
            throw new TmdbNetworkException(httpStatus);
        } else {
            throw new TmdbNetworkException(httpStatus, response.message());
        }
    }

    /***
     * Determine if we should retry the call
     *
     * @param tmdbException TmdbException
     * @return ExceptionCheckReturn
     */
    public ExceptionCheckReturn CheckForNetworkRetry(@NonNull TmdbException tmdbException) {
        ExceptionCheckReturn checkReturn = new ExceptionCheckReturn();
        Integer httpStatus = null;

        if (tmdbException.getHttpResponseCode() != null) {
            httpStatus = tmdbException.getHttpResponseCode();

            if (httpStatus != null) {
                switch (httpStatus) {
                    case 429:
                        Integer retryValue = tmdbException.getRetryTime();
                        if (retryValue != null) {
                            checkReturn.retryTime = retryValue;
                            checkReturn.retry = true;
                        }
                        break;

                    case 502:
                        checkReturn.retryTime = 8;
                        checkReturn.retry = true;
                        break;

                    default:
                        break;
                }
            }
        }

        return checkReturn;
    }

    /***
     * Checks for a retry for hitting the rate limit and throw an error.
     * @see <a href="https://developers.themoviedb.org/3/getting-started/request-rate-limiting">Documentation</a>
     * Retrieval on 2022-03-01, the rate limit has been disabled, but it may return.
     *
     * @param headers okhttp3 Headers
     * @param tmdbException TmdbException
     * @throws TmdbException TmdbException
     */
    public void CheckRetryTime(@NonNull Map<String, List<String>> headers, TmdbException tmdbException) throws TmdbException {
        Integer retryTime = null;
        String message = "Retry later";

        if (headers.containsKey("Retry-After")) {
            List<String> headerRetry = headers.get("Retry-After");
            if (headerRetry != null && headerRetry.size() > 0) {
                String headerValue = headerRetry.get(0);
                if (NumberUtils.isCreatable(headerValue)) {
                    retryTime = Integer.parseInt(headerValue);
                    tmdbException.setRetryTime(retryTime);
                    tmdbException.setErrorKind(TmdbException.RetrofitErrorKind.Retry);
                    tmdbException.setMessage(message);
                    throw tmdbException;
                }
            }
        }
    }

    /**
     * Convert the error body into TMDb's status
     *
     * @param errorBody TMDb's error response
     * @param tmdbException TmdbException
     */
    public void AddErrorInfo(@NonNull String errorBody, TmdbException tmdbException) {
        Status status = this.ConvertTmdbError(errorBody);

        if (status != null) {
            tmdbException.setStatus(status);
            tmdbException.setMessage(status.getStatusMessage());
            tmdbException.setErrorKind(TmdbException.RetrofitErrorKind.Tmdb);
        }
    }

    /**
     * Get json string from TMDb response
     *
     * @param errorBody ResponseBody
     * @return JSON from TMDb or null
     * @throws IOException IOException
     */
    @Nullable
    public String ObtainResponseError(ResponseBody errorBody) throws IOException {
        if (errorBody != null) {
            return errorBody.string();
        }

        return null;
    }

    /***
     * Try to convert the error body (hopefully Json) to a TMDb status
     *
     * @param errorBody Error body
     * @return Status
     */
    @Nullable
    public Status ConvertTmdbError(String errorBody)
    {
        if (StringUtils.isAllBlank(errorBody))
            return null;

        Gson gson = TmdbHelper.getGsonBuilder().create();
        Status status = null;
        try {
            status = gson.fromJson(errorBody, Status.class);
        } catch (Exception ignored) {}

        return status;
    }

    /***
     * Create ErrorRetrofitInfo when a failure occurs
     *
     * @param t Exception
     * @return ErrorRetrofitInfo
     */
    public Exception GetFailure(@NonNull Exception t) {
        boolean throwTmdb = false;
        TmdbException tmdbException = new TmdbException();
        String errMsg = t.getMessage() == null ? null : t.getMessage();

        if (t instanceof IOException) {
            tmdbException.setErrorKind(TmdbException.RetrofitErrorKind.IOError);
            if (errMsg == null) errMsg = "IOError";
            tmdbException.setMessage(errMsg);
            throwTmdb = true;
        } else if (t instanceof IllegalStateException) {
            tmdbException.setErrorKind(TmdbException.RetrofitErrorKind.ConversionError);
            if (errMsg == null) errMsg = "IllegalStateException";
            tmdbException.setMessage(errMsg);
            throwTmdb = true;
        }

        if (throwTmdb) {
            return tmdbException;
        } else {
            return t;
        }
    }
}
