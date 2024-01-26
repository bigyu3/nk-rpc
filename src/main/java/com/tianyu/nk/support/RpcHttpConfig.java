package com.tianyu.nk.support;

import com.tianyu.nk.properties.RpcProperties;
import lombok.Data;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * rpc http配置
 *
 * @author tianyuguo@yeah.net
 * @date 2024/1/26 10:43
 */
@Data
public class RpcHttpConfig {

    private Long readTimeout;

    private Long connectTimeout;

    private Long writeTimeout;

    private String domain;

    private Integer maxRequests;

    private Integer maxRequestsPerHost;

    private Set<Interceptor> interceptors;

    private ConnectionPool connectionPool;

    private RpcHttpConfig(Long readTimeout, Long connectTimeout, Long writeTimeout, String domain, Integer maxRequests, Integer maxRequestsPerHost, Set<Interceptor> interceptors, ConnectionPool connectionPool) {
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
        this.writeTimeout = writeTimeout;
        this.domain = domain;
        this.maxRequests = maxRequests;
        this.maxRequestsPerHost = maxRequestsPerHost;
        this.interceptors = interceptors;
        this.connectionPool = connectionPool;
    }


    public static class RpcHttpConfigBuilder {

        private Long readTimeout = RpcProperties.defaultReadTimeout;

        private Long connectTimeout = RpcProperties.defaultConnectTimeout;

        private Long writeTimeout = RpcProperties.defaultWriteTimeout;

        private String domain = "";

        private Integer maxRequests = 64;

        private Integer maxRequestsPerHost = 5;

        private Set<Interceptor> interceptors = new HashSet<>();

        private ConnectionPool connectionPool = new ConnectionPool();

        public RpcHttpConfigBuilder() {

        }

        public RpcHttpConfigBuilder readTimeout(Long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public RpcHttpConfigBuilder connectTimeout(Long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public RpcHttpConfigBuilder writeTimeout(Long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public RpcHttpConfigBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public RpcHttpConfigBuilder maxRequests(Integer maxRequests) {
            this.maxRequests = maxRequests;
            return this;
        }

        public RpcHttpConfigBuilder maxRequestsPerHost(Integer maxRequestsPerHost) {
            this.maxRequestsPerHost = maxRequestsPerHost;
            return this;
        }

        public RpcHttpConfigBuilder addInterceptor(Interceptor interceptor) {
            this.interceptors.add(interceptor);
            return this;
        }

        public RpcHttpConfigBuilder connectionPool(ConnectionPool connectionPool) {
            this.connectionPool = connectionPool;
            return this;
        }

        public RpcHttpConfig build() {
            return new RpcHttpConfig(readTimeout, connectTimeout, writeTimeout, domain, maxRequests, maxRequestsPerHost, interceptors, connectionPool);
        }

    }

    public int hashCode() {
        return Objects.hash(domain, connectTimeout, readTimeout, writeTimeout);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof RpcHttpConfig)) {
            return false;
        }

        RpcHttpConfig other = (RpcHttpConfig) obj;
        if (this.getDomain().equals(other.getDomain()) && this.getConnectTimeout().equals(other.getConnectTimeout()) && this.getReadTimeout().equals(other.getConnectTimeout()) && this.getWriteTimeout().equals(other.getWriteTimeout())) {
            return true;
        }

        return false;
    }


}
