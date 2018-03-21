package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.AppManager;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;

public class BindWeixinMobileActivity extends BaseActivity {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.title_right_rl)
    RelativeLayout titleRightRl;
    @Bind(R.id.phone_et)
    EditText phoneEt;
    @Bind(R.id.password_et)
    EditText passwordEt;
    @Bind(R.id.call_et)
    EditText callEt;
    @Bind(R.id.yzm_et)
    EditText yzmEt;
    @Bind(R.id.yzm_tv)
    TextView yzmTv;
    @Bind(R.id.yzm_ll)
    LinearLayout yzmLl;
    @Bind(R.id.go_tv)
    TextView goTv;
    private int code = 0;
    private TimeCount time;
    private SharedPreferences.Editor editor;
    private String unionId, openId, deviceId, androidId, lng, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_weixin_mobile);
        ButterKnife.bind(this);
        editor = MyCacheUtil.setshared(this);
        unionId = MyCacheUtil.getshared(this).getString("unionId", "");
        openId = MyCacheUtil.getshared(this).getString("openId", "");
        deviceId = MyCacheUtil.getshared(this).getString("deviceId", "");
        androidId = MyCacheUtil.getshared(this).getString("androidId", "");
        lng = MyCacheUtil.getshared(this).getString("lng", "");
        lat = MyCacheUtil.getshared(this).getString("lat", "");

        initview();
    }

    private void initview() {
        titleTv.setText("绑定手机号");
        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    Getuserinfi();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void Getuserinfi() {
        showLoadingDialog("检测中...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("phoneNumber", phoneEt.getText() + "");

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_userInfoDtl, OkHttpClientManager.TYPE_GET, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");
                        loadingDialogWhole.dismiss();
                        com.alibaba.fastjson.JSONObject oJSON = JSON.parseObject(result + "");

                        // “000000” 成功 “VL0003”用户不存在 “VL0005” 用户已经退出登陆 其他 请求失败
                        if (oJSON.get("RSPCOD").equals("000000")) {// 跳转到积分主页
                            code = 1;
                            yzmLl.setVisibility(View.GONE);

                        } else {
                            code = 0;
                            yzmLl.setVisibility(View.VISIBLE);

                            //   Toast.makeText(BindWeixinMobileActivity.this, oJSON.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(BindWeixinMobileActivity.this, "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    @OnClick({R.id.back_img, R.id.yzm_tv, R.id.go_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                AppManager.getAppManager().finishAllActivity();
                Intent it = new Intent(this, LoginActivity.class);
                startActivity(it);
                finish();
                break;
            case R.id.yzm_tv:
                if (phoneEt.getText().length() != 11) {
                    Toast.makeText(getApplicationContext(), "请输入11位手机号码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                YzmGo();
                time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
                time.start();

                break;
            case R.id.go_tv:
                if (phoneEt.getText().length() != 11) {
                    Toast.makeText(getApplicationContext(), "请输入11位手机号码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwordEt.getText().length() < 6 || passwordEt.getText().equals("")) {
                    Toast.makeText(this, "请输入6-12位登录密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (code == 0) {
                    if (yzmEt.getText().equals("") || yzmEt.getText().length() <= 0) {
                        Toast.makeText(getApplicationContext(), "请输入验证码！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                BindMobile(code);
                break;
        }
    }

    private void BindMobile(int code) {
        showLoadingDialog("绑定中...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        //    phoneNumber	是	String	用户手机号
        //    msgCode	是	String	验证码
        //    openId	是	String	微信openId
        //   unionId	是	String	微信unionID

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("phoneNumber", phoneEt.getText());
        maps.put("password", passwordEt.getText());
        maps.put("openId", openId);
        maps.put("unionId", unionId);
        if (code == 0) {
            maps.put("msgCode", yzmEt.getText() + "");
        } else {
            maps.put("msgCode", "");
        }

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_Bind_phone, OkHttpClientManager.TYPE_GET, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");

                        JSONObject oJSONs = JSON.parseObject(result + "");
                        Log.e("oJSONs", oJSONs + "");
                        if (oJSONs.get("RSPCOD").equals("000000")) {
                            LoginGo();

                        } else {
                            Toast.makeText(getApplicationContext(), oJSONs.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getApplicationContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });

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
        String version = getVersion();

        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("phoneNumber", phoneEt.getText() + "");
        maps.put("password", passwordEt.getText() + "");
        maps.put("clientType", "1");
        maps.put("curVersion", version);
        maps.put("appType", "1");
        maps.put("openId", openId);
        maps.put("unionId", unionId);
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
                            Intent it = new Intent(BindWeixinMobileActivity.this, ViewPagerMainActivity.class);
                            startActivity(it);
                            finish();
                        } else {
                            Toast.makeText(BindWeixinMobileActivity.this, oJSON.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(BindWeixinMobileActivity.this, "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void YzmGo() {
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("phoneNumber", phoneEt.getText() + "");
        maps.put("type", "0");
        //   maps.put("timestamp", timestamp);
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_YZM, OkHttpClientManager.TYPE_GET, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");
                        JSONObject oJSONs = JSON.parseObject(result + "");
                        Log.e("result", result + "");

                        if (oJSONs.get("RSPCOD").equals("000000")) {
                            //   Map<String, Object> map = new HashMap<String, Object>();
                            //     map = JSON.parseObject(oJSONs.get("detail") + "", new TypeReference<Map<String, Object>>() {

                            //    });
                            Toast.makeText(getApplicationContext(), "验证码发送成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), oJSONs.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getApplicationContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            yzmTv.setText("重新验证");
            yzmTv.setClickable(true);
            yzmTv.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            yzmTv.setClickable(false);
            yzmTv.setEnabled(false);
            yzmTv.setText(millisUntilFinished / 1000 + "s");
        }
    }

}
