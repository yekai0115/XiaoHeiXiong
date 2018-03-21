package td.com.xiaoheixiong.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.LQRPhotoSelectFragmentUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.OneButtonDialogWhite;
import td.com.xiaoheixiong.fragments.Merchants.MerchartBFragment;
import td.com.xiaoheixiong.fragments.TabCFragment;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.interfaces.PermissionListener;
import td.com.xiaoheixiong.wechatLogin.Config;
import td.com.xiaoheixiong.wechatLogin.OkHttpUtils;
import td.com.xiaoheixiong.wechatLogin.WeChatInfo;
import td.com.xiaoheixiong.wechatLogin.WxShareAndLoginUtils;

public class LoginActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback, PermissionListener {

    @Bind(R.id.phone_et)
    EditText phoneEt;
    @Bind(R.id.password_et)
    EditText passwordEt;
    @Bind(R.id.go_tv)
    TextView goTv;
    @Bind(R.id.Regist_tv)
    TextView RegistTv;
    @Bind(R.id.retri_pwd_tv)
    TextView retriPwdTv;
    @Bind(R.id.wechat_Login_img)
    TextView wechatLoginImg;
    private TextView title_tv;
    private ImageView back_img, right_img;
    private RelativeLayout title_right_rl;
    private String deviceId = "", androidId = "", lng = "", lat = "", version = "";
    private double latitude = 0.0;
    private double longitude = 0.0;
    private OneButtonDialogWhite button;
    private SharedPreferences.Editor editor;
    private IWXAPI iwxapi;
    public int WX_LOGIN = 1;
    private MyLocationListener myListeners = new MyLocationListener();
    private SensorManager mSensorManager;
    // 定位相关
    LocationClient mLocClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        editor = MyCacheUtil.setshared(this);

        initview();
        GetLatLng();
     //   GetPermisson();
        checkPermisson();
    }

    private void GetLatLng() {
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListeners);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3600 * 1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    private void GetPermisson() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).request();
    }

    private void initview() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        back_img = (ImageView) findViewById(R.id.back_img);
        title_right_rl = (RelativeLayout) findViewById(R.id.title_right_rl);
        right_img = (ImageView) findViewById(R.id.right_img);
        // right_img.setImageResource(R.mipmap.call_icons);

        title_tv.setText("登录");
        back_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent it = new Intent(LoginActivity.this, ViewPagerMainActivity.class);
                startActivity(it);
                finish();
            }
        });
        version = getVersion();
    }


    @OnClick({R.id.Regist_tv, R.id.retri_pwd_tv, R.id.wechat_Login_img, R.id.go_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Regist_tv:
                Intent it = new Intent(LoginActivity.this, RegisterAActivity.class);
                startActivity(it);
                finish();

                break;
            case R.id.retri_pwd_tv:
                Intent it1 = new Intent(LoginActivity.this, RetrievePasswordActivity.class);
                startActivity(it1);
                finish();
                break;
            case R.id.wechat_Login_img:
                //showLoadingDialog("登录中...");
                WxShareAndLoginUtils.WxLogin();
                finish();
                break;

            case R.id.go_tv:
                if (phoneEt.getText().length() != 11) {
                    Toast.makeText(getApplicationContext(), "请输入11位手机号码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwordEt.getText().length() <6) {
                    Toast.makeText(this, "请输入请输入6-12位登录密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginGo();

                break;
        }
    }

    private void LoginGo() {
        showLoadingDialog("登录中...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        //     phoneNumber		String	手机号	clientType 不为3时必填
        //    clientType	是	String	登录类别	1安卓 2 iOS 3微信
        //    password   String	密码	clientType 不为3时必填
        //    curVersion   String	版本号
        //     appType	是	String	客户端的版本	1安卓  2iOS
        //    openId		String	微信openid	clientType 为3时必填
        //     unionId		String	微信	clientType 为3时必填
        //    deviceId		String	设备id	用于判断当前登录设备与上次登录设备是否一致
        //    lng		String	经度
        //    lat		String	纬度


        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("phoneNumber", phoneEt.getText() + "");
        maps.put("password", passwordEt.getText() + "");
        maps.put("clientType", "1");
        maps.put("curVersion", version);
        maps.put("appType", "1");
        maps.put("openId", "");
        maps.put("unionId", "");
        maps.put("deviceId", deviceId);
        maps.put("lng", lng);
        maps.put("lat", lat);

        //  requestPostByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_Login, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        //    detail={"ALLOWBX":"01","EXTENDCOUNT":"0","ISAREAAGENT":"0","ISMER":"01","MERCNUM":"M0081744","MERSTS":"6",
                        // "NOCARDFEERATE":"0.5","NOTICEMESSAGE":"加油哟！您还差5个用户即可尊享0费率！","OEMID":"XhxiongOEM","PHONENUMBER":"13457816903",
                        // "STS":"2 ","TXNSTS":"1","agentId":"Age690315109265","logNum":"0"
                        Log.e("result", result + "");
                        Map<String, Object> map = new HashMap<String, Object>();

                        JSONObject oJSON = JSON.parseObject(result + "");
                        map = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<Map<String, Object>>() {
                        });

                        Log.e("IntegralLogin", map + "");
                        Log.e("oJSON", oJSON + "");
                        // “000000” 成功 “VL0003”用户不存在 “VL0005” 用户已经退出登陆 其他 请求失败
                        if (oJSON.get("RSPCOD").equals("000000")) {// 跳转到积分主页
                            editor.putString("ALLOWBX", map.get("ALLOWBX") + "");
                            editor.putString("EXTENDCOUNT", map.get("EXTENDCOUNT") + "");
                            editor.putString("ISAREAAGENT", map.get("ISAREAAGENT") + "");
                            editor.putString("ISMER", map.get("ISMER") + "");
                            editor.putString("MERCNUM", map.get("MERCNUM") + "");
                            editor.putString("MERSTS", map.get("MERSTS") + "");
                            editor.putString("ACTNAM", map.get("ACTNAM") + "");
                            editor.putString("ISAREAAGENT", map.get("ISAREAAGENT") + "");
                            editor.putString("ISMER", map.get("ISMER") + "");
                            editor.putString("isAgent", map.get("isAgent") + "");
                            editor.putString("agentLevel", map.get("agentLevel") + "");
                            editor.putString("headImgUrl", map.get("headImgUrl") + "");

                            editor.putString("NOCARDFEERATE", map.get("NOCARDFEERATE") + "");
                            editor.putString("NOTICEMESSAGE", map.get("NOTICEMESSAGE") + "");
                            editor.putString("OEMID", map.get("OEMID") + "");
                            editor.putString("PHONENUMBER", map.get("PHONENUMBER") + "");
                            editor.putString("STS", map.get("STS") + "");
                            editor.putString("TXNSTS", map.get("TXNSTS") + "");
                            editor.putString("agentId", map.get("agentId") + "");
                            editor.putString("logNum", map.get("logNum") + "");

                            editor.putString("lng", lng + "");
                            editor.putString("lat", lat + "");
                            editor.commit();

                            Intent it = new Intent(LoginActivity.this, ViewPagerMainActivity.class);
                            startActivity(it);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, oJSON.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(LoginActivity.this, "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public Map<String, Object> getl() {
        HashMap<String, Object> map = new HashMap<>();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return null;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else {
                LocationListener locationListener = new LocationListener() {

                    // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    // Provider被enable时触发此函数，比如GPS被打开
                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    // Provider被disable时触发此函数，比如GPS被关闭
                    @Override
                    public void onProviderDisabled(String provider) {

                    }

                    // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            //		Log.e("Map", "Location changed : Lat: " + location.getLatitude() + " Lng: "
                            //				+ location.getLongitude());
                            latitude = location.getLatitude(); // 纬度
                            longitude = location.getLongitude(); // 经度
                        }
                    }
                };
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
                Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location1 != null) {
                    latitude = location1.getLatitude(); // 纬度
                    longitude = location1.getLongitude(); // 经度
                    map.put("latitude", latitude);
                    map.put("longitude", longitude);
                }
            }
        }
        return map;
    }

    @PermissionSuccess(requestCode = 100)
    public void test() {
       /* Log.e("requestCode", "rrrrrr");
        if (!getl().isEmpty()) {
            //将GPS设备采集的原始GPS坐标转换成百度坐标
            //   CoordinateConverter converter = new CoordinateConverter();
            //   converter.from(CoordinateConverter.CoordType.GPS);
            //sourceLatLng待转换坐标 lng = "", lat
            //  converter.coord(new LatLng(latitude, longitude));
            //   LatLng desLatLng = converter.convert();
            //   lat = desLatLng.latitude + "";
            //   lng = desLatLng.longitude + "";
            lng = getl().get("longitude").toString();// 经度
            lat = getl().get("latitude").toString();// 纬度
            Log.e("", lng + "   " + lat);
        }
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        // final String tmDevice, tmSerial, tmPhone, androidId;
        String SerialNumber = tm.getSimSerialNumber();
        deviceId = tm.getDeviceId();

        androidId = android.provider.Settings.Secure.getString(getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        editor.putString("lng", getl().get("longitude").toString() + "");
        editor.putString("lat", getl().get("latitude").toString() + "");
        editor.putString("deviceId", deviceId);
        editor.putString("androidId", androidId);
        editor.commit();*/
    }

    @PermissionFail(requestCode = 100)
    private void test2() {
        button = new OneButtonDialogWhite(this, "为保证应用正常使用，需开启应用GPS和手机状态权限！", "前往设置", new OnMyDialogClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Uri packageURI = Uri.parse("package:" + "td.com.xiaoheixiong");
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                startActivity(intent);
                button.dismiss();
            }

        });
        button.setCancelable(false);
        button.setCanceledOnTouchOutside(false);
        button.show();
        return;
    }



    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息

            lng = location.getLongitude() + "";// 经度
            lat = location.getLatitude() + "";// 纬度
            Log.e("", lng + "   " + lat);

            final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

            // final String tmDevice, tmSerial, tmPhone, androidId;
        //    String SerialNumber = tm.getSimSerialNumber();
            try {
                deviceId = tm.getDeviceId();
            }catch (Exception e){

            }
            androidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            editor.putString("lng", lng);
            editor.putString("lat", lat);
            editor.putString("deviceId", deviceId);
            editor.putString("androidId", androidId);
            editor.commit();
        }
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

        }
    }


    //获得权限
    @Override
    public void onGranted(int type) {

    }

    // 权限被拒绝
    @Override
    public void onDenied(List<String> deniedPermission) {

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

}



