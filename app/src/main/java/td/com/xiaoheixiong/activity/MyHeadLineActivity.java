package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jaiky.imagespickers.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GsonUtil;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.adapter.NineGridTestAdapter;
import td.com.xiaoheixiong.beans.Detail;
import td.com.xiaoheixiong.beans.JsonRootBean;
import td.com.xiaoheixiong.beans.TouTiaoBean;
import td.com.xiaoheixiong.fragments.BaseFragment;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.interfaces.ListItemClickHelp;
import td.com.xiaoheixiong.views.pulltorefresh.PullLayout;
import td.com.xiaoheixiong.views.pulltorefresh.PullableListView;

/**
 * 我的头条
 */
public class MyHeadLineActivity extends BaseActivity implements PullLayout.OnRefreshListener,ListItemClickHelp{


    private TextView title_tv;
    private ImageView back_img, right_img;
    private RelativeLayout title_right_rl;
    private PullLayout refresh_view;
    private PullableListView lv_toutiao;

    private NineGridTestAdapter adapter;
    private String  pageSize = "10", MERCNUM;
    private int pageNum = 0;
    private int pages;


    private  List<TouTiaoBean> touTiaoBeanList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_headline);
        ButterKnife.bind(this);
        MERCNUM = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        initview();
        getdata(1);
    }

    private void initview() {
        title_tv = (TextView)findViewById(R.id.title_tv);
        back_img = (ImageView)findViewById(R.id.back_img);
        title_right_rl = (RelativeLayout)findViewById(R.id.title_right_rl);
        right_img = (ImageView)findViewById(R.id.right_img2);
        right_img.setVisibility(View.VISIBLE);
        title_tv.setText("我的头条");
        refresh_view= (PullLayout)findViewById(R.id.refresh_view);
        refresh_view.setOnRefreshListener(this);
        lv_toutiao = (PullableListView)findViewById(R.id.lv_toutiao);
        touTiaoBeanList=new ArrayList<>();
        adapter=new NineGridTestAdapter(this,this,2);
        adapter.setList(touTiaoBeanList);
        lv_toutiao.setAdapter(adapter);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                 intent.setClass(MyHeadLineActivity.this, AddHeadLineActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void getdata(final int state) {
        showLoadingDialog("...");
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("mercId", MERCNUM);
        maps.put("page", pageNum);
        maps.put("size", pageSize);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_toutiao, OkHttpClientManager.TYPE_POST_JSON, maps,
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
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();


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

        pageNum=0;
        getdata(2);
    }

    @Override
    public void onLoadMore(PullLayout pullToRefreshLayout) {
        if(pageNum>=pages){
            refresh_view.loadmoreFinish(PullLayout.SUCCEED);
            Toast.makeText(getApplicationContext(), "全部加载完成！", Toast.LENGTH_SHORT).show();
        }else{
            pageNum++;
            getdata(3);
        }

    }


    @Override
    public void onClick(View item, View widget, final int position, int which) {
        TouTiaoBean bean = touTiaoBeanList.get(position);
        final String id = bean.getId() + "";
        HashMap<String, Object> maps = new HashMap<>();
        //         id //被评论的头条ID
        maps.put("id", id);
        switch (which) {
            case R.id.img_delete://删除
                OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_delete_toutiao, OkHttpClientManager.TYPE_GET,
                        maps, OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {

                            @Override
                            public void onReqSuccess(Object result) {
                                // TODO Auto-generated method stub
                                Log.e("result", result + "");
                                JSONObject jsonObj = new JSONObject().parseObject(result + "");
                                if (jsonObj.get("RSPCOD").equals("000000")) {
                                    touTiaoBeanList.remove(position);
                                    adapter.notifyDataSetChanged();
                                }else{

                                }
                            }

                            @Override
                            public void onReqFailed(String errorMsg) {
                                // TODO Auto-generated method stub
                            }
                        });
                break;
            default:
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        switch (requestCode) {
            case 1://刷新
                pageNum=0;
                getdata(2);
                break;
            default:
                break;


        }



    }


}
