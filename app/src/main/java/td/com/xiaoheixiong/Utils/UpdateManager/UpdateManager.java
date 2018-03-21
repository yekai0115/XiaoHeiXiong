package td.com.xiaoheixiong.Utils.UpdateManager;

import android.Manifest;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.permissionManager.PermissionsCheckerUtil;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.OneButtonDialogWhite;
import td.com.xiaoheixiong.dialogs.TwoButtonDialogTitleWhite;

/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    private String NewVersionCode = "";
    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;
    private TwoButtonDialogTitleWhite TwoButtonDialog;
    /* 下载APK网址 */
    private String apkUrl;
    private OneButtonDialogWhite button;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        String isforce = MyCacheUtil.getshared(mContext).getString("isforce", "");
        if (isUpdate()) {
            if (isforce.equals("0")) {
                // 显示提示对话框
                showNoticeDialog();
            } else if (isforce.equals("1")) {//强制升级
                showDownloadDialog();
            }
        } else {
            //     Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    public boolean isUpdate() {
        // 获取当前软件版本
        int versionCode = getVersionCode(mContext);
        // 把version.xml放到网络上，然后获取文件信息
        //	InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
        // 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
        //	ParseXmlService service = new ParseXmlService();
        try {
            //	mHashMap = service.parseXml(inStream);
            NewVersionCode = MyCacheUtil.getshared(mContext).getString("versionCode", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("Code", NewVersionCode + "  " + versionCode);
        if (!NewVersionCode.equals("") && !NewVersionCode.equals("null")) {
            int serviceCode = Integer.valueOf(NewVersionCode);
            // 版本判断
            if (serviceCode > versionCode) {
                return true;
            }
        }
      /*  if (null != mHashMap) {
            int serviceCode = Integer.valueOf(mHashMap.get("version"));
            Log.e("Code", serviceCode + "  " + versionCode);
            // 版本判断
            if (serviceCode > versionCode) {
                return true;
            }
        }*/
        return false;
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo("td.com.xiaoheixiong", 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
       String apkNote =  MyCacheUtil.getshared(mContext).getString("apkNote", "");
        TwoButtonDialog = new TwoButtonDialogTitleWhite(mContext, "版本更新", apkNote+"", "暂不更新", "立即更新",
                new OnMyDialogClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_left:
                                // 显示下载对话框
                                TwoButtonDialog.dismiss();

                                break;
                            case R.id.btn_right:
                                if (PermissionsCheckerUtil.lacksPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    TwoButtonDialog.dismiss();
                                    button = new OneButtonDialogWhite(mContext, "为保证应用正常使用，需开启应用手机存储权限！", "前往设置", new OnMyDialogClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            Uri packageURI = Uri.parse("package:" + "td.com.xiaoheixiong");
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                            mContext.startActivity(intent);
                                            button.dismiss();
                                        }

                                    });
                                    button.setCancelable(false);
                                    button.setCanceledOnTouchOutside(false);
                                    button.show();
                                    return;
                                }
                                ;
                                showDownloadDialog();
                                TwoButtonDialog.dismiss();
                                break;
                        }
                    }
                });
        TwoButtonDialog.show();

    }


    /**
     * 显示软件下载对话框
     */

    private void showDownloadDialog() {
        // 构造软件下载对话框

        Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 0);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        v.setLayoutParams(lp);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        //mProgress.setProgress(0);
        mProgress.setIndeterminate(false);
        /*
         * 需要注意的是setIndeterminate的参数为true 或false：
		 * true表示不确定模式：滚动条的当前值自动在最小到最大值之间来回移动
		 * ，形成这样一个动画效果，这个只是告诉别人“我正在工作”，但不能提示工作进度到哪个阶段。主要是在进行一些无法确定操作时间的任务时作为提示
		 *false表示确定模式根据你实际的进度设置进度值*/


        builder.setView(v);
        // 取消更新
        builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    //URL url = new URL(mHashMap.get("url"));
                    //  URL url = new URL("http://openbox.mobilem.360.cn/index/d/sid/3570373");
                    //  URL url = new URL("http://openbox.mobilem.360.cn/index/d/sid/3891455");//六商360下载网址

                    apkUrl = MyCacheUtil.getshared(mContext).getString("apkUrl", "");
                    Log.e("apkUrl", apkUrl + "");
                    URL url = new URL(apkUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    //	File apkFile = new File(mSavePath, mHashMap.get("name"));
                    File apkFile = new File(mSavePath, "beixiang");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    ;

    /**
     * 安装APK文件
     */
    private void installApk() {
        //	File apkfile = new File(mSavePath, mHashMap.get("name"));
        File apkfile = new File(mSavePath, "beixiang");
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, "td.com.xiaoheixiong.fileprovider", apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
          //  intent.setFlags.(Intent.FLAG_ACTIVITY_NEW_TASK);
        }


        mContext.startActivity(intent);
    }
}
