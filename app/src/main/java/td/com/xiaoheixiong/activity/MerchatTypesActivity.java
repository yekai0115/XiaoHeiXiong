package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.adapter.MerchatTypesAdapter;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

public class MerchatTypesActivity extends BaseActivity {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.mechatName_et)
    EditText mechatNameEt;
    @Bind(R.id.go_tv)
    TextView goTv;
    @Bind(R.id.recyclerview)
    XRecyclerView recyclerview;
    private ArrayList<HashMap<String, Object>> listData;
    private List<Map<String, Object>> detailData;
    private MerchatTypesAdapter MainAdapter;
    private String mercId = "", lng = "", lat = "", pageSize = "20", pageNum = "0", city = "", subCataId = "";
    private LayoutInflater mInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchat_types);
        ButterKnife.bind(this);
        mercId = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        lng = MyCacheUtil.getshared(this).getString("lng", "");
        lat = MyCacheUtil.getshared(this).getString("lat", "");
        city = MyCacheUtil.getshared(this).getString("city", "");
        Intent it = getIntent();
        subCataId = it.getStringExtra("subCataId");

        getData("");
        initview();
    }

    private void initview() {
        listData = new ArrayList<>();
        detailData = new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setRefreshProgressStyle(ProgressStyle.BallPulse);
        recyclerview.setLaodingMoreProgressStyle(ProgressStyle.BallPulse);
        recyclerview.setArrowImageView(R.mipmap.iconfont_downgrey);
        Log.e("Listdata888", listData + "");
        MainAdapter = new MerchatTypesAdapter(this, listData, new RecyclerViewItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                Log.e("position", position + "");
                //    id=1, orgcode=20161202113010569.5516033072907
                int points = position - 1;
                Log.e("points", points + "");
                Log.e("id", listData.get(points).get("id") + "");
                Log.e("orgcode", listData.get(points).get("orgcode") + "");
                Intent it = new Intent(MerchatTypesActivity.this, MechatDetailsActivity.class);
                it.putExtra("mercId", listData.get(points).get("mercId") + "");
                it.putExtra("orgcode", listData.get(points).get("orgcode") + "");
                startActivity(it);

            }

            @Override
            public void onTtemLongTouch(View view, int position) {
            }
        });
        recyclerview.setAdapter(MainAdapter);
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (mechatNameEt.getText() != null && mechatNameEt.getText().length() > 0) {
                    getData(mechatNameEt.getText() + "");
                } else {
                    getData("");
                }
                MainAdapter.notifyDataSetChanged();
                recyclerview.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                recyclerview.loadMoreComplete();
                recyclerview.noMoreLoading();

            }
        });
    }

    private void getData(String type) {
        showLoadingDialog("...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        //    mercId	是	String	商户号
        //   city		String	城市名字	中文名字
        //  pageSize
        //  是	String	搜索的数量
        //   lng		String	经度
        //   lat		String	纬度
        //  merShortName		String	商户名字	 搜索条件
        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();


        int a = Integer.parseInt(pageNum);
        a = ++a;
        pageNum = String.valueOf(a);


        maps.put("mercId", mercId);
        maps.put("city", city);
        maps.put("pageSize", pageSize);
        maps.put("pageNum", pageNum);

        maps.put("lng", lng);
        maps.put("lat", lat);
        maps.put("merShortName", type);
        maps.put("subCataId", subCataId);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_Life_Circle, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        Map<String, Object> map = new HashMap<String, Object>();
                        if (result.equals("") || result == null) {
                            return;
                        }
                        Map<String, Object> mapAll = new HashMap<String, Object>();
                        mapAll = JSON.parseObject(result + "", new TypeReference<Map<String, Object>>() {
                        });
                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            detailData.clear();
                            ArrayList<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();

                            List<Map<String, Object>> detailData = new ArrayList<Map<String, Object>>();
                            Map<String, Object> pageInfo = new HashMap<String, Object>();
                            Log.e("detail", mapAll.get("detail") + "");
                            detailData = JSON.parseObject(mapAll.get("detail") + "", new TypeReference<List<Map<String, Object>>>() {
                            });
                            pageInfo = JSON.parseObject(detailData.get(0).get("pageInfo") + "", new TypeReference<Map<String, Object>>() {
                            });
                            lists = JSON.parseObject(pageInfo.get("list") + "", new TypeReference<ArrayList<HashMap<String, Object>>>() {
                            });
                           // detailData = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<TouTiaoBean<Map<String, Object>>>() {
                           // });
                          //  lists = JSON.parseObject(detailData.get(0).get("merInfos") + "", new TypeReference<ArrayList<HashMap<String, Object>>>() {
                          //  });
                            if (lists.size() > 0) {
                                listData.clear();
                                for (int i = 0; i < lists.size(); i++) {
                                    listData.add(lists.get(i));
                                }

                            }
                            Log.e("listData", listData + "");

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


    @OnClick({R.id.back_img, R.id.go_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.go_tv:
                Log.e("mechatNameEt.getText()",mechatNameEt.getText()+"44444444");
                if (mechatNameEt.getText().equals("") || mechatNameEt.getText().length() <= 0 ) {
                    Toast.makeText(getApplicationContext(), "请输入店铺名称！", Toast.LENGTH_SHORT).show();
                    return;
                }
                pageNum = "0";
                getData(mechatNameEt.getText() + "");

                break;
        }
    }


}
