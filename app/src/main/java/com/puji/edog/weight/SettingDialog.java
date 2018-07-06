package com.puji.edog.weight;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.puji.edog.R;

/**
 * @author WangXuguang
 * @date 2018/5/25.
 */

public class SettingDialog extends Dialog {

    private Context mContext;

    private SettingDialog(Builder builder) {
        super(builder.getContext());
        setContentView(R.layout.layout_setting_dialog);
        if (builder.getOnDismissListener() != null) {
            setOnDismissListener(builder.getOnDismissListener());
        }
        if (!TextUtils.isEmpty(builder.getMsg())) {
            ((TextView) findViewById(R.id.tv_msg)).setText(builder.getMsg());
        }
        if (TextUtils.isEmpty(builder.getConfirmMsg())) {
            ((TextView) findViewById(R.id.tv_confirm)).setVisibility(View.GONE);
        } else {
            TextView view = (TextView) findViewById(R.id.tv_confirm);
            view.setVisibility(View.VISIBLE);
            view.setText(builder.getConfirmMsg());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

    }


    public static class Builder {

        private Context context;

        private String msg;

        private OnDismissListener onDismissListener;

        private String confirmMsg;

        public Builder() {
            context = null;
            msg = null;
        }


        public SettingDialog build() {
            SettingDialog dialog = new SettingDialog(this);
            return dialog;
        }


        public Context getContext() {
            return context;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public String getMsg() {
            return msg;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public String getConfirmMsg() {
            return confirmMsg;
        }

        public Builder setConfirmMsg(String confirmMsg) {
            this.confirmMsg = confirmMsg;
            return this;
        }

        public OnDismissListener getOnDismissListener() {
            return onDismissListener;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }
    }


}
