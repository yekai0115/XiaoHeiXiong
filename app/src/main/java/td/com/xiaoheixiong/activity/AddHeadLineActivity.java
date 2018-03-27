package td.com.xiaoheixiong.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jaiky.imagespickers.ImageSelectorActivity;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.GlideLoader;
import td.com.xiaoheixiong.Utils.ImgSetUtil;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.SPUtils;
import td.com.xiaoheixiong.adapter.AddHeadLineImageAdapter;
import td.com.xiaoheixiong.aliutil.MyOSSConfig;
import td.com.xiaoheixiong.aliutil.PutObjectSamples;
import td.com.xiaoheixiong.beans.MyConstant;
import td.com.xiaoheixiong.eventbus.Msgevent1;
import td.com.xiaoheixiong.fragments.TabA2Fragment;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.MyGridview;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 *
 */

public class AddHeadLineActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    private TextView title_tv, close_tv;
    private ImageView back_img;
    private EditText ed_after_sale;
    private MyGridview gv_pic;
    private TextView tv_address;

    private LinearLayout ll_nick;
    private EditText ed_nick;


    private String imagekey;
    private ArrayList<String> bitmapUrls = new ArrayList<>();
    private ArrayList<String> imagekeyList = new ArrayList<>();
    private Context mContext;

    private AddHeadLineImageAdapter certificateAdapter;
    private OSS oss;

    private ImageConfig imageConfig;
    public static final int REQUEST_CODE = 123;
    private String MERCNUM;

    private LocationClient mLocationClient = null;
    public MyLocationListenner myListener = new MyLocationListenner();
    private String lng, lat, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_headline);
        mContext = this;
        MERCNUM = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        address = (String) SPUtils.get(mContext, "address", "");
        lng = (String) SPUtils.get(mContext, "lng", "");
        lat = (String) SPUtils.get(mContext, "lat", "");
        initViews();
        dingwei();
        EventBus.getDefault().register(this);
    }


    protected void initViews() {

        ed_after_sale = (EditText) findViewById(R.id.ed_after_sale);
        gv_pic = (MyGridview) findViewById(R.id.gv_pic);
        close_tv = (TextView) findViewById(R.id.close_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        back_img = (ImageView) findViewById(R.id.back_img);
        tv_address = (TextView) findViewById(R.id.tv_address);
        ed_nick = (EditText) findViewById(R.id.ed_nick);
        ll_nick = (LinearLayout) findViewById(R.id.ll_nick);

        title_tv.setText("发布动态");
        close_tv.setText("发布");
        close_tv.setVisibility(View.VISIBLE);
        close_tv.setTextColor(getResources().getColor(R.color.red));
        bitmapUrls.add("");
        certificateAdapter = new AddHeadLineImageAdapter(this, bitmapUrls);
        gv_pic.setAdapter(certificateAdapter);
        gv_pic.setOnItemClickListener(this);
        certificateAdapter.setOnDeleteImageListener(new AddHeadLineImageAdapter.OnDeleteImageListener() {
            @Override
            public void click(int position) {
                bitmapUrls.remove(position);
                certificateAdapter.setList(bitmapUrls);
            }
        });

        oss = new OSSClient(getApplicationContext(), MyConstant.ALI_ENDPOINT, MyOSSConfig.getProvider(), MyOSSConfig.getOSSConfig());

        close_tv.setOnClickListener(new View.OnClickListener() {//发布
            @Override
            public void onClick(View view) {
                String description = ed_after_sale.getText().toString();
                if (StringUtils.isEmpty(description)) {
                    Toast.makeText(getApplicationContext(), "请输入消息内容！", Toast.LENGTH_SHORT).show();
                }
//                else if (null == bitmapUrls || bitmapUrls.isEmpty() || bitmapUrls.size() == 1) {
//                    Toast.makeText(getApplicationContext(), "请上传图片！", Toast.LENGTH_SHORT).show();
//                }
                else {
                    upLoadAli(description);

                }

            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void dingwei() {
        if (StringUtils.isEmpty(lat) && StringUtils.isEmpty(lng)) {
            mLocationClient = new LocationClient(mContext);
            mLocationClient.registerLocationListener(myListener);
            setLocationOption();
            mLocationClient.start();
        } else {
            tv_address.setText(address);
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
            SPUtils.put(mContext, "lat", lat);
            SPUtils.put(mContext, "lng", lng);
            if (city.contains("市")) {
                city = city.substring(0, city.length() - 1);
            }
            tv_address.setText(city);

        }

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == getDataSize()) {

            if (null != bitmapUrls && bitmapUrls.size() > 0) {
                bitmapUrls.remove(bitmapUrls.size() - 1);
            }
            imageConfig = new ImageConfig.Builder(
                    new GlideLoader())
                    .steepToolBarColor(getResources().getColor(R.color.titleBlue))
                    .titleBgColor(getResources().getColor(R.color.titleBlue))
                    .titleSubmitTextColor(getResources().getColor(R.color.white))
                    .titleTextColor(getResources().getColor(R.color.white))
                    // 开启多选   （默认为多选）
                    .mutiSelect()
                    // 多选时的最大数量   （默认 9 张）
                    .mutiSelectMaxSize(9)
                    //设置图片显示容器，参数：、（容器，每行显示数量，是否可删除）
                    //      .setContainer(gv_pic, 4, true)
                    // 已选择的图片路径
                    .pathList(bitmapUrls)
                    // 拍照后存放的图片路径（默认 /temp/picture）
                    .filePath("/temp")
                    // 开启拍照功能 （默认关闭）
                    .showCamera()
                    .requestCode(REQUEST_CODE)
                    .build();
            ImageSelector.open(AddHeadLineActivity.this, imageConfig);
        }
    }

    private int getDataSize() {

        return bitmapUrls == null ? 0 : bitmapUrls.size() - 1;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            bitmapUrls.clear();
            bitmapUrls.addAll(pathList);
            bitmapUrls.add("");
            certificateAdapter.setList(bitmapUrls);
        }
    }


    /**
     * @param data
     * @param ac
     * @return
     * @方法说明:获得相册图片的路径
     * @方法名称:getAblumPicPath
     * @返回 String
     */
    public static String getAblumPicPath(Intent data, Activity ac) {
        Uri originalUri = data.getData();
        String path = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        if (originalUri != null && proj != null) {
            Cursor cursor = ac.getContentResolver().query(originalUri, null, null, null, null);
            if (cursor == null) {
                path = originalUri.getPath();
                if (!StringUtils.isEmpty(path)) {
                    String type = ".jpg";
                    String type1 = ".png";
                    if (path.endsWith(type) || path.endsWith(type1)) {
                        return path;
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            } else {
                /**将光标移至开，这个很重要，不小心很容易引起越**/
                cursor.moveToFirst();
                /**按我个人理解 这个是获得用户选择的图片的索引**/
                int column_index = cursor.getColumnIndex(proj[0]);
                /** 最后根据索引值获取图片路**/
                path = cursor.getString(column_index);
                cursor.close();
            }
        }
        return path;
    }


    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(final File f, final int value, final String imgKey) {
        Luban.get(this)
                .load(f)
                .putGear(Luban.THIRD_GEAR)
                .setFilename(System.currentTimeMillis() + "")
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File f) {
                        showLoadingDialog("...");
                        upLoadAli(imgKey, f.getAbsolutePath(), value);
                    }

                    @Override
                    public void onError(Throwable e) {//压缩失败
                        upLoadAli(imgKey, f.getAbsolutePath(), value);
                    }
                }).launch();
    }


    private void upLoadAli(final String key, final String path, final int value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = new PutObjectSamples(oss, MyConstant.ALI_PUBLIC_BUCKET_PUBLIC, key, path).putObjectFromLocalFile();
                if (flag) {//上传成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialogWhole.dismiss();
                            switch (value) {
                                case 1:
                                    //  imgPath = path;
                                    imagekey = key;
                                    imagekeyList.add(imagekey);
                                    bitmapUrls.add(path);
                                    certificateAdapter.setList(bitmapUrls);
                                    break;
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialogWhole.dismiss();
                            Toast.makeText(getApplicationContext(), "上传失败,请重新上传！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    private void upLoadAli(final String description) {
        showLoadingDialog("...");
        if (bitmapUrls.size() == 1) {
            saveHeadlineInfo();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < bitmapUrls.size(); i++) {
                        imagekey = ImgSetUtil.getImgKeyString();
                        String path = bitmapUrls.get(i);
                        if (!StringUtils.isEmpty(path)) {
                            boolean flag = new PutObjectSamples(oss, MyConstant.ALI_PUBLIC_BUCKET_PUBLIC, imagekey, path).putObjectFromLocalFile();
                            if (flag) {//上传成功
                                String headimgurl = MyConstant.ALI_PUBLIC_URL + imagekey;
                                imagekeyList.add(headimgurl);
                            }

                        }
                        if (i == (bitmapUrls.size() - 1)) {
                            EventBus.getDefault().post(new Msgevent1());

                        }
                    }
                }
            }).start();
        }

    }


    private void saveHeadlineInfo() {
        String description = ed_after_sale.getText().toString();
        StringBuilder sb = new StringBuilder();
        for (String key : imagekeyList) {
            if (!StringUtils.isEmpty(key)) {
                sb.append(key).append("|");
            }
        }
        String images;
        if(StringUtils.isEmpty(sb.toString())){
            images="";
        }else{
             images = sb.deleteCharAt(sb.length() - 1).toString();
        }
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("sign", "");
        maps.put("merc_id", MERCNUM);
        maps.put("description", description);
        maps.put("images", images);
        maps.put("location_desc", tv_address.getText().toString());
        maps.put("lng", lng);
        maps.put("lat", lat);
        maps.put("nickName", ed_nick.getText().toString());

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_add_toutiao, OkHttpClientManager.TYPE_GET,
                maps, OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {

                    @Override
                    public void onReqSuccess(Object result) {
                        // TODO Auto-generated method stub
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        JSONObject jsonObj = new JSONObject().parseObject(result + "");
                        if (jsonObj.get("RSPCOD").equals("000000")) {
                            Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(1, intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObj.getString("RSPDATA"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        // TODO Auto-generated method stub
                        loadingDialogWhole.dismiss();
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mLocationClient && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMain(Msgevent1 messageEvent) {

        saveHeadlineInfo();
    }
}
