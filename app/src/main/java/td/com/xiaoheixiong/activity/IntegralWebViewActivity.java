package td.com.xiaoheixiong.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.permissionManager.PermissionsCheckerUtil;
import td.com.xiaoheixiong.Utils.signUtil.MD5Util;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.OneButtonDialogWhite;
import td.com.xiaoheixiong.httpNet.HttpUrls;


@SuppressLint("JavascriptInterface")
public class IntegralWebViewActivity extends BaseActivity implements OnClickListener {
    private String URLs = "", type, mercId, phone;
    private String datas;
    private WebView wb_epos;
    private LinearLayout wechat, ciclefriend;
    private TextView tv_back, tv_title_contre;
    private Boolean state = true;
    private Map<String, String> maps = new HashMap<String, String>();// 存储数据
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private File cardPicFile;
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private Uri imageUri;
    private SharedPreferences.Editor editor;
    private OneButtonDialogWhite button;

    @Override
    @SuppressLint({"NewApi", "JavascriptInterface", "SetJavaScriptEnabled"})
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewintegral_activity);
        mercId = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        phone = MyCacheUtil.getshared(this).getString("PHONENUMBER", "");
        Log.e("mercId", mercId + "");
        Intent it = getIntent();
        String sign=mercId+phone+"XhxLitterBlackBear";
        sign=MD5Util.MD5(sign);
        if (mercId.equals("")){
            URLs = it.getStringExtra("url") + "/6/6";
        }else {
            URLs = it.getStringExtra("url") + "/" + mercId + "/" + phone+"/"+sign;
            URLs = it.getStringExtra("url") + "/" + mercId + "/" + phone;
        }
        wb_epos = (WebView) findViewById(R.id.wv_epos);
        wb_epos.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wb_epos.getSettings().setJavaScriptEnabled(true);
        wb_epos.getSettings().setSupportMultipleWindows(true);
        wb_epos.getSettings().setSupportZoom(true);
        wb_epos.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wb_epos.getSettings().setBuiltInZoomControls(true);// support zoom
        wb_epos.getSettings().setUseWideViewPort(true);// 这个很关键
        wb_epos.getSettings().setLoadWithOverviewMode(true);
        //   wb_epos.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        // 监听有需要再修改
        wb_epos.addJavascriptInterface(new JsInteration(), "webData");
        if (Build.VERSION.SDK_INT >= 23) {// 这是处理HTTPS访问白屏，或者播放不了视频必须设置的
            wb_epos.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        wb_epos.loadUrl(URLs);
        OnlineWebViewClient viewClient = new OnlineWebViewClient();
        wb_epos.setWebViewClient(viewClient);
        wb_epos.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (state) {
                    Log.e("title", title + "");
                    // tv_title_contre.setText(title);
                    // tv_title_contre.setVisibility(view.VISIBLE);
                }
            }


        });
        wb_epos.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (state) {
                    Log.e("title", title + "");
                    // tv_title_contre.setText(title);
                    // tv_title_contre.setVisibility(view.VISIBLE);
                }
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                // mUploadCallbackAboveL = filePathCallback;
                Log.e("t", "执行。。。。");
                mUploadCallbackAboveL = filePathCallback;
                //	TakingPictures();
                if (PermissionsCheckerUtil.lacksPermissions(IntegralWebViewActivity.this, Manifest.permission.CAMERA) || PermissionsCheckerUtil.lacksPermissions(IntegralWebViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    button = new OneButtonDialogWhite(IntegralWebViewActivity.this, "为保证应用正常使用，需开启应用相机和存储权限！", "前往设置", new OnMyDialogClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Uri packageURI = Uri.parse("package:" + "td.com.xiaoheixiong");
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                            Log.e("ddd", "jin....");
                            button.dismiss();
                        }

                    });
                    button.setCancelable(false);
                    button.setCanceledOnTouchOutside(false);
                    button.show();
                    return false;
                } else {
                    take();
                    return true;
                }
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                // mUploadMessage = uploadMsg;
                Log.e("t+", "执行。。。。");
                //	TakingPictures();
                take();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                // mUploadMessage = uploadMsg;
                Log.e("t++", "执行。。。。");
                //	TakingPictures();
                take();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                // mUploadMessage = uploadMsg;
                Log.e("t+++", "执行。。。。");
                take();
                //	TakingPictures();
            }

        });
        /*
         * tv_title_contre = (TextView) findViewById(R.id.tv_title_contre);
		 * tv_title_contre.setVisibility(View.GONE); tv_back = (TextView)
		 * findViewById(R.id.bt_title_left); tv_back.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Log.e("wb_epos", "wb_epos" +
		 * wb_epos.canGoBack()); state = wb_epos.canGoBack(); if
		 * (wb_epos.canGoBack()) { Log.e("goback", "goback"); wb_epos.goBack();
		 * } else { Log.e("finish", "finish"); finish();
		 * 
		 * } } });
		 */
    }


    public class OnlineWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                view.loadUrl(url);
            }
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // TODO Auto-generated method stub
            handler.proceed();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        state = wb_epos.canGoBack();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wb_epos.canGoBack()) {
                Log.e("goback", "goback");
                wb_epos.goBack();
            } else {
                Log.e("finish", "finish");
                finish();
            }
        }
        return state;
    }

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyApp");
        // Create the storage directory if it does not exist
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(
                imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        IntegralWebViewActivity.this.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", requestCode + "");
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL)
                return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                Log.e("result", result + "");
                if (result == null) {
                    // mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage = null;

                    Log.e("imageUri", imageUri + "");
                } else {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }

            }
        }
    }

    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null) {
            return;
        }
        Log.e("requestCode", requestCode + " " + resultCode);
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            Log.e("data", data + "  ");
            if (data == null) {
                results = new Uri[]{imageUri};

            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                Log.e("", dataString + "  " + clipData);
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

        return;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

	/*@Override
    protected void onPause() {
		// wb_epos.onPause();
		wb_epos.reload();
		super.onPause();
	}*/

    @Override
    protected void onResume() {
        wb_epos.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        wb_epos.setVisibility(View.GONE);
        // long timeout = ViewConfiguration.getZoomControlsTimeout();//timeout ==3000
        //Log.i("time==",timeout+"");
       /* new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                wb_epos.destroy();
            }
        }, timeout);*/
        super.onDestroy();
    }

    public class JsInteration {

        @JavascriptInterface
        public void comeback() {
            finish();
        }


    }
}
