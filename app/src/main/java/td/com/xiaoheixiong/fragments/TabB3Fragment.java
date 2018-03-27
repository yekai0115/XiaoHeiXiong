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

import butterknife.Bind;
import butterknife.ButterKnife;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GsonUtil;
import td.com.xiaoheixiong.Utils.JsonUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.activity.BaoXiangActivity;
import td.com.xiaoheixiong.adapter.MainAdapter;
import td.com.xiaoheixiong.adapter.ToutiaoAdapter;
import td.com.xiaoheixiong.beans.Detail;
import td.com.xiaoheixiong.beans.JsonRootBean;
import td.com.xiaoheixiong.beans.TouTiaoBean;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;
import td.com.xiaoheixiong.views.pulltorefresh.PullLayout;
import td.com.xiaoheixiong.views.pulltorefresh.PullableListView;


public class TabB3Fragment extends BaseFragment implements PullLayout.OnRefreshListener{


    private TextView title_tv;
    private ImageView back_img, right_img;
    private RelativeLayout title_right_rl;
    private View view;
    private PullLayout refresh_view;
    private PullableListView lv_toutiao;

    private MainAdapter adapter;
    private String  pageSize = "10", MERCNUM;
    private int pageNum = 1;
    int pages;


    private  List<TouTiaoBean> touTiaoBeanList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_b_new, container, false);
        MERCNUM = MyCacheUtil.getshared(getActivity()).getString("MERCNUM", "");
        initview();
        getdata(1);
        return view;
    }

    private void initview() {
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        back_img = (ImageView) view.findViewById(R.id.back_img);
        back_img.setVisibility(View.GONE);
        title_right_rl = (RelativeLayout) view.findViewById(R.id.title_right_rl);
        right_img = (ImageView) view.findViewById(R.id.right_img);
        title_right_rl.setVisibility(View.GONE);
        title_tv.setText("头条");
        refresh_view= (PullLayout) view.findViewById(R.id.refresh_view);
        refresh_view.setOnRefreshListener(this);
        lv_toutiao = (PullableListView) view.findViewById(R.id.lv_toutiao);

//        MainAdapter = new ToutiaoAdapter(getActivity(), Listdata, new RecyclerViewItemTouchListener() {
//
//            @Override
//            public void onItemTouch(View view, int position) {
//                Log.e("position", position + "");
//                int points = position - 1;
//                if (Listdata.get(points).get("pic") != null && Listdata.get(points).get("pic").equals("1")) {
//                //    Toast.makeText(getActivity(), "点击" + position, Toast.LENGTH_SHORT).show();
//                    Intent it = new Intent(getActivity(), BaoXiangActivity.class);
//                    startActivity(it);
//                }
//            }
//
//            @Override
//            public void onTtemLongTouch(View view, int position) {
//            }
//        });
        touTiaoBeanList=new ArrayList<>();
        adapter=new MainAdapter(getActivity(),touTiaoBeanList);
        lv_toutiao.setAdapter(adapter);

    }

    private void getdata(final int state) {
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

                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            JsonRootBean jsonRootBean= GsonUtil.GsonToBean(oJSON.toString(),JsonRootBean.class);
                            Detail detail=jsonRootBean.getDetail();
                            List<TouTiaoBean> list=  detail.getLists();
                             pages=  detail.getTotalPage();
                            if(state==1){
                                touTiaoBeanList.clear();
                            }else if(state==2){
                                touTiaoBeanList.clear();
                                refresh_view.refreshFinish(PullLayout.SUCCEED);
                            }else{
                                refresh_view.loadmoreFinish(PullLayout.SUCCEED);
                            }
                            touTiaoBeanList.addAll(list);
                        }
                        adapter.updataListView(touTiaoBeanList);
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Toast.makeText(getContext(), "网络不给力！", Toast.LENGTH_SHORT).show();


                        if(state==1){

                        }else if(state==2){
                            refresh_view.refreshFinish(PullLayout.FAIL);
                        }else{
                            refresh_view.loadmoreFinish(PullLayout.FAIL);
                            pageNum--;
                        }
                    }
                });
    }


    @Override
    public void onRefresh(PullLayout pullToRefreshLayout) {

        pageNum=1;
        getdata(2);
    }

    @Override
    public void onLoadMore(PullLayout pullToRefreshLayout) {
        if(pageNum>=pages){
            Toast.makeText(getContext(), "全部加载完成！", Toast.LENGTH_SHORT).show();
        }else{
            pageNum++;
            getdata(3);
        }

    }
}
