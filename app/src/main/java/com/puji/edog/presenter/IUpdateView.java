package com.puji.edog.presenter;

/**
 * @author WangXuguang
 * @date 2018/5/25.
 */

public interface IUpdateView {
    /**
     * 显示服务端app的版本号
     *
     * @param version
     */
    void showServerAppVersion(String version);


    /**
     * 获取服务端版本号失败
     */
    void showServerAppVersionFailed();

    /**
     * 联网过程中出错，或者解析过程中出错
     *
     * @param message
     */
    void showServerAppVersionError(String message);

    /**
     * 显示更新按钮
     */
    void showUpdateButton();
}
