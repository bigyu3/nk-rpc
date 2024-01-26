package com.tianyu.nk.support;

import lombok.Data;

/**
 * http超时
 *
 * @author tianyuguo@yeah.net
 * @date 2024/1/26 10:01
 */
@Data
public class RpcHttpTimeOut {

    private String domain;
    private Long connectTimeout;
    private Long readTimeout;
    private Long writeTimeout;

    public RpcHttpTimeOut() {

    }

    public RpcHttpTimeOut(String domain, Long connectTimeout, Long readTimeout, Long writeTimeout) {
        this.domain = domain;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
    }


}
