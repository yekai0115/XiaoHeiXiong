package td.com.xiaoheixiong.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MySignUtil;
import td.com.xiaoheixiong.Utils.signUtil.MD5Util;
import td.com.xiaoheixiong.adapter.HeadLindDetalAdapter;
import td.com.xiaoheixiong.views.pulltorefresh.PullableRefreshScrollView2;
import td.com.xiaoheixiong.views.toutiao.NineGridTestLayout;


/**
 *
 */
public class HeadLineDetalctivity extends BaseActivity implements View.OnClickListener, PullableRefreshScrollView2.OnScrollViewListener {

    //转发
    private FrameLayout ll_zhuanfa;
    private TextView tv_zhuanfa;
    private TextView tv_zhuanfa2;
    private ImageView img_zhuanfa;
    private ImageView img_zhuanfa2;


    //评论
    private FrameLayout ll_evaulate;
    private TextView tv_evaluate;
    private TextView tv_evaluate2;
    private ImageView img_evaulate;
    private ImageView img_evaulate2;

    //点赞
    private FrameLayout ll_praise;
    private TextView tv_praise;
    private TextView tv_praise2;
    private ImageView img_praise;
    private ImageView img_praise2;
    private ListView lv;
    private HeadLindDetalAdapter adapter;

    private LinearLayout topBuy;
    private LinearLayout centerBuy;
    private ViewGroup parent_layout;
    private PullableRefreshScrollView2 refreshScrollView;
    private NineGridTestLayout gv_pic;

    /**
     *
     */
    private List<String> datas = new ArrayList<>();
    private List<String> picDatas = new ArrayList<>();
    private LinearLayout ll_filter;


    private Context context;

    private String mercImg;//头像
    private String mercName;
    private String description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.headlin_detal);
   //     EventBus.getDefault().register(this);
        context = this;
        Intent intent = getIntent();
        mercImg=intent.getStringExtra("mercImg");
        mercName=intent.getStringExtra("mercName");
        description=intent.getStringExtra("description");
        picDatas=intent.getStringArrayListExtra("imageList");
        initView();
    }


    private void initView() {
        gv_pic= (NineGridTestLayout) findViewById(R.id.gv_pic);
        topBuy = (LinearLayout) findViewById(R.id.top_buy);
        centerBuy = (LinearLayout) findViewById(R.id.center_buy);
        parent_layout = (LinearLayout) findViewById(R.id.parent_layout);
        refreshScrollView = (PullableRefreshScrollView2) findViewById(R.id.refreshScrollView);
        refreshScrollView.setOnScrollViewListener(this);
        tv_zhuanfa2 = (TextView) topBuy.findViewById(R.id.tv_zhuanfa);
        tv_evaluate2 = (TextView) topBuy.findViewById(R.id.tv_evaluate);
        tv_praise2 = (TextView) topBuy.findViewById(R.id.tv_praise);


        ll_zhuanfa = (FrameLayout) findViewById(R.id.ll_zhuanfa);
        tv_zhuanfa = (TextView) findViewById(R.id.tv_zhuanfa);
        img_zhuanfa = (ImageView) topBuy.findViewById(R.id.img_zhuanfa);
        img_zhuanfa2 = (ImageView)centerBuy. findViewById(R.id.img_zhuanfa);


        ll_evaulate = (FrameLayout) findViewById(R.id.ll_evaulate);
        tv_evaluate = (TextView) findViewById(R.id.tv_evaluate);
        img_evaulate = (ImageView) topBuy.findViewById(R.id.img_evaulate);
        img_evaulate2 = (ImageView)centerBuy. findViewById(R.id.img_evaulate);


        ll_filter = (LinearLayout) findViewById(R.id.ll_filter);
        ll_praise = (FrameLayout) findViewById(R.id.ll_praise);
        tv_praise = (TextView) findViewById(R.id.tv_praise);
        img_praise = (ImageView) topBuy.findViewById(R.id.img_praise);
        img_praise2 = (ImageView)centerBuy. findViewById(R.id.img_praise);
        lv = (ListView) findViewById(R.id.lv);
        parent_layout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        onScroll(refreshScrollView.getScrollY());// 这一步很关键，因为在这里我们手动调用了onScroll方法
                        // 开始时初始化界面没滑动，mScrollView.getScrollY()为0,所以此时优先调用一次onScroll方法
                        // 如果我们不在初始化界面监听并手动调用该方法的话，onScroll方法，只会在滑动的时候产生回调
                        // 一开始并不会产生回调，也就会导致topBuy（顶部的布局）和centerBuy(中间的布局)无法在第一时间内整个界面一开始并没有滑动就重合
                        // 就会看见有两个相同的布局，一个在顶部，一个在中间左右，而我们一旦滑动就会触发回调onScroll方法，此时Math.max(scrollY,centerBuy.getTop)
                        // 取最大值肯定就是centerBuy.getTop(),那么接着利用layout方法，重新绘制topBuy（顶部布局），使得顶部布局正好与中间布局重合在一起，从而使得
                        // 所以，如果一开始我们就手动调用一次
                        // onScroll(mScrollView.getScrollY());就能在第一时间内使得两个布局重合，给人感觉就是一个布局，实际上是
                        // 两个布局，只是重新绘制了顶部布局位置，使它重叠在中间布局上面，给人感觉就是顶部布局消失了一样
                    }
                });

        centerBuy.findViewById(R.id.ll_zhuanfa).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        img_zhuanfa.setVisibility(View.VISIBLE);
                        img_evaulate.setVisibility(View.GONE);
                        img_praise.setVisibility(View.GONE);

                        img_zhuanfa2.setVisibility(View.VISIBLE);
                        img_evaulate2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.GONE);

                    }
                });
        centerBuy.findViewById(R.id.ll_evaulate).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        img_evaulate.setVisibility(View.VISIBLE);
                        img_zhuanfa.setVisibility(View.GONE);
                        img_praise.setVisibility(View.GONE);

                        img_evaulate2.setVisibility(View.VISIBLE);
                        img_zhuanfa2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.GONE);

                    }
                });
        ;
        centerBuy.findViewById(R.id.ll_praise).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        img_evaulate.setVisibility(View.GONE);
                        img_zhuanfa.setVisibility(View.GONE);
                        img_praise.setVisibility(View.VISIBLE);

                        img_evaulate2.setVisibility(View.GONE);
                        img_zhuanfa2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.VISIBLE);
                    }
                });
        ;

        topBuy.findViewById(R.id.ll_zhuanfa).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        img_zhuanfa.setVisibility(View.VISIBLE);
                        img_evaulate.setVisibility(View.GONE);
                        img_praise.setVisibility(View.GONE);

                        img_zhuanfa2.setVisibility(View.VISIBLE);
                        img_evaulate2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.GONE);

                    }
                });
        topBuy.findViewById(R.id.ll_evaulate).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        img_evaulate.setVisibility(View.VISIBLE);
                        img_zhuanfa.setVisibility(View.GONE);
                        img_praise.setVisibility(View.GONE);

                        img_evaulate2.setVisibility(View.VISIBLE);
                        img_zhuanfa2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.GONE);

                    }
                });
        ;
        topBuy.findViewById(R.id.ll_praise).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        img_zhuanfa.setVisibility(View.GONE);
                        img_evaulate.setVisibility(View.GONE);
                        img_praise.setVisibility(View.VISIBLE);

                        img_zhuanfa2.setVisibility(View.GONE);
                        img_evaulate2.setVisibility(View.GONE);
                        img_praise2.setVisibility(View.VISIBLE);
                    }
                });



        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        datas.add("转发");
        adapter = new HeadLindDetalAdapter(context, datas);
        lv.setAdapter(adapter);

       gv_pic.setIsShowAll(false);
        gv_pic.setUrlList(picDatas);

        img_zhuanfa.setVisibility(View.VISIBLE);
        img_evaulate.setVisibility(View.GONE);
        img_praise.setVisibility(View.GONE);
        img_zhuanfa2.setVisibility(View.VISIBLE);
        img_evaulate2.setVisibility(View.GONE);
        img_praise2.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_zhuanfa://转发
                img_zhuanfa.setVisibility(View.VISIBLE);
                img_evaulate.setVisibility(View.GONE);
                img_praise.setVisibility(View.GONE);

                img_zhuanfa2.setVisibility(View.VISIBLE);
                img_evaulate2.setVisibility(View.GONE);
                img_praise2.setVisibility(View.GONE);
                break;
            case R.id.ll_evaulate://评论
                img_evaulate.setVisibility(View.VISIBLE);
                img_zhuanfa.setVisibility(View.GONE);
                img_praise.setVisibility(View.GONE);

                img_evaulate2.setVisibility(View.VISIBLE);
                img_zhuanfa2.setVisibility(View.GONE);
                img_praise2.setVisibility(View.GONE);
                break;
            case R.id.ll_praise://筛选
                img_praise.setVisibility(View.VISIBLE);
                img_evaulate.setVisibility(View.GONE);
                img_praise.setVisibility(View.GONE);

                img_praise2.setVisibility(View.VISIBLE);
                img_evaulate2.setVisibility(View.GONE);
                img_praise2.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
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
        ((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listView.setLayoutParams(params);
    }


    @Override
    public void onScroll(int scrollY) {
//        System.out.println("ScrollY---->" + scrollY);
//        System.out.println("centerBuy_getTop中间布局离ScrollView距离:------->"+ centerBuy.getTop());
        int topBuyParentTop = Math.max(scrollY, centerBuy.getTop());
//        System.out.println("topBuy顶部布局离ScrollView顶部距离:" + topBuyParentTop);
        topBuy.layout(0, topBuyParentTop, topBuy.getWidth(), topBuyParentTop + topBuy.getHeight());

    }


}