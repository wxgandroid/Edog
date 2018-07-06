package com.puji.edog.presenter;

import android.util.Log;

import com.puji.edog.http.ResponseHttpback;
import com.puji.edog.http.VolleyRequest;

/**
 * @author WangXuguang
 * @date 2018/5/28.
 */

public class NetWorkPresenter {

    private INetWorkView mView;

    public NetWorkPresenter(INetWorkView view) {
        mView = view;
    }


    /**
     * 检验internet连接是否正常
     */
    public void checkInternet() {
        VolleyRequest.getInstance().RequestHttpString(false, "http://wwww.baidu.com", new ResponseHttpback() {
            @Override
            public void onResponseSuccess(String response) {
                Log.e("TAG", "msg======" + response);
            }

            @Override
            public void onErrorResponse(String errorResponse, int statusCode) {
                if (statusCode == 320) {
                    //服务器错误，联网正常的
                    if (mView != null) {
                        mView.showInternetNormal();
                    }
                } else {
                    if (mView != null) {
                        mView.showInternetError();
                    }
                }

                Log.e("TAG", "error======" + errorResponse);
            }
        }, "NetWorkPresenter");


    }


}
