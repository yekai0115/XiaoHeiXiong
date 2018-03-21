package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.adapter.AppraiseInfoAdapter;
import td.com.xiaoheixiong.adapter.MainRecyclerViewAdapter;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

public class AppraiseInfoActivity extends BaseActivity {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.close_tv)
    TextView closeTv;
    @Bind(R.id.recyclerview)
    XRecyclerView recyclerview;
    private AppraiseInfoAdapter MainAdapter;
    private List<Map<String, Object>> listData, Appraiselist;
    private String pageNum = "0", pageSize = "10", orgcode, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise_info);
        ButterKnife.bind(this);
        phone = MyCacheUtil.getshared(this).getString("PHONENUMBER", "");
        Intent it = getIntent();
        orgcode = it.getStringExtra("orgcode");
        getAppraiseData();
        initview();
    }

    private void initview() {
        listData = new ArrayList<>();
        titleTv.setText("评论");
        closeTv.setText("发布");
        closeTv.setTextColor(getResources().getColor(R.color.red));
        closeTv.setVisibility(View.VISIBLE);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.equals("") || phone == null) {
                    Intent it = new Intent(AppraiseInfoActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                } else {
                    Intent it = new Intent(AppraiseInfoActivity.this, AppraiseActivity.class);
                    it.putExtra("orgcode", orgcode);
                    startActivity(it);
                }


            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setRefreshProgressStyle(ProgressStyle.BallPulse);
        recyclerview.setLaodingMoreProgressStyle(ProgressStyle.BallPulse);
        recyclerview.setArrowImageView(R.mipmap.iconfont_downgrey);
        MainAdapter = new AppraiseInfoAdapter(AppraiseInfoActivity.this, listData, new RecyclerViewItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
             /*   Log.e("position", position + "");
                //    id=1, orgcode=20161202113010569.5516033072907
                int points = position - 2;
                Log.e("points", points + "");
                Log.e("id", listData.get(points).get("id") + "");
                Log.e("orgcode", listData.get(points).get("orgcode") + "");
                Intent it = new Intent(AppraiseInfoActivity.this, MechatDetailsActivity.class);
                it.putExtra("mercId", listData.get(points).get("mercId") + "");
                it.putExtra("orgcode", listData.get(points).get("orgcode") + "");
                startActivity(it);*/

            }

            @Override
            public void onTtemLongTouch(View view, int position) {
            }
        });
        recyclerview.setAdapter(MainAdapter);
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
              //  pageNum = "0";
                getAppraiseData();
            //    MainAdapter.notifyDataSetChanged();
                recyclerview.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                getAppraiseData();
                recyclerview.loadMoreComplete();
                recyclerview.noMoreLoading();

            }
        });

    }

    private void getAppraiseData() {
        showLoadingDialog("。。。");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        //   associationId //被评论的对象ID(必填)
        //  evaluationType //评论类型：0是商家 1是头条
        //pageNum //当前页码
        //         pageSize // 每页记录数
        int a = Integer.parseInt(pageNum);
        a = ++a;
        pageNum = String.valueOf(a);

        HashMap<String, Object> maps = new HashMap<>();

        maps.put("associationId", orgcode);
        maps.put("evaluationType", "0");
        maps.put("pageNum", pageNum);
        maps.put("pageSize", pageSize);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_Appraise_Content, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");

                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            //     "amount":2, //评论数量
                            //          "avg_grade":3.5 //评分
                            Map<String, Object> DetailMap = new HashMap<String, Object>();
                            Appraiselist = new ArrayList<Map<String, Object>>();

                            DetailMap = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<Map<String, Object>>() {
                            });
                            Appraiselist = JSON.parseObject(DetailMap.get("list") + "", new TypeReference<List<Map<String, Object>>>() {
                            });
                            Log.e("Appraiselist", Appraiselist + "");
                            if (Appraiselist.size() > 0) {
                             //   listData.clear();
                                for (int i = 0; i < Appraiselist.size(); i++) {
                                    listData.add(Appraiselist.get(i));
                                }
                            }
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
}
