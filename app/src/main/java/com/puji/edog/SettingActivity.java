package com.puji.edog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.puji.edog.service.EdogService;
import com.puji.edog.weight.SettingDialog;

public class SettingActivity extends BaseActivity implements OnClickListener,
        DialogInterface.OnClickListener, DialogInterface.OnDismissListener {

    private CheckBox mToggleButton;// 电子狗开关按钮
    private EditText mIntervalText;// 用于输入电子狗嗅探时间间隔的文本框
    private Button mSaveSettingBtn;// 保存用户设置的按钮
    private ImageView mFinishBtn;// 退出本设置界面的按钮
    private TextView mPickAppTv;
    private TextView mTitle;

    private static final int REQUEST_CODE = 200;

    private static final String TAG = "SettingActivity";

    private boolean mIsAutoOpenDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        Intent intent = getIntent();
        if (intent != null) {
            mIsAutoOpenDialog = intent.getBooleanExtra("isAutoOpenDialog", false);
        }
        initView();
        //需要自动打开要监测应用的弹框
        if (mIsAutoOpenDialog) {
            showPickAppDialog();
        }
    }

    /**
     * 初始化UI组件
     */
    public void initView() {

        mToggleButton = findViewById(R.id.toogle);
        mToggleButton.setChecked(mPreferenceUtil.isSwitch());

        mIntervalText = (EditText) findViewById(R.id.interval);
        mIntervalText.setText(mPreferenceUtil.getInterval() + "");

        mSaveSettingBtn = (Button) findViewById(R.id.save_setting);
        mSaveSettingBtn.setOnClickListener(this);

        mFinishBtn = (ImageView) findViewById(R.id.finish);
        mFinishBtn.setOnClickListener(this);

        mPickAppTv = (TextView) findViewById(R.id.pick_app_btn);
        mPickAppTv.setOnClickListener(this);

        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.edog_setting);

        if (!TextUtils.isEmpty(mPreferenceUtil.getAppName())) {
            mPickAppTv.setText(mPreferenceUtil.getAppName());
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        mToggleButton.setChecked(mPreferenceUtil.isSwitch());
    }

    /**
     * 保存用户设置
     */
    public void saveSetting() {

        String intervalStr = mIntervalText.getText().toString().trim();

        if (!intervalStr.matches("[0-9]+")) {
            mIntervalText.setHint(getResources().getString(
                    R.string.interval_error));
            return;
        }

        int interval = Integer.parseInt(intervalStr);
        if (interval < 3) {
            mIntervalText.setError(getString(R.string.second_than_three_large));
            mIntervalText.requestFocus();
            return;
        }
        mPreferenceUtil.saveInterval(interval);

        boolean toggle = mToggleButton.isChecked();
        mPreferenceUtil.saveSwitch(toggle);

        Intent intent = new Intent(this, EdogService.class);

        stopService(intent);
        Log.i(TAG, "stopService");
        if (toggle) {
            mPreferenceUtil.setEnable(true);

            startService(intent);
            Log.i(TAG, "startService");

        }

        new SettingDialog.Builder()
                .setOnDismissListener(this)
                .setContext(this)
                .setMsg("设置成功")
                .setConfirmMsg("确定")
                .build()
                .show();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.save_setting:
                saveSetting();
                break;
            case R.id.pick_app_btn:
                showPickAppDialog();
                break;
            default:
                break;
        }

    }

    private void showPickAppDialog() {
        Intent intent = new Intent(this, PickAppDialogAct.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PickAppDialogAct.RESULT_CODE_SUCCESS) {
            mPickAppTv.setText(data.getStringExtra(PickAppDialogAct.APP_NAME));
        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        finish();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }
}
