package td.com.xiaoheixiong;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.activity.BaseActivity;
import td.com.xiaoheixiong.activity.LoginActivity;
import td.com.xiaoheixiong.activity.ViewPagerMainActivity;
import td.com.xiaoheixiong.beans.MyConstant;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.interfaces.PermissionListener;

public class MainActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback, PermissionListener{
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        //  SDKInitializer.initialize(getApplication());
        setContentView(R.layout.activity_main);
        editor = MyCacheUtil.setshared(this);
        getVersionData();
        checkPermisson();
        getPicDpi();
    }

    private void getVersionData() {
        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("appType", "1");
        maps.put("mercId", "");
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_AppVersion , OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");

                        Map<String, Object> map = new HashMap<String, Object>();
                        map = JSON.parseObject(result + "", new TypeReference<Map<String, Object>>() {
                        });
                        JSONObject oJSON = JSON.parseObject(map.get("detail") + "");

                        Log.e("IntegralLogin", map + "");
                        Log.e("oJSON", oJSON + "");

                        if (map.get("RSPCOD").equals("000000")) {
                            editor.putString("apkNote", oJSON.get("apkNote") + "");//版本升级内容
                            editor.putString("apkUrl", oJSON.get("apkUrl") + ""); //安装包下载地址
                          //  editor.putString("appName", oJSON.get("appName") + ""); //app应用名称
                            editor.putString("isforce", oJSON.get("isforce") + "");//是否强制升级  0否  1是
                            editor.putString("versionCode", oJSON.get("versionCode") + "");//版本序列号
                            editor.putString("versionNo", oJSON.get("versionNo") + "");//版本号
                            editor.commit();
                        } else {
                            Toast.makeText(MainActivity.this, map.get("message") + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(MainActivity.this, "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void checkPermisson() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};
        requestRuntimePermission(permissions, this, 1);
    }

    // andrpoid 6.0 及以上需要写运行时权限
    public void requestRuntimePermission(String[] permissions,
                                         PermissionListener listener, int type) {

        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {// 如果permissionList不为空，说明需要申请这些权限
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]),
                    type);
        } else {//权限已有
            startMain();
        }
    }


    //获得权限
    @Override
    public void onGranted(int type) {
        startMain();
    }

    // 权限被拒绝
    @Override
    public void onDenied(List<String> deniedPermission) {
        startMain();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                String permission = permissions[i];
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission);
                }
            }
            if (deniedPermissions.isEmpty()) {
                onGranted(requestCode);
            } else {
                onDenied(deniedPermissions);
            }
        }
    }


    private void startMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //      Intent it = new Intent(MainActivity.this, ViewPagerMainActivity.class);LoginActivity
                //   Intent it = new Intent(MainActivity.this, LoginActivity.class);
                //   startActivity(it);
                //  finish();
                if (!MyCacheUtil.getshared(MainActivity.this).getString("PHONENUMBER","").equals("")) {
                    Intent it = new Intent(MainActivity.this, ViewPagerMainActivity.class);
                    startActivity(it);
                    finish();
                } else {
                    Intent it = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        }, 3500);
    }


    private void getPicDpi() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        double diagonalPixels = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        double screenSize = diagonalPixels / (160 * density);
        if (densityDpi == 240) {
            MyConstant.PIC_DPI2 = 1;
        } else if (densityDpi == 320) {
            MyConstant.PIC_DPI2 = 2;
        } else if (densityDpi == 480) {
            MyConstant.PIC_DPI2 = 3;
        }

    }


}
