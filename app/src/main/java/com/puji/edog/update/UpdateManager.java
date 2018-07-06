package com.puji.edog.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.puji.edog.config.Config;
import com.puji.edog.http.ResponseHttpback;
import com.puji.edog.http.VolleyRequest;
import com.puji.edog.util.SharedPreferenceUtil;
import com.puji.edog.util.uitl.PackageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 应用程序更新工具包
 *
 * @version 1.1
 */
public class UpdateManager {

    private static final String UPDATE_TAG = "HTTP_UPDATE_TAG";
    private static final int DOWN_NOSDCARD = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_ERROR = 3;
    private static final int DOWN_RETRY = 4;

    private static UpdateManager updateManager;

    private Context mContext;
    // 通知对话框
    // private Dialog noticeDialog;
    // 下载对话框
    // private Dialog downloadDialog;
    // '已经是最新' 或者 '无法获取最新版本' 的对话框
    // private Dialog latestOrFailDialog;
    // 进度条
    // private ProgressBar mProgress;
    // 显示下载数值
    // private TextView mProgressText;
    // 查询动画
    // private ProgressDialog mProDialog;
    // private DefaultProgressDialog pd;
    // 进度值
    private int progress;
    // 下载线程
    private Thread downLoadThread;
    // 终止标记
    private boolean interceptFlag;
    // 提示语
    private String updateMsg = "";
    // 返回的安装包url
    private String apkUrl = "";
    // 下载包保存路径
    private String savePath = "";
    // apk保存完整路径
    private String apkFilePath = "";
    // 临时下载文件路径
    private String tmpFilePath = "";
    // 下载文件大小
    private String apkFileSize;
    // 已下载文件大小
    private String tmpFileSize;

    private String curVersionName = "";
    private int curVersionCode;
    // private Update mUpdate;

    SharedPreferenceUtil mPreferenceUtil;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    // mProgress.setProgress(progress);
                    // mProgressText.setText(tmpFileSize + "/" + apkFileSize);
                    Log.v("log", "--------DOWN_UPDATE-------" + tmpFileSize + "/"
                            + apkFileSize);
                    sendString(tmpFileSize + "/" + apkFileSize);
                    break;
                case DOWN_OVER:
                    // downloadDialog.dismiss();
                    // installApk();
                    Log.v("log", "--------DOWN_OVER-------");
                    sendString("下载完成");
                    SilentInstall();
                    break;
                case DOWN_ERROR:
                    //下载应用的过程中出错了
                    sendEmptyMessageDelayed(DOWN_RETRY, Config.DOWNLOAD_RETRY_TIME);
                    downloadApk();
                    Toast.makeText(mContext, "下载失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                    break;
                case DOWN_NOSDCARD:
                    // downloadDialog.dismiss();
                    Toast.makeText(mContext, "无法下载安装文件，请检查SD卡是否正常", 0).show();
                    break;
            }
        }

        ;
    };

    public static UpdateManager getUpdateManager() {
        if (updateManager == null) {
            updateManager = new UpdateManager();
        }
        updateManager.interceptFlag = false;
        return updateManager;
    }

    /**
     * 检查App更新
     *
     * @param context
     */
    public void checkAppUpdate(final Context context, final boolean isTo) {
        // pd = new DefaultProgressDialog(context);
        // if (isTo)
        // pd.show();
        this.mContext = context;
        mPreferenceUtil = new SharedPreferenceUtil(context);
        final int cur = mPreferenceUtil.getVersionCode();

        if ("com.hachi.villagemediascreen".equals(Config.PACKAGE_NAME)) {
            checkNewApp(cur);
        } else {
            checkOldApp(cur);
        }


    }


    private void checkNewApp(final int cur) {
        try {
            final JSONObject json = new JSONObject();

            System.out.println("-------checkAppUpdate------------" + json.toString());
            VolleyRequest.getInstance().RequestHttpString(false, Config.NEW_HTTP_URL, new ResponseHttpback() {

                @Override
                public void onResponseSuccess(String response) {
                    // TODO Auto-generated method stub
                    Log.v("log", "-------checkAppUpdate------------" + response);
                    System.out.println("-------checkAppUpdate------------" + response);
                    sendString(response);
                    try {
                        JSONObject json = new JSONObject(response);//2016.12.6后台侯攀琦要求改的
                        JSONObject data = json.getJSONObject("data");
                        apkUrl = data.get("download").toString();
                        String version = data.getString("version").replace(".", "");
                        int v = Integer.parseInt(version);
                        if (v > cur) {// 可更新版本
                            Toast.makeText(mContext, "开始更新", Toast.LENGTH_SHORT).show();
                            downloadApk();
                        } else {
                            Toast.makeText(mContext, "当前是最新版本了", Toast.LENGTH_SHORT).show();
                            sendString("已经是最新版本");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "抛出异常", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onErrorResponse(String errorResponse, int statusCode) {
                    // TODO Auto-generated method stub
                    Log.v("log", "-------checkAppUpdate------------" + errorResponse);
                    Toast.makeText(mContext, "数据异常：..." + errorResponse, Toast.LENGTH_SHORT)
                            .show();
                    sendString(errorResponse);
                }
            }, UPDATE_TAG); // 手机端
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void checkOldApp(final int cur) {
        try {
            JSONObject json = new JSONObject();
//			json.put("Method", "versions.update");
//			json.put("TypeID", "3");// 类型1
//			json.put("Appkey", "comma");
//			json.put("TimeStamp", ConverToStringEmpty(new Date((System.currentTimeMillis()))));
//			json.put("Sign", "997F46E4F789D96E3300B66C8C2EEC67");
            json.put("route", "terminal.upgrade");// 检查App更新 2016.12.6后台侯攀琦要求改的 新后台
            json.put("project_id", "5");// 产品ID：1.太原版商场导购（粉色的） 2.富力小区媒体机竖版 3.北京版商场导购（粉色的） 4.普及小区媒体机竖版  5.天津 售楼版大屏机(竖版)
            json.put("version_code", mPreferenceUtil.getVersionCode());
            json.put("sign", "94B5CCD5BF48B9FC0D3BE8E7BDA2A190");
            Log.v("log", "-------checkAppUpdate------------" + json.toString());
            System.out.println("-------checkAppUpdate------------" + json.toString());
            VolleyRequest.getInstance().RequestHttpJSON(true, Config.HTTP_URL,
                    json, new ResponseHttpback() {

                        @Override
                        public void onResponseSuccess(String response) {
                            // TODO Auto-generated method stub
                            Log.v("log", "-------checkAppUpdate------------" + response);
                            System.out.println("-------checkAppUpdate------------" + response);
                            sendString(response);
//							Toast.makeText(mContext, "返回数据：" + response, 1)
//									.show();
                            // {"Status":1,"Data":{"VersionsNum":"1.0.1","Path":"http:\/\/pujihome.pujiapp.com\/package\/pujihome.apk"},"Msg":"请求成功"}
                            try {
//								JSONObject json = new JSONObject(response);
//								JSONObject data = json.getJSONObject("Data");
//								apkUrl = data.get("Path").toString();
//								String version = data.getString("VersionsNum")
//										.replace(".", "");
//								int v = Integer.parseInt(version);
//								if (v > cur) {// 可更新版本
//									downloadApk();
//								} else {
//									sendString("已经是最新版本");
//								}
                                JSONObject json = new JSONObject(response);//2016.12.6后台侯攀琦要求改的
                                JSONObject data = json.getJSONObject("data");
                                apkUrl = data.get("path").toString();
                                String version = data.getString("code")
                                        .replace(".", "");
                                int v = Integer.parseInt(version);
                                if (v > cur) {// 可更新版本
                                    downloadApk();
                                } else {
                                    sendString("已经是最新版本");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onErrorResponse(String errorResponse, int statusCode) {
                            // TODO Auto-generated method stub
                            Log.v("log", "-------checkAppUpdate------------" + errorResponse);
                            Toast.makeText(mContext, "数据异常：" + errorResponse, Toast.LENGTH_SHORT)
                                    .show();
                            sendString(errorResponse);
                        }
                    }, UPDATE_TAG); // 手机端
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendString(String s) {
        mPreferenceUtil.setLog(s);
        mContext.sendBroadcast(new Intent("TEXT"));
    }

    /**
     * 获取当前客户端版本信息
     */
    private int getCurrentVersion() {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            curVersionName = info.versionName;
            curVersionCode = info.versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return curVersionCode;
    }

    /**
     * 获取当前客户端版本号
     */
    public String getVersionName() {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "Get Version fail";
        }
    }

    /**
     * 更新应用的操作
     */
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                String apkName = "text.apk";
                String tmpApk = "text.tmp";
                // // 判断是否挂载了SD卡
                String storageState = Environment.getExternalStorageState();
                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                    savePath = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/Comma/update/";
                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    apkFilePath = savePath + apkName;
                    tmpFilePath = savePath + tmpApk;
                    File apkfile = new File(apkFilePath);
                    if (apkfile.exists()) {
                        apkfile.delete();
                    }
                    File tmpfile = new File(tmpFilePath);
                    if (tmpfile.exists()) {
                        tmpfile.delete();
                    }
                }

                // File file = mContext.getFilesDir();
                // if (!file.exists()) {
                // file.mkdirs();
                // }
                // savePath = file.getPath();
                // Log.v("log", "--------savePath-------"+savePath );
                // apkFilePath = savePath + apkName;
                // tmpFilePath = savePath + tmpApk;
                // File apkfile = new File(apkFilePath);
                // if (apkfile.exists()) {
                // apkfile.delete();
                // }
                // File tmpfile = new File(tmpFilePath);
                // if (tmpfile.exists()) {
                // tmpfile.delete();
                // }
                // 没有挂载SD卡，无法下载文件
                // if (apkFilePath == null || apkFilePath == "") {
                // mHandler.sendEmptyMessage(DOWN_NOSDCARD);
                // return;
                // }
                Log.v("log", "--------apkFilePath-------" + apkFilePath);
                File ApkFile = new File(apkFilePath);

                // 是否已下载更新文件
                if (ApkFile.exists()) {
                    SilentInstall();
                    return;
                }

                // 输出临时下载文件
                File tmpFile = new File(tmpFilePath);
                FileOutputStream fos = new FileOutputStream(tmpFile);

                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                // 显示文件大小格式：2个小数点显示
                DecimalFormat df = new DecimalFormat("0.00");
                // 进度条下面显示的总文件大小
                apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    // 进度条下面显示的当前下载文件大小
                    tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
                    // 当前进度值
                    progress = (int) (((float) count / length) * 100);
                    Log.e("TAG", "progress" + progress);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成 - 将临时下载文件转成APK文件
                        if (tmpFile.renameTo(ApkFile)) {
                            // 通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                        }
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                //下载过程中抛出异常了
                e.printStackTrace();
                mHandler.sendEmptyMessage(DOWN_ERROR);

            } catch (IOException e) {
                //下载过程中出现异常了
                e.printStackTrace();
                mHandler.sendEmptyMessage(DOWN_ERROR);
            }

        }
    };

    /**
     * 下载apk
     *
     * @param
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     *
     * @param
     */
    // private void installApk() {
    // File apkfile = new File(apkFilePath);
    // if (!apkfile.exists()) {
    // return;
    // }
    // Intent i = new Intent(Intent.ACTION_VIEW);
    // i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
    // "application/vnd.android.package-archive");
    // // mContext.startActivity(i);
    // mContext.startActivityForResult(i, 90);
    // }
    public void SilentInstall() {
        System.out.println(apkFilePath);
        sendString("下载完成");
        int a = PackageUtils.installSilent(mContext, apkFilePath);
        Log.v("log", "--------a ==" + a);
        if (a == 1) {// 安装成功 并启动
            sendString("安装完成");
            Intent intent = mContext.getPackageManager()
                    .getLaunchIntentForPackage(Config.PACKAGE_NAME);
            mContext.startActivity(intent);
        }
    }

    /**
     * 本地时间戳
     *
     * @param date
     * @return
     */
    public static String ConverToStringEmpty(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        return df.format(date).replace(":", "").replace(" ", "");
    }

    /**
     * 支持断点续传的下载方法
     */
    private void resumeDownload(String downloadUrl) {
        HttpURLConnection conn = null;
        InputStream inStream = null;
        RandomAccessFile oSavedFile = null;
        long breakPoint = 0L;
        long totalSize = 0;
        long downloadSize = 0;
        long preTime = System.currentTimeMillis();
        long currentTime = 0;
        long usedTime = 0;
        long downloadSpeed = 0;
        //文件的下载路径
        String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Comma/update/";
        File downloadDir = new File(downloadPath);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        File tempFile = new File(downloadDir, "test.tmp");
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            URL url = new URL(downloadUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setRequestMethod("GET");
        } catch (IOException e) {
            e.printStackTrace();
            if (conn != null) {
                conn.disconnect();
            }
        }
        conn.setRequestProperty(
                "Accept",
                "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("Referer", downloadUrl);
        conn.setRequestProperty("Charset", "UTF-8");
        // 如果下载文件存在
        if (tempFile.exists() && tempFile.isFile() && tempFile.length() > 0) {
            breakPoint = tempFile.length();
            downloadSize = breakPoint;
        } else {
            downloadSize = 0;
        }
        conn.setRequestProperty("Range", "bytes=" + breakPoint + "-");
        conn.setRequestProperty(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("content-type", "text/html");
        int responseCode = -1;
        try {
            conn.connect();
            responseCode = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
        }
        //请求数据成功
        if (responseCode == 200) {
            try {
                inStream = conn.getInputStream();
            } catch (IOException e) {
                //抛出异常了,关闭连接
                e.printStackTrace();
                closeHttpConnect(conn, inStream, oSavedFile);
            }
            try {
                oSavedFile = new RandomAccessFile(tempFile, "rw");
            } catch (FileNotFoundException e) {
                //抛出异常了,关闭连接
                e.printStackTrace();
                closeHttpConnect(conn, inStream, oSavedFile);
            }
            try {
                oSavedFile.seek(breakPoint);
            } catch (IOException e) {
                e.printStackTrace();
                closeHttpConnect(conn, inStream, oSavedFile);
            }
            try {
                long filesize = conn.getContentLength();
                if (downloadSize == 0) {
                    totalSize = filesize;
                } else {
                    totalSize = filesize - downloadSize;
                }
                byte buffer[] = new byte[800096];
                int length = 0;
                while ((length = inStream.read(buffer, 0, buffer.length)) != -1) {
                    oSavedFile.write(buffer, 0, length);
                    downloadSize += length;
                    currentTime = System.currentTimeMillis();
                    usedTime = (int) ((currentTime - preTime) / 1000);
                    preTime = currentTime;
                    if (usedTime == 0) {
                        usedTime = 1;
                    }
                    downloadSpeed = (int) ((length / usedTime) / 1024);
                    Log.e("TAG", "downloadSpeek=======================" + downloadSpeed);
                    Log.e("TAG", "downloadSize========================" + downloadSize);
                    Log.e("TAG", "totalSize==============================" + totalSize);
                }
                tempFile.renameTo(new File(downloadPath, "test.apk"));
            } catch (IOException e) {
                e.printStackTrace();
                closeHttpConnect(conn, inStream, oSavedFile);

            } finally {
                closeHttpConnect(conn, inStream, oSavedFile);
            }
        } else {
            Log.e("TAG", "获取连接失败");
        }
    }

    /**
     * 关闭http连接
     *
     * @param conn
     * @param inStream
     * @param file
     */
    private void closeHttpConnect(HttpURLConnection conn, InputStream inStream, RandomAccessFile file) {
        if (conn != null) {
            conn.disconnect();
        }
        if (file != null) {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inStream != null) {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
