package com.tianyu.nk.properties;

import com.tianyu.nk.support.ApplicationContextUtils;
import com.tianyu.nk.support.RpcHttpTimeOut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.*;

/**
 * todo 梳理是否需要这个类
 *
 * @author tianyuguo@yeah.net
 * @date 2024/1/25 11:31
 */
@Slf4j
public class RpcApiProperties {

    public static final String CONFIG_PREFIX = RpcProperties.CONFIG_PREFIX + ".api.";
    private static final List<String> API_PROPERTY_LIST = new ArrayList();
    private static volatile RpcApiProperties instance = null;
    private Map<String, RpcHttpTimeOut> httpTimeOutMap = new HashMap<>();

    static {
        API_PROPERTY_LIST.add(RpcProperties.DOMAIN_PROPERTY);
        API_PROPERTY_LIST.add(RpcProperties.CONNECT_TIMEOUT_PROPERTY);
        API_PROPERTY_LIST.add(RpcProperties.READ_TIMEOUT_PROPERTY);
        API_PROPERTY_LIST.add(RpcProperties.WRITE_TIMEOUT_PROPERTY);
    }

    private RpcApiProperties() {
        parseConfig();
    }

    public static RpcApiProperties getInstance() {
        if (instance == null) {
            synchronized (RpcApiProperties.class) {
                if (instance == null) {
                    instance = new RpcApiProperties();
                }
            }
        }
        return instance;
    }

    public Map<String, RpcHttpTimeOut> gethttpTimeOutMap() {
        return httpTimeOutMap;
    }

    private void parseConfig() {
        Environment environment = ApplicationContextUtils.getEnvironment();
        StandardEnvironment standardServletEnvironment = (StandardEnvironment) environment;
        Iterator<PropertySource<?>> propertyIterator = standardServletEnvironment.getPropertySources().iterator();

        while (propertyIterator.hasNext()) {
            PropertySource<?> propertySource = propertyIterator.next();
            Object source = propertySource.getSource();
            if (!(source instanceof Map)) {
                continue;
            }

            parseSourceItem(standardServletEnvironment, source);
        }
    }

    private void parseSourceItem(StandardEnvironment standardServletEnvironment, Object source) {
        for (Map.Entry<String, Object> entry : ((Map<String, Object>) source).entrySet()) {
            if (!entry.getKey().startsWith(CONFIG_PREFIX)) {
                continue;
            }

            String temp = entry.getKey().replace(CONFIG_PREFIX, "");
            String[] arr = temp.split("\\.");
            if (arr.length != 3) {
                continue;
            }

            String propertyName = arr[2];
            if (!API_PROPERTY_LIST.contains(propertyName)) {
                continue;
            }

            String httpTimeoutKey = arr[0] + "#" + arr[1];
            RpcHttpTimeOut httpTimeout = getHttpTimeout(httpTimeoutKey);
            String value = standardServletEnvironment.getProperty(entry.getKey());

            setHttpTimeout(httpTimeout, propertyName, value);
        }
    }

    private RpcHttpTimeOut getHttpTimeout(String key) {
        RpcHttpTimeOut httpTimeout;
        if (!httpTimeOutMap.containsKey(key)) {
            httpTimeout = new RpcHttpTimeOut();
            httpTimeOutMap.put(key, httpTimeout);
        } else {
            httpTimeout = httpTimeOutMap.get(key);
        }
        return httpTimeout;
    }

    private void setHttpTimeout(RpcHttpTimeOut httpTimeout, String propertyName, String value) {
        if (RpcProperties.DOMAIN_PROPERTY.equals(propertyName)) {
            httpTimeout.setDomain(value);
        } else if (RpcProperties.CONNECT_TIMEOUT_PROPERTY.equals(propertyName)) {
            try {
                httpTimeout.setConnectTimeout(Long.valueOf(value));
            } catch (NumberFormatException ex) {
                log.info("解析api个性化配置{}异常,ex: {}", propertyName, ex.getMessage());
            }
        } else if (RpcProperties.READ_TIMEOUT_PROPERTY.equals(propertyName)) {
            try {
                httpTimeout.setReadTimeout(Long.valueOf(value));
            } catch (NumberFormatException ex) {
                log.info("解析api个性化配置{}异常,ex: {}", propertyName, ex.getMessage());
            }
        } else if (RpcProperties.WRITE_TIMEOUT_PROPERTY.equals(propertyName)) {
            try {
                httpTimeout.setWriteTimeout(Long.valueOf(value));
            } catch (NumberFormatException ex) {
                log.info("解析api个性化配置{}异常,ex: {}", propertyName, ex.getMessage());
            }
        }
    }


}
