package td.com.xiaoheixiong.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import td.com.xiaoheixiong.adapter.MyAccountAdapter;
import td.com.xiaoheixiong.api.APIService;
import td.com.xiaoheixiong.api.RetrofitWrapper;
import td.com.xiaoheixiong.beans.BaseResponse;
import td.com.xiaoheixiong.beans.MyAccount.MyAccountBean;
import td.com.xiaoheixiong.beans.MyAccount.MyAccountData;
import td.com.xiaoheixiong.beans.MyConstant;
import td.com.xiaoheixiong.beans.earns.MyEarnsBean;
import td.com.xiaoheixiong.beans.earns.MyEarnsData;
import td.com.xiaoheixiong.views.MyListView;
import td.com.xiaoheixiong.views.pulltorefresh.PullLayout;
import td.com.xiaoheixiong.views.pulltorefresh.PullableRefreshScrollView;

/**
 * 我的账本
 */
public class MyAccountActivity extends BaseActivity implements PullLayout.OnRefreshListener {


    private TextView title_tv, tv_dai_earns, tv_daoliu_earns;
    private View view1, view2;
    private ImageView back_img;
    private PullLayout refresh_view;
    private PullableRefreshScrollView msrcollview;
    private MyListView lv_order;
    private LinearLayout ll_dao, ll_dai;
    private MyAccountAdapter adapter;
    private String pageSize = "10", MERCNUM;
    private int pageNum = 0;
    private int pages;

    private int type = 1;
    private List<MyAccountBean> myAccountBeanList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);
        MERCNUM = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        initview();
        getdata(1);
    }

    private void initview() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        back_img = (ImageView) findViewById(R.id.back_img);
        tv_dai_earns = (TextView) findViewById(R.id.tv_dai_earns);
        tv_daoliu_earns = (TextView) findViewById(R.id.tv_daoliu_earns);
        ll_dai = (LinearLayout) findViewById(R.id.ll_dai);
        ll_dao = (LinearLayout) findViewById(R.id.ll_dao);
        view1 = (View) findViewById(R.id.view1);
        view2 = (View) findViewById(R.id.view2);
        title_tv.setText("收益");
        refresh_view = (PullLayout) findViewById(R.id.refresh_view);
        refresh_view.setOnRefreshListener(this);
        lv_order = (MyListView) findViewById(R.id.lv_order);
        msrcollview = (PullableRefreshScrollView) findViewById(R.id.msrcollview);
        msrcollview.needPullUp = false;
        msrcollview.smoothScrollTo(0, 0);//避免
        myAccountBeanList = new ArrayList<>();
        adapter = new MyAccountAdapter(this, myAccountBeanList, type);
        lv_order.setAdapter(adapter);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_dai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.INVISIBLE);
                type=1;
                getdata(2);
            }
        });
        ll_dao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.VISIBLE);
                type=2;
                getdata(2);
            }
        });
    }

    private void getdata(final int state) {
        showLoadingDialog("...");
        APIService userBiz = RetrofitWrapper.getInstance().create(APIService.class);
        Call<BaseResponse<MyAccountData>> call = userBiz.getMyMerchantWalletList(MERCNUM, type);

        call.enqueue(new Callback<BaseResponse<MyAccountData>>() {

            @Override
            public void onResponse(Call<BaseResponse<MyAccountData>> arg0,
                                   Response<BaseResponse<MyAccountData>> response) {
                loadingDialogWhole.dismiss();
                if (state == 2) {
                    if (null != myAccountBeanList) {
                        myAccountBeanList.clear();
                    }
                    refresh_view.refreshFinish(PullLayout.SUCCEED);
                }
                BaseResponse<MyAccountData> baseResponse = response.body();
                if (null != baseResponse) {
                    String retCode = baseResponse.getRSPCOD();
                    if (retCode.equals(MyConstant.SUCCESS)) {
                        MyAccountData myMemberData = baseResponse.getRSPDATA();
                        String merchantTotal = myMemberData.getMerchantTotal();
                        String flowmeterTotal = myMemberData.getFlowmeterTotal();
                        if (StringUtils.isEmpty(merchantTotal)) {
                            tv_dai_earns.setText("0.00");
                        } else {
                            tv_dai_earns.setText(merchantTotal);
                        }

                        if (StringUtils.isEmpty(flowmeterTotal)) {
                            tv_daoliu_earns.setText("0.00");
                        } else {
                            tv_daoliu_earns.setText(flowmeterTotal);
                        }

                        List<MyAccountBean> list = myMemberData.getList();
                        if (null != list && list.size() > 0) {
                            myAccountBeanList.addAll(list);
                        }
                        adapter.updateListview(myAccountBeanList,type);
                        setListviewHeight(lv_order);
                    } else {
                        String desc = baseResponse.getRSPMSG();
                        Toast.makeText(getApplicationContext(), desc, Toast.LENGTH_SHORT).show();

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BaseResponse<MyAccountData>> arg0, Throwable arg1) {
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
