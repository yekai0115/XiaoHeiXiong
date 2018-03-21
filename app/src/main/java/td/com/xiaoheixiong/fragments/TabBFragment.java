package td.com.xiaoheixiong.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.activity.BaoXiangActivity;
import td.com.xiaoheixiong.adapter.ToutiaoAdapter;
import td.com.xiaoheixiong.beans.Detail;
import td.com.xiaoheixiong.beans.JsonRootBean;
import td.com.xiaoheixiong.beans.TouTiaoBean;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;


public class TabBFragment extends BaseFragment {


    private TextView title_tv;
    private ImageView back_img, right_img;
    private RelativeLayout title_right_rl;
    private View view;
    public LayoutInflater mInflater;
    private XRecyclerView mRecyclerView;
    private ArrayList<HashMap<String, Object>> Listdata;
    private ToutiaoAdapter MainAdapter;
    private String pageNum = "1", pageSize = "100", MERCNUM;
    private ArrayList<HashMap<String, Object>> GridAllLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_b, container, false);
        mInflater = inflater;
        MERCNUM = MyCacheUtil.getshared(getActivity()).getString("MERCNUM", "");
        initview();
        getdata("");
        return view;
    }

    private void initview() {
        // TODO Auto-generated method stub
        Listdata = new ArrayList<>();
        //  GridAllLists = new ArrayList<>();
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        back_img = (ImageView) view.findViewById(R.id.back_img);
        back_img.setVisibility(View.GONE);
        title_right_rl = (RelativeLayout) view.findViewById(R.id.title_right_rl);
        right_img = (ImageView) view.findViewById(R.id.right_img);
        title_right_rl.setVisibility(View.GONE);
        title_tv.setText("头条");

        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(10));//设置item间距
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulse);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallPulse);
        mRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
        MainAdapter = new ToutiaoAdapter(getActivity(), Listdata, new RecyclerViewItemTouchListener() {

            @Override
            public void onItemTouch(View view, int position) {
                Log.e("position", position + "");
                int points = position - 1;
                if (Listdata.get(points).get("pic") != null && Listdata.get(points).get("pic").equals("1")) {
                //    Toast.makeText(getActivity(), "点击" + position, Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(getActivity(), BaoXiangActivity.class);
                    startActivity(it);
                }
            }

            @Override
            public void onTtemLongTouch(View view, int position) {
            }
        });
        mRecyclerView.setAdapter(MainAdapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getdata("");
                //  MainAdapter.notifyDataSetChanged();
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                mRecyclerView.loadMoreComplete();
                mRecyclerView.noMoreLoading();

            }
        });
    }

    private void getdata(String code) {
        showLoadingDialog("...");
        HashMap<String, Object> maps = new HashMap<>();
        // maps.put("mercId", MERCNUM);
        maps.put("mercId", "");
        maps.put("pageNum", pageNum);
        maps.put("pageSize", pageSize);

        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.XHX_toutiao, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        Map<String, Object> map = new HashMap<String, Object>();

                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            JSONObject oJSONdetail = JSON.parseObject(oJSON.get("detail") + "");
                            GridAllLists = new ArrayList<>();
                            GridAllLists = JSON.parseObject(oJSONdetail.get("list") + "", new TypeReference<ArrayList<HashMap<String, Object>>>() {
                            });
                            Log.e("GridAllLists", GridAllLists + "  " + GridAllLists.size());

                        }




                        if (GridAllLists.size() > 0) {
                            Listdata.clear();
                            for (int i = 0; i < GridAllLists.size(); i++) {
                                Listdata.add(GridAllLists.get(i));
                            }

                            int n = Listdata.size() / 5;
                            for (int i = 0; i < n; i++) {
                                HashMap<String, Object> picMap = new HashMap<String, Object>();
                                picMap.put("pic", "1");

                                int m = 5 * (i + 1) + i;
                                Listdata.add(m, picMap);
                                //       MainAdapter.notifyItemInserted(m);
                                //      MainAdapter.notifyItemRangeChanged(m, Listdata.size() - 5);
                                //   Listdata.add(m, picMap);
                            }
                        } else if (GridAllLists.size() == 0) {
                            Listdata.clear();
                        }
                        Log.e("Listdata pic", Listdata + "");
                       // Log.e("Listdata pic", Listdata.get(5) + "");
                        MainAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
