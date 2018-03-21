package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;

public class BaoXiangActivity extends BaseActivity {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.bx_img)
    ImageView bxImg;
    @Bind(R.id.out_img)
    ImageView outImg;
    @Bind(R.id.bx_rl)
    RelativeLayout bxRl;
    @Bind(R.id.Openbx_img)
    ImageView OpenbxImg;
    @Bind(R.id.OpenOut_img)
    ImageView OpenOutImg;
    @Bind(R.id.Openbx_rl)
    RelativeLayout OpenbxRl;
    @Bind(R.id.quan_img)
    ImageView quanImg;
    @Bind(R.id.quanOut_img)
    ImageView quanOutImg;
    @Bind(R.id.head_img)
    ImageView headImg;
    @Bind(R.id.kq_tv)
    TextView kqTv;
    @Bind(R.id.kqf_tv)
    TextView kqfTv;
    @Bind(R.id.use_tv)
    TextView useTv;
    @Bind(R.id.LookKQ_img)
    ImageView LookKQImg;
    @Bind(R.id.LookDP_img)
    ImageView LookDPImg;
    @Bind(R.id.getQuan_rl)
    RelativeLayout getQuanRl;
    @Bind(R.id.merchatName_tv)
    TextView merchatNameTv;
    private String mercId, lng, lat, merName, logo;
    private Map<String, Object> merMarkActivitmap;
    private RequestManager glideRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_xiang);
        ButterKnife.bind(this);
        mercId = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        lng = MyCacheUtil.getshared(this).getString("lng", "");
        lat = MyCacheUtil.getshared(this).getString("lat", "");
        initview();
    }

    private void initview() {
        merMarkActivitmap = new HashMap<String, Object>();
        titleTv.setText("领取宝物");
    }

    @OnClick({R.id.back_img, R.id.bx_img, R.id.out_img, R.id.OpenOut_img, R.id.quanOut_img, R.id.LookKQ_img, R.id.LookDP_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.bx_img:
                getData();
                break;
            case R.id.out_img:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.OpenOut_img:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.quanOut_img:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.LookKQ_img:
                Intent it = new Intent(this, MerMarkDetailsActivity.class);
                it.putExtra("markId", merMarkActivitmap.get("id") + "");
                startActivity(it);
                finish();
                break;
            case R.id.LookDP_img:
                break;
        }
    }

    private void getData() {
        showLoadingDialog("...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        //   "mercId":"*****",      用户唯一标识
        //  "lat":"1.3246546546541"     维度
        // "lng":"25245645645"    经度
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("mercId", mercId);
        maps.put("lng", lng);
        maps.put("lat", lat);


        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_GetGameMerMark, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        if (result == null) {
                            return;
                        }

                        Map<String, Object> mapAll = new HashMap<String, Object>();
                        Map<String, Object> Detailsmap = new HashMap<String, Object>();

                        mapAll = JSON.parseObject(result + "", new TypeReference<Map<String, Object>>() {
                        });
                        if (mapAll.get("RSPCOD").equals("000000")) {
                            bxRl.setVisibility(View.INVISIBLE);
                            Detailsmap = JSON.parseObject(mapAll.get("detail") + "", new TypeReference<Map<String, Object>>() {
                            });

                            merMarkActivitmap = JSON.parseObject(Detailsmap.get("merMarkActivit") + "", new TypeReference<Map<String, Object>>() {
                            });

                            merName = Detailsmap.get("merName") + "";
                            logo = Detailsmap.get("logo") + "";

                            //  id:1,
                            //   markTitle:"标题"，
                            //  "disAmount":"50" 优惠金额
                            //  "disRate":"5" 优惠比例
                            //   minAmount:"100" 最低使用金额
                            //  "validStartDate":"2018-1-20"  有效期开始时间
                            //  "validEndDate":"2018-1-21" 有效期结束时间
                            //  "markExplain":使用说明
                            inputData();

                        } else {
                            bxRl.setVisibility(View.INVISIBLE);
                            OpenbxRl.setVisibility(View.VISIBLE);
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

    private void inputData() {
        merchatNameTv.setText(merName);
        glideRequest = Glide.with(this);
        glideRequest.load(logo).transform(new GlideCircleTransform(this)).into(headImg);
        kqTv.setText(merMarkActivitmap.get("markTitle") + "");
        kqfTv.setText(merMarkActivitmap.get("markSubhead") + "");
        useTv.setText(merMarkActivitmap.get("markExplain") + "");
        getQuanRl.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
