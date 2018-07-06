package com.puji.edog.presenter;

import com.puji.edog.config.Config;
import com.puji.edog.http.ResponseHttpback;
import com.puji.edog.http.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author WangXuguang
 * @date 2018/5/25.
 * 处理应用版本更新的逻辑类
 */

public class UpdatePresenter {

    private IUpdateView mView;

    /**
     * app应用更新地址
     */
    private String mApkUrl;

    public UpdatePresenter(IUpdateView view) {
        mView = view;
    }


    /**
     * 获取服务端应用的信息
     */
    public void getAppVersionData(final int code) {

        VolleyRequest.getInstance().RequestHttpString(false, Config.NEW_HTTP_URL, new ResponseHttpback() {

            @Override
            public void onResponseSuccess(String response) {
                //请求网络成功
                try {
                    JSONObject json = new JSONObject(response);//2016.12.6后台侯攀琦要求改的
                    if (json.optInt("code") == 1000) {
                        //请求接口成功
                        JSONObject data = json.getJSONObject("data");
                        String apkUrl = data.get("download").toString();
                        String version = data.getString("version").replace(".", "");
                        if (mView != null) {
                            mView.showServerAppVersion(data.getString("version"));
                        }
                        int v = Integer.parseInt(version);
                        if (v > code) {// 可更新版本
                            if(mView!= null) {
                                mView.showUpdateButton();
                            }
                            mApkUrl = apkUrl;
                        }
                    } else {
                        //请求接口失败
                        if (mView != null) {
                            mView.showServerAppVersionFailed();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (mView != null) {
                        mView.showServerAppVersionError(e.getMessage());
                    }
                }
            }

            @Override
            public void onErrorResponse(String errorResponse,int statusCode) {
                if (mView != null) {
                    mView.showServerAppVersionError(errorResponse);
                }
            }
        }, "update"); // 手机端

    }


}
