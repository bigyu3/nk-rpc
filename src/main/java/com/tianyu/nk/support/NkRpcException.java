package com.tianyu.nk.support;

import com.tianyu.nk.base.NkRpcResp;

/**
 * rpc异常处理
 *
 * @author tianyuguo@yeah.net
 * @date 2024/1/25 11:06
 */
public class NkRpcException extends RuntimeException {


    public NkRpcException(String message) {
        super(message);
    }

    public NkRpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public NkRpcException(Throwable cause) {
        super(cause);
    }

    public NkRpcException(NkRpcResp.Code code) {
        super(code.getErrmsg());
    }
}
