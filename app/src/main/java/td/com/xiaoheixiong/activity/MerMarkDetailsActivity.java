package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GetDateUtils;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.adapter.KaquanAdapter;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.OneButtonDialogWhite;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

public class MerMarkDetailsActivity extends BaseActivity {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.go_tv)
    TextView goTv;
    @Bind(R.id.MechatDetail_tv)
    TextView MechatDetailTv;

    private CountdownView timeCountdownView;
    private ImageView headImg;
    private TextView KJTitleTv, KJFTitleTv, djqNumTv, contentTv, Time_limit_tv, quan_tv;
    private List<View> viewList;
    private View view;
    private LayoutInflater mInflater;
    private String markId, mercId, MechatId, orgcode;//MechatId商家ID，mercId用户ID
    private ArrayList<HashMap<String, Object>> listData;
    private KaquanAdapter MainAdapter;
    private OneButtonDialogWhite button;
    private Map<String, Object> markInfomap;
    private String shopCompanyName, headImgUrl;
    private RequestManager glideRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kjuan_details);
        ButterKnife.bind(this);
        mInflater = getLayoutInflater();
        mercId = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        Intent it = getIntent();
        markId = it.getStringExtra("markId");
        initview();
        getData();
    }

    private void initview() {
        listData = new ArrayList<>();
        viewList = new ArrayList<>();
        view = mInflater.inflate(R.layout.item_kj_head, null);
        timeCountdownView = (CountdownView) view.findViewById(R.id.time_CountdownView);
        headImg = (ImageView) view.findViewById(R.id.head_img);
        KJTitleTv = (TextView) view.findViewById(R.id.KJ_title_tv);
        KJFTitleTv = (TextView) view.findViewById(R.id.KJF_title_tv);
        djqNumTv = (TextView) view.findViewById(R.id.djq_num_tv);
        contentTv = (TextView) view.findViewById(R.id.content_tv);
        quan_tv = (TextView) view.findViewById(R.id.quan_tv);

        Time_limit_tv = (TextView) view.findViewById(R.id.Time_limit_tv);
        if (view != null) {
            viewList.add(view);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //   MainAdapter = new HomeAdapter(getActivity(), listData, viewList);
      /*  MainAdapter = new HomeAdapter(getActivity(), datalist, viewList,new RecyclerViewItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                Log.e("position", position + "");

            }

            @Override
            public void onTtemLongTouch(View view, int position) {

            }
        });*/

        MainAdapter = new KaquanAdapter(this, listData, viewList, new RecyclerViewItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                Log.e("position", position + "");

            }

            @Override
            public void onTtemLongTouch(View view, int position) {

            }
        });
        recyclerView.setAdapter(MainAdapter);


    }

    private void getData() {
        showLoadingDialog("...");
        // markId	是	String	卡劵id
        // mercId	是	String	用户id

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("markId", markId);
        maps.put("mercId", mercId);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_MarkCardDtl, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");
                        loadingDialogWhole.dismiss();
                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON == null) {
                            return;
                        }
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            Map<String, Object> detailmap = new HashMap<>();
                            Map<String, Object> markInfoVOmap = new HashMap<>();
                            markInfomap = new HashMap<>();
                            ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                            detailmap = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<Map<String, Object>>() {
                            });
                            markInfomap = JSON.parseObject(detailmap.get("markInfo") + "", new TypeReference<Map<String, Object>>() {
                            });
                            markInfoVOmap = JSON.parseObject(detailmap.get("markInfoVO") + "", new TypeReference<Map<String, Object>>() {
                            });
                            MechatId = markInfoVOmap.get("mercId") + "";
                            orgcode = markInfoVOmap.get("orgcode") + "";
                            if (detailmap.get("commList") != null && !detailmap.get("commList").equals("") ) {
                                list = JSON.parseObject(detailmap.get("commList") + "", new TypeReference<ArrayList<HashMap<String, Object>>>() {
                                });
                            }
                            //    shopCompanyName	String	店铺简称
                            //     mainImgUrl	String	店铺主图
                            //   headImgUrl	String	店铺logo

                            shopCompanyName = detailmap.get("shopCompanyName") + "";
                            headImgUrl = detailmap.get("headImgUrl") + "";


                            listData.clear();
                            if (viewList != null && viewList.size() > 0) {//关键
                                for (int i = 0; i < viewList.size(); i++) {
                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    listData.add(map);
                                }
                            }
                            if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    listData.add(list.get(i));
                                }
                            }
                            Log.e("listData", listData + "");
                            initData();
                            MainAdapter.notifyDataSetChanged();

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

    private void initData() {
        //   markType	String	卡劵类型0代金券  1折扣卷
        //  markTitle	String	标题
        //   markSubhead	String	副标题
        //   disAmount	String	优惠金额
        //  mainImgUrl	String	店铺主图
        //disRate	String	折扣比例

        glideRequest = Glide.with(this);
        Log.e("headImgUrl", headImgUrl + "");

        glideRequest.load(headImgUrl).transform(new GlideCircleTransform(this)).into(headImg);

        //  Glide.with(this).load(headImgUrl).asBitmap().into(headImg);
        titleTv.setText(markInfomap.get("markTitle") + "");
        String markType = markInfomap.get("markType") + "";
        String money = markInfomap.get("disAmount") + "";
        String Enddate = markInfomap.get("validEndDate") + "";
        String Startdate = markInfomap.get("validStartDate") + "";

        String date = Enddate + " " + "23:59:59";//"yyyy-MM-dd HH:mm:ss")
        int time = GetDateUtils.getTimeInterval(date);//获取时间差，单位秒
        Log.e("time", time + "");
        //    long time = (long) days * 24 * 60 * 60;
        long times = (long) time * 1000;
        timeCountdownView.start(times);

        KJTitleTv.setText(markInfomap.get("markTitle") + "");
        KJFTitleTv.setText(markInfomap.get("markSubhead") + "");
        if (markType.equals("0")) {
            djqNumTv.setText(money + "元代金券");
            quan_tv.setText("代金券");
        } else if (markType.equals("1")) {
            if (!markInfomap.get("disRate").equals("null") && markInfomap.get("disRate") != null) {
                BigDecimal Umoney = new BigDecimal(markInfomap.get("disRate") + "");
                final BigDecimal hongbaoMon = Umoney.divide(new BigDecimal("10")).setScale(1);
                String disRate = hongbaoMon.toString();
                djqNumTv.setText(disRate + "折扣券");
                quan_tv.setText("折扣券");
            }
        } else if (markType.equals("3")) {//游戏
            djqNumTv.setText(money + "元代金券");
            quan_tv.setText("代金券");
            goTv.setVisibility(View.INVISIBLE);
        } else if (markType.equals("4")) {//游戏
            if (!markInfomap.get("disRate").equals("null") && markInfomap.get("disRate") != null) {
                BigDecimal Umoney = new BigDecimal(markInfomap.get("disRate") + "");
                final BigDecimal hongbaoMon = Umoney.divide(new BigDecimal("10")).setScale(1);
                String disRate = hongbaoMon.toString();
                djqNumTv.setText(disRate + "折扣券");
                quan_tv.setText("折扣券");
                goTv.setVisibility(View.INVISIBLE);
            }

        }
        contentTv.setText(markInfomap.get("markExplain") + "");
        Time_limit_tv.setText("有效期：" + Startdate + "至" + Enddate);
    }

    @OnClick({R.id.back_img, R.id.go_tv, R.id.MechatDetail_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.go_tv:
                GetMerMark();

                break;
            case R.id.MechatDetail_tv:
                Log.e("点击。。。", "点击。。。");
                Intent it = new Intent(this, MechatDetailsActivity.class);
                it.putExtra("mercId", MechatId);
                it.putExtra("orgcode", orgcode);
                startActivity(it);
                finish();
                break;

        }
    }

    private void GetMerMark() {
        // markId	是	String	卡劵id
        // mercId	是	String	用户id

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("markId", markId);
        maps.put("mercId", mercId);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_GetMerMark, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");

                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON == null) {
                            return;
                        }
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            button = new OneButtonDialogWhite(MerMarkDetailsActivity.this, oJSON.get("RSPMSG") + "",
                                    "确定", new OnMyDialogClickListener() {
                                @Override
                                public void onClick(View v) {
                                    button.dismiss();
                                }
                            });
                            button.show();
                        } else {
                            button = new OneButtonDialogWhite(MerMarkDetailsActivity.this, oJSON.get("RSPMSG") + "",
                                    "确定", new OnMyDialogClickListener() {
                                @Override
                                public void onClick(View v) {
                                    button.dismiss();
                                }
                            });
                            button.show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getApplicationContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
