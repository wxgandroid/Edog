package com.puji.edog.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.puji.edog.config.Config;
import com.puji.edog.update.UpdateManager;
import com.puji.edog.util.SharedPreferenceUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EdogService extends Service {

    private String mCurrentPackageName;
    private ActivityManager mActivityManager;
    private SharedPreferenceUtil mPreferenceUtil;
    private ScheduledExecutorService mExecutorService;

    private static final int SUCCESS = 1;
    private static final int NO_CHOICE_APP = 2;

    private static final String TAG = "EdogService";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    Intent intent = new Intent(Config.LAUNCH_KEYGUAGRD_ACTION);
                    sendBroadcast(intent);
                    break;
                case NO_CHOICE_APP:
                    Intent noChoiceAppIntent = new Intent(Config.NO_CHOICE_APP);
                    sendBroadcast(noChoiceAppIntent);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        getPackageManager();
        mCurrentPackageName = getPackageName();
        mPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("log", "--------onStartCommand-------");
        mExecutorService = Executors.newScheduledThreadPool(1);
        mExecutorService.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {

                if (!mPreferenceUtil.isSwitch()) {
                    stopSelf();
                }

                // 得到当前正在前台运行的应用程序包名
                String runingBagName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();

                // 如果当前所要监听的程序正在前台运行则不用执行后续代码
                if (runingBagName.equals(mPreferenceUtil.getPackage())) {
                    mPreferenceUtil.setEnable(true);
                    return;
                }

                // 如果当前在前台运行的程序是电子狗则不用执行后续代码
                if (runingBagName.equals(mCurrentPackageName)) {
                    return;
                }
                // 所要监听的程序不在前台运行，切换到该程序
                if (TextUtils.isEmpty(mPreferenceUtil.getPackage())) {
                    mHandler.sendEmptyMessage(NO_CHOICE_APP);
                } else if (mPreferenceUtil.isEnable()) {
                    mHandler.sendEmptyMessage(SUCCESS);
                }

            }
        }, 0, mPreferenceUtil.getInterval(), TimeUnit.SECONDS);

        return super.onStartCommand(intent, START_REDELIVER_INTENT, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutorService.shutdown();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
