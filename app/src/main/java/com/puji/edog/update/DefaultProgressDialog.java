package com.puji.edog.update;


import com.puji.edog.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

public class DefaultProgressDialog extends ProgressDialog {

	private String message;
	private TextView define_progress_msg;

	public DefaultProgressDialog(Context context) {
		super(context);
//		message = context.getString(R.string.waiting);
	}

	public DefaultProgressDialog(Context context, String message) {
		super(context);
		this.message = message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.progressdialog);
//		define_progress_msg = (TextView) findViewById(R.id.progress_text);
//		define_progress_msg.setText(message);
//		setCanceledOnTouchOutside(false);
	}

	public void cancelRequest(final Activity context, final int id) {
		setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
//				context.getLoaderManager().destroyLoader(id);
			}
		});
	}

}
