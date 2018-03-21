package td.com.xiaoheixiong.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import java.util.Calendar;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.AppManager;
import td.com.xiaoheixiong.Utils.views.StatusBarUtil;
import td.com.xiaoheixiong.dialogs.LoadingDialogWhole;

/**
 * 应用程序Activity的基类
 */
public class BaseActivity extends AppCompatActivity {
    /** 记录日志的标记. */
    // private String TAG = BaseActivity.class.getSimpleName();
    /**
     * 保存主密钥的名字
     */
    protected final String BCTPAY_KEYS = "bctpaykeys";
    /**
     * 主密钥
     */
    protected final String MIAN_KEY = "miankey";
    /**
     * 网络监听IntentFilter对象
     */
    protected IntentFilter filter;
    /**
     * 当前的Context
     */
    protected Context nowContext;
    /**
     * 当前Activity
     */
    protected Activity activity = this;
    /**
     * 计数器
     */
    protected int timeCount;

    protected boolean timeFlag;
    // protected ScreenBroadcastReceiver mScreenBroadcastReceiver;
    protected LoadingDialogWhole loadingDialogWhole;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 获取当前的Context
        nowContext = this;

/*
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.titlie_green);*/
        //    settitle(R.color.titlie_green);
        AppManager.getAppManager().addActivity(this);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // // 关闭没有结束的广播
        // if (filter != null) {
        // nowContext.unregisterReceiver(broadcastReceiver);
        // }
        // nowContext.unregisterReceiver(rssiReceiver);
        timeFlag = false;
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    public abstract class NoDoubleClickListener implements OnClickListener {

        public static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNoDoubleClick(v);
            }
        }

        public void onNoDoubleClick(View arg0) {
            // TODO Auto-generated method stub

        }
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.red));
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
    }

    /**
     * 先加载数据显示dialog
     *
     * @param msg
     */
    public void showLoadingDialog(String msg) {
        loadingDialogWhole = new LoadingDialogWhole(this, R.style.CustomDialog, msg);
        loadingDialogWhole.setCancelable(false);
        loadingDialogWhole.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                } else {
                    return true; // 默认返回 false
                }
            }
        });
        loadingDialogWhole.setCanceledOnTouchOutside(false);
        loadingDialogWhole.show();
    }

    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "找不到版本号";
        }
    }
}
