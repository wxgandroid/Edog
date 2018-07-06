package com.puji.edog.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Iterator;
import java.util.List;

/**
 * @author WangXuguang
 * @date 2018/5/23.
 * 用来获取Hachi系列app产品的列表的工具类
 */

public class HachiAppListUtils {

    private HachiAppListUtils() {

    }

    private static HachiAppListUtils instance;

    public static HachiAppListUtils getInstance() {
        if (instance == null) {
            synchronized (HachiAppListUtils.class) {
                instance = new HachiAppListUtils();
            }
        }
        return instance;
    }

    /**
     * 获取hachi系列APP 的列表
     *
     * @return
     */
    public List<PackageInfo> getHachiAppList(Context context) {
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> infos = manager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        Iterator<PackageInfo> iterator = infos.iterator();
        while (iterator.hasNext()) {
            PackageInfo packageInfo = iterator.next();
            if (!packageInfo.packageName.contains("com.puji") && !packageInfo.packageName.contains("com.hachi")) {
                iterator.remove();
            }
            if (packageInfo.packageName.contains(context.getPackageName())) {
                iterator.remove();
            }
        }
        return infos;
    }


}
