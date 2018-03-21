package td.com.xiaoheixiong.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.SuccessOrFailDialog;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;

public class TiedCardRealNameActivity extends BaseActivity {


    @Bind(R.id.name_et)
    EditText nameEt;
    @Bind(R.id.sfz_et)
    EditText sfzEt;
    @Bind(R.id.bank_img)
    ImageView bankImg;
    @Bind(R.id.bank_sp)
    TextView bankSp;
    @Bind(R.id.phone_et)
    EditText phoneEt;
    @Bind(R.id.tv_go)
    Button tvGo;
    @Bind(R.id.bankName_ll)
    LinearLayout bankNameLl;
    @Bind(R.id.cardNum_et)
    EditText cardNumEt;
    @Bind(R.id.title_tv)
    TextView titleTv;
    private String mobile, mercnum;
    int imgid;
    private String bakname, c_mobile, c_sfzNum, c_name;

    private String banknum = "null";
    private TranslateAnimation taLeft, taRight, taTop, taBlow;
    public static Activity epayacticity;
    private String c_card;
    private String bank = "";
    private SuccessOrFailDialog SuccessDialog, FailDialog;
    private String[] b, c;
    private int[] d;
    private SharedPreferences.Editor editor;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savpay_activity);
        ButterKnife.bind(this);
        epayacticity = this;
        editor = MyCacheUtil.setshared(this);
        mercnum = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        mobile = MyCacheUtil.getshared(this).getString("PHONENUMBER", "");
        InitAnima();
        initview();

    }

    private void initview() {
        titleTv.setText("实名认证");
        findViewById(R.id.back_img).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    private void InitAnima() {
        // TODO Auto-generated method stub
        taLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        taRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        taTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        taBlow = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        taLeft.setDuration(1000);
        taRight.setDuration(1000);
        taTop.setDuration(1000);
        taBlow.setDuration(1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 3) {
            int id = data.getExtras().getInt("result");// 得到新Activity 关闭后返回的数据
            banknum = data.getStringExtra("banknum");
            bakname = data.getStringExtra("bankname");
            imgid = data.getExtras().getInt("imgid");
            bankImg.setImageResource(imgid);
            bankSp.setText(bakname);
        }
    }

    @SuppressWarnings("unchecked")
    protected void GetFtiedCardData() {
        showLoadingDialog("...");
        HashMap<String, Object> map = new HashMap<>();
        map.put("mercId", mercnum);
        map.put("authenMobile", c_mobile);
        map.put("authenIdcard", c_sfzNum);
        map.put("cardNum", c_card);
        map.put("actName", c_name);
        map.put("bankEn", banknum);
        map.put("bankName", bakname);

        //   参数名 是否必填 类型 名称 说明
        //    mercId 是 String 用户id  
        //   authenMobile 是 String 银行卡手机号  
        ///   authenIdcard 是 String 身份证号码  
        //   cardNum 是 String 银行卡号  
        //   actName     卡姓名  
        //   bankEn     银行编码  
        //   bankName 是 String 银行名称

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_Real_Name, OkHttpClientManager.TYPE_GET, map,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        Map<String, Object> map = new HashMap<String, Object>();
                        GetBankData(result);
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getApplicationContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected void GetBankData(Object result) {
        // TODO Auto-generated method stub
        if (result != null) {
            JSONObject jsonObj = JSONObject.parseObject(result + "");

            if (jsonObj.get("RSPCOD").equals("000000")) {
                editor.putString("STS", "0");

                editor.commit();

                SuccessDialog = new SuccessOrFailDialog(TiedCardRealNameActivity.this, "提交成功", jsonObj.get("RSPMSG") + "", "确定",
                        new OnMyDialogClickListener() {

                            @Override
                            public void onClick(View v) {
                                SuccessDialog.dismiss();
                                finish();
                            }
                        });
                SuccessDialog.show();

            } else {
                FailDialog = new SuccessOrFailDialog(TiedCardRealNameActivity.this, "提交失败",
                        jsonObj.get("RSPMSG") + "", "确定", new OnMyDialogClickListener() {

                    @Override
                    public void onClick(View v) {
                        FailDialog.dismiss();

                    }
                });
                FailDialog.show();
            }

        } else {
            Toast.makeText(TiedCardRealNameActivity.this, "系统错误！", Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick({R.id.bankName_ll, R.id.tv_go})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bankName_ll:
                Intent it = new Intent(TiedCardRealNameActivity.this, ChooseBankActivity.class);
                it.putExtra("tag", "1");
                startActivityForResult(it, 3);
                break;
            case R.id.tv_go:
                if (nameEt.getText().length() <=0 ) {
                    Toast.makeText(getApplicationContext(), "请输入您姓名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (sfzEt.getText().length() <=0) {
                    Toast.makeText(getApplicationContext(), "请输入身份证号码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (banknum.equals("null")) {
                    Toast.makeText(getApplicationContext(), "请先选择银行", Toast.LENGTH_SHORT).show();
                    return;
                } else if (cardNumEt.getText().length() <=0) {
                    Toast.makeText(getApplicationContext(), "请输入您本人储蓄卡号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (phoneEt.getText() == null || phoneEt.getText().length() != 11) {
                    Toast.makeText(getApplicationContext(), "请输入11位手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                c_name = nameEt.getText() + "";
                c_sfzNum = sfzEt.getText() + "";
                c_card = cardNumEt.getText() + "";
                c_mobile = phoneEt.getText() + "";
                GetFtiedCardData();
                break;
        }
    }


}
