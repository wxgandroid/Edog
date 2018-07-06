package com.puji.edog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.puji.edog.util.SharedPreferenceUtil;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

public class BaseActivity extends FragmentActivity {

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    protected SharedPreferenceUtil mPreferenceUtil;

    /**
     * 打开新的Activity
     *
     * @param clazz
     */
    protected void openActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 将数据封装在一个Bundle对象然后传递给要打开的Activity
     *
     * @param clazz
     * @param bundle
     */
    protected void openActivity(Class<?> clazz, Bundle bundle) {

        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceUtil = new SharedPreferenceUtil(this);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;

        try {
            int requestedOrientation = getRequestedOrientation();
            ApplicationInfo activityInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            switch (requestedOrientation) {
                case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                    activityInfo.metaData.putString("design_width", "1920");
                    activityInfo.metaData.putString("design_height", "1080");
                    break;
                default:
                    activityInfo.metaData.putString("design_width", "1080");
                    activityInfo.metaData.putString("design_height", "1920");
                    break;
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) {
            return view;
        }

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mPreferenceUtil.setEnable(true);
    }

}
