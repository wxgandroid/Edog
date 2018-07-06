package com.puji.edog;

import android.os.Bundle;
import android.view.MotionEvent;

import com.puji.edog.util.LongTimeDelayUtils;

/**
 * @author WangXuguang
 * @date 2018/5/23.
 */

public class Base2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LongTimeDelayUtils.getInstance().startCountDown(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LongTimeDelayUtils.getInstance().stopCountDown();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LongTimeDelayUtils.getInstance().restartCountDown(this);
        return super.dispatchTouchEvent(ev);
    }
}
