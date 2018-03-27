package td.com.xiaoheixiong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.fragments.MarkingFrament;
import td.com.xiaoheixiong.views.PagerSlidingTabStrip;
import td.com.xiaoheixiong.views.popwindow.ActionItem;
import td.com.xiaoheixiong.views.popwindow.TitlePopAdapter;
import td.com.xiaoheixiong.views.popwindow.TitlePopWindow;


/**
 * 营销
 */
public class MarketingActivity extends BaseActivity {

    @Bind(R.id.orderTabs)
     PagerSlidingTabStrip tabs;

    @Bind(R.id.main_viewpager)
     ViewPager pager;
    private TextView title_tv, close_tv;
    private ImageView back_img;
    /**
     * 获取当前屏幕的密度
     */
    private DisplayMetrics dm;

    // 上下文
    private Context context;
    private TitlePopWindow window;
    private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marking);

        ButterKnife.bind(this);
        context = this;
        dm = getResources().getDisplayMetrics();
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(pager);
        setTabsValue();
        pager.setOffscreenPageLimit(0);
        initViews();

    }

    protected void initViews() {
        mActionItems.add(new ActionItem("拼团卡券"));
        mActionItems.add(new ActionItem("秒杀卡券"));
        mActionItems.add(new ActionItem("游戏卡券"));
        close_tv = (TextView) findViewById(R.id.close_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        back_img = (ImageView)findViewById(R.id.back_img);
        title_tv.setText("营销活动");
        close_tv.setText("新增");
        close_tv.setVisibility(View.VISIBLE);
        close_tv.setTextColor(getResources().getColor(R.color.red));
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        close_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMorePopWindow();
            }
        });
    }


    private void showMorePopWindow() {
        if(null==window){
            window = new TitlePopWindow(this, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT, mActionItems);
        }
        if (Build.VERSION.SDK_INT == 24) {
            // 适配 android 7.0
            int[] location = new int[2];
            close_tv.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            window.showAtLocation(close_tv, Gravity.NO_GRAVITY, -100, y + close_tv.getHeight());
        } else {
            window.showAsDropDown(close_tv, 30, 0);
        }
//            window.showAsDropDown(img_pop, 30, 0);
        // 实例化标题栏按钮并设置监听
        window.setOnItemClickListener(new TitlePopAdapter.onItemClickListener() {

            @Override
            public void click(int position, View view) {
                //0 团团  1 秒秒 2 游戏卡券
                Intent intent=new Intent(MarketingActivity.this,AddMarkingActivity.class);
                intent.putExtra("position",position+1);
                startActivity(intent);
                if(position==0){

                }else  if(position==1){

                }else  if(position==2){

                }
            }
        });
    }




    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 0, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 14, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(context.getResources().getColor(
                R.color.red));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(context.getResources().getColor(
                R.color.colorPrimary));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 监听返回键
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private String[] titles = { getString(R.string.tv_1), getString(R.string.tv_2), getString(R.string.tv_3)};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            int status=position+1;
            return MarkingFrament.newInstance(status);
        }

    }

}


