package com.puji.edog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.puji.edog.config.Config;
import com.puji.edog.util.MachineUtil;
import com.puji.edog.util.SharedPreferenceUtil;
import com.puji.edog.util.TimeUtils;
import com.puji.edog.weight.SettingDialog;

@SuppressLint("NewApi")
public class TimeSettingFragment extends Fragment implements OnClickListener,
        OnItemSelectedListener, MachineUtil.OnStateListener,
        DialogInterface.OnClickListener, DialogInterface.OnDismissListener {

    private Spinner mSpinner;// 定时选项菜单

    private TextView mOnTimeBtn; // 显示开机时间或重启时间
    private TextView mOnTimeLable;

    private TextView mOffTimeBtn;// 显示关机时间
    private TextView mOffTimeLabel;

    private View mOnTimeContainer;// 开机时间控件容器
    private View mOffTimeContainer;// 关机时间控件容器

    private LinearLayout ll_off_container;
    private LinearLayout ll_on_container;

    private TimePickerDialog mPickOnTimeDialog;// 用来设置开机时间的对话框
    private TimePickerDialog mPickOffTimeDialog;// 用来设置关机时间的对话框

    private Button mSaveSettingBtn;// 保存用户设置的按钮

    private TextView mAlertTextView;// 用于提示用于错误信息的文本域

    private SharedPreferenceUtil mPreferenceUtil;// 保存用户设置数据的工具类的实例

    private AlarmManager mAlarmManager;

    private boolean mIsExitActivity = false;

    MachineUtil mMachineUtil;// 定时开关机工具类的实例

    /**
     * 开机时间
     */
    private int mOnHour = 0;
    private int mOnMinute = 0;

    /**
     * 关机时间
     */
    private int mOffHour = 0;
    private int mOffMinute = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            mPreferenceUtil = ((BaseActivity) getActivity()).mPreferenceUtil;
            mAlarmManager = (AlarmManager) getActivity().getSystemService(
                    Context.ALARM_SERVICE);
            mMachineUtil = new MachineUtil();
            mMachineUtil.setOnStateListener(this);
        }
    }

    /**
     * 初始化UI组件
     */
    public void initView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, R.id.spinner_item, getResources()
                .getStringArray(R.array.power_option));
        mSpinner = (Spinner) getView().findViewById(R.id.spinner);
        mSpinner.setSelection(0);
        mSpinner.setOnItemSelectedListener(this);
        mSpinner.setAdapter(adapter);

        mOnTimeContainer = getView().findViewById(R.id.on_time_container);
        mOffTimeContainer = getView().findViewById(R.id.off_time_container);

        mOnTimeLable = (TextView) getView().findViewById(R.id.on_time_lable);
        mOffTimeLabel = (TextView) getView().findViewById(R.id.off_time_lable);

        mOnTimeBtn = (TextView) getView().findViewById(R.id.on_time);
        mOnTimeBtn.setOnClickListener(this);

        mOffTimeBtn = (TextView) getView().findViewById(R.id.off_time);
        mOffTimeBtn.setOnClickListener(this);

        mSaveSettingBtn = (Button) getView().findViewById(R.id.save_setting);
        mSaveSettingBtn.setOnClickListener(this);

        mAlertTextView = (TextView) getView().findViewById(R.id.alert);

        try {
            ll_off_container = getView().findViewById(R.id.ll_off_container);
            ll_on_container = getView().findViewById(R.id.ll_on_container);
        } catch (Exception e) {
            e.printStackTrace();
        }


        initDisplayTime(mPreferenceUtil.getType());

        mPickOnTimeDialog = new TimePickerDialog(getActivity(),
                new OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        mOnHour = hourOfDay;
                        mOnMinute = minute;

                        mOnTimeBtn.setText(String.format(
                                getString(R.string.time_format), mOnHour,
                                mOnMinute));

                    }
                }, mOnHour, mOnMinute, true);

        mPickOffTimeDialog = new TimePickerDialog(getActivity(),
                new OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        mOffHour = hourOfDay;
                        mOffMinute = minute;

                        mOffTimeBtn.setText(String.format(
                                getString(R.string.time_format), mOffHour,
                                mOffMinute));
                    }
                }, mOffHour, mOffMinute, true);


        mSpinner.setSelection(mPreferenceUtil.getType());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.time_setting_fragment_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.save_setting:
                saveSetting();
                break;
            case R.id.on_time:
                mPickOnTimeDialog.setCancelable(true);
                mPickOnTimeDialog.show();
                break;
            case R.id.off_time:
                mPickOffTimeDialog.setCancelable(true);
                mPickOffTimeDialog.show();
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch (position) {
            case 0:
                closeTimingOption();
                break;
            case 1:
                switchTimingOnAndOff();
                break;
            case 2:
                switchTimingReboot();
                break;
            case 3:
                switchTimingOff();
                break;
        }

        initDisplayTime(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void initDisplayTime(int position) {
        switch (position) {

            case SharedPreferenceUtil.AUTO_OFF:

                mOffHour = mPreferenceUtil.getOffHour();
                mOffMinute = mPreferenceUtil.getOffMinute();

                mOffTimeBtn.setText(String.format(getString(R.string.time_format),
                        mOffHour, mOffMinute));
                break;
            case SharedPreferenceUtil.AUTO_REBOOT:

                mOnHour = mPreferenceUtil.getRebootHour();
                mOnMinute = mPreferenceUtil.getRebootMinute();

                mOnTimeBtn.setText(String.format(getString(R.string.time_format),
                        mOnHour, mOnMinute));
                break;
            case SharedPreferenceUtil.AUTO_ON_OFF:

                mOnHour = mPreferenceUtil.getOnHour();
                mOnMinute = mPreferenceUtil.getOnMinute();

                mOffHour = mPreferenceUtil.getOffHour();
                mOffMinute = mPreferenceUtil.getOffMinute();

                mOffTimeBtn.setText(String.format(getString(R.string.time_format),
                        mOffHour, mOffMinute));
                mOnTimeBtn.setText(String.format(getString(R.string.time_format),
                        mOnHour, mOnMinute));
                break;

            default:
                break;
        }
    }

    // 保存设置
    private void saveSetting() {
        Intent intent = new Intent(Config.SWITCH_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.cancel(pendingIntent);
        mMachineUtil.close();

        switch (mSpinner.getSelectedItemPosition()) {
            case 0:
                mIsExitActivity = true;
                mPreferenceUtil.setType(SharedPreferenceUtil.CLOSE_SETTING);
                SettingDialog dialog = new SettingDialog.Builder()
                        .setMsg("已设定为关闭定时选项")
                        .setConfirmMsg("确定")
                        .setContext(getContext())
                        .setOnDismissListener(this)
                        .build();
                dialog.show();
                break;
            case 1:
                //设置定时开关机
                mPreferenceUtil.setOnHour(mOnHour);
                mPreferenceUtil.setOnMinute(mOnMinute);

                mPreferenceUtil.setOffHour(mOffHour);
                mPreferenceUtil.setOffMinute(mOffMinute);

                mPreferenceUtil.setType(SharedPreferenceUtil.AUTO_ON_OFF);

                mMachineUtil.setBonh((byte) mOnHour);
                mMachineUtil.setBonm((byte) mOnMinute);

                mMachineUtil.setBoffh((byte) mOffHour);
                mMachineUtil.setBoffm((byte) mOffMinute);

                mMachineUtil.openMachine();
                break;
            case 2:

                mIsExitActivity = true;

                mPreferenceUtil.setRebootHour(mOnHour);
                mPreferenceUtil.setRebootMinute(mOnMinute);

                mPreferenceUtil.setType(SharedPreferenceUtil.AUTO_REBOOT);

                mAlarmManager.set(AlarmManager.RTC_WAKEUP, TimeUtils.calculateRebootTime(mOnHour, mOnMinute), pendingIntent);
                SettingDialog dialog2 = new SettingDialog.Builder()
                        .setMsg("已设定为定时重启")
                        .setContext(getContext())
                        .setConfirmMsg("确定")
                        .setOnDismissListener(this)
                        .build();
                dialog2.show();
                break;
            case 3:
                mIsExitActivity = true;

                mPreferenceUtil.setOffHour(mOffHour);
                mPreferenceUtil.setOffMinute(mOffMinute);

                mPreferenceUtil.setType(SharedPreferenceUtil.AUTO_OFF);

                mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                        TimeUtils.calculateRebootTime(mOffHour, mOffMinute),
                        pendingIntent);
                SettingDialog dialog3 = new SettingDialog.Builder()
                        .setMsg("已设定为定时关机")
                        .setConfirmMsg("确定")
                        .setOnDismissListener(this)
                        .setContext(getContext())
                        .build();
                dialog3.show();
                break;
            default:
                break;
        }

    }

    private void switchTimingOff() {

        if (ll_on_container != null) {
            ll_on_container.setVisibility(View.GONE);
        }
        mOnTimeContainer.setVisibility(View.GONE);
        mOnTimeLable.setVisibility(View.GONE);

        if (ll_off_container != null) {
            ll_off_container.setVisibility(View.VISIBLE);
        }
        mOffTimeContainer.setVisibility(View.VISIBLE);
        mOffTimeLabel.setVisibility(View.VISIBLE);

        mAlertTextView.setVisibility(View.INVISIBLE);

    }

    private void switchTimingOnAndOff() {

        if (ll_on_container != null) {
            ll_on_container.setVisibility(View.VISIBLE);
        }
        mOnTimeContainer.setVisibility(View.VISIBLE);
        mOnTimeLable.setVisibility(View.VISIBLE);
        mOnTimeLable.setText(R.string.on_time);

        LayoutParams lp = (LinearLayout.LayoutParams) mOnTimeLable
                .getLayoutParams();
        lp.setMargins(0, 0, 0, 0);
        mOnTimeLable.setLayoutParams(lp);

        if (ll_off_container != null) {
            ll_off_container.setVisibility(View.VISIBLE);
        }
        mOffTimeContainer.setVisibility(View.VISIBLE);
        mOffTimeLabel.setVisibility(View.VISIBLE);
        mAlertTextView.setVisibility(View.VISIBLE);
    }

    private void switchTimingReboot() {

        if (ll_on_container != null) {
            ll_on_container.setVisibility(View.VISIBLE);
        }
        mOnTimeContainer.setVisibility(View.VISIBLE);
        mOnTimeLable.setVisibility(View.VISIBLE);
        mOnTimeLable.setText(R.string.timing_reboot);

        LayoutParams lp = (LinearLayout.LayoutParams) mOnTimeLable
                .getLayoutParams();
        lp.setMargins(0, 50, 0, 0);

        mOnTimeLable.setLayoutParams(lp);

        if (ll_off_container != null) {
            ll_off_container.setVisibility(View.GONE);
        }

        mOffTimeContainer.setVisibility(View.GONE);
        mOffTimeLabel.setVisibility(View.GONE);

        mAlertTextView.setVisibility(View.INVISIBLE);
    }

    private void closeTimingOption() {

        if (ll_on_container != null) {
            ll_on_container.setVisibility(View.GONE);
        }

        if (ll_off_container != null) {
            ll_off_container.setVisibility(View.GONE);
        }

        mOnTimeContainer.setVisibility(View.GONE);
        mOnTimeLable.setVisibility(View.GONE);

        mOffTimeContainer.setVisibility(View.GONE);
        mOffTimeLabel.setVisibility(View.GONE);

        mAlertTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onError(String msg) {

        mIsExitActivity = false;

        SettingDialog dialog = new SettingDialog.Builder()
                .setMsg(msg)
                .setContext(getContext())
                .build();
        dialog.show();

    }

    @Override
    public void onSccessful(String msg) {

        mIsExitActivity = true;

        SettingDialog dialog = new SettingDialog.Builder()
                .setMsg(msg)
                .setConfirmMsg("确定")
                .setOnDismissListener(this)
                .setContext(getContext())
                .build();
        dialog.show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mIsExitActivity) {
            getActivity().finish();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }
}
