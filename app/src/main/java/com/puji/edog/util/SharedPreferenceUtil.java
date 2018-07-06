package com.puji.edog.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    /**
     * 电子狗开关的key
     */
    private static final String SWITCH = "switch";

    /**
     * 电子狗开关的默认状态
     */
    private static final boolean DEFAULT_SWITCH = true;

    /**
     * 用于保存数据的文件名
     */
    private static final String FILE_NAME = "edog";

    /**
     * 管理员密码所对应的key
     */
    public static final String PWD_KEY = "pwd";

    /**
     * 电子狗嗅探时间间隔所对应的key
     */
    public static final String INTERVAL_KEY = "interval";

    /**
     * 默认的管理员密码
     */
    private static final String DEFAULT_PWD = "123456";

    /**
     * 默认的电子狗嗅探时间间隔
     */
    private static final int DEFAULT_INTERVAL = 3;

    /**
     * 开机时间所对应的key
     */
    public static final String ON_HOUR_KEY = "on_hour";
    public static final String ON_MINUTE_KEY = "on_minute";

    /**
     * 关机时间所对应的key
     */
    public static final String OFF_HOUR_KEY = "off_hour";
    public static final String OFF_MINUTE_KEY = "off_minute";
    /**
     * 重启时间所对应的key
     */
    public static final String REBOOT_HOUR_KEY = "reboot_hour";
    public static final String REBOOT_MINUTE_KEY = "reboot_minute";

    /**
     * 定时开关机设置的类型所对应的key
     */
    public static final String TYPE_KEY = "type";

    /**
     * 暂停电子狗嗅探的key
     */
    public static final String IS_ENABLE_KEY = "is_enable_key";

    /**
     * 被嗅探程序的包名所对应的key
     */
    public static final String PACKAGE_NAME_KEY = "package_name";

    /**
     * 被嗅探程序的包名所对应的version_name
     */
    public static final String PACKAGE_VERSION_NAME = "package_versionname";

    /**
     * 被嗅探程序的包名所对应的version_code
     */
    public static final String PACKAGE_VERSION_CODE = "package_version_code";

    /**
     * 被嗅探程序名所对应的key
     */
    public static final String APP_NAME_KEY = "app_name";

    /**
     * 关闭定时选项
     */
    public static final int CLOSE_SETTING = 0;

    /**
     * 定时开关机的tag
     */
    public static final int AUTO_ON_OFF = 1;

    /**
     * 定时重启的tag
     */
    public static final int AUTO_REBOOT = 2;

    /**
     * 定时关机的tag
     */
    public static final int AUTO_OFF = 3;

    /**
     * 设置应用是否自动更新的key
     */
    private final String IS_AUTO_UPDATE = "is_auto_update";

    /**
     * 存储自动更新时间的小时的key
     */
    private final String UPDATE_TIME_HOUR = "update_time_hour";

    /**
     * 存储自动更新的分钟的key
     */
    private final String UPDATE_TIME_MINUTE = "update_time_minute";


    /**
     * 日志
     */
    public static final String PACKAGE_LOG = "package_log";

    private SharedPreferences mSharedPreferences;

    public SharedPreferenceUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
    }


    public String getLog() {
        return mSharedPreferences.getString(PACKAGE_LOG, null);
    }

    public void setLog(String log) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PACKAGE_LOG, log);
        editor.commit();
    }


    public void setAppName(String appName) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(APP_NAME_KEY, appName);
        editor.commit();
    }

    public String getAppName() {
        return mSharedPreferences.getString(APP_NAME_KEY, "");
    }

    public void setPackage(String packageName) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PACKAGE_NAME_KEY, packageName);
        editor.commit();
    }

    public String getPackage() {
        return mSharedPreferences.getString(PACKAGE_NAME_KEY, "");
    }

    public String getVersionName() {
        return mSharedPreferences.getString(PACKAGE_VERSION_NAME, "");
    }

    public void setVersionName(String versionName) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PACKAGE_VERSION_NAME, versionName);
        editor.commit();
    }

    public int getVersionCode() {
        return mSharedPreferences.getInt(PACKAGE_VERSION_CODE, 0);
    }

    public void setVersionCode(int versioncode) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(PACKAGE_VERSION_CODE, versioncode);
        editor.commit();
    }

    public void setOnHour(int hour) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(ON_HOUR_KEY, hour);
        editor.commit();
    }

    public int getOnHour() {
        return mSharedPreferences.getInt(ON_HOUR_KEY, 0);
    }

    public void setOnMinute(int minute) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(ON_MINUTE_KEY, minute);
        editor.commit();
    }

    public int getOnMinute() {
        return mSharedPreferences.getInt(ON_MINUTE_KEY, 0);
    }

    public void setOffHour(int hour) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(OFF_HOUR_KEY, hour);
        editor.commit();
    }

    public int getOffHour() {
        return mSharedPreferences.getInt(OFF_HOUR_KEY, 0);
    }

    public void setOffMinute(int minute) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(OFF_MINUTE_KEY, minute);
        editor.commit();
    }

    public int getOffMinute() {
        return mSharedPreferences.getInt(OFF_MINUTE_KEY, 0);
    }

    public void setRebootHour(int hour) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(REBOOT_HOUR_KEY, hour);
        editor.commit();
    }

    public int getRebootHour() {
        return mSharedPreferences.getInt(REBOOT_HOUR_KEY, 0);
    }

    public void setRebootMinute(int minute) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(REBOOT_MINUTE_KEY, minute);
        editor.commit();
    }

    public int getRebootMinute() {
        return mSharedPreferences.getInt(REBOOT_MINUTE_KEY, 0);
    }

    public void setType(int type) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(TYPE_KEY, type);
        editor.commit();
    }

    public int getType() {
        return mSharedPreferences.getInt(TYPE_KEY, CLOSE_SETTING);
    }

    public void setEnable(boolean enable) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(IS_ENABLE_KEY, enable);
        editor.commit();
    }

    public boolean isEnable() {
        return mSharedPreferences.getBoolean(IS_ENABLE_KEY, true);
    }

    /**
     * 保存管理员密码到文件中
     *
     * @param pwd
     */
    public void savePassword(String pwd) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PWD_KEY, pwd);
        editor.commit();

    }

    /**
     * 从文件获得管理员密码
     *
     * @return
     */
    public String getPassword() {
        return mSharedPreferences.getString(PWD_KEY, DEFAULT_PWD);
    }

    /**
     * 保存电子狗嗅探时间间隔到文件中
     *
     * @param interval
     */
    public void saveInterval(int interval) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(INTERVAL_KEY, interval);
        editor.commit();
    }

    /**
     * 从文件中获得电子狗嗅探时间间隔
     *
     * @return
     */
    public int getInterval() {
        return mSharedPreferences.getInt(INTERVAL_KEY, DEFAULT_INTERVAL);
    }

    /**
     * 用于保存电子狗的开关状态
     *
     * @param is
     */
    public void saveSwitch(boolean is) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(SWITCH, is);
        editor.commit();
    }

    /**
     * 得到电子狗的开关状态
     *
     * @return
     */
    public boolean isSwitch() {
        return mSharedPreferences.getBoolean(SWITCH, DEFAULT_SWITCH);
    }

    /**
     * 清除文件中保存的数据
     */
    public void clear() {
        mSharedPreferences.edit().clear().commit();
    }

    /**
     * 设置是否开启自动更新
     *
     * @param isAutoUpdate
     */
    public void setAutoUpdate(boolean isAutoUpdate) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putBoolean(IS_AUTO_UPDATE, isAutoUpdate).apply();
    }

    /**
     * 获取是否开启了自动更新
     *
     * @return
     */
    public boolean isAutoUpdate() {
        return mSharedPreferences.getBoolean(IS_AUTO_UPDATE, false);
    }

    /**
     * 设置自动更新的时间
     *
     * @param hour   小时
     * @param minute 分钟
     */
    public void setAutoUpdateTime(int hour, int minute) {
        setUpdateHour(hour);
        setUpdateMinute(minute);
    }

    /**
     * 获取自动更新的小时
     *
     * @return
     */
    public int getUpdateHour() {
        return mSharedPreferences.getInt(UPDATE_TIME_HOUR, 0);
    }

    public void setUpdateHour(int hour) {
        mSharedPreferences.edit().putInt(UPDATE_TIME_HOUR, hour).apply();
    }

    public int getUpdateMinute() {
        return mSharedPreferences.getInt(UPDATE_TIME_MINUTE, 0);
    }

    public void setUpdateMinute(int minute) {
        mSharedPreferences.edit().putInt(UPDATE_TIME_MINUTE, minute).apply();
    }

}
