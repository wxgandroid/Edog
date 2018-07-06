package com.puji.edog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.puji.edog.config.Config;
import com.puji.edog.service.EdogService;
import com.puji.edog.util.HachiAppListUtils;
import com.puji.edog.util.LaunchCheckAppUtils;
import com.puji.edog.util.SharedPreferenceUtil;
import com.puji.edog.weight.SettingDialog;

import java.lang.ref.WeakReference;
import java.util.List;

public class PickAppDialogAct extends Activity implements OnClickListener, DialogInterface.OnDismissListener {

    private ListView mPickAppListView;
    private List<PackageInfo> mInfos;

    private SharedPreferenceUtil mPreferenceUtil;
    private Button mConfirmButton;
    private ProgressBar mProgressBar;

    public static final String PACKAGE_NAME = "package_name";
    public static final String APP_NAME = "app_name";

    public static final int RESULT_CODE_SUCCESS = 200;
    public static final int RESULT_CODE_ERROR = 101;

    private Handler mHandler;

    private static final int PICK_APP_DELAY_TIME = Config.PICK_APP_DELAY_TIME;

    private static final int PICK_APP_WHAT_ID = 10002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_app_dialog);
        initView();
        mHandler = new AutoPickAppHandler(new WeakReference<Context>(this));
        mPreferenceUtil = new SharedPreferenceUtil(this);
        mInfos = HachiAppListUtils.getInstance().getHachiAppList(this);

        if (mInfos == null || mInfos.size() == 0) {
            mProgressBar.setVisibility(View.GONE);
            mPickAppListView.setVisibility(View.GONE);
            mConfirmButton.setVisibility(View.GONE);
            new SettingDialog.Builder()
                    .setMsg("未找到哈奇应用")
                    .setConfirmMsg("确定")
                    .setContext(this)
                    .setOnDismissListener(this)
                    .build()
                    .show();
        } else {
            mPickAppListView.setAdapter(new PickAppAdapter());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(PICK_APP_WHAT_ID, PICK_APP_DELAY_TIME);
    }

    private void initView() {

        setTitle(R.string.please_choice_app);

        mConfirmButton = (Button) findViewById(R.id.confirm_button);
        mConfirmButton.setOnClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.app_picker_progress_bar);

        mPickAppListView = (ListView) findViewById(R.id.pick_app_list_view);

        mPickAppListView.setEmptyView(mProgressBar);
        mPickAppListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String packageName = mInfos.get(position).packageName;
                String appName = mInfos.get(position).applicationInfo.loadLabel(getPackageManager()).toString();
                int versionCode = mInfos.get(position).versionCode;

                mPreferenceUtil.setPackage(packageName);
                mPreferenceUtil.setAppName(appName);
                mPreferenceUtil.setVersionCode(versionCode);
                Log.i("yxx", "-------" + versionCode + "-------" + mInfos.get(position).versionName);

                Intent intent = new Intent();
                intent.putExtra(PACKAGE_NAME, packageName);
                intent.putExtra(APP_NAME, appName);

                setResult(RESULT_CODE_SUCCESS, intent);
                finish();

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeMessages(PICK_APP_WHAT_ID);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }

    class PickAppAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mInfos.size();
        }

        @Override
        public Object getItem(int position) {

            return mInfos.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;

            if (convertView == null) {

                convertView = getLayoutInflater().inflate(
                        R.layout.pick_app_list_item, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.appIcon = (ImageView) convertView
                        .findViewById(R.id.app_icon);
                viewHolder.appName = (TextView) convertView
                        .findViewById(R.id.pack_app_list_item);
                viewHolder.packageName = (TextView) convertView
                        .findViewById(R.id.package_name_text);

                convertView.setTag(viewHolder);
            }

            viewHolder = (ViewHolder) convertView.getTag();

            viewHolder.appName.setText(mInfos.get(position).applicationInfo
                    .loadLabel(getPackageManager()).toString());

            viewHolder.packageName.setText(mInfos.get(position).packageName);

            try {
                Intent intent = getPackageManager().getLaunchIntentForPackage(
                        mInfos.get(position).packageName);
                if (intent != null) {
                    Drawable drawable = getPackageManager().getActivityIcon(
                            intent);
                    viewHolder.appIcon.setImageDrawable(drawable);
                } else {
                    viewHolder.appIcon.setImageResource(R.drawable.ic_launcher);
                }

            } catch (NameNotFoundException e) {

                e.printStackTrace();
            }
            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_button:
                mPreferenceUtil.saveSwitch(false);
                Intent intent = new Intent(PickAppDialogAct.this, EdogService.class);
                stopService(intent);
                setResult(RESULT_CODE_ERROR);
                finish();
                break;

            default:
                break;
        }

    }

    class ViewHolder {
        TextView appName;
        TextView packageName;
        ImageView appIcon;
    }

    static class AutoPickAppHandler extends Handler {

        private WeakReference<Context> weakReference;

        public AutoPickAppHandler(WeakReference<Context> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PICK_APP_WHAT_ID:
                    LaunchCheckAppUtils.getInstance().startCheckApp(weakReference.get(), true);
                    break;
                default:
                    break;
            }

        }
    }
}
