package td.com.xiaoheixiong.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GsonUtil;
import td.com.xiaoheixiong.Utils.KeyBoardUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.SPUtils;
import td.com.xiaoheixiong.Utils.UpdateManager.UpdateManager;
import td.com.xiaoheixiong.activity.LetterSortActivity;
import td.com.xiaoheixiong.activity.MechatDetailsActivity;
import td.com.xiaoheixiong.activity.MerMarkDetailsActivity;
import td.com.xiaoheixiong.activity.MerchatTypesActivity;
import td.com.xiaoheixiong.activity.MiaoMiaoDetalActivity;
import td.com.xiaoheixiong.activity.MiaomiaoDetailsActivity;
import td.com.xiaoheixiong.activity.TuanTuanDetalActivity;
import td.com.xiaoheixiong.adapter.BannerAdapter;
import td.com.xiaoheixiong.adapter.CatGridViewAdapter;
import td.com.xiaoheixiong.adapter.MiaoMiaoAdapter;
import td.com.xiaoheixiong.adapter.TuanTuanAdapter;
import td.com.xiaoheixiong.adapter.ViewPagerAdapter;
import td.com.xiaoheixiong.adapter.YouYouAdapter;
import td.com.xiaoheixiong.beans.TuanTuan.TTBean;
import td.com.xiaoheixiong.beans.TuanTuan.TTDetalBean;
import td.com.xiaoheixiong.beans.home.Adlist;
import td.com.xiaoheixiong.beans.home.Catas;
import td.com.xiaoheixiong.beans.home.Detail;
import td.com.xiaoheixiong.beans.home.GroupMmerMarkList;
import td.com.xiaoheixiong.beans.home.HomeDetalBean;
import td.com.xiaoheixiong.beans.home.MerMarkList;
import td.com.xiaoheixiong.beans.home.PageInfo;
import td.com.xiaoheixiong.beans.home.YouYouList;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.MyGridview;
import td.com.xiaoheixiong.views.MyListView;
import td.com.xiaoheixiong.views.pulltorefresh.PullLayout;
import td.com.xiaoheixiong.views.pulltorefresh.PullableRefreshScrollView;
import td.com.xiaoheixiong.views.viewpager.LoopViewPager;
import td.com.xiaoheixiong.views.viewpager.MaterialIndicator;

public class TabA2Fragment extends BaseFragment implements View.OnClickListener, PullLayout.OnRefreshListener {

    private TextView address_tv;
    private EditText search_tv;

    private FrameLayout fl_banner;
    private LoopViewPager ve_pager;
    private TextView indicator;
    private MaterialIndicator bannerIndicator;

    private TextView tv_miaomiao;
    private MyGridview gv_miaomiao;

    private TextView tv_tuantuan;
    private MyListView lv_tuantuan;

    private TextView tv_youyou;
    private MyListView lv_yy;

    private PullLayout refresh_view;
    private PullableRefreshScrollView mScrollView;


    private LinearLayout ll_dot;
    private ViewPager viewpager;
    private View view;


    private LocationClient mLocationClient = null;
    public MyLocationListenner myListener = new MyLocationListenner();


    private GeoCoder mSearch;
    private SharedPreferences.Editor editor;
    private String mercId, lng, lat, pageSize = "10", city = "深圳", HeadName, total = "4", lastPage = "";
    private int pageNum = 1;

    private BannerAdapter bannerAdapter;

    private List<Catas> mDatas;
    /**
     * 总的页数
     */
    private int pageCount;
    /**
     * 每一页显示的个数
     */
    private int catPageSize = 10;
    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;

    private List<View> mPagerList;

    private LayoutInflater inflater;
    private Context mContext;


    private MiaoMiaoAdapter miaoMiaoAdapter;
    private List<MerMarkList> merMarkList = new ArrayList<>();
    private List<TTBean> mmBeanList = new ArrayList<>();


    private TuanTuanAdapter tuanTuanAdapter;
    private List<GroupMmerMarkList> groupMmerMarkList = new ArrayList<>();
    private List<TTBean> ttBeanList = new ArrayList<>();


    private YouYouAdapter youYouAdapter;
    private List<YouYouList> youYouLists = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_a_new, container, false);
        ButterKnife.bind(this, view);

        editor = MyCacheUtil.setshared(getActivity());
        mercId = MyCacheUtil.getshared(getActivity()).getString("MERCNUM", "");
        lng = MyCacheUtil.getshared(getActivity()).getString("lng", "");
        lat = MyCacheUtil.getshared(getActivity()).getString("lat", "");
        getdata("", 1);
        initeview();
        dingwei();
        getCityLngLat();
        UpdateManager manager = new UpdateManager(getActivity());
        manager.checkUpdate();
        return view;

    }

    private void initeview() {
        address_tv = (TextView) view.findViewById(R.id.address_tv);
        search_tv = (EditText) view.findViewById(R.id.search_tv);
        fl_banner = (FrameLayout) view.findViewById(R.id.fl_banner);
        ve_pager = (LoopViewPager) view.findViewById(R.id.ve_pager);
        bannerIndicator= (MaterialIndicator) view.findViewById(R.id.bannerIndicator);
        indicator = (TextView) view.findViewById(R.id.indicator);
        tv_miaomiao = (TextView) view.findViewById(R.id.tv_miaomiao);
        gv_miaomiao = (MyGridview) view.findViewById(R.id.gv_miaomiao);
        tv_tuantuan = (TextView) view.findViewById(R.id.tv_tuantuan);
        lv_tuantuan = (MyListView) view.findViewById(R.id.lv_tuantuan);
        tv_youyou = (TextView) view.findViewById(R.id.tv_youyou);
        lv_yy = (MyListView) view.findViewById(R.id.lv_yy);
        refresh_view = (PullLayout) view.findViewById(R.id.refresh_view);
        mScrollView = (PullableRefreshScrollView) view.findViewById(R.id.mScrollView);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);


        miaoMiaoAdapter = new MiaoMiaoAdapter(mContext, mmBeanList);
        gv_miaomiao.setAdapter(miaoMiaoAdapter);
        tuanTuanAdapter = new TuanTuanAdapter(mContext, ttBeanList);
        lv_tuantuan.setAdapter(tuanTuanAdapter);
        youYouAdapter = new YouYouAdapter(mContext, youYouLists);
        lv_yy.setAdapter(youYouAdapter);
        address_tv.setOnClickListener(this);
        tv_miaomiao.setOnClickListener(this);
        tv_tuantuan.setOnClickListener(this);
        tv_youyou.setOnClickListener(this);
        refresh_view.setOnRefreshListener(this);
//        search_tv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String text = search_tv.getText().toString().trim();
//                if (StringUtils.isNotBlank(text)) {
//                    pageNum = 0;
//                    lastPage = "";
//                    getdata(search_tv.getText() + "",1);
//                } else {
//
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });


        search_tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))

                {
                    KeyBoardUtils.closeKeybord(search_tv, getActivity());
                    pageNum = 1;
                    lastPage = "";
                    getdata(search_tv.getText() + "", 2);
                    return true;
                }
                return false;
            }
        });


        gv_miaomiao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent it = new Intent(getActivity(), MerMarkDetailsActivity.class);
//                it.putExtra("markId", merMarkList.get(i).getId() + "");
//                startActivity(it);

                Intent it = new Intent(getActivity(), MiaoMiaoDetalActivity.class);
                TTBean ttBean = mmBeanList.get(i);
                it.putExtra("ttBean", ttBean);
                startActivity(it);
            }
        });

        lv_yy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(getActivity(), MechatDetailsActivity.class);
                it.putExtra("mercId", youYouLists.get(i).getMercId() + "");
                it.putExtra("orgcode", youYouLists.get(i).getOrgcode() + "");
                it.putExtra("imgUrl", youYouLists.get(i).getMainImgUrl() + "");
                startActivity(it);
            }
        });

        lv_tuantuan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent it = new Intent(getActivity(), MerMarkDetailsActivity.class);
//                it.putExtra("markId", groupMmerMarkList.get(i).getId() + "");
//                startActivity(it);


                Intent it = new Intent(getActivity(), TuanTuanDetalActivity.class);
                TTBean ttBean = ttBeanList.get(i);
                it.putExtra("ttBean", ttBean);
                startActivity(it);

            }
        });
    }


    //根据城市获取经纬度
    private void getCityLngLat() {
        Log.e("进++", "进0.。。。。" + city);
        mSearch = GeoCoder.newInstance();
        //mSearch.setOnGetGeoCodeResultListener(listeners);
        //设置查询结果监听者
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            /**
             * 反地理编码查询结果回调函数
             * @param result  反地理编码查询结果
             */
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                Log.e("反地理编码查询result++", result.getLocation() + "");
                //    lat = result.getLocation().latitude + "";
                // lng = result.getLocation().longitude + "";
                //  getdata("");
            }


            /**
             * 地理编码查询结果回调函数
             * @param result  地理编码查询结果
             */
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {

                System.out.println("地理编码查询结果" + result.getLocation());
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                    Log.e("result++", result.getLocation() + "");
                    return;
                }
                lat = result.getLocation().latitude + "";
                lng = result.getLocation().longitude + "";
                pageNum = 1;
                lastPage = "";
                getdata("", 2);
            }
        });

    }

    public class MyLocationListenner extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说Log.e("location", "jin...");
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            String detal = city + district + street;
            SPUtils.put(mContext, "address", detal);
            lat = location.getLatitude() + "";
            lng = location.getLongitude() + "";
            SPUtils.put(mContext, "lat", detal);
            SPUtils.put(mContext, "address", detal);
            if (city.contains("市")) {
                city = city.substring(0, city.length() - 1);
            }
//            if (location.getCity().equals("") || location.getCity() == null) {
//                address_tv.setText(city);
//                editor.putString("city", city + "");
//            } else {
//                address_tv.setText(location.getCity());
//                editor.putString("city", location.getCity() + "");
//            }
            address_tv.setText(city);
            editor.putString("city", city + "");
            editor.commit();
        }

    }

    private void dingwei() {
        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener(myListener);
        setLocationOption();
        mLocationClient.start();
    }


    private void getdata(String MechatName, final int state) {
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        //    mercId	是	String	商户号
        //   city		String	城市名字	中文名字
        //  pageSize
        //  是	String	搜索的数量
        //   lng		String	经度
        //   lat		String	纬度
        //  merShortName		String	商户名字	 搜索条件
//        int a = Integer.parseInt(pageNum);
//        a = ++a;
//        if (!lastPage.equals("")) {
//            int allpage = Integer.parseInt(lastPage);
//            if (a > allpage) {
//                Toast.makeText(getActivity(), "没有更多了！", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//        pageNum = String.valueOf(a);

        if (null == loadingDialogWhole) {
            showLoadingDialog("...");
        } else {
            loadingDialogWhole.show();
        }

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("mercId", mercId);
        maps.put("city", city);
        maps.put("pageSize", pageSize);
        maps.put("lng", lng);
        maps.put("lat", lat);
        maps.put("merShortName", MechatName);
        maps.put("pageNum", pageNum);

        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.XHX_Life_Circle, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
//                        if(state==2){
//                            refresh_view.refreshFinish(PullLayout.SUCCEED);
//                        }
                        if (result == null) {
                            return;
                        }

                        JSONObject oJSON = JSON.parseObject(result + "");
                        try {
                            if (oJSON.get("RSPCOD").equals("000000")) {
                                HomeDetalBean jsonRootBean = GsonUtil.GsonToBean(oJSON.toString(), HomeDetalBean.class);
                                List<Adlist> adlist = jsonRootBean.getAdlist();//轮播图
                                initBanner(adlist);
                                mDatas = jsonRootBean.getCatas();//分类
                                showclassification();
                                List<Detail> detail = jsonRootBean.getDetail();//优优商家数据
                                if (detail == null || detail.isEmpty()) {

                                } else {
                                    Detail detail1 = detail.get(0);
                                    PageInfo pageInfo = detail1.getPageInfo();
                                    youYouLists = pageInfo.getList();
                                    youYouAdapter.updateListview(youYouLists);
                                    setListviewHeight(lv_yy);
                                }

//                            merMarkList = jsonRootBean.getMerMarkList();//秒秒数据
//                            miaoMiaoAdapter.setList(merMarkList);
//                            groupMmerMarkList = jsonRootBean.getGroupMmerMarkList();//团团数据
//                            tuanTuanAdapter.updateListview(groupMmerMarkList);
//                            setListviewHeight(lv_tuantuan);

                                getMiaoMiaoList(state);
                                getTuanTuanList(state);
                            } else {

                            }
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                        if (state == 2) {
                            refresh_view.refreshFinish(PullLayout.FAIL);
                        }
                    }
                });
    }


    private void initBanner(List<Adlist> adlist) {
        bannerAdapter = new BannerAdapter(getContext(), adlist);
        if (null == adlist || adlist.size() == 0) {
            fl_banner.setVisibility(View.GONE);
        } else {
            ve_pager.setAdapter(bannerAdapter);
            CharSequence text = getString(R.string.viewpager_indicator, 1, ve_pager.getAdapter().getCount());
            indicator.setText(text);
            fl_banner.setVisibility(View.VISIBLE);
            if (adlist.size() == 1) {
                ve_pager.setLooperPic(false);
            } else {
                ve_pager.setLooperPic(true);
                ve_pager.addOnPageChangeListener(bannerIndicator);
                bannerIndicator.setAdapter(ve_pager.getAdapter());
            }
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
    }


    private void showclassification() {

        if (null == mDatas || mDatas.isEmpty()) return;
        inflater = LayoutInflater.from(mContext);
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / catPageSize);
        mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview, viewpager, false);
            gridView.setAdapter(new CatGridViewAdapter(mContext, mDatas, i, catPageSize));
            mPagerList.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + curIndex * catPageSize;
                    // Toast.makeText(getActivity(),mDatas.get(pos).getSubCataName()+":id"+mDatas.get(pos).getId(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mContext, MerchatTypesActivity.class);
                    intent.putExtra("subCataId", mDatas.get(pos).getId() + "");
                    mContext.startActivity(intent);
                }
            });
        }
        //设置适配器
        viewpager.setAdapter(new ViewPagerAdapter(mPagerList));
        //设置圆点
        if (pageCount > 1) {
            setOvalLayout();
        }
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.smoothScrollTo(0, 0);
            }
        });

    }

    /**
     * 设置圆点
     */
    public void setOvalLayout() {
        if (ll_dot.getChildCount() == pageCount) {
            ll_dot.removeAllViews();
        }
        for (int i = 0; i < pageCount; i++) {
            ll_dot.addView(inflater.inflate(R.layout.dot, null));
        }
        // 默认显示第一页
        ll_dot.getChildAt(0).findViewById(R.id.v_dot)
                .setBackgroundResource(R.drawable.dot_selected);

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                ll_dot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_normal);
                // 圆点选中
                ll_dot.getChildAt(position)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.address_tv:
                it = new Intent(getActivity(), LetterSortActivity.class);
                it.putExtra("code", "0");
                startActivityForResult(it, 1);
                break;
            case R.id.tv_miaomiao:
                it = new Intent(getActivity(), MiaomiaoDetailsActivity.class);
                it.putExtra("type", "1");
                it.putExtra("lng", lng);
                it.putExtra("lat", lat);
                it.putExtra("city", city);
                startActivity(it);
                break;
            case R.id.tv_tuantuan:
                it = new Intent(getActivity(), MiaomiaoDetailsActivity.class);
                it.putExtra("type", "2");
                it.putExtra("lng", lng);
                it.putExtra("lat", lat);
                it.putExtra("city", city);
                startActivity(it);
                break;
            case R.id.tv_youyou:

                break;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLocationClient.stop();
        ButterKnife.unbind(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "onResume");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("hidden", hidden + "");
        if (!hidden) {
            UpdateManager manager = new UpdateManager(getActivity());
            manager.checkUpdate();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            String cityJson = data.getStringExtra("city");// 得到新Activity 关闭后返回的数据
            Log.e("resultCode", cityJson + "");
            //   JSONObject json = JSONObject.parseObject(cityJson);
            //   JSONArray ja = JSONArray.parseArray(cityJson+ "");
            address_tv.setText(cityJson + "");
            //   getCityLngLat(cityJson + "");
            city = cityJson + "";
            mSearch.geocode(new GeoCodeOption()
                    .city(city)
                    .address(city));
            editor.putString("city", cityJson + "");
            editor.commit();
        }
    }

    // 设置相关参数
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setServiceName("com.baidu.location.service_v2.9");
        option.setIsNeedLocationPoiList(true);
        option.setAddrType("all");
        option.setPriority(LocationClientOption.NetWorkFirst);
        option.setPriority(LocationClientOption.GpsFirst); // gps
        option.disableCache(true);
        option.setScanSpan(3600 * 1000);
        mLocationClient.setLocOption(option);
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


    @Override
    public void onRefresh(PullLayout pullToRefreshLayout) {
        if (search_tv.getText() != null && search_tv.getText().length() > 0) {
            getdata(search_tv.getText() + "", 2);
        } else {
            getdata("", 2);
        }
    }


    @Override
    public void onLoadMore(PullLayout pullToRefreshLayout) {

    }


    private void getMiaoMiaoList(final int state) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("size", 10);
        maps.put("page", 0);
        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.XHX_miaomiao_list, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        if (state == 2) {
                            refresh_view.refreshFinish(PullLayout.SUCCEED);
                        }
                        if (result == null) {
                            return;
                        }
                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            TTDetalBean jsonRootBean = GsonUtil.GsonToBean(oJSON.toString(), TTDetalBean.class);
                            List<TTBean> datas = jsonRootBean.getRSPDATA();//
                            if (state == 2) {
                                mmBeanList.clear();
                            }
                            mmBeanList.addAll(datas);
                            miaoMiaoAdapter.setList(mmBeanList);
                        } else {

                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                        if (state == 2) {
                            refresh_view.refreshFinish(PullLayout.FAIL);
                        }
                    }
                });
    }


    private void getTuanTuanList(final int state) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("size", 10);
        maps.put("page", 0);
        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.XHX_tuantuan_list, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        if (state == 2) {
                            refresh_view.refreshFinish(PullLayout.SUCCEED);
                        }
                        if (result == null) {
                            return;
                        }
                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            TTDetalBean jsonRootBean = GsonUtil.GsonToBean(oJSON.toString(), TTDetalBean.class);
                            List<TTBean> datas = jsonRootBean.getRSPDATA();//
                            if (state == 2) {
                                ttBeanList.clear();
                            }
                            ttBeanList.addAll(datas);
                            tuanTuanAdapter.updateListview(ttBeanList);
                            setListviewHeight(lv_tuantuan);
                        } else {

                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                        if (state == 2) {
                            refresh_view.refreshFinish(PullLayout.FAIL);
                        }
                    }
                });
    }


}
