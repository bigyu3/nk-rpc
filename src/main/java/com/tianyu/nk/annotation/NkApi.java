package com.tianyu.nk.annotation;

import java.lang.annotation.*;

/**
 * Api注解
 *
 * @author tianyuguo@yeah.net
 * @date 2024/1/25 10:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface NkApi {

    String domain() default "";

    String path() default "";

    String method() default "";

    /**
     * 单位为秒
     **/
    long connectTimeout() default -1;

    /**
     * 单位为秒
     **/
    long readTimeout() default -1;

    /**
     * 单位为秒
     **/
    long writeTimeout() default -1;


}
