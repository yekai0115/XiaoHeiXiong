package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.adapter.MiaomiaoDetailsAdapter;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

public class MiaomiaoDetailsActivity extends BaseActivity {

    @Bind(R.id.XRecyclerView)
    XRecyclerView XRecyclerView;
    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    private MiaomiaoDetailsAdapter MiaomiaoAdapter;
    private ArrayList<HashMap<String, Object>> listData;
    LinearLayout more_ll;
    View footview;
    private String pageSize = "20", pageNum = "0", mercId, type;//type 1为妙秒，2为团团
    private String lng,lat,city;
    private LayoutInflater mInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miaomiao_details);
        ButterKnife.bind(this);
        mercId = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        Intent it = getIntent();
        type = it.getStringExtra("type");
        lng = it.getStringExtra("lng");
        lat = it.getStringExtra("lat");
        city = it.getStringExtra("city");

        mInflater = getLayoutInflater();
        initview();
        getData();
    }

    private void initview() {
        listData = new ArrayList<>();
        if (type.equals("1")) {
            titleTv.setText("秒秒");
        } else if (type.equals("1")) {
            titleTv.setText("团团");
        }

        XRecyclerView.addItemDecoration(new SpacesItemDecoration(1));//设置item间距
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        XRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulse);
        XRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallPulse);
        XRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
        XRecyclerView.setLayoutManager(layoutManager);
        MiaomiaoAdapter = new MiaomiaoDetailsAdapter(this, listData, new RecyclerViewItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                int points = position - 1;
                Intent it = new Intent(MiaomiaoDetailsActivity.this, MerMarkDetailsActivity.class);
                it.putExtra("markId", listData.get(points).get("id") + "");
                startActivity(it);
            }

            ;

            @Override
            public void onTtemLongTouch(View view, int position) {

            }
        });
        XRecyclerView.setAdapter(MiaomiaoAdapter);

    }

    private void getData() {
        showLoadingDialog("...");

        //   pageNum	是	String	当前页数
        //   pageSize	是	String	每页显示条数
        //  mercId	是	String	用户id
        //  markType	是	String 	卡劵类型
        int a = Integer.parseInt(pageNum);
        a = ++a;
        pageNum = String.valueOf(a);

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("pageNum", pageNum);
        maps.put("pageSize", pageSize);
        maps.put("mercId", mercId);
        maps.put("markType", type);
        maps.put("lng", lng);
        maps.put("lat", lat);
        maps.put("city", city);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_MerMarkList, OkHttpClientManager.TYPE_POST_JSON, maps,
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
                            ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                            HashMap<String, Object> map = new HashMap<>();

                            map = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<HashMap<String, Object>>() {
                            });
                            list = JSON.parseObject(map.get("list") + "", new TypeReference<ArrayList<HashMap<String, Object>>>() {
                            });

                            listData.clear();
                            if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    listData.add(list.get(i));
                                }
                            }
                            Log.e("listData", listData + "");
                            MiaomiaoAdapter.notifyDataSetChanged();
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

    @OnClick(R.id.back_img)
    public void onViewClicked() {
        finish();
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }

    }
}
