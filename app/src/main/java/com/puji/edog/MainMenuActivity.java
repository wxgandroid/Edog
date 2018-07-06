package com.puji.edog;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.puji.edog.presenter.IUpdateView;
import com.puji.edog.presenter.UpdatePresenter;
import com.puji.edog.util.uitl.NetworkUtils;
import com.puji.edog.util.uitl.StringUtils;

public class MainMenuActivity extends Base2Activity implements OnClickListener, IUpdateView {

    /**
     * 设置定时开关机
     */
    private Button mPowerBtn;

    /**
     * 电子狗监测设置
     */
    private Button mEdogBtn;

    /**
     * 修改密码设置
     */
    private Button mPwdSettingBtn;

    /**
     * 启动监测的应用
     */
    private Button mOpenPuJiBtn;
    /**
     * 更新设置按钮
     */
    private Button btn_check_update;

    /**
     * 网络诊断按钮
     */
    private Button btn_check_network;

    /**
     * 当前监测应用的名称
     */
    private TextView tvCheckName;

    /**
     * 监测应用的当前版本号
     */
    private TextView tvCurVersionCode;

    /**
     * 监测应用的服务端最新的版本号
     */
    private TextView tvNewVersionCode;

    /**
     * 本机的电子狗版本号
     */
    private TextView tvEdogCurVersionCode;

    /**
     * 服务端的电子狗版本号
     */
    private TextView tvEdogNewVersionCode;

    /**
     * 大屏机设定的开关机时间
     */
    private TextView tvRestartTime;

    /**
     * 当前大屏机联网的方式
     */
    private TextView tvNetworkMethod;

    /**
     * 大屏机当前的网络状态
     */
    private TextView tvNetworkStatus;

    /**
     * 关闭应用按钮
     */
    private ImageView mFinishBtn;

    private AlertDialog mAlertDialog;


    private static final int REQUEST_CODE = 101;

    private UpdatePresenter mUpdatePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initView();
        initData();
    }

    private void initData() {
        mUpdatePresenter.getAppVersionData(mPreferenceUtil.getVersionCode());
    }

    /**
     *
     */
    public void initView() {

        mUpdatePresenter = new UpdatePresenter(this);

        mPowerBtn = (Button) findViewById(R.id.on_off_btn);
        mPowerBtn.setOnClickListener(this);

        mEdogBtn = (Button) findViewById(R.id.edog_seting_btn);
        mEdogBtn.setOnClickListener(this);

        mPwdSettingBtn = (Button) findViewById(R.id.modify_pwd_setting_btn);
        mPwdSettingBtn.setOnClickListener(this);

        mOpenPuJiBtn = (Button) findViewById(R.id.open_puji_guanjia_btn);
        mOpenPuJiBtn.setOnClickListener(this);

        mFinishBtn = (ImageView) findViewById(R.id.finish);
        mFinishBtn.setOnClickListener(this);

        btn_check_update = (Button) findViewById(R.id.btn_check_update);
        btn_check_update.setOnClickListener(this);

        btn_check_network = (Button) findViewById(R.id.btn_check_network);
        btn_check_network.setOnClickListener(this);

        //设置当前监测的应用的名称
        tvCheckName = (TextView) findViewById(R.id.tv_check_name);
        tvCheckName.setText(mPreferenceUtil.getAppName());
        //设置当前应用的版本号
        tvCurVersionCode = (TextView) findViewById(R.id.tv_cur_version_code);
        tvCurVersionCode.setText(String.valueOf(mPreferenceUtil.getVersionName()));

        tvNewVersionCode = (TextView) findViewById(R.id.tv_new_version_code);
        tvEdogCurVersionCode = (TextView) findViewById(R.id.tv_edog_cur_version_code);
        tvEdogNewVersionCode = (TextView) findViewById(R.id.tv_edog_new_version_code);
        tvRestartTime = (TextView) findViewById(R.id.tv_restart_time);

        //定时开关机的类型
        int type = mPreferenceUtil.getType();
        switch (type) {
            case 1:
                //定时开关机
                int onHour = mPreferenceUtil.getOnHour();
                int onMinute = mPreferenceUtil.getOnMinute();
                int offHour = mPreferenceUtil.getOffHour();
                int offMinute = mPreferenceUtil.getOffMinute();
                tvRestartTime.setText(StringUtils.getHourMinuteStr(onHour, onMinute) + "~" + StringUtils.getHourMinuteStr(offHour, offMinute) + "自动开关");
                break;
            case 2:
                //定时重启
                int hour = mPreferenceUtil.getRebootHour();
                int minute = mPreferenceUtil.getRebootMinute();
                tvRestartTime.setText(StringUtils.getHourMinuteStr(hour, minute) + "重启");
                break;
            case 3:
                //定时关机
                int hour1 = mPreferenceUtil.getOffHour();
                int offMinute1 = mPreferenceUtil.getOffMinute();
                tvRestartTime.setText(StringUtils.getHourMinuteStr(hour1, offMinute1) + "关机");
                break;
            default:
                tvRestartTime.setText("已关闭");
                break;
        }

        tvNetworkMethod = (TextView) findViewById(R.id.tv_network_method);
        tvNetworkStatus = (TextView) findViewById(R.id.tv_network_status);

        try {
            tvEdogCurVersionCode.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int networkMethod = NetworkUtils.getInstance().getNetworkMethod(this);
        switch (networkMethod) {
            case NetworkUtils.NETWORN_WIFI:
                tvNetworkMethod.setText("WIFI");
                tvNetworkStatus.setText("连接正常");
                break;
            case NetworkUtils.NETWORN_MOBILE:
                tvNetworkMethod.setText("移动网络");
                tvNetworkStatus.setText("连接正常");
                break;
            case NetworkUtils.NETWORN_NONE:
                tvNetworkMethod.setText("");
                tvNetworkStatus.setText("无网络连接");
                break;
            case NetworkUtils.NETWORN_WLAN:
                tvNetworkMethod.setText("以太网");
                tvNetworkStatus.setText("连接正常");
                break;
            default:
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.info_prompt_title));
        builder.setMessage(getString(R.string.not_install_puji_guajia_text));
        builder.setNegativeButton(getString(R.string.info_prompt_title), null);

        mAlertDialog = builder.create();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(mPreferenceUtil.getPackage());
            if (intent != null) {
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.on_off_btn:
                //设置开关机
                openActivity(TimeSettingActivity.class);
                break;
            case R.id.edog_seting_btn:
                //监测设置
                openActivity(SettingActivity.class);
                break;
            case R.id.modify_pwd_setting_btn:
                //修改密码
                openActivity(ModifyActivity.class);
                break;
            case R.id.finish:
                finish();
                break;
            case R.id.open_puji_guanjia_btn:
                //打开应用
                if (TextUtils.isEmpty(mPreferenceUtil.getPackage())) {
                    Intent intent = new Intent(MainMenuActivity.this, PickAppDialogAct.class);
                    startActivityForResult(intent, REQUEST_CODE);
                    return;
                }
                Intent intent = getPackageManager().getLaunchIntentForPackage(
                        mPreferenceUtil.getPackage());
                if (intent != null) {
                    startActivity(intent);
                    finish();
                } else {
                    mAlertDialog.show();
                }

                break;
            case R.id.btn_check_update:
                //更新设置
                openActivity(UpdateSettingActivity.class);
                break;
            case R.id.btn_check_network:
                //网络诊断
                openActivity(NetworkDiagnosisActivity.class);
                break;
            default:
                break;
        }

    }

    @Override
    public void showServerAppVersion(String version) {
        tvNewVersionCode.setText(version);
    }

    @Override
    public void showServerAppVersionFailed() {
    }

    @Override
    public void showServerAppVersionError(String message) {

    }

    @Override
    public void showUpdateButton() {

    }
}
