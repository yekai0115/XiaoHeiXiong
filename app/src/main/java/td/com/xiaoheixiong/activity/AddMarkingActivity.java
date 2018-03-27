package td.com.xiaoheixiong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.bumptech.glide.Glide;
import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jaiky.imagespickers.ImageSelectorActivity;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.DimenUtils;
import td.com.xiaoheixiong.Utils.GlideLoader;
import td.com.xiaoheixiong.Utils.ImgSetUtil;
import td.com.xiaoheixiong.Utils.ListUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.adapter.AddHeadLineImageAdapter;
import td.com.xiaoheixiong.aliutil.MyOSSConfig;
import td.com.xiaoheixiong.aliutil.PutObjectSamples;
import td.com.xiaoheixiong.beans.MyConstant;
import td.com.xiaoheixiong.beans.TuanTuan.TTBean;
import td.com.xiaoheixiong.dialogs.DialogCalendar;
import td.com.xiaoheixiong.eventbus.Msgevent4;
import td.com.xiaoheixiong.eventbus.Msgevent5;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.imagepicker.ImagePicker;
import td.com.xiaoheixiong.imagepicker.cropper.CropImage;
import td.com.xiaoheixiong.imagepicker.cropper.CropImageView;
import td.com.xiaoheixiong.views.MyGridview;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 新增团团、秒秒券
 */

public class AddMarkingActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    private TextView title_tv;
    private ImageView back_img;
    private EditText ed_title;
    private EditText ed_money;
    private EditText ed_endTime;
    private EditText ed_num;
    private EditText ed_tt_num;
    private LinearLayout ll_tt;
    private LinearLayout ll_mm;
    private ImageView img_mm;
    private ImageView img_tt;

    private MyGridview gv_pic_more;
    private EditText ed_card_detal;

    private LinearLayout ll_time;
    private LinearLayout ll_mm_num;
    private LinearLayout ll_tt_num;
    private View view_line;
    private TextView tv_submit;


    private String imagekey;
    private ArrayList<String> bitmapUrls = new ArrayList<>();
    private ArrayList<String> imagekeyList = new ArrayList<>();
    private String mainImg; //    主图
    private String endTime;
    private Context mContext;
    private AddHeadLineImageAdapter certificateAdapter;
    private OSS oss;

    private ImageConfig imageConfig;
    public static final int REQUEST_CODE = 123;
    public static final int TT_SINGLE_REQUEST_CODE = 124;
    public static final int MM_SINGLE_REQUEST_CODE = 125;
    private String MERCNUM;
    final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    //1 团团  2 秒秒 3 游戏卡券
    private int position;
    private ImagePicker imagePicker = new ImagePicker();
    private int width;
    private TTBean ttBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tt_mm);
        mContext = this;
        position = getIntent().getIntExtra("position", 0);
        ttBean=(TTBean)getIntent().getSerializableExtra("ttBean");
        MERCNUM = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        initViews();
        EventBus.getDefault().register(this);
        width = DimenUtils.getWidth(mContext);
    }


    protected void initViews() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        back_img = (ImageView) findViewById(R.id.back_img);
        ed_title = (EditText) findViewById(R.id.ed_title);
        ed_money = (EditText) findViewById(R.id.ed_money);
        ed_endTime = (EditText) findViewById(R.id.ed_endTime);
        ed_num = (EditText) findViewById(R.id.ed_num);
        ed_tt_num = (EditText) findViewById(R.id.ed_tt_num);
        ll_tt = (LinearLayout) findViewById(R.id.ll_tt);
        ll_mm = (LinearLayout) findViewById(R.id.ll_mm);
        img_mm = (ImageView) findViewById(R.id.img_mm);
        img_tt = (ImageView) findViewById(R.id.img_tt);
        gv_pic_more = (MyGridview) findViewById(R.id.gv_pic_more);
        ed_card_detal = (EditText) findViewById(R.id.ed_card_detal);

        ll_time = (LinearLayout) findViewById(R.id.ll_time);
        ll_mm_num = (LinearLayout) findViewById(R.id.ll_mm_num);
        ll_tt_num = (LinearLayout) findViewById(R.id.ll_tt_num);
        view_line = (View) findViewById(R.id.view_line);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        if (position == 1) {//团团
            ll_tt.setVisibility(View.VISIBLE);
            ll_mm.setVisibility(View.GONE);
            ll_mm_num.setVisibility(View.GONE);
            ll_time.setVisibility(View.GONE);
            view_line.setVisibility(View.GONE);
            ll_tt_num.setVisibility(View.VISIBLE);
            title_tv.setText("新建团团");
        } else if (position == 2) {//秒秒
            ll_tt.setVisibility(View.GONE);
            ll_mm.setVisibility(View.VISIBLE);
            ll_mm_num.setVisibility(View.VISIBLE);
            ll_time.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
            ll_tt_num.setVisibility(View.GONE);
            title_tv.setText("新建秒秒");
        }

        if (null != ttBean) {//预览
            String name = ttBean.getName();
            String price = ttBean.getPrice();
            String cardNum = ttBean.getCardNum();
            String description = ttBean.getDescription();
            String endTime = ttBean.getEndTime();
            String detailImg = ttBean.getDetailImg();
            mainImg = ttBean.getMainImg();
            String enterNum = ttBean.getEnterNum();
            ed_title.setText(name);
            ed_card_detal.setText(description);
            ed_endTime.setText(endTime);
            ed_money.setText(price);
            ed_num.setText(cardNum);
            ed_tt_num.setText(enterNum);

            ed_title.setClickable(false);
            ed_card_detal.setClickable(false);
            ed_endTime.setClickable(false);
            ed_money.setClickable(false);
            ed_num.setClickable(false);
            ed_tt_num.setClickable(false);

            ed_title.setFocusable(false);
            ed_card_detal.setFocusable(false);
            ed_endTime.setFocusable(false);
            ed_money.setFocusable(false);
            ed_num.setFocusable(false);
            ed_tt_num.setFocusable(false);

            tv_submit.setVisibility(View.GONE);
            if (position == 2) {
                title_tv.setText("秒秒");
                Glide.with(mContext).load(mainImg)
                        .fitCenter()
                        //    .override(width, DimenUtils.dip2px(context, 540))
                        .placeholder(R.drawable.pic_nomal_loading_style)
                        .error(R.drawable.pic_nomal_loading_style)
                        .into(img_mm);
            } else if (position == 1) {
                title_tv.setText("团团");
                Glide.with(mContext).load(mainImg)
                        .fitCenter()
                        //    .override(width, DimenUtils.dip2px(context, 540))
                        .placeholder(R.drawable.pic_nomal_loading_style)
                        .error(R.drawable.pic_nomal_loading_style)
                        .into(img_tt);
            }

            bitmapUrls = ListUtils.getList(detailImg);
        } else {//新增
            bitmapUrls.add("");
        }
        certificateAdapter = new AddHeadLineImageAdapter(this, bitmapUrls);
        gv_pic_more.setAdapter(certificateAdapter);
        gv_pic_more.setOnItemClickListener(this);
        certificateAdapter.setOnDeleteImageListener(new AddHeadLineImageAdapter.OnDeleteImageListener() {
            @Override
            public void click(int position) {
                try{
                    bitmapUrls.remove(position);
                }catch (Exception e){

                }
                certificateAdapter.setList(bitmapUrls);
            }
        });

        oss = new OSSClient(getApplicationContext(), MyConstant.ALI_ENDPOINT, MyOSSConfig.getProvider(), MyOSSConfig.getOSSConfig());

        tv_submit.setOnClickListener(new View.OnClickListener() {//发布
            @Override
            public void onClick(View view) {
                String name = ed_title.getText().toString();//标题
                String price = ed_money.getText().toString();//价格
                String mm_num = ed_num.getText().toString();//秒秒券数量
                String description = ed_card_detal.getText().toString();//描述
                String enterNum = ed_tt_num.getText().toString();//团团券参团人数
                if (StringUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "请输入卡券标题！", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(price)) {
                    Toast.makeText(getApplicationContext(), "请输入价格！", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(description)) {
                    Toast.makeText(getApplicationContext(), "请输入卡券描述信息！", Toast.LENGTH_SHORT).show();
                } else if (null == bitmapUrls || bitmapUrls.isEmpty() || bitmapUrls.size() == 1) {
                    Toast.makeText(getApplicationContext(), "请上传详情页展示图！", Toast.LENGTH_SHORT).show();
                } else {
                    if (position == 1) {
                        if (StringUtils.isEmpty(mainImg)) {
                            Toast.makeText(getApplicationContext(), "请上传首页展示图！", Toast.LENGTH_SHORT).show();
                        } else if (StringUtils.isEmpty(enterNum)) {
                            Toast.makeText(getApplicationContext(), "请输入成团人数！", Toast.LENGTH_SHORT).show();
                        }
                    } else if (position ==2) {
                        if (StringUtils.isEmpty(mainImg)) {
                            Toast.makeText(getApplicationContext(), "请上传活动展示图！", Toast.LENGTH_SHORT).show();
                        } else if (StringUtils.isEmpty(mm_num)) {
                            Toast.makeText(getApplicationContext(), "请输入卡券数量！", Toast.LENGTH_SHORT).show();
                        } else if (StringUtils.isEmpty(endTime)) {
                            Toast.makeText(getApplicationContext(), "请选择秒杀持续时间！", Toast.LENGTH_SHORT).show();
                        }
                    }
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


        ed_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(2);
            }
        });
        img_mm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null==ttBean){
                    startChooser(487, 158);
                }

            }
        });

        img_tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null==ttBean){
                    startChooser((width-60),312);
                }



//                imageConfig = new ImageConfig.Builder(
//                        new GlideLoader())
//                        .steepToolBarColor(getResources().getColor(R.color.titleBlue))
//                        .titleBgColor(getResources().getColor(R.color.titleBlue))
//                        .titleSubmitTextColor(getResources().getColor(R.color.white))
//                        .titleTextColor(getResources().getColor(R.color.white))
//                        // 开启单选   （默认为多选）
//                        .singleSelect()
//                        // 裁剪 (只有单选可裁剪)
//                        .crop(1, 2, 200, 400)
//                        // 开启拍照功能 （默认关闭）
//                        .showCamera()
//                        //设置显示容器
//                        //     .setContainer(llContainer)
//                        .requestCode(TT_SINGLE_REQUEST_CODE)
//                        .build();
//                ImageSelector.open(AddMarkingActivity.this, imageConfig);
            }
        });

        imageConfig = new ImageConfig.Builder(
                new GlideLoader())
                .steepToolBarColor(getResources().getColor(R.color.titleBlue))
                .titleBgColor(getResources().getColor(R.color.titleBlue))
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启多选   （默认为多选）
                .mutiSelect()
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(3)
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
    }


    private void startChooser(final int width,final int height) {
        // 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {

            }

            // 裁剪图片回调
            @Override
            public void onCropImage(Uri imageUri) {

                String path = getAblumPicPath(imageUri);
                String imagekey = ImgSetUtil.getImgKeyString();
                upLoadAli(imagekey, path, position);
            }

            // 自定义裁剪配置
            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(width, height);
                // 宽高比
                //.setAspectRatio(2, 1);
            }

            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
            }
        });
    }


    private void showCalender(final int type) {
        new DialogCalendar().showDialog(this, new DialogCalendar.onConfirmClickedListener() {
            @Override
            public void onClick(Date selectedDate) {


                if (selectedDate != null) {
                    endTime = format.format(selectedDate);
                } else {
                    Date date = new Date();
                    endTime = format.format(date);
                }
                ed_endTime.setText(endTime);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == getDataSize()) {
            if (null != bitmapUrls && bitmapUrls.size() > 0) {
                bitmapUrls.remove(bitmapUrls.size() - 1);
            }

            ImageSelector.open(AddMarkingActivity.this, imageConfig);
        }
    }

    private int getDataSize() {

        return bitmapUrls == null ? 0 : bitmapUrls.size() - 1;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            if (requestCode == REQUEST_CODE) {
                bitmapUrls.clear();
                bitmapUrls.addAll(pathList);
                bitmapUrls.add("");
                certificateAdapter.setList(bitmapUrls);
            } else {
                imagePicker.onActivityResult(this, requestCode, resultCode, data);
            }
        }else{
            if (requestCode == REQUEST_CODE) {
                bitmapUrls.add("");
            }
        }
    }


    /**
     * @return
     * @方法说明:获得相册图片的路径
     * @方法名称:getAblumPicPath
     * @返回 String
     */
    public String getAblumPicPath(Uri originalUri) {
        String path = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        if (originalUri != null && proj != null) {
            Cursor cursor = getContentResolver().query(originalUri, null, null, null, null);
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
        showLoadingDialog("...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = new PutObjectSamples(oss, MyConstant.ALI_PUBLIC_BUCKET_PUBLIC, key, path).putObjectFromLocalFile();
                if (flag) {//上传成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialogWhole.dismiss();
                            mainImg = MyConstant.ALI_PUBLIC_URL + key;
                            switch (value) {
                                case 2:
                                    Glide.with(mContext).load(mainImg)
                                            .fitCenter()
                                            //    .override(width, DimenUtils.dip2px(context, 540))
                                            .placeholder(R.drawable.pic_nomal_loading_style)
                                            .error(R.drawable.pic_nomal_loading_style)
                                            .into(img_mm);
                                    break;
                                case 1:
                                    Glide.with(mContext).load(mainImg)
                                            .fitCenter()
                                            //    .override(width, DimenUtils.dip2px(context, 540))
                                            .placeholder(R.drawable.pic_nomal_loading_style)
                                            .error(R.drawable.pic_nomal_loading_style)
                                            .into(img_tt);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < bitmapUrls.size(); i++) {
                    imagekey = ImgSetUtil.getImgKeyString();
                    String path = bitmapUrls.get(i);
                    if(!StringUtils.isEmpty(path)){
                        boolean flag = new PutObjectSamples(oss, MyConstant.ALI_PUBLIC_BUCKET_PUBLIC, imagekey, path).putObjectFromLocalFile();
                        if (flag) {//上传成功
                            String headimgurl = MyConstant.ALI_PUBLIC_URL + imagekey;
                            imagekeyList.add(headimgurl);

                        }
                    }
                    if (i == (bitmapUrls.size() - 1)) {
                        EventBus.getDefault().post(new Msgevent4());

                    }
                }
            }
        }).start();
    }


    private void saveHeadlineInfo(String images) {
        HashMap<String, Object> maps = new HashMap<>();
        String name = ed_title.getText().toString();//标题
        String price = ed_money.getText().toString();//价格
        String mm_num = ed_num.getText().toString();//秒秒券数量
        String description = ed_card_detal.getText().toString();//描述
        String enterNum = ed_tt_num.getText().toString();//团团券参团人数
        maps.put("name", name);
        maps.put("price", price);
        maps.put("mainImg", mainImg);
        maps.put("detailImg", images);
        maps.put("description", description);
        maps.put("mercId", MERCNUM);
        String url = "";
        if (position == 1) {
            url = HttpUrls.XHX_add_tuantuan;
            maps.put("enterNum", enterNum);
        } else if (position == 2) {//秒秒
            url = HttpUrls.XHX_add_miaomiao;
            maps.put("endTime", endTime);
            maps.put("numbers", mm_num);
        }
        OkHttpClientManager.getInstance(this).requestAsyn(url, OkHttpClientManager.TYPE_GET,
                maps, OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {

                    @Override
                    public void onReqSuccess(Object result) {
                        // TODO Auto-generated method stub
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        JSONObject jsonObj = new JSONObject().parseObject(result + "");
                        if (jsonObj.get("RSPCOD").equals("000000")) {
                            Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new Msgevent5());
                            finish();
                        } else {

                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
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
        for (String key : imagekeyList) {
            if (!StringUtils.isEmpty(key)) {
                sb.append(key).append("|");
            }
        }
        String images = sb.deleteCharAt(sb.length() - 1).toString();
        saveHeadlineInfo(images);
    }
}
