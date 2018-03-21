package td.com.xiaoheixiong.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.views.StatusBarUtil;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;


public class RegisterAActivity extends BaseActivity {


    EditText companyNameEt;
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
    @Bind(R.id.go_tv)
    TextView goTv;
    @Bind(R.id.read_cb)
    CheckBox readCb;
    @Bind(R.id.xieyi_tv)
    TextView xieyiTv;
    private TextView title_tv;
    private ImageView back_img, right_img;
    private RelativeLayout title_right_rl;
    private SharedPreferences.Editor editor;
    private TimeCount time;
    private String num = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        ButterKnife.bind(this);
        editor = MyCacheUtil.setshared(this);
        intiView();
    }

    @Override
    protected void setStatusBar() {
        // super.setStatusBar();
        //  StatusBarUtil.setTransparent(this);
        //   StatusBarUtil.setTranslucent(this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
        //   StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.red), 0);
    }

    private void intiView() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        back_img = (ImageView) findViewById(R.id.back_img);
        title_right_rl = (RelativeLayout) findViewById(R.id.title_right_rl);
        right_img = (ImageView) findViewById(R.id.right_img);
        // right_img.setImageResource(R.mipmap.call_icons);

        title_tv.setText("注册");
        back_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent it = new Intent(RegisterAActivity.this, LoginActivity.class);
                startActivity(it);
                finish();
            }
        });

        readCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    num = "1";
                    Log.e("num", num);
                } else {
                    num = "0";
                    Log.e("num", num);
                }
            }
        });

    }

    @OnClick({R.id.yzm_tv, R.id.go_tv, R.id.xieyi_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
                } else if (passwordEt.getText().length() < 6) {
                    Toast.makeText(this, "请输入请输入6-12位登录密码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (yzmEt.getText().equals("") || yzmEt.getText().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "请输入验证码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (num.equals("0")) {
                    Toast.makeText(getApplicationContext(), "请勾选阅读条款！", Toast.LENGTH_SHORT).show();
                    return;
                }
                RegisterGo();

                break;
            case R.id.xieyi_tv:
                Intent it = new Intent(this, IntegralWebViewActivity.class);
                it.putExtra("url", HttpUrls.XHX_privacyPolicy);
                startActivity(it);
                break;
        }
    }


    private void RegisterGo() {
        showLoadingDialog("注册中...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);


        //   phoneNumber	是	String	手机号	商家的手机号
        //    msgCode	是	String	验证码
        //   password	是	String	密码
        //   parentMobile		String	上级手机号
        //   parentMercNum		String	上级商户号
        //   openId		String	微信id
        //   unionId		String	微信


        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("phoneNumber", phoneEt.getText());
        maps.put("msgCode", yzmEt.getText());
        maps.put("password", passwordEt.getText());
        //  maps.put("parentMobile", callEt.getText());
        maps.put("parentMercNum", "");
        maps.put("validCode", yzmEt.getText());
        maps.put("openId", "");
        maps.put("unionId", "");

        //  requestPostByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_Register, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");

                        JSONObject oJSONs = JSON.parseObject(result + "");
                        Log.e("oJSONs", oJSONs + "");
                        if (oJSONs.get("RSPCOD").equals("000000")) {
                            Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(RegisterAActivity.this, LoginActivity.class);
                            startActivity(it);
                            finish();

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