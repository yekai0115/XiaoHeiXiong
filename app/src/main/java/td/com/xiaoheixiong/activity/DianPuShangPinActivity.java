package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.adapter.LanMuListViewAdapter;
import td.com.xiaoheixiong.adapter.ShangPinListAdapter;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

public class DianPuShangPinActivity extends BaseActivity {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;

    @Bind(R.id.tt_tv)
    TextView ttTv;
    @Bind(R.id.dianpu_xrv)
    XRecyclerView dianpuXrv;
    @Bind(R.id.lanmu_listview)
    ListView lanmuListview;
    private LanMuListViewAdapter TypeAdapter;
    private ShangPinListAdapter dianpuAdapter;
    private ArrayList<HashMap<String, Object>> typelist, shangpinlist;
    private String orgcode, pageNum = "1", pageSize = "20";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dian_pu_shang_pin);
        ButterKnife.bind(this);
        Intent it = getIntent();
        orgcode = it.getStringExtra("orgcode");
        initview();
        getTypesList();

    }

    private void initview() {
        typelist = new ArrayList<>();
        shangpinlist = new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        dianpuXrv.addItemDecoration(new SpacesItemDecoration(1));//设置item间距
        dianpuXrv.setLayoutManager(layoutManager);
        dianpuAdapter = new ShangPinListAdapter(this, shangpinlist, new RecyclerViewItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                Log.e("position", position + "");

            }

            @Override
            public void onTtemLongTouch(View view, int position) {

            }
        });
        dianpuXrv.setAdapter(dianpuAdapter);
        dianpuXrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //  pageNum = "0";
                //    MainAdapter.notifyDataSetChanged();
               // getShangpinList(typelist.get(0).get("id") + "");
             //   dianpuAdapter.notifyDataSetChanged();
                dianpuXrv.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                dianpuXrv.loadMoreComplete();
            //    dianpuXrv.noMoreLoading();

            }
        });

        TypeAdapter = new LanMuListViewAdapter(this, typelist);
        lanmuListview.setAdapter(TypeAdapter);
        lanmuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("onItemClick", "onItemClick: " + position);
                TypeAdapter.setEable(position);
                ttTv.setText(typelist.get(position).get("typeName") + "");
                getShangpinList(typelist.get(position).get("id") + "");
            }
        });
    }

    private void getTypesList() {
        showLoadingDialog("...");
        // "orgcode":""        商家唯一标识

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("orgcode", orgcode);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_goods_selectType, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result++type", result + "");
                        loadingDialogWhole.dismiss();
                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON != null) {

                            if (oJSON.get("RSPCOD").equals("000000")) {
                                Map<String, Object> detailmap = new HashMap<>();
                                Map<String, Object> markInfoVOmap = new HashMap<>();
                                ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                                list = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<ArrayList<HashMap<String, Object>>>() {
                                });

                         /*   markInfomap = new HashMap<>();

                            detailmap = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<Map<String, Object>>() {
                            });
                            markInfoVOmap = JSON.parseObject(detailmap.get("markInfoVO") + "", new TypeReference<Map<String, Object>>() {
                            });*/

                                if (list.size() > 0) {
                                    typelist.clear();
                                    for (int i = 0; i < list.size(); i++) {
                                        typelist.add(list.get(i));
                                    }
                                }
                                TypeAdapter.notifyDataSetChanged();
                            }
                            Log.e("typelist",typelist+"");
                            if (typelist.size()>0 && typelist != null) {
                                getShangpinList(typelist.get(0).get("id") + "");
                            }
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

    private void getShangpinList(String id) {
        //    "orgcode":""      商家唯一标识
        //  "goodsStatus":"0"       写死为1
        // "typeId":1              商品分类的id
        // "pageNum":"1"           当前页
        // "pageSize"："10" `       每页条数

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("orgcode", orgcode);
        maps.put("goodsStatus", "1");
        maps.put("typeId", id);
        maps.put("pageNum", pageNum);
        maps.put("pageSize", pageSize);
        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_goods_list, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result++type", result + "");
                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON != null) {

                            if (oJSON.get("RSPCOD").equals("000000")) {
                                Map<String, Object> DetailMap = new HashMap<String, Object>();
                                ArrayList<HashMap<String, Object>> godlist = new ArrayList<HashMap<String, Object>>();
                                DetailMap = JSON.parseObject(oJSON.get("detail") + "", new TypeReference<Map<String, Object>>() {
                                });
                                godlist = JSON.parseObject(DetailMap.get("list") + "", new TypeReference<ArrayList<HashMap<String, Object>>>() {
                                });
                                Log.e("godlist", godlist + "");
                                if (godlist.size() > 0) {
                                    shangpinlist.clear();
                                    for (int i = 0; i < godlist.size(); i++) {
                                        shangpinlist.add(godlist.get(i));
                                    }
                                }
                                Log.e("shangpinlist", shangpinlist + "");
                                dianpuAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getApplicationContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });
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

    @OnClick(R.id.back_img)
    public void onViewClicked() {
        finish();
    }
}
