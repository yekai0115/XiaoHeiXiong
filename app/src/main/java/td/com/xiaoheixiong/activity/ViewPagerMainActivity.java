package td.com.xiaoheixiong.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.AppManager;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.views.StatusBarUtil;
import td.com.xiaoheixiong.adapter.FragmentTabAdapter;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.TwoButtonDialogTitleWhite;
import td.com.xiaoheixiong.fragments.TabA2Fragment;
import td.com.xiaoheixiong.fragments.TabB2Fragment;
import td.com.xiaoheixiong.fragments.TabCFragment;
import td.com.xiaoheixiong.fragments.TabEFragment;
import td.com.xiaoheixiong.interfaces.PermissionListener;

public class ViewPagerMainActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback, PermissionListener {
    @Bind(R.id.tab_one)
    RadioButton tabOne;
    @Bind(R.id.tab_two)
    RadioButton tabTwo;

    @Bind(R.id.tab_thrid)
    RadioButton tabThrid;
    @Bind(R.id.tab_four)
    RadioButton tabFour;
    @Bind(R.id.tabs_rg)
    RadioGroup tabsRg;
    private FragmentTabAdapter tabAdapter;
    private List<RadioButton> Tabview;
    public List<Fragment> fragments = new ArrayList<Fragment>();
    private SharedPreferences.Editor editor;
    private TwoButtonDialogTitleWhite TwoButtonDialog;
    SupportMapFragment map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpmain);
        ButterKnife.bind(this);
        editor = MyCacheUtil.setshared(this);
        //  View cv = getWindow().getDecorView();
        //   cv.setPadding(0, StatusBarUtil.getStatusBarHeight(this),0,0);
        intiView();
        //  intiMap();
        //  settitle(R.color.transparent);
        checkPermisson();
        checkNewVersion();
    }

    @Override
    protected void setStatusBar() {
        //StatusBarUtil.setColor(this, getResources().getColor(R.color.titlie_green), 0);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.red), 0);
    }

    private void intiMap() {
        Intent intent = getIntent();
        MapStatus.Builder builder = new MapStatus.Builder();
        if (intent.hasExtra("x") && intent.hasExtra("y")) {
            // 当用intent参数时，设置中心点为指定点
            Bundle b = intent.getExtras();
            LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
            builder.target(p);
        }
        builder.overlook(-20).zoom(15);
        BaiduMapOptions bo = new BaiduMapOptions().mapStatus(builder.build())
                .compassEnabled(false).zoomControlsEnabled(false);
        map = SupportMapFragment.newInstance(bo);
    }

    private void intiView() {
        Tabview = new ArrayList<RadioButton>();
        Tabview.add(tabOne);
        Tabview.add(tabTwo);
        // Tabview.add(tabAdd);
        Tabview.add(tabThrid);
        Tabview.add(tabFour);
        fragments.add(new TabA2Fragment());
        fragments.add(new TabB2Fragment());
        fragments.add(new TabCFragment());
        //   fragments.add(new TabDFragment());
        fragments.add(new TabEFragment());

        tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content, tabsRg, Tabview);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @SuppressWarnings("static-access")
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {

            }
        });

    }


    @OnClick({R.id.tab_content, R.id.tab_one, R.id.tab_two, R.id.tab_thrid, R.id.tab_four, R.id.tabs_rg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab_content:
                break;
            case R.id.tab_one:
                break;
            case R.id.tab_two:
                break;

            case R.id.tab_thrid:
                break;
            case R.id.tab_four:
                String MOBILE = MyCacheUtil.getshared(this).getString("PHONENUMBER", "");
                Log.e("MOBILE++++", MOBILE + "   88");
                if (MOBILE.equals("") || MOBILE == null) {
                    Intent it = new Intent(this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
                break;
            case R.id.tabs_rg:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TwoButtonDialog = new TwoButtonDialogTitleWhite(ViewPagerMainActivity.this, "提示", "确定要退出吗！", "取消", "确定",

                    new OnMyDialogClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.btn_left:
                                    // 显示下载对话框
                                    TwoButtonDialog.dismiss();
                                    break;
                                case R.id.btn_right:
                                    TwoButtonDialog.dismiss();
                                    AppManager.getAppManager().AppExit(getApplicationContext());
                                    break;
                            }
                        }

                    });
            TwoButtonDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void checkPermisson() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
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

        }
    }


    //获得权限
    @Override
    public void onGranted(int type) {

    }

    // 权限被拒绝
    @Override
    public void onDenied(List<String> deniedPermission) {
        checkPermisson();
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


    private void checkNewVersion() {
        PgyUpdateManager.register(ViewPagerMainActivity.this,
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        new AlertDialog.Builder(ViewPagerMainActivity.this)
                                .setTitle("更新")
                                .setMessage("发现新版本啦,请立即更新！")
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String url;
                                                JSONObject jsonData;
                                                try {
                                                    jsonData = new JSONObject(result);
                                                    if ("0".equals(jsonData.getString("code"))) {
                                                        JSONObject jsonObject = jsonData.getJSONObject("data");
                                                        url = jsonObject.getString("downloadURL");
                                                        startDownloadTask(ViewPagerMainActivity.this, url);
                                                    }
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated
                                                    // catch block
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {

                    }
                });
    }

}



