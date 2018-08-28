package cl.qvo.net.http;

import cl.qvo.net.http.exception.HttpException;
import lombok.NonNull;

import java.net.HttpURLConnection;
import java.net.URL;

public interface HttpChannel {
    HttpURLConnection createGetConnection(@NonNull URL endpoint) throws HttpException;

    HttpURLConnection createGetConnection(@NonNull URL endpoint, String query) throws HttpException;

    HttpURLConnection createPostConnection(@NonNull final URL endpoint) throws HttpException;

    HttpURLConnection createPostConnection(@NonNull final URL endpoint,
                                           final boolean ignoreUncknownSSLCertificates) throws HttpException;

    HttpURLConnection createConnection(@NonNull final URL endpoint,
                                       @NonNull final HttpRequestMethod method) throws HttpException;

    HttpURLConnection createConnection(@NonNull final URL endpoint,
                                       @NonNull final HttpRequestMethod method,
                                       final boolean ignoreUncknownSSLCertificates) throws HttpException;
}
