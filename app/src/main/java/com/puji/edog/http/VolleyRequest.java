package com.puji.edog.http;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.puji.edog.app.BaseApp;

import org.json.JSONObject;

import java.util.Map;

/**
 * Volley网络请求
 *
 * @author ZQL
 */
public class VolleyRequest {
    private static JsonObjectRequest mJsonObjectRequest;
    private static StringRequest mStringRequest;
    private static int requestTime = 10 * 1000;
    public static VolleyRequest volleyRequest;

    public static VolleyRequest getInstance() {
        if (volleyRequest == null) {
            volleyRequest = new VolleyRequest();
        }
        return volleyRequest;
    }

    /**
     * JSON请求
     *
     * @param isPost           是post 否get
     * @param url              请求地址
     * @param jsonRequest      请求JSON参数
     * @param responseHttpback 返回回调
     * @param tag              Volley队列唯一标识,为了方便取消请求
     */
    public void RequestHttpJSON(boolean isPost, String url,
                                JSONObject jsonRequest, final ResponseHttpback responseHttpback,
                                String tag) {
        mJsonObjectRequest = new JsonObjectRequest(isPost ? Method.POST
                : Method.GET, url, jsonRequest,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO Auto-generated method stub
                        System.out.println(response.toString());
                        responseHttpback.onResponseSuccess(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                System.out.println(error.toString());
                responseHttpback.onErrorResponse(error.toString(), error.networkResponse.statusCode);
            }
        }) {

            @Override
            public RetryPolicy getRetryPolicy() {
                return super.getRetryPolicy();
            }
        };
        mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(requestTime,
                1, 1.0f));
        BaseApp.getInstance().addToRequestQueue(mJsonObjectRequest, tag);
    }

    /**
     * String请求
     *
     * @param isPost           是post 否get
     * @param url              请求地址
     * @param responseHttpback 返回回调
     * @param tag              Volley队列唯一标识,为了方便取消请求
     */
    public void RequestHttpString(boolean isPost, String url,
                                  final ResponseHttpback responseHttpback, String tag) {
        mStringRequest = new StringRequest(isPost ? Method.POST : Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                responseHttpback.onResponseSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                responseHttpback.onErrorResponse(error.toString(), error.networkResponse.statusCode);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub
                return super.getParams();
            }
        };
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(requestTime, 1,
                1.0f));
        BaseApp.getInstance().addToRequestQueue(mStringRequest, tag);
    }
}
