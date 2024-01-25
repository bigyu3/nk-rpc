package com.tianyu.nk.context;

import lombok.Getter;

/**
 * @author tianyuguo@yeah.net
 * @date 2024/1/24 20:02
 */
@Getter
public class ApiMeta {

    protected String domain;

    protected String path;

    protected String httpMethod;

    protected Long connectTimeout;

    protected Long readTimeout;

    protected Long writeTimeout;

    public ApiMeta() {
    }



}
