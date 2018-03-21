package td.com.xiaoheixiong.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;

public class GetbackPayPasswordActivity extends BaseActivity {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.phone_et)
    EditText phoneEt;
    @Bind(R.id.yzm_et)
    EditText yzmEt;
    @Bind(R.id.yzm_tv)
    TextView yzmTv;
    @Bind(R.id.password_et)
    EditText passwordEt;
    @Bind(R.id.password_agin_et)
    EditText passwordAginEt;
    @Bind(R.id.go_tv)
    TextView goTv;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getback_pay_password);
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        titleTv.setText("找回支付密码");
    }

    @OnClick({R.id.back_img, R.id.yzm_tv, R.id.go_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
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
                } else if (yzmEt.getText().equals("") || yzmEt.getText().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "请输入验证码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwordEt.getText().length() !=6) {
                    Toast.makeText(this, "请输入新密码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwordAginEt.getText().length() !=6) {
                    Toast.makeText(this, "请确认新密码！", Toast.LENGTH_SHORT).show();
                    return;
                }else if (passwordEt.getText().equals(passwordAginEt.getText())){
                    Toast.makeText(this, "两次新密码不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }

                SetPassword();

                break;
        }
    }

    private void SetPassword() {
        showLoadingDialog("...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        //  "phoneNumber":"*****",     手机号
        // "msgCode":"123456", 验证码
        //  "newPayPwd":"123456ABC"     支付密码
        // "surePayPwd":"123456ABC"        确认支付密码
        // "appType":"2"    验证码类型  写死为2，2代表找回支付密码

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("phoneNumber", phoneEt.getText() + "");
        maps.put("msgCode", yzmEt.getText() + "");
        maps.put("newPayPwd", passwordEt.getText() + "");
        maps.put("surePayPwd", passwordAginEt.getText() + "");
        maps.put("appType", "2");
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_Getback_Paypwd, OkHttpClientManager.TYPE_GET, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        if (result == null) {
                            return;
                        }
                        JSONObject oJSONs = JSON.parseObject(result + "");
                        Log.e("oJSONs", oJSONs + "");
                        if (oJSONs.get("RSPCOD").equals("000000")) {
                            Toast.makeText(getApplicationContext(), "设置支付密码成功！", Toast.LENGTH_SHORT).show();
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
        maps.put("type", "2");
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
