package com.b12kab.tmdblibrary;

import com.b12kab.tmdblibrary.entities.Status;
import com.b12kab.tmdblibrary.exceptions.TmdbException;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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

    /***
     * Process a non-good error result
     *
     * @param response Response<?>
     * @throws TmdbException
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

        if (!hasBody) {
            tmdbException.setErrorKind(TmdbException.RetrofitErrorKind.Other);
            tmdbException.setMessage("Unknown Error");
            tmdbException.setCode(-1);
        }

        throw tmdbException;
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
        int retryTime;

        if (tmdbException.getHttpResponseCode() != null) {
            httpStatus = tmdbException.getHttpResponseCode();

            if (httpStatus != null) {
                switch (httpStatus) {
                    case 429:
                        Integer retryValue = tmdbException.getRetryTime();
                        if (retryValue != null) {
                            checkReturn.retryTime = retryValue.intValue();
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
     * @throws TmdbException
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
     * @param errorBody TMDb's error response
     * @param tmdbException TmdbException
     */
    public void AddErrorInfo(@NonNull String errorBody, TmdbException tmdbException) {
        Status status = this.ConvertTmdbError(errorBody);

        if (status != null) {
            tmdbException.setCode(status.getStatusCode());
            tmdbException.setMessage(status.getStatusMessage());
        }
    }

    /**
     * Get json string from TMDb response
     * @param errorBody
     * @return
     * @throws IOException
     */
    public String ObtainResponseError(ResponseBody errorBody) throws IOException {
        if (errorBody != null) {
            return errorBody.string();
        }

        return null;
    }

    /***
     * Try to convert the error body (hopefully Json) to a status
     *
     * @param errorBody Error body
     * @return Status
     */
    public Status ConvertTmdbError(String errorBody)
    {
        if (StringUtils.isAllBlank(errorBody))
            return null;

        Gson gson = TmdbHelper.getGsonBuilder().create();
        Status status = null;
        try {
            status = gson.fromJson(errorBody, Status.class);
        } catch (Exception ex) {}

        return status;
    }

    /***
     * Create ErrorRetrofitInfo when a failure occurs
     *
     * @param t Throwable
     * @return ErrorRetrofitInfo
     */
    public TmdbException GetFailure(Throwable t) {
        TmdbException tmdbException = new TmdbException();
        if (t instanceof IOException) {
            tmdbException.setErrorKind(TmdbException.RetrofitErrorKind.Timeout);
            tmdbException.setMessage(t.getCause() == null ? StringUtils.EMPTY : String.valueOf(t.getCause()));
        }
        else if (t instanceof IllegalStateException) {
            tmdbException.setErrorKind(TmdbException.RetrofitErrorKind.ConversionError);
            tmdbException.setMessage(t.getCause() == null ? StringUtils.EMPTY : String.valueOf(t.getCause()));
        } else {
            tmdbException.setErrorKind(TmdbException.RetrofitErrorKind.Other);
            tmdbException.setMessage(t.getCause() == null ? StringUtils.EMPTY : String.valueOf(t.getCause()));
        }

        return tmdbException;
    }
}