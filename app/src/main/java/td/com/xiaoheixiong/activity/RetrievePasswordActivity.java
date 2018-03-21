package td.com.xiaoheixiong.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
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


public class RetrievePasswordActivity extends BaseActivity {


    @Bind(R.id.phone_et)
    EditText phoneEt;
    @Bind(R.id.yzm_et)
    EditText yzmEt;
    @Bind(R.id.yzm_tv)
    TextView yzmTv;
    @Bind(R.id.go_tv)
    TextView goTv;
    @Bind(R.id.password_et)
    EditText passwordEt;
    private TextView title_tv;
    private ImageView back_img, right_img;
    private RelativeLayout title_right_rl;
    private SharedPreferences.Editor editor;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrievepassword);
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

        title_tv.setText("找回密码");
        back_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent it = new Intent(RetrievePasswordActivity.this, LoginActivity.class);
                startActivity(it);
                finish();
            }
        });


    }

    @OnClick({R.id.yzm_tv, R.id.go_tv})
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
                } else if (yzmEt.getText().equals("") || yzmEt.getText().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "请输入验证码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwordEt.getText().length() < 6) {
                    Toast.makeText(this, "请输入6-12位新密码！", Toast.LENGTH_SHORT).show();
                    return;
                }

                RetrieveGo();

                break;
        }
    }

    private void RetrieveGo() {
        showLoadingDialog("找回中...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        //     phoneNumber		String	手机号	clientType 不为3时必填
        //    msgCode	是	String	短信验证码
        //    newLoginPwd String	密码	clientType 不为3时必填
        //     appType	是	String	客户端的版本	1安卓  2iOS


        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("phoneNumber", phoneEt.getText());
        maps.put("msgCode", yzmEt.getText());
        maps.put("newLoginPwd", passwordEt.getText());
        maps.put("appType", "1");


        //  requestPostByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_RPWD, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        JSONObject oJSONs = JSON.parseObject(result + "");
                        Log.e("oJSONs", oJSONs + "");
                        if (oJSONs.get("RSPCOD").equals("000000")) {
                            Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(RetrievePasswordActivity.this, LoginActivity.class);
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
        maps.put("type", "1");
        //   maps.put("timestamp", timestamp);
        //  requestPostByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
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