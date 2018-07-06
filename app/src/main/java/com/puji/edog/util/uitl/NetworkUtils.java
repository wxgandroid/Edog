package com.puji.edog.util.uitl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author WangXuguang
 * @date 2018/5/24.
 */

public class NetworkUtils {

    //没有网络连接
    public static final int NETWORN_NONE = 0;
    //wifi连接
    public static final int NETWORN_WIFI = 1;
    //手机网络数据连接类型
    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_MOBILE = 5;
    public static final int NETWORN_WLAN = 6;

    private static NetworkUtils instance;

    private NetworkUtils() {


    }


    public static NetworkUtils getInstance() {
        if (instance == null) {
            synchronized (NetworkUtils.class) {
                instance = new NetworkUtils();
            }
        }
        return instance;
    }

    /**
     * 返回当前入网的方式
     */
    public int getNetworkMethod(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) {
            return NETWORN_NONE;
        }
        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connMgr.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORN_NONE;
        }

        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            NetworkInfo.State state = networkInfo.getState();
            if (state != null) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORN_WIFI;
                }
            }
        }

        //获取移动数据连接的信息
        NetworkInfo mobileInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileInfo != null) {
            NetworkInfo.State state = mobileInfo.getState();
            String subtypeName = mobileInfo.getSubtypeName();
            if (state != null) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        //如果是2g类型
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORN_2G;
                        //如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORN_3G;
                        //如果是4g类型
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORN_4G;
                        case TelephonyManager.NETWORK_TYPE_IWLAN:
                            return NETWORN_WLAN;
                        default:
                            //中国移动 联通 电信 三种3G制式
                            if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                    || subtypeName.equalsIgnoreCase("WCDMA")
                                    || subtypeName.equalsIgnoreCase("CDMA2000")) {
                                return NETWORN_3G;
                            } else {
                                return NETWORN_MOBILE;
                            }
                    }
                }
            }
        }
        return NETWORN_NONE;
    }

    public String getPhoneIP() {

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }


        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "-";
    }

}
