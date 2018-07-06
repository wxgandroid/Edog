package com.puji.edog.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.puji.edog.config.Config;
import com.puji.edog.update.UpdateManager;
import com.puji.edog.util.AlermUtil;
import com.puji.edog.util.SharedPreferenceUtil;

import java.util.Iterator;
import java.util.List;

/**
 * 更新服务
 *
 * @author ZQL
 */
public class UpdateService extends Service {
    private List<PackageInfo> mInfos;// 已安装的普及程序列表
    private SharedPreferenceUtil mPreferenceUtil;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.v("log", "-------UpdateService------------");
        mPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        regReceiver();
        return super.onStartCommand(intent, flags, startId);
    }

    public void regReceiver() {
        //没有开启自动更新，直接返回
        if (!mPreferenceUtil.isAutoUpdate()) {
            return;
        }
        IntentFilter filter = new IntentFilter();
        // filter.addAction(Constants.BR_VISIBLE);
        filter.addAction(Config.ALERM_UPDATE_APP);
        registerReceiver(mAlermReceiver, filter);
        AlermUtil.setTimeAlarm(getApplicationContext(), Config.ALERM_UPDATE_APP, mPreferenceUtil.getUpdateHour(), mPreferenceUtil.getUpdateMinute(), Config.TIME_HOUR);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAlermReceiver != null) {
            getApplicationContext().unregisterReceiver(mAlermReceiver);
        }
        AlermUtil.ClearAlarm(getApplicationContext(), Config.ALERM_UPDATE_APP);
        Intent intent1 = new Intent(this, UpdateService.class);
        startService(intent1);
    }

    /**
     * android:versionCode="100" android:versionName="1.0.0"
     */
    public void getPJadv() {
        PackageManager manager = getPackageManager();
        mInfos = manager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        Iterator<PackageInfo> iterator = mInfos.iterator();
        // 从mInfos中移除所有非普及媒体机产品
        while (iterator.hasNext()) {
            PackageInfo packageInfo = iterator.next();
            if (!packageInfo.packageName.contains(Config.PACKAGE_NAME)) {
                iterator.remove();
            }
        }
        if (mInfos != null && mInfos.size() == 1) {
            String packageName = mInfos.get(0).packageName;
            String appName = mInfos.get(0).applicationInfo.loadLabel(
                    getPackageManager()).toString();

            int versionCode = mInfos.get(0).versionCode;
            String versionName = mInfos.get(0).versionName;
            mPreferenceUtil.setVersionCode(versionCode);
            mPreferenceUtil.setVersionName(versionName);
            mPreferenceUtil.setPackage(packageName);
            mPreferenceUtil.setAppName(appName);
        } else {// 如果未安装设置最小版本号
            mPreferenceUtil.setVersionCode(1);
            mPreferenceUtil.setVersionName("1");
        }
        Log.v("log", "-------UpdateManager------------");
        UpdateManager.getUpdateManager().checkAppUpdate(
                getApplicationContext(), true);

    }

    private BroadcastReceiver mAlermReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            Log.v("log", "-------------------" + action);
            getPJadv();
        }
    };
}
