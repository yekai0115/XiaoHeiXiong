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
import td.com.xiaoheixiong.activity.ViewPagerMainActivity;

public class TabDFragment extends BaseFragment {
    private View view;
    private WebView webview;
    private HashMap<String, Object> map;
    private HashMap<String, String> maps = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_d, container, false);
        map = new HashMap<>();
        initview();
        return view;

    }

    @SuppressLint("NewApi")
    private void initview() {
        // TODO Auto-generated method stub

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

