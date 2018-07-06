package com.puji.edog.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.puji.edog.SettingActivity;

import java.util.List;

/**
 * @author WangXuguang
 * @date 2018/5/23.
 */

public class LaunchCheckAppUtils {

    private static LaunchCheckAppUtils instance;

    private LaunchCheckAppUtils() {

    }

    public static LaunchCheckAppUtils getInstance() {
        if (instance == null) {
            synchronized (LaunchCheckAppUtils.class) {
                instance = new LaunchCheckAppUtils();
            }
        }
        return instance;
    }


    public void startCheckApp(Context context) {
        startCheckApp(context, false);
    }


    /**
     * 启动监测的app,并结束当前页面
     *
     * @param isAutoChoice 是否自动选择默认应用
     */
    public void startCheckApp(Context context, boolean isAutoChoice) {
        if (!isCheckApp(context)) {
            //没有正在监测中的应用
            if (isAutoChoice) {
                //自动选择应用
                autoChoiceDefaultApp(context);
            } else {
                startSettingApp(context);
                return;
            }
        }
        startDefaultApp(context);
    }

    /**
     * 默认选中监测的应用
     *
     * @param context
     */
    public void autoChoiceDefaultApp(Context context) {
        List<PackageInfo> hachiAppList = HachiAppListUtils.getInstance().getHachiAppList(context);
        if (hachiAppList != null && hachiAppList.size() > 0) {
            //默认选中唯一的一个产品
            SharedPreferenceUtil preferenceUtil = new SharedPreferenceUtil(context);
            String packageName = hachiAppList.get(0).packageName;
            String appName = hachiAppList.get(0).applicationInfo.loadLabel(context.getPackageManager()).toString();
            int versionCode = hachiAppList.get(0).versionCode;
            String versionName = hachiAppList.get(0).versionName;
            preferenceUtil.setPackage(packageName);
            preferenceUtil.setAppName(appName);
            preferenceUtil.setVersionCode(versionCode);
            preferenceUtil.setVersionName(versionName);
        }
    }


    /**
     * 跳转到设置监测应用的页面
     */
    public void startSettingApp(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SettingActivity.class);
        intent.putExtra("isAutoOpenDialog", true);
        context.startActivity(intent);
    }

    /**
     * 当前是否有监控中的应用
     * true 有监测中的应用
     * false 没有监测中的应用
     */
    public boolean isCheckApp(Context context) {
        SharedPreferenceUtil preferenceUtil = new SharedPreferenceUtil(context);
        List<PackageInfo> hachiAppList = HachiAppListUtils.getInstance().getHachiAppList(context);
        boolean isOnlyOne = false;
        if (hachiAppList.size() == 1) {
            String packageName = hachiAppList.get(0).packageName;
            String appName = hachiAppList.get(0).applicationInfo.loadLabel(context.getPackageManager()).toString();
            int versionCode = hachiAppList.get(0).versionCode;
            String versionName = hachiAppList.get(0).versionName;
            preferenceUtil.setPackage(packageName);
            preferenceUtil.setAppName(appName);
            preferenceUtil.setVersionCode(versionCode);
            preferenceUtil.setVersionName(versionName);
            isOnlyOne = true;
        }
        return !TextUtils.isEmpty(preferenceUtil.getAppName()) || isOnlyOne;
    }

    /**
     * 开启默认的应用
     */
    public void startDefaultApp(Context context) {
        SharedPreferenceUtil preferenceUtil = new SharedPreferenceUtil(context);
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(preferenceUtil.getPackage());
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

}
