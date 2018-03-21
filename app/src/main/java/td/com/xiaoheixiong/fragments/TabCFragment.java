package td.com.xiaoheixiong.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.activity.LetterSortActivity;
import td.com.xiaoheixiong.dialogs.HongbaoButtonDialog;
import td.com.xiaoheixiong.dialogs.HongbaoButtonDialog2;
import td.com.xiaoheixiong.dialogs.HongbaoButtonDialog3;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;

import static android.content.Context.SENSOR_SERVICE;

public class TabCFragment extends BaseFragment implements SensorEventListener {
    @Bind(R.id.bmapView)
    MapView bmapView;
    @Bind(R.id.head_img)
    ImageView headImg;
    @Bind(R.id.CompanyName_tv)
    TextView CompanyNameTv;
    @Bind(R.id.address_tv)
    TextView addressTv;
    @Bind(R.id.name_rl)
    RelativeLayout nameRl;
    @Bind(R.id.dingwei_tv)
    TextView dingweiTv;
    @Bind(R.id.qiehuan_tv)
    TextView qiehuanTv;

    private View view;
    public LayoutInflater mInflater;
    private SharedPreferences.Editor editor;
    private String MERCNUM;
    private SensorManager mSensorManager;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    BaiduMap mBaiduMap;
    BitmapDescriptor mCurrentMarker;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    // UI相关
    RadioGroup.OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private List<Map<String, Object>> list, HBlist, HBlist2;
    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.mipmap.hb_icon);
    BitmapDescriptor hb = BitmapDescriptorFactory
            .fromResource(R.mipmap.hbb_icon);
    private List<Marker> maplist, maplist2;
    private List<Map<String, Object>> Markerlist;
    private List<OverlayOptions> OverlayList;
    private RequestManager glideRequest;
    private String allocServer = "http://103.246.161.44:9999";
    private HongbaoButtonDialog hongbaoDialog;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListeners = new MyLocationListener();
    private PoiSearch mPoiSearch = null;
    private DistrictSearch mdistrictsearch;
    private static LatLng mlatlng;
    private GeoCoder mSearch;
    private String Location = "";
    private LinearLayout hb_bg_ll, openHB_ll;
    private RelativeLayout mainImg_rl;
    private double lng = 0, lat = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        // SDKInitializer.initialize(getContext());
        view = inflater.inflate(R.layout.fragment_tab_c, container, false);
        ButterKnife.bind(this, view);
        MERCNUM = MyCacheUtil.getshared(getActivity()).getString("MERCNUM", "");
        mInflater = inflater;
        initview();
        //  Mpush();
        getMechatMapData();

        return view;

    }

    private void getHBdata() {

        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("mercnum", MERCNUM);

        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.XHX_hongbao, OkHttpClientManager.TYPE_GET, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result+getHBdata", result + "");
                        if (result == null) {
                            return;
                        }
                        JSONObject jsonObj0 = new JSONObject().parseObject(result + "");
                        if (jsonObj0 == null) {
                            HBlist2 = new ArrayList<Map<String, Object>>();
                            initOverlay2();
                            return;
                        }
                        if (jsonObj0.get("RSPCOD").equals("000000")) {
                            HBlist = new ArrayList<Map<String, Object>>();
                            HBlist2 = new ArrayList<Map<String, Object>>();

                            HBlist = JSON.parseObject(jsonObj0.get("detail") + "", new TypeReference<List<Map<String, Object>>>() {
                            });
                            //    initOverlay();
                            for (int i = 0; i < HBlist.size(); i++) {
                                Map<String, Object> mapshb = new HashMap<String, Object>();
                                Map<String, Object> mapsredPacket = new HashMap<String, Object>();
                                mapshb = JSON.parseObject(HBlist.get(i).get("lbsCloudSer") + "", new TypeReference<Map<String, Object>>() {
                                });
                                mapsredPacket = JSON.parseObject(HBlist.get(i).get("redPacket") + "", new TypeReference<Map<String, Object>>() {
                                });
                                //  JSONObject jsonObjhb = new JSONObject().parseObject(HBlist.get(i).get("redPacket") + "");

                                mapshb.put("id", mapsredPacket.get("id"));
                                mapshb.put("redAmount", mapsredPacket.get("redAmount"));
                                Log.e("mapshb", mapshb + "");
                                HBlist2.add(mapshb);
                            }

                            initOverlay2();
                            Log.e("HBlist2", HBlist2 + "");
                        } else {
                            Toast.makeText(getContext(), jsonObj0.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                        }

                        handler();
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getActivity(), "网络不给力！", Toast.LENGTH_SHORT).show();
                        handler();
                    }
                });

    }

    private void handler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getMechatMapData();
            }
        }, 60 * 1000);

    }

    private void initview() {

        glideRequest = Glide.with(getActivity());

        // bmapView = (MapView) view.findViewById(R.id.bmapView);
        // TODO Auto-generated method stub
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mdistrictsearch = DistrictSearch.newInstance();
        mdistrictsearch.setOnDistrictSearchListener(new mydirtrictsearch());
        mCurrentMarker = null;
        // 传入null则，恢复默认图标
        //   mCurrentMarker = null;
        //  mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
        //          mCurrentMode, true, null));

        // 修改为自定义marker
        //   mCurrentMarker = BitmapDescriptorFactory
        //           .fromResource(R.drawable.icon_geo);
        //   mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
        //          mCurrentMode, true, mCurrentMarker,
        //          accuracyCircleFillColor, accuracyCircleStrokeColor));
        // 地图初始化
        mBaiduMap = bmapView.getMap();
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, null));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        mLocClient.registerLocationListener(myListeners);
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3600 * 1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                Log.e("dianji++", "点击。。...");
                for (int i = 0; i < Markerlist.size(); i++) {
                    Log.e("dianji++for", "点击。。...");
                    Log.e("dianji++marker", marker + "    " + Markerlist.get(i).get("mMarker"));

                    if (marker == Markerlist.get(i).get("mMarker")) {
                        Log.e("dianji", "点击。。");

                        if (Markerlist.get(i).get("money").equals("")) {

                            nameRl.setVisibility(View.VISIBLE);
                            addressTv.setText(Markerlist.get(i).get("address") + "");
                            CompanyNameTv.setText(Markerlist.get(i).get("companyName") + "");
                            // Glide.with(getActivity()).load("ddd").into(headImg);
                            glideRequest.load(Markerlist.get(i).get("headImgUrl") + "").transform(new GlideCircleTransform(getActivity())).into(headImg);// 设置图片圆角
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    nameRl.setVisibility(View.INVISIBLE);
                                }
                            }, 3000);
                            break;
                        } else {
                            final int k = i;
                            hongbaoDialog = new HongbaoButtonDialog(getActivity(), "", new OnMyDialogClickListener() {
                                @Override
                                public void onClick(View v) {

                                    switch (v.getId()) {
                                        case R.id.mainImg_rl:
                                            mainImg_rl.setVisibility(View.INVISIBLE);
                                            hb_bg_ll.setVisibility(View.VISIBLE);
                                            scaleAnimation(hb_bg_ll);
                                            break;
                                        case R.id.imageView:
                                            OpenHbao(Markerlist.get(k).get("id") + "", Markerlist.get(k).get("money") + "");
                                            final ImageView imageView = (ImageView) hongbaoDialog.findViewById(R.id.imageView);
                                            imageView.setVisibility(View.INVISIBLE);
                                            hb_bg_ll.setBackgroundResource(R.mipmap.hongbao_bg2);
                                            break;
                                        case R.id.hb_bg_ll:
                                            break;
                                        case R.id.openHB_ll:
                                            hongbaoDialog.dismiss();
                                            break;
                                    }

                                }
                            });
                            //  hongbaoDialog.setCancelable(false);
                            //  hongbaoDialog.setCanceledOnTouchOutside(false);
                            hongbaoDialog.show();
                            mainImg_rl = (RelativeLayout) hongbaoDialog.findViewById(R.id.mainImg_rl);
                            hb_bg_ll = (LinearLayout) hongbaoDialog.findViewById(R.id.hb_bg_ll);
                            openHB_ll = (LinearLayout) hongbaoDialog.findViewById(R.id.openHB_ll);
                            ImageView main_img = (ImageView) hongbaoDialog.findViewById(R.id.main_img);
                            Glide.with(getActivity()).load(Markerlist.get(k).get("mainImgUrl") + "").asBitmap().into(main_img);
                            scaleAnimation(mainImg_rl);

                            break;
                        }
                    }
                }

                return true;
            }

        });

        //地图状态改变相关接口
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                // 手势操作地图，设置地图状态等操作导致地图状态开始改变。
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                // 地图状态改变结束
                //target地图操作的中心点。

                LatLng target = mBaiduMap.getMapStatus().target;
                Log.e("移动后中心点坐标", target + "");
                double chaLng = target.longitude;
                double chaLat = target.latitude;
                if (lng != 0) {
                    double distance = getDistance(lng, lat, chaLng, chaLat);
                    if (distance > 10000) {
                        lng = chaLng;
                        lat = chaLat;
                        Location = chaLng + "," + chaLat;
                        getMechatMapData();
                    }
                }


            }
        });
    }

    private void OpenHbao(String id, final String money) {
        showLoadingDialog("...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("mercnum", MERCNUM);
        maps.put("redPacketId", id);

        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.XHX_Open_hongbao, OkHttpClientManager.TYPE_GET, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        JSONObject jsonObj0 = new JSONObject().parseObject(result + "");
                        Log.e("result open", result + "");
                        if (jsonObj0.get("RSPCOD").equals("000000")) {

                            BigDecimal Umoney = new BigDecimal(money);
                            final BigDecimal hongbaoMon = Umoney.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN);

                            hb_bg_ll.setVisibility(View.INVISIBLE);
                            openHB_ll.setVisibility(View.VISIBLE);
                            TextView money_tv = (TextView) hongbaoDialog.findViewById(R.id.money_tv);
                            money_tv.setText(hongbaoMon + "元");
                            scaleAnimation(openHB_ll);
                        } else {
                            hb_bg_ll.setBackgroundResource(R.mipmap.miss_hb_icon);
                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        hongbaoDialog.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getActivity(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void getMechatMapData() {
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        //    mercNum	是	String	商户号
        //    radius	是	String	搜索半径 	单位米
        //     pageSize 是	String	搜索的数量
        //     location	是	String	百度经纬度	114.066375,22.535874


        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();

        maps.put("MERCNUM", MERCNUM);
        maps.put("radius", "10000");
        maps.put("pageSize", "20");
        maps.put("location", Location);


        //  requestPostByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.XHX_Mechat_Map, OkHttpClientManager.TYPE_GET, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        list = new ArrayList<Map<String, Object>>();
                        JSONObject jsonObj0 = new JSONObject().parseObject(result + "");
                        Log.e("result111", result + "");

                        if (jsonObj0 != null) {
                            if (jsonObj0.get("RSPCOD").equals("000000")) {
                                JSONObject jsonObj1 = new JSONObject().parseObject(jsonObj0.get("detail") + "");

                                list = JSON.parseObject(jsonObj1.get("list") + "", new TypeReference<List<Map<String, Object>>>() {
                                });
                                //   initOverlay();

                                getHBdata();
                                Log.e("list", list + "第一次");
                            } else {
                                Toast.makeText(getContext(), jsonObj0.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                                getHBdata();
                            }
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getActivity(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.qiehuan_tv)
    public void onViewClicked() {
        Intent it = new Intent(getActivity(), LetterSortActivity.class);
        it.putExtra("code", "1");
        startActivityForResult(it, 2);
    }




    /**
     * 补充：计算两点之间真实距离
     *
     * @return 米
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 维度
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;

        // 经度
        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;

        // 地球半径
        double R = 6371;

        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        return d * 1000;
    }




    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || bmapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            if (city != null) {
                dingweiTv.setText(city + district + "");
            }
            lng = location.getLongitude();
            lat = location.getLatitude();
            Location = lng + "," + lat;

        }

    }

    public void initOverlay() {
        // add marker overlay
        maplist = new ArrayList<Marker>();
        OverlayList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {

            JSONArray ja = JSONArray.parseArray(list.get(i).get("location") + "");

            MarkerOptions ooA = new MarkerOptions().position(new LatLng(Double.valueOf(ja.get(1) + ""), Double.valueOf(ja.get(0) + ""))).icon(bd)
                    .zIndex(9).draggable(true);

            Marker mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
            maplist.add(mMarker);

            //   mBaiduMap.addOverlay(new MarkerOptions().position(new LatLng( Double.valueOf(ja.get(1)+""),Double.valueOf(ja.get(0)+""))).icon(bd)
            //          .zIndex(9).draggable(true));
        }

        Log.e("maplist", maplist + "");
    }

    public void initOverlay2() {
        // add marker overlay
        Markerlist = new ArrayList<>();
        maplist2 = new ArrayList<Marker>();

        OverlayList = new ArrayList<>();
        mBaiduMap.clear();
        if (list != null && list.size() > 0) {
           /* for (int i = 0; i < list.size(); i++) {
                Log.e("i",i+"  "+ list.size());
                JSONArray ja = JSONArray.parseArray(list.get(i).get("location") + "");
                for (int j = 0; j < HBlist2.size(); j++) {
                    JSONArray ja2 = JSONArray.parseArray(HBlist2.get(j).get("location") + "");
                    if (ja.get(0).equals(ja2.get(0))) {
                        Log.e("i++",i+"");
                        list.remove(i);
                    }
                }
            }*/

            if (list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    JSONArray ja = JSONArray.parseArray(list.get(i).get("location") + "");
                    MarkerOptions ooA = new MarkerOptions().position(new LatLng(Double.valueOf(ja.get(1) + ""), Double.valueOf(ja.get(0) + ""))).icon(bd)
                            .zIndex(9).draggable(true);

                    Marker mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
                    Log.e("mMarker+list", mMarker + "");
                    map.put("mMarker", mMarker);
                    map.put("id", "");
                    map.put("money", "");
                    map.put("address", list.get(i).get("address") + "");
                    map.put("companyName", list.get(i).get("companyName") + "");
                    map.put("headImgUrl", list.get(i).get("headImgUrl") + "");

                    Markerlist.add(map);

                }
            }
        }


        if (HBlist2 != null && HBlist2.size() > 0) {
            for (int i = 0; i < HBlist2.size(); i++) {
                Map<String, Object> map2 = new HashMap<>();
                JSONArray ja = JSONArray.parseArray(HBlist2.get(i).get("location") + "");

                MarkerOptions ooA = new MarkerOptions().position(new LatLng(Double.valueOf(ja.get(1) + ""), Double.valueOf(ja.get(0) + ""))).icon(hb)
                        .zIndex(9).draggable(true);

                Marker mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
                Log.e("mMarker+HBlist2", mMarker + "");
                map2.put("money", HBlist2.get(i).get("redAmount") + "");
                map2.put("id", HBlist2.get(i).get("id") + "");
                map2.put("mainImgUrl", HBlist2.get(i).get("mainImgUrl") + "");
                map2.put("mMarker", mMarker);
                Markerlist.add(map2);
                //   mBaiduMap.addOverlay(new MarkerOptions().position(new LatLng( Double.valueOf(ja.get(1)+""),Double.valueOf(ja.get(0)+""))).icon(bd)
                //          .zIndex(9).draggable(true));
            }

        }
        Log.e("Markerlist", Markerlist + "");

    }

    public class myGocCoderListener implements OnGetGeoCoderResultListener {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                Log.d("geocode", "no change result");
            }
            //获取地理编码结果
            //  String mlatlng = result.getLocation();


        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                Log.d("geocode", "no result");
                return;
            }
            //获取反向地理编码结果
            StringBuilder resultAddress = new StringBuilder();
            resultAddress.append(result.getAddress());
            Log.d("geocode", resultAddress.toString());

        }
    }

    public class mydirtrictsearch implements OnGetDistricSearchResultListener {

        @Override
        public void onGetDistrictResult(DistrictResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Log.d("districtresout", "no resout");
                return;
            }
            mlatlng = result.getCenterPt();
            Log.e("districtresout", mlatlng.toString());
            setCenter(mlatlng);
            Location = mlatlng.longitude + "," + mlatlng.latitude;
            lng = mlatlng.longitude;
            lat = mlatlng.latitude;
            getMechatMapData();
        }
    }

    private void setCenter(LatLng l) {
        LatLng cenpt = l;
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        if (bmapView != null) {
            bmapView.onDestroy();
        }
        bmapView = null;
        if (mSearch != null) {
            mSearch.destroy();
        }

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        bmapView.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        bmapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("hidden", hidden + "");
      /*  if (!hidden) {
            getHBdata();
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2) {
            String cityJson = data.getStringExtra("city");// 得到新Activity 关闭后返回的数据
            Log.e("resultCode", cityJson + "");
            //   JSONObject json = JSONObject.parseObject(cityJson);
            //   JSONArray ja = JSONArray.parseArray(cityJson+ "");
            mdistrictsearch.searchDistrict(new DistrictSearchOption().cityName(cityJson + "").districtName(cityJson + ""));
            dingweiTv.setText(cityJson + "");
        }
    }

    //缩放
    public void scaleAnimation(View view) {
        /**
         * 参数1：fromX：x轴起始坐标位置
         * 参数2：toX：结束时x轴位置
         * 参数3：fromY：
         * 参数4：toY
         */
        Log.e("zhixing..", "执行。。。");

        ScaleAnimation an = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        an.setDuration(500);
        //执行的次数，起始0-2，到2共3次
        an.setRepeatCount(0);
        view.startAnimation(an);
    }
}
