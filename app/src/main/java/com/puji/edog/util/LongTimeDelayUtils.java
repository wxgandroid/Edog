package com.puji.edog.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.puji.edog.config.Config;

/**
 * @author WangXuguang
 * @date 2018/5/23.
 * 处理操作倒计时的类
 */

public class LongTimeDelayUtils {

    private long mDelayTime = Config.NO_TOUCH_DELAY_TIME;

    private static LongTimeDelayUtils instance;

    private TimeDelayHandler mHandler;

    private static final int DELAY_TIME_ARRIVE = 100001;

    private LongTimeDelayUtils() {
    }

    public static LongTimeDelayUtils getInstance() {
        if (instance == null) {
            synchronized (LongTimeDelayUtils.class) {
                instance = new LongTimeDelayUtils();
            }
        }
        return instance;
    }

    /**
     * 开始倒计时
     */
    public void startCountDown(Context context) {
        if (mHandler == null) {
            sendStartMsg(context);
        } else {
            restartCountDown(context);
        }
    }

    public void sendStartMsg(Context context) {
        if (mHandler == null) {
            mHandler = new TimeDelayHandler();
        }
        Message msg = Message.obtain();
        msg.what = DELAY_TIME_ARRIVE;
        msg.obj = context;
        mHandler.sendMessageDelayed(msg, mDelayTime);
    }

    /**
     * 重置倒计时
     */
    public void restartCountDown(Context context) {
        mHandler.removeMessages(DELAY_TIME_ARRIVE);
        sendStartMsg(context);
    }

    /**
     * 停止倒计时
     */
    public void stopCountDown() {
        mHandler.removeMessages(DELAY_TIME_ARRIVE);
    }

    static class TimeDelayHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DELAY_TIME_ARRIVE:
                    //延迟的时间到了,开启监测的应用
                    LaunchCheckAppUtils.getInstance().startCheckApp((Context) msg.obj);
                    break;
                default:
                    break;
            }


        }
    }


}
