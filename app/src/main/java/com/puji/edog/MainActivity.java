package com.puji.edog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.puji.edog.app.BaseApp;
import com.puji.edog.service.EdogService;
import com.puji.edog.service.UpdateService;
import com.puji.edog.util.HachiAppListUtils;
import com.puji.edog.util.LaunchCheckAppUtils;

import java.util.List;

public class MainActivity extends BaseActivity implements OnClickListener, TextWatcher {

    private ImageView mFinishBtn;// 退出程序按钮
    private ImageView mClearBtn;// 用于清空密码输入框内容的按钮

    private Button mEnterSetting;// 用户设置按钮
    private EditText mPwdText;// 用户密码框
    private TextView mErrorDisplay;// 显示错误信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        BaseApp.getInstance().setActivity(this);
        regReceiver();
        initView();

    }

    /**
     * 初始化UI组件
     */
    public void initView() {

        mFinishBtn = (ImageView) findViewById(R.id.finish);
        mFinishBtn.setOnClickListener(this);

        mClearBtn = (ImageView) findViewById(R.id.clear);
        mClearBtn.setOnClickListener(this);

        mEnterSetting = (Button) findViewById(R.id.enter_setting);
        mEnterSetting.setOnClickListener(this);

        mErrorDisplay = (TextView) findViewById(R.id.error_display);
        mPwdText = (EditText) findViewById(R.id.password_text);

        Intent intent = new Intent(this, EdogService.class);
        stopService(intent);
        startService(intent);

        Intent intent1 = new Intent(this, UpdateService.class);
//		stopService(intent1);
        startService(intent1);

        mPwdText.addTextChangedListener(this);

        String packageName = mPreferenceUtil.getPackage();
        boolean checkAppExist = false;
        if (!TextUtils.isEmpty(packageName)) {
            //当前有监测的应用
            List<PackageInfo> hachiAppList = HachiAppListUtils.getInstance().getHachiAppList(this);
            for (PackageInfo info : hachiAppList) {
                if (packageName.equals(info.packageName)) {
                    mPreferenceUtil.setVersionCode(info.versionCode);
                    mPreferenceUtil.setVersionName(info.versionName);
                    mPreferenceUtil.setPackage(info.packageName);
                    mPreferenceUtil.setAppName(info.applicationInfo.loadLabel(getPackageManager()).toString());
                    checkAppExist = true;
                }
            }
        }

        if (!checkAppExist) {
            //上次监控的app被卸载了
            mPreferenceUtil.setVersionCode(0);
            mPreferenceUtil.setVersionName("");
            mPreferenceUtil.setPackage("");
            mPreferenceUtil.setAppName("");
        }

    }

    /**
     * 验证用户密码，如果密码正确则进入到菜单页面，否则显示错误信息
     */
    public void verifyPassword() {

        String pwd = mPwdText.getText().toString().trim();

        if (pwd.equals(mPreferenceUtil.getPassword())) {
            clear();
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(MainActivity.this, "输入错误请重试", Toast.LENGTH_SHORT).show();
//            mErrorDisplay.setText(getResources().getString(R.string.error_display));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlermReceiver != null) {
            unregisterReceiver(mAlermReceiver);
        }
    }

    public void regReceiver() {
        IntentFilter filter = new IntentFilter();
        // filter.addAction(Constants.BR_VISIBLE);
        filter.addAction("TEXT");
        registerReceiver(mAlermReceiver, filter);
    }

    private BroadcastReceiver mAlermReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            mErrorDisplay.setText(mPreferenceUtil.getLog());
            // String action = intent.getAction();
        }
    };

    /**
     * 清除显示的错误信息
     */
    public void clear() {
        mErrorDisplay.setText(null);
        mPwdText.setText(null);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.finish:
                //如果有监测中的应用，则开启应用，否则退出当前页面
                if (LaunchCheckAppUtils.getInstance().isCheckApp(this)) {
                    LaunchCheckAppUtils.getInstance().startDefaultApp(this);
                } else {
                    finish();
                }
                break;
            case R.id.clear:
                clear();
                break;
            case R.id.enter_setting:
                verifyPassword();
                break;
            default:
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (TextUtils.isEmpty(charSequence.toString())) {
            mEnterSetting.setEnabled(false);
        } else {
            mEnterSetting.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
