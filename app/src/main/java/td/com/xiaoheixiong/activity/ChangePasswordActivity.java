package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import td.com.xiaoheixiong.Utils.AppManager;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;

public class ChangePasswordActivity extends BaseActivity {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.password_et)
    EditText passwordEt;
    @Bind(R.id.password_agin_et)
    EditText passwordAginEt;
    @Bind(R.id.go_tv)
    TextView goTv;
    @Bind(R.id.password_old_et)
    EditText passwordOldEt;
    private String userId;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        editor = MyCacheUtil.setshared(this);
        userId = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        initview();
    }

    private void initview() {
        titleTv.setText("修改登录密码");

    }

    @OnClick({R.id.back_img, R.id.go_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.go_tv:
                String newPassword = passwordEt.getText() + "";
                String newPasswordAgin = passwordAginEt.getText() + "";

                if (passwordOldEt.getText().length() < 6) {
                    Toast.makeText(this, "请输入6-12位旧密码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwordEt.getText().length() < 6) {
                    Toast.makeText(this, "请输入6-12位新密码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwordAginEt.getText().length() < 6) {
                    Toast.makeText(this, "请确认6-12位新密码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!newPassword.equals(newPasswordAgin)) {
                    Toast.makeText(this, "两次新密码不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }

                ChagePassword();

                break;
        }
    }

    private void ChagePassword() {

        showLoadingDialog("...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        // mercNum	是	String	商户号
        //   oldLoginPwd	是	String	旧密码
        // newLoginPwd
        // 是	String	新密码
        // sureLoginPwd	是	String	确认密码
        // appType	是	String	客户端的版本


        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("mercNum", userId);
        maps.put("oldLoginPwd", passwordOldEt.getText() + "");
        maps.put("newLoginPwd", passwordEt.getText() + "");
        maps.put("sureLoginPwd", passwordAginEt.getText() + "");
        maps.put("appType", "1");
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_change_login_pwd, OkHttpClientManager.TYPE_GET, maps,
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
                            editor.clear();
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "修改成功，请重新登陆！", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                            startActivity(it);
                            AppManager.getAppManager().finishAllActivity();

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
