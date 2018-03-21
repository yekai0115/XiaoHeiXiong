package td.com.xiaoheixiong.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.json.JSONObject;

import java.util.HashMap;

import td.com.xiaoheixiong.R;

public class TabFFragment extends BaseFragment {
    private View view;
    private WebView webview;
    public String userid = "", LoginMobile = "";
    private HashMap<String, Object> map;
    private HashMap<String, String> maps = new HashMap<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_f, container, false);
        map = new HashMap<>();
        initview();
        return view;

    }

    @SuppressLint("NewApi")
    private void initview() {
        // TODO Auto-generated method stub
     //   String url = HttpUrls.BXpoints_quick+"?models=Android";
        webview = (WebView) view.findViewById(R.id.webview);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setBuiltInZoomControls(true);// support zoom
        webview.getSettings().setUseWideViewPort(true);// 这个很关键
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 监听有需要再修改
        webview.addJavascriptInterface(new JsInteration(), "webData");
        if (Build.VERSION.SDK_INT >= 23) {// 这是处理HTTPS访问白屏，或者播放不了视频必须设置的
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    //    webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {
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

        });

        map.put("LoginMobile", LoginMobile);
        map.put("userid", userid);
        JSONObject json = (JSONObject) JSONObject.wrap(map);
        String mechartData = json + "";
        maps.put("mechartData", mechartData);
    }
    public class JsInteration {

        @JavascriptInterface
        public void storageData(String key, String json) {
            maps.put(key, json);
        }

        @JavascriptInterface
        public String readData(String key) {
            String resule;
            if (!maps.get(key).equals("")) {
                resule = maps.get(key);
            } else {
                resule = "";
            }
            return resule;
        }

        @JavascriptInterface
        public void comeback() {
            //	finish();
        }

        @JavascriptInterface
        private void call(String phone) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
}

