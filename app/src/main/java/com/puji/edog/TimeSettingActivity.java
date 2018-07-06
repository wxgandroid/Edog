package com.puji.edog;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TimeSettingActivity extends BaseActivity implements
        OnClickListener {

    private TimeSettingFragment mSettingFragment;

    private FragmentManager mFragmentManager;

    private ImageView mFinishBtn;

    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);
        initView();

    }

    private void initView() {

        mFinishBtn = (ImageView) findViewById(R.id.finish);
        mFinishBtn.setOnClickListener(this);

        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.timing_on_off_setting);

        mFragmentManager = getFragmentManager();
        mSettingFragment = new TimeSettingFragment();
        mFragmentManager.beginTransaction()
                .add(R.id.main_container, mSettingFragment).commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:
                finish();
                break;
            default:
                break;
        }

    }

}
