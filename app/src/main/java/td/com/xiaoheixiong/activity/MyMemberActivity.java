package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.adapter.MyMemberAdapter;
import td.com.xiaoheixiong.api.APIService;
import td.com.xiaoheixiong.api.RetrofitWrapper;
import td.com.xiaoheixiong.beans.BaseResponse;
import td.com.xiaoheixiong.beans.MyConstant;
import td.com.xiaoheixiong.beans.MyMember.MyMemberData;
import td.com.xiaoheixiong.beans.MyMember.MymemberBean;
import td.com.xiaoheixiong.views.pulltorefresh.PullLayout;
import td.com.xiaoheixiong.views.pulltorefresh.PullableListView;

/**
 * 我的会员
 */
public class MyMemberActivity extends BaseActivity implements PullLayout.OnRefreshListener {


    private TextView title_tv;
    private ImageView back_img;
    private PullLayout refresh_view;
    private PullableListView lv_member;

    private MyMemberAdapter adapter;
    private String pageSize = "10", MERCNUM;
    private int pageNum = 0;
    private int pages;


    private List<MymemberBean> mymemberBeanList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_member);
        ButterKnife.bind(this);
        MERCNUM = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        initview();
        getdata(1);
    }

    private void initview() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        back_img = (ImageView) findViewById(R.id.back_img);
        title_tv.setText("我的会员");
        refresh_view = (PullLayout) findViewById(R.id.refresh_view);
        refresh_view.setOnRefreshListener(this);
        lv_member = (PullableListView) findViewById(R.id.lv_member);
        lv_member.canPullUp=false;
        mymemberBeanList = new ArrayList<>();
        adapter = new MyMemberAdapter(this, mymemberBeanList);
        lv_member.setAdapter(adapter);
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
        Call<BaseResponse<MyMemberData>> call = userBiz.getFlowList(MERCNUM);

        call.enqueue(new Callback<BaseResponse<MyMemberData>>() {

            @Override
            public void onResponse(Call<BaseResponse<MyMemberData>> arg0,
                                   Response<BaseResponse<MyMemberData>> response) {
                loadingDialogWhole.dismiss();
                if (state == 2) {
                    if (null != mymemberBeanList) {
                        mymemberBeanList.clear();
                    }
                    refresh_view.refreshFinish(PullLayout.SUCCEED);
                }
                BaseResponse<MyMemberData> baseResponse = response.body();
                if (null != baseResponse) {
                    String retCode = baseResponse.getRSPCOD();
                    if (retCode.equals(MyConstant.SUCCESS)) {
                        MyMemberData myMemberData= baseResponse.getRSPDATA();
                        List<MymemberBean>  weichat=myMemberData.getWeichat();
                        List<MymemberBean>  alipay=myMemberData.getAlipay();
                        List<MymemberBean>  unionpay= myMemberData.getUnionpay();
                        if(null!=weichat&&weichat.size()>0){
                                mymemberBeanList.addAll(weichat);
                        }
                        if(null!=alipay&&alipay.size()>0){
                            mymemberBeanList.addAll(alipay);
                        }
                        if(null!=unionpay&&unionpay.size()>0){
                            mymemberBeanList.addAll(unionpay);
                        }
                        adapter.updateListview(mymemberBeanList);
                    } else {
                        String desc = baseResponse.getRSPMSG();
                        Toast.makeText(getApplicationContext(), desc, Toast.LENGTH_SHORT).show();

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BaseResponse<MyMemberData>> arg0, Throwable arg1) {
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


}
