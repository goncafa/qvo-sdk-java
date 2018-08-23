package cl.qvo.net.http;

import cl.qvo.net.http.exception.HttpChannelException;
import lombok.NonNull;

import java.net.HttpURLConnection;
import java.net.URL;

public interface HttpChannel {
    HttpURLConnection createPostConnection(@NonNull final URL endpoint) throws HttpChannelException;

    HttpURLConnection createPostConnection(@NonNull final URL endpoint,
                                           final boolean ignoreUncknownSSLCertificates) throws HttpChannelException;

    HttpURLConnection createConnection(@NonNull final URL endpoint,
                                       @NonNull final HttpRequestMethod method,
                                       final boolean useCaches,
                                       final boolean doInput,
                                       final boolean doOutput) throws HttpChannelException;

    HttpURLConnection createConnection(@NonNull final URL endpoint,
                                       @NonNull final HttpRequestMethod method,
                                       final boolean useCaches,
                                       final boolean doInput,
                                       final boolean doOutput,
                                       final boolean ignoreUncknownSSLCertificates) throws HttpChannelException;
}
