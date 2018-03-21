package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.os.Bundle;
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
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;

public class SetOrChangePayPwdActivity extends BaseActivity {
    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.password_old_et)
    EditText passwordOldEt;
    @Bind(R.id.password_et)
    EditText passwordEt;
    @Bind(R.id.password_agin_et)
    EditText passwordAginEt;
    @Bind(R.id.go_tv)
    TextView goTv;
    private String userId, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_or_change_pay_pwd);
        ButterKnife.bind(this);
        Intent it = getIntent();
        code = it.getStringExtra("code");

        userId = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        initview();
    }

    private void initview() {
        if (code.equals("0")) {
            passwordOldEt.setVisibility(View.GONE);
            titleTv.setText("设置支付密码");

        } else if (code.equals("1")) {
            titleTv.setText("修改支付密码");
        }
    }

    @OnClick({R.id.back_img, R.id.go_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.go_tv:
                if (code.equals("1")) {
                    if (passwordOldEt.getText().length() != 6) {
                        Toast.makeText(this, "请输入6位旧密码！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (passwordEt.getText().length() != 6) {
                    Toast.makeText(this, "请输入6位新密码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwordAginEt.getText().length() != 6) {
                    Toast.makeText(this, "请确认6位新密码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwordEt.getText().equals(passwordAginEt.getText())) {
                    Toast.makeText(this, "两次新密码不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (code.equals("0")) {
                    SetPassword();
                } else if (code.equals("1")) {
                    ChangePassword();
                }
                break;
        }
    }

    private void SetPassword() {
        showLoadingDialog("...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        // mercNum	是	String	商户号

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("mercNum", userId);
        maps.put("actpw", passwordEt.getText() + "");
        maps.put("okActpw", passwordAginEt.getText() + "");
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_setPaypwd, OkHttpClientManager.TYPE_GET, maps,
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

    private void ChangePassword() {
        showLoadingDialog("...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        // mercNum	是	String	商户号
        //"oldActpw":"123456",    旧支付密码
        //"newActpw":"123456ABC"      支付密码
        //  "okNewActpw":"123456ABC"        确认支付密码
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("mercNum", userId);
        maps.put("oldActpw", passwordOldEt.getText() + "");
        maps.put("newActpw", passwordEt.getText() + "");
        maps.put("okNewActpw", passwordAginEt.getText() + "");
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_change_Paypwd, OkHttpClientManager.TYPE_GET, maps,
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
                            Toast.makeText(getApplicationContext(), "修改支付密码成功！", Toast.LENGTH_SHORT).show();
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
}
