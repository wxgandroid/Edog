package com.puji.edog.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author ZQL
 *
 */
public class AlermUtil {

	/**
	 * 循环周期设置定时
	 *
	 * @param context
	 * @param action
	 *            广播名次
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param cycletime
	 *            循环周期
	 */
	public static void setTimeAlarm(Context context, String action, int hour,
									int minute, long cycletime) {
		Intent intent = new Intent(action);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
		long systemTime = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		// 这里时区需要设置一下，不然会有8个小时的时间差
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		// 选择的定时时间
		long selectTime = calendar.getTimeInMillis();
		// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
		if (systemTime > selectTime) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			selectTime = calendar.getTimeInMillis();
		}
		// 计算现在时间到设定时间的时间差
		long time = selectTime - systemTime;
		firstTime += time;
		AlarmManager mAlarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				firstTime, cycletime, pi);
	}

	/**
	 * 关闭定时
	 *
	 * @param action
	 */
	public static void ClearAlarm(Context context, String action) {
		Intent intent = new Intent(action);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarm = (AlarmManager) context
				.getSystemService(context.ALARM_SERVICE);
		alarm.cancel(sender);
	}
}
