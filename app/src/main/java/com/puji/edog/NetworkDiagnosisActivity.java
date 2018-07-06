package com.puji.edog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.puji.edog.presenter.INetWorkView;
import com.puji.edog.presenter.NetWorkPresenter;
import com.puji.edog.util.uitl.NetworkUtils;

public class NetworkDiagnosisActivity extends Base2Activity implements View.OnClickListener, INetWorkView {

    private TextView title;

    /**
     * 关闭页面按钮
     */
    private ImageView ivNetworkDiagnosisFinish;

    /**
     * 入网方式
     */
    private TextView tvNetworkDiagnosisMethod;

    /**
     * 网络状态
     */
    private TextView tvNetworkDiagnosisState;

    /**
     * 网关地址
     */
    private TextView tvNetworkDiagnosisNetAddress;

    /**
     * 网关连接状态
     */
    private TextView tvNetworkDiagnosisGatewayConnect;

    /**
     * internet连接状态
     */
    private TextView tvNetworkDiagnosisInternetConnect;

    /**
     * 服务器连接状态
     */
    private TextView tvNetworkDiagnosisServerConnect;

    /**
     * 返回按钮
     */
    private Button btnNetworkDiagnosisBack;

    private NetWorkPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_diagnosis);
        initView();
        addListener();
        initData();
    }

    /**
     * 添加事件监听
     */
    private void addListener() {
        ivNetworkDiagnosisFinish.setOnClickListener(this);
        btnNetworkDiagnosisBack.setOnClickListener(this);
    }


    private void initData() {
        int networkMethod = NetworkUtils.getInstance().getNetworkMethod(this);
        String method = "";
        String state = "";
        String gatewayConnect = "";
        String internetConnect = "";
        String serverConnect = "";
        mPresenter.checkInternet();

        switch (networkMethod) {
            case NetworkUtils.NETWORN_NONE:
                //无网络连接
                method = "无网络连接";
                state = "无网络连接";
                gatewayConnect = "网络异常";
                internetConnect = "网络异常";
                serverConnect = "网络异常";
                break;
            case NetworkUtils.NETWORN_WIFI:
                method = "WIFI连接";
                state = "连接正常";
                gatewayConnect = "连接正常";
                internetConnect = "连接正常";
                serverConnect = "连接正常";
                break;
            case NetworkUtils.NETWORN_MOBILE:
                method = "SIM网络";
                state = "连接正常";
                gatewayConnect = "连接正常";
                internetConnect = "连接正常";
                serverConnect = "连接正常";
                break;
            default:
                break;

        }
        tvNetworkDiagnosisMethod.setText(method);
        tvNetworkDiagnosisState.setText(state);
        tvNetworkDiagnosisGatewayConnect.setText(gatewayConnect);
        tvNetworkDiagnosisInternetConnect.setText(internetConnect);
        tvNetworkDiagnosisServerConnect.setText(serverConnect);
    }

    /**
     * 初始化控件的方法
     */
    private void initView() {
        title = findViewById(R.id.title);
        title.setText("网络诊断");
        ivNetworkDiagnosisFinish = findViewById(R.id.finish);
        tvNetworkDiagnosisMethod = findViewById(R.id.tv_network_diagnosis_method);
        tvNetworkDiagnosisState = findViewById(R.id.tv_network_diagnosis_state);
        tvNetworkDiagnosisNetAddress = findViewById(R.id.tv_network_diagnosis_net_address);
        tvNetworkDiagnosisNetAddress.setText(NetworkUtils.getInstance().getPhoneIP());
        tvNetworkDiagnosisGatewayConnect = findViewById(R.id.tv_network_diagnosis_gateway_connect);
        tvNetworkDiagnosisInternetConnect = findViewById(R.id.tv_network_diagnosis_internet_connect);
        tvNetworkDiagnosisServerConnect = findViewById(R.id.tv_network_diagnosis_server_connect);
        btnNetworkDiagnosisBack = findViewById(R.id.btn_network_diagnosis_back);
        mPresenter = new NetWorkPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:
            case R.id.btn_network_diagnosis_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void showInternetNormal() {
        tvNetworkDiagnosisInternetConnect.setText("连接正常");
    }

    @Override
    public void showInternetError() {
        tvNetworkDiagnosisInternetConnect.setText("连接异常");
    }
}
