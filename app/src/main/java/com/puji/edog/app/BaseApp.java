package com.puji.edog.app;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

public class BaseApp extends Application {
	private RequestQueue mRequestQueue;
	private static BaseApp sInstance;
	private Activity activity;
	public static final String TAG = "VolleyPatterns";// 默认标签


	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// initialize the singleton
		sInstance = this;
	}

	public static synchronized BaseApp getInstance() {
		return sInstance;
	}

	// 请求方式
	// JsonObjectRequest — To send and receive JSON Object from the Server
	// JsonArrayRequest — To receive JSON Array from the Server
	// StringRequest — To retrieve response body as String (ideally if you
	// intend to parse the response by yourself)
	/**
	 * 初始化请求队列，访问时创建
	 *
	 * @return
	 */
	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	/**
	 * 设置标签，添加队列
	 *
	 * @param req
	 * @param tag
	 */
	public void addToRequestQueue(Request req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	/**
	 * 设置默认标签，添加队列
	 *
	 * @param req
	 */
	public void addToRequestQueue(Request req) {
		// set the default tag if tag is empty
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	/**
	 * 取消请求
	 *
	 * @param tag
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
			Log.v("log", "-------------" + tag);
		}
		System.out.println("-------------" + tag);
	}
}
