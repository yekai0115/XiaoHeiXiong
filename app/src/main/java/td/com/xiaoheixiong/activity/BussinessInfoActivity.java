package td.com.xiaoheixiong.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;

public class BussinessInfoActivity extends BaseActivity {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.title_right_rl)
    RelativeLayout titleRightRl;
    @Bind(R.id.info_name_tv)
    TextView infoNameTv;
    @Bind(R.id.card_id_tv)
    TextView cardIdTv;
    @Bind(R.id.bank_name_tv)
    TextView bankNameTv;
    @Bind(R.id.sfz_id_tv)
    TextView sfzIdTv;
    @Bind(R.id.mobile_tv)
    TextView mobileTv;
    @Bind(R.id.my_frend)
    TextView myFrend;
    private String phone;
    Map<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_info);
        ButterKnife.bind(this);
        phone = MyCacheUtil.getshared(this).getString("PHONENUMBER", "");
        titleTv.setText("用户信息");
        getData();
    }

    private void getData() {
        showLoadingDialog("...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("phoneNumber", phone);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_userInfoDtl, OkHttpClientManager.TYPE_GET, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");
                        loadingDialogWhole.dismiss();
                        com.alibaba.fastjson.JSONObject oJSON = JSON.parseObject(result + "");

                        if (oJSON.get("RSPCOD").equals("000000")) {
                            map = new HashMap<String, Object>();
                            map = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<Map<String, Object>>() {
                            });
                            initData();
                        } else {

                            //   Toast.makeText(BindWeixinMobileActivity.this, oJSON.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(BussinessInfoActivity.this, "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void initData() {
        //  mercnum	String	用户id
        //   mercnam	String	用户别名
        //   status	String	0正常
        //   mersts	String	0表示实名通过
        //  applydat	String	注册日期yyyyMMddHHmmss
        //   actnam	String	用户实名认证姓名
        //  merphonenumber	String	手机号
        //   actno		银行卡号
        //  parentName		推荐人
        //  opnbnk		银行名称
        //  corporateidentity 身份证
        infoNameTv.setText(map.get("mercnam") + "");
        if (map.get("actno") != null && !map.get("actno").equals("")) {
            String card = map.get("actno").toString();
            String setpcard = card.substring(0, 6);
            String getpcard = card.substring(card.length() - 4);
            cardIdTv.setText(setpcard + "*************" + getpcard);
        }
        bankNameTv.setText(map.get("opnbnk") + "");
        if (map.get("corporateidentity") != null && !map.get("corporateidentity").equals("")) {
            String card = map.get("corporateidentity").toString();
            String setpcard = card.substring(0, 4);
            String getpcard = card.substring(card.length() - 3);
            sfzIdTv.setText(setpcard + "***********" + getpcard);
        }
        mobileTv.setText(map.get("merphonenumber") + "");
        if (map.get("parentName") != null) {
            myFrend.setText(map.get("parentName") + "");
        }

    }

    @OnClick(R.id.back_img)
    public void onViewClicked() {
        finish();
    }
}
