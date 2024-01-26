package com.tianyu.nk.support;

import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 封装okHttp client实现
 *
 * @author tianyuguo@yeah.net
 * @date 2024/1/26 10:13
 */
@Slf4j
public class RpcHttpClient {

    private OkHttpClient okHttpClient = null;
    private static RpcHttpClient instance = null;

    private static final String JSON_MEDIA_TYPE = "application/json; charset=utf-8";
    private static final String MULTIPART_MEDIA_TYPE = "multipart/form-data";

    private static final TrustManager[] TRUST_MANAGERS = new TrustManager[]{new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }};

    public RpcHttpClient(RpcHttpConfig rpcHttpConfig) {
        init(rpcHttpConfig);
    }

    private void init(RpcHttpConfig rpcHttpConfig) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(rpcHttpConfig.getConnectTimeout(), TimeUnit.MILLISECONDS).readTimeout(rpcHttpConfig.getReadTimeout(), TimeUnit.MILLISECONDS).writeTimeout(rpcHttpConfig.getWriteTimeout(), TimeUnit.MILLISECONDS).connectionPool(rpcHttpConfig.getConnectionPool());
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(rpcHttpConfig.getMaxRequests());
        dispatcher.setMaxRequestsPerHost(rpcHttpConfig.getMaxRequestsPerHost());
        builder.dispatcher(dispatcher);

        for (Interceptor interceptor : rpcHttpConfig.getInterceptors()) {
            builder.addInterceptor(interceptor);
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, TRUST_MANAGERS, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException ignored) {
            log.info("init trust ssl", ignored);
        }

        SSLSocketFactory socketFactory = Objects.requireNonNull(sslContext).getSocketFactory();
        builder.sslSocketFactory(socketFactory, (X509TrustManager) TRUST_MANAGERS[0]);
        builder.hostnameVerifier((s, sslSession) -> true);

        this.okHttpClient = builder.build();
    }


    public String post(String url, String body) {
        return post(url, body, null, null);
    }

    public String post(String url, String body, Map<String, String> paramMap) {
        return post(url, body, paramMap, null);
    }

    public String post(String url, String body, Map<String, String> paramMap, Map<String, String> headerMap) {
        log.info("http post[application/json]请求开始，url: {}, reqBody: {}, paramMap: {}, headerMap: {}", url, body, paramMap, headerMap);
        try {
            String reqUrl = buildParam(url, paramMap);
            RequestBody requestBody = RequestBody.create(MediaType.parse(JSON_MEDIA_TYPE), body);

            Request.Builder requestBuilder = new Request.Builder().url(reqUrl).post(requestBody);
            buildHead(requestBuilder, headerMap);

            Request request = requestBuilder.build();
            Response response = okHttpClient.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new NkRpcException(response.code() + " " + response.message());
            }

            String resJsonBody = response.body().string();
            log.info("http post[application/json]请求结束，url: {}, reqBody: {}, paramMap: {}, headerMap: {}, resBody: {}", url, body, paramMap, headerMap, resJsonBody);
            return resJsonBody;
        } catch (NkRpcException e) {
            log.info("http post[application/json]请求结束，url: {}, reqBody: {}, paramMap: {}, headerMap: {}, exception: {}", url, body, paramMap, headerMap, e.getMessage());
            throw e;

        } catch (Exception e) {
            log.info("http post[application/json]请求结束，url: {}, reqBody: {}, paramMap: {}, headerMap: {}, exception: {}", url, body, paramMap, headerMap, e.getMessage());
            log.info("http post[application/json]请求异常", e);
            throw new NkRpcException("发起http post[application/json]请求出现异常", e);
        }
    }

    public String get(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
        log.info("http get请求开始，url: {}, paramMap: {}, headerMap: {}", url, paramMap, headerMap);

        // todo time2024
        return null;
    }


    private void buildHead(Request.Builder requestBuilder, Map<String, String> headerMap) {
        if (headerMap == null || headerMap.size() <= 0) {
            return;
        }

        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
    }

    private String buildParam(String url, Map<String, String> paramMap) {
        if (paramMap == null || paramMap.size() <= 0) {
            return url;
        }
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?");

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);

        return urlBuilder.toString();
    }


}
