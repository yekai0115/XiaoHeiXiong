package td.com.xiaoheixiong.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.CircleImageTransformation;
import td.com.xiaoheixiong.Utils.DimenUtils;
import td.com.xiaoheixiong.Utils.GetDateUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.views.GlideRoundTransform;
import td.com.xiaoheixiong.adapter.BannerAdapter;
import td.com.xiaoheixiong.adapter.BannerAdapter2;
import td.com.xiaoheixiong.beans.TuanTuan.TTBean;
import td.com.xiaoheixiong.beans.home.Adlist;
import td.com.xiaoheixiong.eventbus.Msgevent4;
import td.com.xiaoheixiong.views.SpringProgressView;
import td.com.xiaoheixiong.views.countdown.CountdownView;
import td.com.xiaoheixiong.views.countdown.CountdownView2;
import td.com.xiaoheixiong.views.pulltorefresh.PullLayout;
import td.com.xiaoheixiong.views.pulltorefresh.PullableRefreshScrollView;
import td.com.xiaoheixiong.views.viewpager.LoopViewPager;

/**
 * 秒秒详情
 */

public class MiaoMiaoDetalActivity extends BaseActivity implements PullableRefreshScrollView.OnScrollChangeListener,PullLayout.OnRefreshListener{


    private TextView tv_return;
    private LoopViewPager ve_pager;
    private TextView indicator;
    private TextView tv_title;
    private TextView tv_money;
    private TextView tv_sail;
    private TextView tv_add_num;
    private LinearLayout ll_djs;
    private LinearLayout ll_percent;
    private CountdownView cd_time;
    private TextView tv_tuantuan_need;
    private TextView tv_detal;


    private ImageView img_shop_pic;
    private TextView tv_shop_name;
    private TextView tv_address;
    private TextView tv_shop_type;
    private ImageView img_telephone;
    private Button btn_add;
    private Context mContext;
    private String MERCNUM;

    private TTBean ttBean;
    private BannerAdapter2 bannerAdapter;
    List<Adlist> adlist = new ArrayList<>();

    private PullLayout refresh_view;
    private PullableRefreshScrollView mScrollView;
    private LinearLayout ll_title;
    private int fadingHeight = 400; // 当ScrollView滑动到什么位置时渐变消失（根据需要进行调整）
    private Drawable drawable; // 顶部渐变布局需设置的Drawable
    private static final int START_ALPHA = 0;//scrollview滑动开始位置
    private static final int END_ALPHA = 255;//scrollview滑动结束位置
    private Drawable drawableLeft1;
    private Drawable drawableLeft2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miaomiao_detal);
        mContext = this;
        MERCNUM = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        EventBus.getDefault().register(this);
        ttBean = (TTBean) getIntent().getSerializableExtra("ttBean");
        initViews();
    }


    protected void initViews() {
        refresh_view= (PullLayout) findViewById(R.id.refresh_view);
        refresh_view.setOnRefreshListener(this);
        mScrollView= (PullableRefreshScrollView) findViewById(R.id.mScrollView);
        ll_title= (LinearLayout) findViewById(R.id.ll_title);
        drawableLeft1 = getResources().getDrawable(R.drawable.map_return);
        drawableLeft2= getResources().getDrawable(R.drawable.map_return1);
        // 这一步必须要做,否则不会显示.
        drawableLeft1.setBounds(0, 0, drawableLeft1.getMinimumWidth(), drawableLeft1.getMinimumHeight());
        drawableLeft2.setBounds(0, 0, drawableLeft2.getMinimumWidth(), drawableLeft2.getMinimumHeight());

        mScrollView.setOnScrollChangeListener(this);

        tv_return = (TextView) findViewById(R.id.tv_return);
        ve_pager = (LoopViewPager) findViewById(R.id.ve_pager);
        indicator = (TextView) findViewById(R.id.indicator);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_sail = (TextView) findViewById(R.id.tv_sail);
        tv_add_num = (TextView) findViewById(R.id.tv_add_num);
        ll_djs = (LinearLayout) findViewById(R.id.ll_djs);
        ll_percent = (LinearLayout) findViewById(R.id.ll_percent);
        cd_time = (CountdownView) findViewById(R.id.cd_time);
        tv_tuantuan_need = (TextView) findViewById(R.id.tv_tuantuan_need);
        tv_detal = (TextView) findViewById(R.id.tv_detal);


        img_shop_pic = (ImageView) findViewById(R.id.img_shop_pic);
        tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_shop_type = (TextView) findViewById(R.id.tv_shop_type);
        img_telephone = (ImageView) findViewById(R.id.img_telephone);

        btn_add = (Button) findViewById(R.id.btn_add);

        String price = ttBean.getPrice();
        String description = ttBean.getDescription();
        String shopName = ttBean.getShopName();
        String mainImg = ttBean.getMainImg();
        String merLabel = ttBean.getMerLabel();
        String detailImg = ttBean.getDetailImg();
        tv_title.setText(ttBean.getName());
        tv_money.setText(price);
        tv_detal.setText(description);
        tv_shop_type.setText(merLabel);
        tv_shop_name.setText(shopName);
        tv_address.setText(ttBean.getMerAdderess());

        String date = ttBean.getEndTime();
        date = date + " " + "23:59:59";//"yyyy-MM-dd HH:mm:ss")
        int time = GetDateUtils.getTimeInterval(date);//获取时间差，单位秒
        long times = (long) time * 1000;
        cd_time.start(times);
        Glide.with(mContext).load(mainImg)
                .centerCrop()
                .override(DimenUtils.dip2px(mContext, 80), DimenUtils.dip2px(mContext, 80))
                .transform(new GlideRoundTransform(mContext, 4))
                .placeholder(R.drawable.pic_nomal_loading_style)
                .error(R.drawable.pic_nomal_loading_style)
                .into(img_shop_pic);


        bannerAdapter = new BannerAdapter2(mContext, adlist);
        if (!StringUtils.isEmpty(detailImg)) {
            String[] temp = detailImg.split("[|]");
            Adlist bean;
            for (int i = 0; i < temp.length; i++) {
                bean = new Adlist();
                bean.setImgUrl(temp[i]);
                adlist.add(bean);
            }
            ve_pager.setAdapter(bannerAdapter);
            CharSequence text = getString(R.string.viewpager_indicator, 1, ve_pager.getAdapter().getCount());
            indicator.setText(text);

        } else {
            ve_pager.setVisibility(View.GONE);
        }
        bannerAdapter.notifyDataSetChanged();
        // 更新下标
        ve_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, ve_pager.getAdapter().getCount());
                indicator.setText(text);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {


            }

            @Override
            public void onPageScrollStateChanged(int arg0) {


            }
        });

        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMain(Msgevent4 messageEvent) {
        StringBuilder sb = new StringBuilder();

    }


    @Override
    public void onScrollChange(PullableRefreshScrollView view, int x, int y, int oldx, int oldy) {
        if (y > fadingHeight) {
            y = fadingHeight; // 当滑动到指定位置之后设置颜色为纯色，之前的话要渐变---实现下面的公式即可
            drawable = getResources().getDrawable(R.color.white);
            drawable.setAlpha(END_ALPHA);
            ll_title.setBackgroundDrawable(drawable);
            tv_return.setTextColor(getResources().getColor(R.color.tv_color2));
            tv_return.setCompoundDrawables(drawableLeft2, null, null, null);
//            my_message.setBackground(getResources().getDrawable(R.drawable.home_news_black));
//                my_message.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_news1));
        } else if (y < 0) {
            y = 0;
        } else {
            drawable = getResources().getDrawable(R.color.transparent);
            drawable.setAlpha(y * (END_ALPHA - START_ALPHA) / fadingHeight + START_ALPHA);
//                drawable.setAlpha(START_ALPHA);
            tv_return.setTextColor(getResources().getColor(R.color.white));
            tv_return.setCompoundDrawables(drawableLeft1, null, null, null);
            ll_title.setBackgroundDrawable(drawable);
            //          my_message.setBackground(getResources().getDrawable(R.drawable.home_news));



        }
    }


    @Override
    public void onRefresh(PullLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullLayout pullToRefreshLayout) {

    }
}
