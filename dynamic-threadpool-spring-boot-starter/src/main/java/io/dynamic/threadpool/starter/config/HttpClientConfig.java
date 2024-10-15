package io.dynamic.threadpool.starter.config;

import io.dynamic.threadpool.starter.remote.HttpAgent;
import io.dynamic.threadpool.starter.remote.ServerHttpAgent;
import io.dynamic.threadpool.starter.toolkit.HttpClientUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp3 bean
 */
@Slf4j
public class HttpClientConfig {

    /**
     * 配置 OkHttpClient Bean
     *
     * @return
     */
    @Bean
    public OkHttpClient dtpOkHttpClient() {
        OkHttpClient.Builder build = new OkHttpClient.Builder();
        build.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        supportHttps(build);
        return build.build();
    }

    @Bean
    public HttpClientUtil dtpHttpClientUtil() {
        return new HttpClientUtil();
    }

    @Bean
    public HttpAgent httpAgent(BootstrapProperties properties, HttpClientUtil dtpHttpClientUtil) {
        return new ServerHttpAgent(properties, dtpHttpClientUtil);
    }

    /**
     * 支持 Https
     *
     * @param builder
     */
    @SneakyThrows
    private void supportHttps(OkHttpClient.Builder builder) {
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};

        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        builder.hostnameVerifier((hostname, session) -> true);
    }

}
