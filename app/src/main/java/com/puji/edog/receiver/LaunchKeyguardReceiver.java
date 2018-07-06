package com.puji.edog.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.puji.edog.KeyguardActivity;
import com.puji.edog.config.Config;

public class LaunchKeyguardReceiver extends BroadcastReceiver {
	public LaunchKeyguardReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Config.LAUNCH_KEYGUAGRD_ACTION)) {
			Intent newIntent = new Intent(context, KeyguardActivity.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(newIntent);
		} else if (intent.getAction().equals(Config.NO_CHOICE_APP)) {
//			Intent pickAppIntent = new Intent(context, PickAppDialogAct.class);
//			pickAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(pickAppIntent);
			Intent newIntent = new Intent(context, KeyguardActivity.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(newIntent);

		}
	}
}
