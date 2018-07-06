package com.puji.edog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.puji.edog.weight.SettingDialog;

public class ModifyActivity extends BaseActivity implements OnClickListener,
        DialogInterface.OnClickListener, DialogInterface.OnDismissListener {

    private EditText mNewPwdEdt; // 新密码文本框
    private EditText mReNewPwdEdt;// 再次输入密码文本框
    private Button mSaveButton;// 保存设置按钮

    private TextView mTitle;// 界面标题

    private ImageView mFinishBtn;// 退出程序按钮

    private String mReNewPwd;// 确认新密码
    private String mNewPwd;// 新密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        initView();
    }

    /**
     * 初始化UI组件
     */
    private void initView() {

        mNewPwdEdt = (EditText) findViewById(R.id.new_pwd);
        mReNewPwdEdt = (EditText) findViewById(R.id.re_new_pwd);

        mSaveButton = (Button) findViewById(R.id.save_setting);
        mSaveButton.setOnClickListener(this);

        mFinishBtn = (ImageView) findViewById(R.id.finish);
        mFinishBtn.setOnClickListener(this);

        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.modify_pwd);
    }

    private void initData() {
        mNewPwd = mNewPwdEdt.getText().toString();
        mReNewPwd = mReNewPwdEdt.getText().toString();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.save_setting: {
                initData();
                if (verifyNewPassword(mNewPwd, mReNewPwd)) {
                    mPreferenceUtil.savePassword(mNewPwd);
                    SettingDialog build = new SettingDialog.Builder()
                            .setContext(this)
                            .setOnDismissListener(this)
                            .build();
                    build.show();
                }
            }
            break;
            case R.id.finish:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 验证用户输入的新密码是否满足指定条件
     *
     * @param newPwd
     * @return
     */
    private boolean verifyNewPassword(String newPwd, String reNewPwd) {

        if (newPwd == null || "".equals(newPwd)) {

            mNewPwdEdt.setError(getString(R.string.password_can_not_null));
            mNewPwdEdt.requestFocus();

            return false;

        } else if (newPwd.length() < 4) {

            mNewPwdEdt
                    .setError(getString(R.string.password_must_than_length_than_four_big));
            mNewPwdEdt.requestFocus();
            return false;

        }

        if (newPwd.equals(reNewPwd)) {
            return true;
        } else {
            mReNewPwdEdt.setError(getString(R.string.password_diff_error));
            mReNewPwdEdt.requestFocus();

        }

        return false;

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
