package org.proven.decisions2;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class SecureConnection {
    private OkHttpClient client;

    /**
     * The code provided creates a SecureConnection class with a constructor that sets up a secure connection using an OkHttpClient with a custom trust manager.
     * Here's an explanation of the code:
     *
     * The constructor initializes a trust manager that does not validate certificate chains.
     * It creates a new array of TrustManager objects with a single implementation of X509TrustManager.
     * This implementation overrides the methods for checking client and server trusted certificates and returns an empty array of accepted issuers.
     *
     * The code then installs the all-trusting trust manager by creating an SSLContext instance with the "SSL" protocol and initializing it with the trust manager.
     *
     * Next, it retrieves the SSLSocketFactory from the SSLContext to create an SSL socket factory with the all-trusting manager.
     *
     * An OkHttpClient.Builder is created, and the SSL socket factory and trust manager are set on the builder using the sslSocketFactory()
     * and hostnameVerifier() methods, respectively. The hostnameVerifier() is set to accept all hostnames.
     *
     * Finally, the OkHttpClient instance is built with the configured builder and assigned to the client variable.
     */
    public SecureConnection() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            client = builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //Method returns an client object that can be used to make HTTP requests, but ignores any SSL certificate issues that might arise when establishing an HTTPS connection.
    public OkHttpClient getClient() {
        return client;
    }

}