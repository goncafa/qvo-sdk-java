package cl.qvo.net.http;

import cl.qvo.net.http.exception.HttpException;
import lombok.NonNull;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpChannelImpl implements HttpChannel {
    public HttpURLConnection createGetConnection(@NonNull URL endpoint) throws HttpException {
        return createGetConnection(endpoint, null);
    }

    public HttpURLConnection createGetConnection(@NonNull URL endpoint, String query) throws HttpException {
        if (null != query && !query.isEmpty()) {
            String url = endpoint.toString();
            // In some cases, URL can already contain a question mark
            String separator = url.contains("?") ? "&" : "?";
            try {
                endpoint = new URL(String.format("%s%s%s", url, separator, query));
            } catch (MalformedURLException e) {
                throw new IllegalStateException(e);
            }
        }

        return createConnection(endpoint, HttpRequestMethod.GET);
    }
    public HttpURLConnection createPostConnection(@NonNull final URL endpoint)
            throws HttpException {
        return createPostConnection(endpoint, false);
    }

    public HttpURLConnection createPostConnection(@NonNull final URL endpoint,
                                                  final boolean ignoreUncknownSSLCertificates)
            throws HttpException {
        final HttpURLConnection httpURLConnection = createConnection(
                endpoint,
                HttpRequestMethod.POST,
                ignoreUncknownSSLCertificates);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);

        return httpURLConnection;
    }

    public HttpURLConnection createConnection(@NonNull final URL endpoint,
                                       @NonNull final HttpRequestMethod method) throws HttpException {
        return createConnection(endpoint, method, false);
    }

    public HttpURLConnection createConnection(@NonNull final URL endpoint,
                                              @NonNull final HttpRequestMethod method,
                                              final boolean ignoreUncknownSSLCertificates) throws HttpException {
        // if ignoreUncknownSSLCertificates is true then accept
        // any certificate to avoid SSL Exceptions.
        // RECOMMENDED ONLY FOR TEST CASES
        if (ignoreUncknownSSLCertificates) {
            ignoreUnknownSSLCerticates();
        }

        HttpURLConnection httpURLConnection;

        // try to pen http connection
        try {
            final URLConnection urlConnection = endpoint.openConnection();

            // if connection is an http connection assign it
            // else throw an exception
            if (urlConnection instanceof HttpURLConnection)
                httpURLConnection = (HttpURLConnection) urlConnection;
            else
                throw new HttpException("Provided endpoint could not open a valid http connection");
        } catch (IOException e) {
           throw new HttpException(e);
        }

        // we use an enum with valid http methods only so this exception should never be thrown
        try {
            httpURLConnection.setRequestMethod(method.name());
        } catch (ProtocolException e) {
            throw new HttpException(e);
        }

        httpURLConnection.setUseCaches(false);

        return httpURLConnection;
    }

    private void ignoreUnknownSSLCerticates() throws HttpException {
        TrustManager[] trustManagers = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                    throws CertificateException {
                if (getAcceptedIssuers().length > 0) throw new CertificateException();
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                    throws CertificateException {
                if (getAcceptedIssuers().length > 0) throw new CertificateException();
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
        }};

        final SSLContext sc;
        try {
            sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, trustManagers, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new HttpException(e);
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostValid = new HostnameVerifier() {
            @Override
            public boolean verify(String requestedHost, SSLSession sslSession) {
                return requestedHost.equalsIgnoreCase(sslSession.getPeerHost());
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(allHostValid);
    }
}