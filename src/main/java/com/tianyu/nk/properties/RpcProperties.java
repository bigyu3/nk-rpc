package com.tianyu.nk.properties;

import com.tianyu.nk.support.ApplicationContextUtils;
import com.tianyu.nk.support.DefaultRpcEnvValues;
import com.tianyu.nk.support.ProfileUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * rpc属性配置
 *
 * @author tianyuguo@yeah.net
 * @date 2024/1/25 11:31
 */
@Slf4j
@Data
public class RpcProperties {

    public static final String CONFIG_PREFIX = "nk.rpc"; // 属性配置
    public static final String DOMAIN_PROPERTY = "domain";
    public static final String CONNECT_TIMEOUT_PROPERTY = "connectTimeout";
    public static final String READ_TIMEOUT_PROPERTY = "readTimeout";
    public static final String WRITE_TIMEOUT_PROPERTY = "writeTimeout";
    private static final String MAX_IDLE_CONNECTIONS_PROPERTY = "maxIdleConnections";
    private static final String KEEP_ALIVE_DURATION_PROPERTY = "keepAliveDurationNs";
    public static final String CLASSPATH_SCAN_PROPERTY = "classPathScan";
    public static final String PROPERTY_CUT = ".";

    public static final Long defaultConnectTimeout = 5000L;
    public static final Long defaultReadTimeout = 5000L;
    public static final Long defaultWriteTimeout = 5000L;
    public static final Long INIT_VAL = -1L;
    public static final String POST_HTTP_METHOD = "POST";

    private String domain;
    private Long connectTimeout = defaultConnectTimeout;
    private Long readTimeout = defaultReadTimeout;
    private Long writeTimeout = defaultWriteTimeout;

    private String classPathScan;
    private String defaultDomain;

    private Integer maxIdleConnections = 5;
    private Long keepAliveDurationNs = 5L;

    private static volatile RpcProperties instance = null;

    private RpcProperties() {
        parseConfig();
    }

    public static RpcProperties getInstance() {
        if (instance == null) {
            synchronized (RpcProperties.class) {
                if (instance == null) {
                    instance = new RpcProperties();
                }
            }
        }
        return instance;
    }


    private void parseConfig() {
        Environment environment = ApplicationContextUtils.getEnvironment();
        domain = setAndGetString(environment, domain, DOMAIN_PROPERTY);
        connectTimeout = setAndGetLong(environment, connectTimeout, CONNECT_TIMEOUT_PROPERTY);
        readTimeout = setAndGetLong(environment, readTimeout, CONNECT_TIMEOUT_PROPERTY);
        writeTimeout = setAndGetLong(environment, writeTimeout, WRITE_TIMEOUT_PROPERTY);
        maxIdleConnections = setAndGetInteger(environment, maxIdleConnections, MAX_IDLE_CONNECTIONS_PROPERTY);
        keepAliveDurationNs = setAndGetLong(environment, keepAliveDurationNs, KEEP_ALIVE_DURATION_PROPERTY);

        classPathScan = environment.getProperty(CONFIG_PREFIX + PROPERTY_CUT + CLASSPATH_SCAN_PROPERTY);
        defaultDomain = ProfileUtils.guessFromEnvironment(ApplicationContextUtils.getEnvironment(), new DefaultRpcEnvValues());
        if (StringUtils.isEmpty(domain)) {
            domain = defaultDomain;
        }
    }

    private String setAndGetString(Environment environment, String var, String key) {
        String temp = environment.getProperty(CONFIG_PREFIX + PROPERTY_CUT + key);
        if (!StringUtils.isEmpty(temp)) {
            var = temp;
        }

        return var;
    }

    private Long setAndGetLong(Environment environment, Long var, String key) {
        String temp = environment.getProperty(CONFIG_PREFIX + PROPERTY_CUT + key);
        if (!StringUtils.isEmpty(temp)) {
            try {
                var = Long.valueOf(temp);
            } catch (NumberFormatException ex) {
                log.info("{}格式不对, ex: {}", key, ex.getMessage());
            }
        }
        return var;
    }

    private Integer setAndGetInteger(Environment environment, Integer var, String key) {
        String temp = environment.getProperty(CONFIG_PREFIX + PROPERTY_CUT + key);
        if (!StringUtils.isEmpty(temp)) {
            try {
                var = Integer.valueOf(temp);
            } catch (NumberFormatException ex) {
                log.info("{}格式不对, ex: {}", key, ex.getMessage());
            }
        }
        return var;
    }
}
