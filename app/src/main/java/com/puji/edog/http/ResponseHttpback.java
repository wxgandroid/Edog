package com.puji.edog.http;

/**
 * 网络请求返回接口
 *
 * @author ZQL
 */
public interface ResponseHttpback {
    void onResponseSuccess(String response);// 成功

    void onErrorResponse(String errorResponse,int statusCode);// 失败
}
