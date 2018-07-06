package com.puji.edog.config;

public class Config {

    /**
     * 被电子狗嗅探的程序包名
     */
    public static final String PACKAGE_NAME = "com.hachi.villagemediascreen";

    public static final String SWITCH_ACTION = "com.puji.edog.SWITCH_ACTION";

    public static final String LAUNCH_KEYGUAGRD_ACTION = "com.puji.edog.LAUNCH_KEYGUAND_ACTION";

    public static final String NO_CHOICE_APP = "com.puji.edog.NO_CHOICE_APP";

    public static final int WAIT_REBOOT_TIME = 30;

    /**
     * 无操作监控的时间
     */
    public static final long NO_TOUCH_DELAY_TIME = 1000 * 120;

    /**
     * 下载失败以后，重试下载的间隔时间
     */
    public static final long DOWNLOAD_RETRY_TIME = 15 * 1000 * 60;
    /**
     * 在选择应用弹框界面最多的停留时间
     */
    public static final int PICK_APP_DELAY_TIME = 60 * 1000;

    public static final String ALERM_UPDATE_APP = "com.puji.edog.alerm_update_APP";

    public static final long TIME_MIN = 1000 * 60;// 一分钟
    public static final long TIME_HOUR = 1000 * 60 * 60;// 一个小时 1000 * 60 * 60
    public static final long TIME_DAY = 1000 * 60 * 60 * 24;// 一天

    //App的更新时间(废弃不再使用，可以通过APP内部动态的进行设置更新时间)
//    public static final int ON_TIME_HOUR = 14;// 更新小时 14
//    public static final int ON_TIME_MINUTE = 00;// 更新分钟 00

//     public static final int UPDATE_HOUR = 15;//更新小时
    // public static final int UPDATE_MIN = 30;//更新小时分钟

    public static final String HTTP_URL = "http://apiedog.pujiapp.com";//普及e家 小区大屏升级 正式接口
    /**
     * 媒体机应用更新的url地址
     */
    public static final String NEW_HTTP_URL = "https://hachiapp.pujiapp.com/apiV3/app/media/versionUpgrade/v1.0";
//    public static final String NEW_HTTP_URL = "https://dev6.pujiapp.com/hachiedition/app/media/versionUpgrade/v1.0";



//	public static final String HTTP_URL = "http://fulihome.pujiapp.com/xiaoqudaping/interface/admachine.php";//老的 普及e家 小区大屏升级 正式接口
//	public static final String HTTP_URL = "http://comma.pujiapp.com/interface/comma.php";//逗号大屏升级 正式接口

}
