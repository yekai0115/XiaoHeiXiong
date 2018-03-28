package td.com.xiaoheixiong.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.adapter.AgentEarnsAdapter;
import td.com.xiaoheixiong.api.APIService;
import td.com.xiaoheixiong.api.RetrofitWrapper;
import td.com.xiaoheixiong.beans.BaseResponse;
import td.com.xiaoheixiong.beans.MyConstant;
import td.com.xiaoheixiong.beans.earns.MyEarnsBean;
import td.com.xiaoheixiong.beans.earns.MyEarnsData;
import td.com.xiaoheixiong.views.MyListView;
import td.com.xiaoheixiong.views.pulltorefresh.PullLayout;
import td.com.xiaoheixiong.views.pulltorefresh.PullableRefreshScrollView;

/**
 * 代理商收益
 */
public class AgentEarnsActivity extends BaseActivity implements PullLayout.OnRefreshListener {


    private TextView title_tv,tv_earns;
    private ImageView back_img;
    private PullLayout refresh_view;
    private PullableRefreshScrollView msrcollview;
    private MyListView lv_order;

    private AgentEarnsAdapter adapter;
    private String pageSize = "10", MERCNUM;
    private int pageNum = 0;
    private int pages;


    private List<MyEarnsBean> myEarnsBeanList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_earns);
        ButterKnife.bind(this);
        MERCNUM = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        initview();
        getdata(1);
    }

    private void initview() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        back_img = (ImageView) findViewById(R.id.back_img);
        tv_earns= (TextView) findViewById(R.id.tv_earns);
        title_tv.setText("收益");
        refresh_view = (PullLayout) findViewById(R.id.refresh_view);
        refresh_view.setOnRefreshListener(this);
        lv_order = (MyListView) findViewById(R.id.lv_order);
        msrcollview= (PullableRefreshScrollView) findViewById(R.id.msrcollview);
        msrcollview.needPullUp=false;
        msrcollview.smoothScrollTo(0, 0);//避免
        myEarnsBeanList = new ArrayList<>();
        adapter = new AgentEarnsAdapter(this, myEarnsBeanList);
        lv_order.setAdapter(adapter);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getdata(final int state) {
        showLoadingDialog("...");
        APIService userBiz = RetrofitWrapper.getInstance().create(APIService.class);
        Call<BaseResponse<MyEarnsData>> call = userBiz.getMyWalletList(MERCNUM);

        call.enqueue(new Callback<BaseResponse<MyEarnsData>>() {

            @Override
            public void onResponse(Call<BaseResponse<MyEarnsData>> arg0,
                                   Response<BaseResponse<MyEarnsData>> response) {
                loadingDialogWhole.dismiss();
                if (state == 2) {
                    if (null != myEarnsBeanList) {
                        myEarnsBeanList.clear();
                    }
                    refresh_view.refreshFinish(PullLayout.SUCCEED);
                }
                BaseResponse<MyEarnsData> baseResponse = response.body();
                if (null != baseResponse) {
                    String retCode = baseResponse.getRSPCOD();
                    if (retCode.equals(MyConstant.SUCCESS)) {
                        MyEarnsData myMemberData= baseResponse.getRSPDATA();
                        String total=myMemberData.getTotal();
                        if(StringUtils.isEmpty(total)){
                            tv_earns.setText("0.00");
                        }else{
                            tv_earns.setText(total);
                        }

                        List<MyEarnsBean>  list=myMemberData.getList();
                        if(null!=list&&list.size()>0){
                            myEarnsBeanList.addAll(list);
                        }
                        adapter.updateListview(myEarnsBeanList);
                        setListviewHeight(lv_order);
                    } else {
                        String desc = baseResponse.getRSPMSG();
                        Toast.makeText(getApplicationContext(), desc, Toast.LENGTH_SHORT).show();

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BaseResponse<MyEarnsData>> arg0, Throwable arg1) {
                loadingDialogWhole.dismiss();
                if (state == 2) {
                    refresh_view.refreshFinish(PullLayout.FAIL);
                }
            }
        });
    }


    @Override
    public void onRefresh(PullLayout pullToRefreshLayout) {

        pageNum = 0;
        getdata(2);
    }

    @Override
    public void onLoadMore(PullLayout pullToRefreshLayout) {
        if (pageNum >= pages) {
            refresh_view.loadmoreFinish(PullLayout.SUCCEED);
            Toast.makeText(getApplicationContext(), "全部加载完成！", Toast.LENGTH_SHORT).show();
        } else {
            pageNum++;
            getdata(3);
        }

    }

    /**
     * 自动匹配listview的高度
     *
     * @param
     */
    private void setListviewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listitemView = listAdapter.getView(i, null, listView);
            listitemView.measure(0, 0);
            totalHeight += listitemView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listView.setLayoutParams(params);
    }
}
