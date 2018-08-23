package cl.qvo.net.http;

import cl.qvo.net.http.exception.HttpChannelException;
import lombok.NonNull;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpChannelImpl implements HttpChannel {
    public HttpURLConnection createPostConnection(@NonNull final URL endpoint)
            throws HttpChannelException {
        return createConnection(endpoint, HttpRequestMethod.POST, false, true, true);
    }

    public HttpURLConnection createPostConnection(@NonNull final URL endpoint,
                                                  final boolean ignoreUncknownSSLCertificates)
            throws HttpChannelException {
        return createConnection(endpoint, HttpRequestMethod.POST, false, true, true, ignoreUncknownSSLCertificates);
    }

    public HttpURLConnection createConnection(@NonNull final URL endpoint,
                                       @NonNull final HttpRequestMethod method,
                                       final boolean useCaches,
                                       final boolean doInput,
                                       final boolean doOutput) throws HttpChannelException {
        return createConnection(endpoint, HttpRequestMethod.POST, false, true, true, false);
    }

    public HttpURLConnection createConnection(@NonNull final URL endpoint,
                                              @NonNull final HttpRequestMethod method,
                                              final boolean useCaches,
                                              final boolean doInput,
                                              final boolean doOutput,
                                              final boolean ignoreUncknownSSLCertificates) throws HttpChannelException {
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
                throw new HttpChannelException("Provided endpoint could not open a valid http connection");
        } catch (IOException e) {
           throw new HttpChannelException(e);
        }

        httpURLConnection.setUseCaches(useCaches);
        httpURLConnection.setDoInput(doInput);
        httpURLConnection.setDoOutput(doOutput);

        // we use an enum with valid http methods only so this exception should never be thrown
        try {
            httpURLConnection.setRequestMethod(method.name());
        } catch (ProtocolException e) {
            throw new HttpChannelException(e);
        }

        return httpURLConnection;
    }

    private void ignoreUnknownSSLCerticates() throws HttpChannelException {
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
            throw new HttpChannelException(e);
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