package td.com.xiaoheixiong.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import td.com.xiaoheixiong.Customcontrols.BannerAdapter;
import td.com.xiaoheixiong.Customcontrols.CustomLayout;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.UpdateManager.UpdateManager;
import td.com.xiaoheixiong.activity.LetterSortActivity;
import td.com.xiaoheixiong.activity.MechatDetailsActivity;
import td.com.xiaoheixiong.activity.MerMarkDetailsActivity;
import td.com.xiaoheixiong.activity.MiaomiaoDetailsActivity;
import td.com.xiaoheixiong.adapter.HomeAdapter;
import td.com.xiaoheixiong.adapter.MainRecyclerViewAdapter;
import td.com.xiaoheixiong.adapter.MiaomiaoRecyclerviewAdapter;
import td.com.xiaoheixiong.adapter.TuantuanRecyclerviewAdapter;
import td.com.xiaoheixiong.adapter.YouyouRecyclerViewAdapter;
import td.com.xiaoheixiong.beans.MultiType.MyGridViewAdpter;
import td.com.xiaoheixiong.beans.MultiType.MyViewPagerAdapter;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.RecyclerViewItemTouchListener;

public class TabAFragment extends BaseFragment implements View.OnClickListener {

    private XRecyclerView recyclerview;
    private RecyclerView recyclerviewYY, recyclerviewMM;
    private View view;
    private View headView, YY_view, MM_view, footview, TTT_view;
    private List<View> viewList;
    public LayoutInflater mInflater, footmInflater;
    private LinearLayout viewsHead, points, more_ll;
    private static final int SPAN_COUNT = 5;
    private GeoCoder mSearch;
    private SharedPreferences.Editor editor;
    private String mercId, lng, lat, pageSize = "8", pageNum = "0", city = "深圳", HeadName, total = "4", lastPage = "";
    private List<Map<String, Object>> GridAllLists;
    private MainRecyclerViewAdapter MainAdapter;
    private MiaomiaoRecyclerviewAdapter MiaomiaoAdapter;
    private TuantuanRecyclerviewAdapter TuantuanAdapter;
    private YouyouRecyclerViewAdapter YouyouAdapter;
    private TextView textTitle, address_tv, more_tv1, more_tv2;
    private EditText search_tv;
    private ImageView img_banner, img_ss;
    private ViewPager viewpager;
    private ImageView[] ivPoints;//小圆点图片的集合
    private int totalPage; //总的页数
    private int mPageSize = 8; //每页显示的最大的数量
    private List<Map<String, Object>> GridDatas;//总的数据源
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private List<Map<String, Object>> detailData;
    private ArrayList<HashMap<String, Object>> listData, MMlistData, TTlistData;
    private LocationClient mLocationClient = null;
    public MyLocationListenner myListener = new MyLocationListenner();

    private ViewPager vp_shuffling;
    private CustomLayout pic_banner;

    private LinearLayout dotLayout; // 圈圈 布局
    private List<String> mADParseArray;
    private final int HOME_AD_RESULT = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 广告
                case HOME_AD_RESULT:
                    vp_shuffling.setCurrentItem(vp_shuffling.getCurrentItem() + 1,
                            true);
                    break;
            }
        }

        ;
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_a, container, false);
        ButterKnife.bind(this, view);
        mInflater = inflater;
        editor = MyCacheUtil.setshared(getActivity());
        mercId = MyCacheUtil.getshared(getActivity()).getString("MERCNUM", "");
        lng = MyCacheUtil.getshared(getActivity()).getString("lng", "");
        lat = MyCacheUtil.getshared(getActivity()).getString("lat", "");
        getdata("");
        initeview();
        dingwei();
        getCityLngLat();
        UpdateManager manager = new UpdateManager(getActivity());
        manager.checkUpdate();
        return view;

    }

    private void initeview() {
        GridDatas = new ArrayList<>();
        GridAllLists = new ArrayList<>();
        listData = new ArrayList<>();
        MMlistData = new ArrayList<>();
        TTlistData = new ArrayList<>();
        detailData = new ArrayList<>();
        mADParseArray = new ArrayList<>();
        viewList = new ArrayList<>();
        //添加头部viewpager，gridview
        headView = mInflater.inflate(R.layout.item_viewpager, null);
        Setheadview();
        //添加优优商家
        YY_view = mInflater.inflate(R.layout.recyclerview_youyou, null);
        SetYYview();

        //添加妙秒商家
        MM_view = mInflater.inflate(R.layout.recyclerview_miaomiao, null);
        SetMMview();

        //添加团团标题
        TTT_view = mInflater.inflate(R.layout.layout_tuantuan, null);
        //  SetTTTview();


        viewList.add(headView);
        viewList.add(YY_view);
        viewList.add(MM_view);
        viewList.add(TTT_view);

       more_ll = (LinearLayout) view.findViewById(R.id.more_ll);
        more_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), MiaomiaoDetailsActivity.class);
                it.putExtra("type", "2");
                startActivity(it);
            }
        });
        recyclerview = (XRecyclerView) view.findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerview.addItemDecoration(new SpacesItemDecoration(1));//设置item间距
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setRefreshProgressStyle(ProgressStyle.BallPulse);
           recyclerview.setLaodingMoreProgressStyle(ProgressStyle.BallPulse);
        recyclerview.setLoadingMoreEnabled(false);
        recyclerview.setArrowImageView(R.mipmap.iconfont_downgrey);

        TuantuanAdapter = new TuantuanRecyclerviewAdapter(getActivity(), TTlistData, viewList, new RecyclerViewItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                Log.e("position+++++++", position + "");
                int points = position - 1;
                if (position >= 4) {
                    Intent it = new Intent(getActivity(), MerMarkDetailsActivity.class);
                    it.putExtra("markId", TTlistData.get(points).get("id") + "");
                    startActivity(it);
                }

            }

            @Override
            public void onTtemLongTouch(View view, int position) {

            }
        });
        recyclerview.setAdapter(TuantuanAdapter);

        footview = mInflater.inflate(R.layout.item_more, null);
        more_ll = (LinearLayout) footview.findViewById(R.id.more_ll);
        more_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), MiaomiaoDetailsActivity.class);
                it.putExtra("type", "2");
                it.putExtra("lng", lng);
                it.putExtra("lat", lat);
                it.putExtra("city", city);
                startActivity(it);
            }
        });
       /* if (footview != null) {
            Log.e("footview", footview + "");
            recyclerview.addFootView(footview);
        }*/
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (search_tv.getText() != null && search_tv.getText().length() > 0) {
                    getdata(search_tv.getText() + "");
                } else {
                    getdata("");
                }
                TuantuanAdapter.notifyDataSetChanged();
                recyclerview.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                //  recyclerview.loadMoreComplete();
                //  recyclerview.noMoreLoading();

            }
        });

    }


    private void Setheadview() {
        search_tv = (EditText) headView.findViewById(R.id.search_tv);
        textTitle = (TextView) headView.findViewById(R.id.textTitle);
        address_tv = (TextView) headView.findViewById(R.id.address_tv);
        // img_banner = (ImageView) viewsHead.findViewById(R.id.img_banner);
        img_ss = (ImageView) headView.findViewById(R.id.img_ss);
        viewpager = (ViewPager) headView.findViewById(R.id.viewpager);
        points = (LinearLayout) headView.findViewById(R.id.points);
        address_tv.setOnClickListener(this);

        vp_shuffling = (ViewPager) headView.findViewById(R.id.vp_shuffling);
        pic_banner = (CustomLayout) headView.findViewById(R.id.pic_banner);

        //   tv_content = (TextView) findViewById(R.id.tv_content);
        dotLayout = (LinearLayout) headView.findViewById(R.id.layout_point);
        img_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNotBlank(search_tv.getText())) {
                    pageNum = "0";
                    lastPage = "";
                    getdata(search_tv.getText() + "");

                } else {
                    Toast.makeText(getActivity(), "搜索不能为空!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void SetYYview() {
        recyclerviewYY = (RecyclerView) YY_view.findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerviewYY.addItemDecoration(new SpacesItemDecoration(1));//设置item间距
        recyclerviewYY.setLayoutManager(layoutManager);
        //  recyclerviewYY.setRefreshProgressStyle(ProgressStyle.BallPulse);
        //  recyclerviewYY.setLaodingMoreProgressStyle(ProgressStyle.BallPulse);
        //  recyclerviewYY.setArrowImageView(R.mipmap.iconfont_downgrey);
        more_tv1 = (TextView) YY_view.findViewById(R.id.more_tv1);
        //优优商家查看更多
        more_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNotBlank(search_tv.getText())) {
                    getdata(search_tv.getText() + "");
                } else {
                    getdata("");
                }
            }
        });


        Log.e("Listdata888", listData + "");
        YouyouAdapter = new YouyouRecyclerViewAdapter(getActivity(), listData, new RecyclerViewItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                Log.e("position", position + "");
                //    id=1, orgcode=20161202113010569.5516033072907
                Log.e("points", points + "");
                Log.e("id", listData.get(position).get("id") + "");
                Log.e("orgcode", listData.get(position).get("orgcode") + "");
                Intent it = new Intent(getActivity(), MechatDetailsActivity.class);
                it.putExtra("mercId", listData.get(position).get("mercId") + "");
                it.putExtra("orgcode", listData.get(position).get("orgcode") + "");
                startActivity(it);
            }

            @Override
            public void onTtemLongTouch(View view, int position) {

            }
        });
        recyclerviewYY.setAdapter(YouyouAdapter);
    }

    private void SetMMview() {
        recyclerviewMM = (RecyclerView) MM_view.findViewById(R.id.recyclerview);
        more_tv2 = (TextView) MM_view.findViewById(R.id.more_tv2);
        more_tv2.setOnClickListener(new View.OnClickListener() {//秒秒查看更多
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), MiaomiaoDetailsActivity.class);
                it.putExtra("type", "1");
                it.putExtra("lng", lng);
                it.putExtra("lat", lat);
                it.putExtra("city", city);
                startActivity(it);
            }
        });
        GridLayoutManager layoutManager2 = new GridLayoutManager(getActivity(), 1);
        recyclerviewMM.addItemDecoration(new SpacesItemDecoration(1));//设置item间距
        recyclerviewMM.setLayoutManager(layoutManager2);
        MiaomiaoAdapter = new MiaomiaoRecyclerviewAdapter(getActivity(), MMlistData, new RecyclerViewItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                Intent it = new Intent(getActivity(), MerMarkDetailsActivity.class);
                it.putExtra("markId", MMlistData.get(position).get("id") + "");
                startActivity(it);
            }

            ;

            @Override
            public void onTtemLongTouch(View view, int position) {

            }
        });
        recyclerviewMM.setAdapter(MiaomiaoAdapter);
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
                pageNum = "0";
                lastPage = "";
                getdata("");
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
            if (location.getCity().equals("") || location.getCity() == null) {
                address_tv.setText(city);
                editor.putString("city", city + "");
            } else {
                address_tv.setText(location.getCity());
                editor.putString("city", location.getCity() + "");
            }
            editor.commit();
        }

    }

    private void dingwei() {
        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener(myListener);
        setLocationOption();
        mLocationClient.start();
    }


    private void getdata(String MechatName) {
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);
        //    mercId	是	String	商户号
        //   city		String	城市名字	中文名字
        //  pageSize
        //  是	String	搜索的数量
        //   lng		String	经度
        //   lat		String	纬度
        //  merShortName		String	商户名字	 搜索条件
        int a = Integer.parseInt(pageNum);
        a = ++a;
        if (!lastPage.equals("")) {
            int allpage = Integer.parseInt(lastPage);
            if (a > allpage) {
                Toast.makeText(getActivity(), "没有更多了！", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        showLoadingDialog("...");
        pageNum = String.valueOf(a);
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
                        if (result == null) {
                            return;
                        }

                        Map<String, Object> mapAll = new HashMap<String, Object>();
                        mapAll = JSON.parseObject(result + "", new TypeReference<Map<String, Object>>() {
                        });

                        if (mapAll.get("RSPCOD").equals("000000")) {
                            GridAllLists.clear();
                            GridDatas.clear();
                            // listData.clear();

                            ArrayList<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();
                            ArrayList<HashMap<String, Object>> MMlist = new ArrayList<HashMap<String, Object>>();
                            ArrayList<HashMap<String, Object>> TTlist = new ArrayList<HashMap<String, Object>>();
                            List<Map<String, Object>> Imglists = new ArrayList<Map<String, Object>>();

                            List<Map<String, Object>> detailData = new ArrayList<Map<String, Object>>();
                            Map<String, Object> pageInfo = new HashMap<String, Object>();
                            Log.e("detail", mapAll.get("detail") + "");
                            detailData = JSON.parseObject(mapAll.get("detail") + "", new TypeReference<List<Map<String, Object>>>() {
                            });
                            HeadName = detailData.get(0).get("belongPlateName") + "";
                            pageInfo = JSON.parseObject(detailData.get(0).get("pageInfo") + "", new TypeReference<Map<String, Object>>() {
                            });
                            lists = JSON.parseObject(pageInfo.get("list") + "", new TypeReference<ArrayList<HashMap<String, Object>>>() {
                            });
                            total = pageInfo.get("total") + "";//总条数
                            lastPage = pageInfo.get("lastPage") + "";//总页数
                            Imglists = JSON.parseObject(mapAll.get("adlist") + "", new TypeReference<List<Map<String, Object>>>() {
                            });

                            GridAllLists = JSON.parseObject(mapAll.get("catas") + "", new TypeReference<List<Map<String, Object>>>() {
                            });
                            MMlist = JSON.parseObject(mapAll.get("merMarkList") + "", new TypeReference<ArrayList<HashMap<String, Object>>>() {
                            });
                            TTlist = JSON.parseObject(mapAll.get("groupMmerMarkList") + "", new TypeReference<ArrayList<HashMap<String, Object>>>() {
                            });
                            Log.e("TTlist", TTlist + "");
                            Log.e("lists", lists + "");
                            Log.e("MMlist", MMlist + "");
                            Log.e("groupMmerMarkList", mapAll.get("groupMmerMarkList") + "");


                            if (pageNum.equals("1")) {
                                listData.clear();
                            }
                            if (lists != null && lists.size() > 0) {//优优商家
                                //listData.clear();
                                YY_view.setVisibility(View.VISIBLE);
                                textTitle.setVisibility(View.VISIBLE);
                                for (int i = 0; i < lists.size(); i++) {
                                    listData.add(lists.get(i));
                                }
                            } else if (lists.size() == 0) {
                                // listData.clear();
                                YY_view.setVisibility(View.GONE);
                                textTitle.setVisibility(View.GONE);
                            }

                            if (MMlist != null && MMlist.size() > 0) {//妙秒
                                MMlistData.clear();
                                MM_view.setVisibility(View.VISIBLE);
                                for (int i = 0; i < MMlist.size(); i++) {
                                    MMlistData.add(MMlist.get(i));
                                }
                            } else if (MMlist.size() == 0) {
                                MMlistData.clear();
                                MM_view.setVisibility(View.GONE);
                            }

                            TTlistData.clear();
                            if (viewList != null && viewList.size() > 0) {//关键
                                for (int i = 0; i < viewList.size(); i++) {
                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    TTlistData.add(map);
                                }
                            }
                            if (TTlist != null && TTlist.size() > 0) {//团团
                              //  recyclerview.setVisibility(View.VISIBLE);
                                  TTT_view.setVisibility(View.VISIBLE);
                                recyclerview.addFootView(footview);
                                for (int i = 0; i < TTlist.size(); i++) {
                                    TTlistData.add(TTlist.get(i));
                                }
                            } else {
                                TTT_view.setVisibility(View.GONE);
                            }

                            if (Imglists != null && Imglists.size() > 0) {//轮播图
                                pic_banner.setVisibility(View.VISIBLE);
                                mADParseArray.clear();
                                for (int i = 0; i < Imglists.size(); i++) {
                                    mADParseArray.add(Imglists.get(i).get("imgUrl") + "");
                                }
                                initImgviewpager();
                            } else {
                                pic_banner.setVisibility(View.GONE);
                            }
                            Log.e("TTlistData", TTlistData + "");
                            Log.e("Imglists", Imglists + "");
                            Log.e("mADParseArray", mADParseArray + "");
                            Log.e("GridAllLists", GridAllLists + "");
                            Log.e("listData", listData + "");
                            for (int i = 0; i < GridAllLists.size(); i++) {//viewpager头部滑动菜单
                                Map<String, Object> maps = new HashMap<>();
                                String name = GridAllLists.get(i).get("subCataName") + "";
                                String ImgUrl = GridAllLists.get(i).get("iconMidAndroid") + "";
                                // long id = (long) GridAllLists.get(i).get("id");
                                long id = Long.valueOf(GridAllLists.get(i).get("id") + "");
                                // ProdctBean post00 = new ProdctBean(name, ImgUrl, id);
                                // GridList.add(post00);
                                maps.put("name", name);
                                maps.put("ImgUrl", ImgUrl);
                                maps.put("id", id);
                                GridDatas.add(maps);
                            }
                            Log.e("GridDatas", GridDatas + "");
                            //  initeview();

                            initViewpager();

                            //recyclerview.setAdapter(MainAdapter);
                            MiaomiaoAdapter.notifyDataSetChanged();
                            YouyouAdapter.notifyDataSetChanged();
                            TuantuanAdapter.notifyDataSetChanged();
                            Log.e("listData11", listData + "");
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void initViewpager() {
        // TODO Auto-generated method stub
        //总的页数向上取整
        totalPage = (int) Math.ceil(GridDatas.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<View>();
        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            final GridView gridView = (GridView) View.inflate(getActivity(), R.layout.item_gridview, null);
            gridView.setAdapter(new MyGridViewAdpter(getActivity(), GridDatas, i, mPageSize));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    // TODO Auto-generated method stub
                    // Object obj = gridView.getItemAtPosition(position);
                    Toast.makeText(getContext(), "您点击了---" + GridDatas.get(position).get("name") + "", Toast.LENGTH_SHORT).show();
                    //   Intent intent = new Intent(getActivity(), MerchatTypesActivity.class);
                    //  intent.putExtra("city", address_tv.getText() + "");
                    //  intent.putExtra("subCataId", GridDatas.get(position).get("id") + "");
                    //  startActivity(intent);
                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器

        viewpager.setAdapter(new MyViewPagerAdapter(viewPagerList));

        //添加小圆点
        ivPoints = new ImageView[totalPage];
        points.removeAllViews();
        for (int i = 0; i < totalPage; i++) {
            //循坏加入点点图片组
            ivPoints[i] = new ImageView(getActivity());
            if (i == 0) {
                ivPoints[i].setImageResource(R.mipmap.page_focuese);
            } else {
                ivPoints[i].setImageResource(R.mipmap.page_unfocused);
            }
            ivPoints[i].setPadding(8, 8, 8, 8);
            points.addView(ivPoints[i]);
        }
        //设置ViewPager的滑动监听，主要是设置点点的背景颜色的改变
        viewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                //currentPage = position;
                for (int i = 0; i < totalPage; i++) {
                    if (i == position) {
                        ivPoints[i].setImageResource(R.mipmap.page_focuese);
                    } else {
                        ivPoints[i].setImageResource(R.mipmap.page_unfocused);
                    }
                }
            }
        });

    }

    private void initImgviewpager() {
        final int size = mADParseArray.size();
        vp_shuffling.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                refreshPoint(position % size);
                if (mHandler.hasMessages(HOME_AD_RESULT)) {
                    mHandler.removeMessages(HOME_AD_RESULT);
                }
                mHandler.sendEmptyMessageDelayed(HOME_AD_RESULT, 3000);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (ViewPager.SCROLL_STATE_DRAGGING == arg0
                        && mHandler.hasMessages(HOME_AD_RESULT)) {
                    mHandler.removeMessages(HOME_AD_RESULT);
                }
            }
        });
        BannerAdapter adapter = new BannerAdapter(getActivity(), mADParseArray);
        vp_shuffling.setAdapter(adapter);

        initPointsLayout(size);
        vp_shuffling.setCurrentItem(size * 1000, false);
        // 自动轮播线程
        mHandler.sendEmptyMessageDelayed(HOME_AD_RESULT, 3000);


    }

    private void initPointsLayout(int size) {
        for (int i = 0; i < size; i++) {
            ImageView image = null;
            if (getActivity() != null) {
                image = new ImageView(getActivity());
            }
            float denstity = getResources().getDisplayMetrics().density;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) (1 * denstity), (int) (1 * denstity));
            params.leftMargin = (int) (2 * denstity);
            params.rightMargin = (int) (2 * denstity);
            image.setLayoutParams(params);
            if (i == 0) {
                image.setBackgroundResource(R.mipmap.page_focuese);
            } else {
                image.setBackgroundResource(R.mipmap.page_unfocused);
            }
            dotLayout.addView(image);
        }
    }

    private void refreshPoint(int position) {
        if (dotLayout != null) {
            for (int i = 0; i < dotLayout.getChildCount(); i++) {
                if (i == position) {
                    dotLayout.getChildAt(i).setBackgroundResource(
                            R.mipmap.page_focuese);
                    //      tv_content.setText(content[position]);
                } else {
                    dotLayout.getChildAt(i).setBackgroundResource(
                            R.mipmap.page_unfocused);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_tv:
                Intent it = new Intent(getActivity(), LetterSortActivity.class);
                it.putExtra("code", "0");
                startActivityForResult(it, 1);
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

   /* public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }

    }*/

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
}
