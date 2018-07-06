package com.puji.edog;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.puji.edog.presenter.IUpdateView;
import com.puji.edog.presenter.UpdatePresenter;
import com.puji.edog.service.UpdateService;
import com.puji.edog.update.UpdateManager;
import com.puji.edog.util.uitl.StringUtils;
import com.puji.edog.weight.SettingDialog;

/**
 * 更新设置的页面
 */
public class UpdateSettingActivity extends Base2Activity implements View.OnClickListener, IUpdateView, DialogInterface.OnDismissListener, CompoundButton.OnCheckedChangeListener, TimePickerDialog.OnTimeSetListener {

    private TextView title;

    /**
     * 关闭按钮
     */
    private ImageView ivUpdateSettingFinish;

    /**
     * 监测的应用名称
     */
    private TextView tvUpdateSettingCheckApp;

    /**
     * 监测应用的当前版本号
     */
    private TextView tvUpdateSettingCurVersion;


    /**
     * 监测应用的最新版本号
     */
    private TextView tvUpdateSettingNewVersion;

    /**
     * 立即更新的按钮
     */
    private Button btnUpdateSettingUpdate;

    /**
     * 电子狗的当前版本号
     */
    private TextView tvUpdateSettingEdogCurVersion;

    /**
     * 电子狗的最新版本号
     */
    private TextView tvUpdateSettingEdogNewVersion;

    /**
     * 自动更新的开关
     */
    private CheckBox switchUpdateSettingAutoUpdate;

    /**
     * 自动更新的时间点
     */
    private TextView tvUpdateSettingUpdateTime;

    /**
     * 保存设置的按钮
     */
    private Button btnUpdateSettingSaveSetting;

    private UpdatePresenter mUpdatePresenter;

    private TimePickerDialog mTimePickerDialog;

    private int mUpdateHour;

    private int mUpdateMinute;

    /**
     * 自动更新的布局
     */
    private LinearLayout ll_auto_update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_setting);
        //初始化控件
        initView();
        //添加事件处理
        addListener();
    }

    /**
     * 添加控件的事件
     */
    private void addListener() {
        ivUpdateSettingFinish.setOnClickListener(this);
        btnUpdateSettingUpdate.setOnClickListener(this);
        tvUpdateSettingUpdateTime.setOnClickListener(this);
        btnUpdateSettingSaveSetting.setOnClickListener(this);
        switchUpdateSettingAutoUpdate.setOnCheckedChangeListener(this);
    }

    /**
     * 初始化控件的操作
     */
    private void initView() {

        mUpdatePresenter = new UpdatePresenter(this);
        title = findViewById(R.id.title);
        title.setText("更新设置");
        ivUpdateSettingFinish = findViewById(R.id.finish);
        tvUpdateSettingCheckApp = findViewById(R.id.tv_update_setting_check_app);
        tvUpdateSettingCheckApp.setText(mPreferenceUtil.getAppName());
        tvUpdateSettingCurVersion = findViewById(R.id.tv_update_setting_cur_version);
        tvUpdateSettingCurVersion.setText(mPreferenceUtil.getVersionName());
        tvUpdateSettingNewVersion = findViewById(R.id.tv_update_setting_new_version);
        btnUpdateSettingUpdate = findViewById(R.id.btn_update_setting_update);
        tvUpdateSettingEdogCurVersion = findViewById(R.id.tv_update_setting_edog_cur_version);
        ll_auto_update = findViewById(R.id.ll_auto_update);
        try {
            tvUpdateSettingEdogCurVersion.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvUpdateSettingEdogNewVersion = findViewById(R.id.tv_update_setting_edog_new_version);
        switchUpdateSettingAutoUpdate = findViewById(R.id.switch_update_setting_auto_update);

        tvUpdateSettingUpdateTime = findViewById(R.id.tv_update_setting_update_time);
        btnUpdateSettingSaveSetting = findViewById(R.id.btn_update_setting_save_setting);

        if (mPreferenceUtil.isAutoUpdate()) {
            //开启了自动更新按钮,显示自动更新的布局
            ll_auto_update.setVisibility(View.VISIBLE);
        } else {
            ll_auto_update.setVisibility(View.GONE);
        }


        switchUpdateSettingAutoUpdate.setChecked(mPreferenceUtil.isAutoUpdate());

        mUpdateHour = mPreferenceUtil.getUpdateHour();
        mUpdateMinute = mPreferenceUtil.getUpdateMinute();

        //设置更新时间
        tvUpdateSettingUpdateTime.setText(StringUtils.getHourMinuteStr(mUpdateHour, mUpdateMinute));

        //选择时间的弹框
        mTimePickerDialog = new TimePickerDialog(this, this, mUpdateHour, mUpdateMinute, true);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:
                //结束当前页面
                finish();
                break;
            case R.id.btn_update_setting_update:
                //手动更新
                UpdateManager.getUpdateManager().checkAppUpdate(this, false);
                break;
            case R.id.tv_update_setting_update_time:
                //设置自动更新时间
                mTimePickerDialog.show();
                break;
            case R.id.btn_update_setting_save_setting:
                //保存设置
                mPreferenceUtil.setAutoUpdate(switchUpdateSettingAutoUpdate.isChecked());
                mPreferenceUtil.setAutoUpdateTime(mUpdateHour, mUpdateMinute);
                //重新启动更新的服务
                Intent intent = new Intent(this, UpdateService.class);
                startService(intent);
                new SettingDialog.Builder()
                        .setConfirmMsg("确定")
                        .setContext(this)
                        .setMsg("已保存更新设置")
                        .setOnDismissListener(this)
                        .build()
                        .show();
                break;
            default:
                break;
        }
    }


    @Override
    public void showServerAppVersion(String version) {
        tvUpdateSettingNewVersion.setText(version);
    }

    @Override
    public void showServerAppVersionFailed() {

    }

    @Override
    public void showServerAppVersionError(String message) {

    }

    @Override
    public void showUpdateButton() {
        btnUpdateSettingUpdate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }

    /**
     * 当自动更新按钮更改时的监听
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            ll_auto_update.setVisibility(View.VISIBLE);
            tvUpdateSettingUpdateTime.setText(StringUtils.getHourMinuteStr(mUpdateHour, mUpdateMinute));
        } else {
            ll_auto_update.setVisibility(View.GONE);
            mUpdateHour = 0;
            mUpdateMinute = 0;
            mTimePickerDialog.updateTime(mUpdateHour, mUpdateMinute);
        }

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mUpdateHour = hourOfDay;
        mUpdateMinute = minute;
        tvUpdateSettingUpdateTime.setText(StringUtils.getHourMinuteStr(mUpdateHour, mUpdateMinute));
    }

}
