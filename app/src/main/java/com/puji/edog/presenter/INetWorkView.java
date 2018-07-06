package com.puji.edog.presenter;

/**
 * @author WangXuguang
 * @date 2018/5/28.
 */

public interface INetWorkView {

    /**
     * 请求百度，获得了百度服务器返回的错误信息
     */
    void showInternetNormal();

    /**
     * internet连接错误
     */
    void showInternetError();
}
