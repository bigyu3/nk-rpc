package com.tianyu.nk.base;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 出参公共响应类
 *
 * @author tianyuguo@yeah.net
 * @date 2024/1/25 10:19
 */
@Data
public class NkRpcResp<T> {

    private Code code;
    private T data;

    public NkRpcResp(Code code, T data) {
        this.code = code;
        this.data = data;
    }

    public Boolean isFail() {
        if (!code.getErrcode().equals("0")) {
            return true;
        }
        return false;
    }

    public static NkRpcResp fail(Code code) {
        return new NkRpcResp(code, null);
    }

    public static <T> NkRpcResp<T> success(T data) {
        return new NkRpcResp<>(SUCCESS, data);
    }

    @AllArgsConstructor
    @Data
    public static class Code {
        private String errcode;
        private String errmsg;
    }

    public static Code SYSTEM_EXCEPTION = new Code("1", "系统异常，请联系我们");
    public static Code NOT_EXIT_ANNOTATION = new Code("2", "接口方法不存在RpcApi注解");
    public static Code PARAM_ERROR = new Code("3", "接口方法参数个数或类型不正确");
    public static Code RETURN_TYPE_ERROR = new Code("4", "接口方法返回类型不正确");
    public static Code PATH_EMPTY = new Code("5", "接口请求地址不能为空");
    public static Code DOMAIN_EMPTY = new Code("6", "接口域名不能为空");
    public static Code GRANT_TYPE_ERROR = new Code("7", "授权类型错误");
    public static Code ACCESS_TOKEN_GET_FAIL = new Code("8", "accessToken获取失败");
    public static Code METHOD_UN_SUPPORT = new Code("9", "WeimobAPI注解的method不支持");
    public static Code GRANT_TYPE_UN_SUPPORT = new Code("10", "授权方式不支持");
    public static Code REDIRECT_URI_EMPTY = new Code("11", "使用授权码时，回调地址参数不能为空");
    public static Code PARAM_EMPTY = new Code("12", "参数不能为空");
    public static Code JSON_PARSE_RESPONSE_FAIL = new Code("13", "json解析rpc响应失败");
    public static Code CODE_OAUTH_CODE_NOT_EMPTY = new Code("14", "授权码授权方式code和redirectUri不能为空");
    public static Code REFRESH_OAUTH_REFRESH_TOKEN_NOT_EMPTY = new Code("15", "刷新令牌授权方式freshToken不能为空");
    public static Code RESPONSE_NOT_EMPTY = new Code("16", "响应不能为空");
    public static Code UN_KNOW_EXCEPTION = new Code("17", "位置异常，请联系我们");
    public static Code SUCCESS = new Code("0", "成功");

}
