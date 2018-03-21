package td.com.xiaoheixiong.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.ImgSetUtil;
import td.com.xiaoheixiong.Utils.LQRPhotoSelectFragmentUtils;
import td.com.xiaoheixiong.Utils.LQRPhotoSelectUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.aliutil.MyOSSConfig;
import td.com.xiaoheixiong.aliutil.PutObjectSamples;
import td.com.xiaoheixiong.beans.MyConstant;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.SelectPicPopupWindow;
import td.com.xiaoheixiong.dialogs.TwoButtonDialogTitleWhite;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.RatingBar;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class AppraiseActivity extends BaseActivity {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.ratingbar)
    RatingBar ratingbar;
    @Bind(R.id.add_img)
    ImageView addImg;
    @Bind(R.id.add_img_ll)
    LinearLayout addImgLl;
    @Bind(R.id.view_ll)
    LinearLayout viewLl;
    @Bind(R.id.content_tv)
    EditText contentTv;
    @Bind(R.id.close_tv)
    TextView closeTv;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private SelectPicPopupWindow menuWindow;
    private String MOBILE, Imgurls = "", mercId, orgcode, grade = "1";
    private int imgNum = 0;
    private TwoButtonDialogTitleWhite twoButton;

    public OSS oss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise);
        ButterKnife.bind(this);
        Intent it = getIntent();
        orgcode = it.getStringExtra("orgcode");
        MOBILE = MyCacheUtil.getshared(this).getString("PHONENUMBER", "");
        mercId = MyCacheUtil.getshared(this).getString("MERCNUM", "");

        initview();
        initPic();
        oss = new OSSClient(getApplicationContext(), MyConstant.ALI_ENDPOINT, MyOSSConfig.getProvider(), MyOSSConfig.getOSSConfig());
    }

    private void initPic() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(AppraiseActivity.this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
                //    Glide.with(MainActivity.this).load(outputUri).into(mIvPic);
                Log.e("outputFile", outputFile + "");
                //   String str = outputUri.getPath();
                //    upload(new File(str));
                Luban.get(AppraiseActivity.this)
                        .load(outputFile)                     //传人要压缩的图片
                        .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                        .setCompressListener(new OnCompressListener() { //设置回调

                            @Override
                            public void onStart() {
                                //TODO 压缩开始前调用，可以在方法内启动 loading UI
                            }

                            @Override
                            public void onSuccess(File file) {
                                //TODO 压缩成功后调用，返回压缩后的图片文件
                                Log.e("file", file + "");
                                //   upload(file);
                           //     uploadPicture(file);
                                String imgKey = ImgSetUtil.getImgKeyString();
                                upLoadAli(imgKey,file.getAbsolutePath());

                            }

                            @Override
                            public void onError(Throwable e) {
                                //TODO 当压缩过去出现问题时调用
                            }
                        }).launch();    //启动压缩

            }
        }, false);//true裁剪，false不裁剪


    }

    private void initview() {
        titleTv.setText("评论");
        closeTv.setText("提交");
        closeTv.setTextColor(getResources().getColor(R.color.red));
        closeTv.setVisibility(View.VISIBLE);
        ratingbar.setmClickable(true);
        ratingbar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(int var1) {
                //  Toast.makeText(AppraiseActivity.this, "你的评分：" + var1, Toast.LENGTH_SHORT).show();
                grade = var1 + "";
            }
        });
    }

    @OnClick({R.id.back_img, R.id.ratingbar, R.id.add_img, R.id.close_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.ratingbar:

                break;
            case R.id.add_img:

                if (imgNum >= 5) {
                    Toast.makeText(AppraiseActivity.this, "最多只能上传5张图片！", Toast.LENGTH_SHORT).show();
                    return;
                }
                setPic();
                break;
            case R.id.close_tv:
                if (contentTv.getText().length() <= 0) {
                    Toast.makeText(AppraiseActivity.this, "评价不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadpraise();
                break;


        }
    }

    public void setPic() {
        menuWindow = new SelectPicPopupWindow(AppraiseActivity.this, itemsOnClick);
        //  LinearLayout lin = (LinearLayout) findViewById(R.id.fm_main);
        menuWindow.showAtLocation(viewLl, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        // WindowManager.LayoutParams params = this.getWindow().getAttributes();
        // params.alpha=0.7f;
        // this.getWindow().setAttributes(params);
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    //  camera();
                    // 3、调用拍照方法
                    PermissionGen.with(AppraiseActivity.this)
                            .addRequestCode(LQRPhotoSelectFragmentUtils.REQ_TAKE_PHOTO)
                            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                            ).request();

                    break;
                case R.id.btn_pick_photo:
                    // 3、调用从图库选取图片方法
                    PermissionGen.needPermission(AppraiseActivity.this,
                            LQRPhotoSelectFragmentUtils.REQ_SELECT_PHOTO,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    );
                    //    gallery();
                    break;
                default:
                    break;
            }
        }
    };

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        //  showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        //  showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public void uploadPicture(final File file) {
        showLoadingDialog("正在上传照片中。。。");
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, File> mapfile = new HashMap<>();
        map.put("mobile", MOBILE);

        mapfile.put("cardPicFile", file);
        OkHttpClientManager.getInstance(AppraiseActivity.this).upload(AppraiseActivity.this, HttpUrls.upload_Pic, map, mapfile, 0,
                new OkHttpClientManager.ReqCallBack() {

                    @Override
                    public void onReqSuccess(Object result) {
                        // TODO Auto-generated method stub
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        if (result != null) {

                            Map<String, Object> maps = new HashMap<String, Object>();

                            JSONObject jsonObj = new JSONObject().parseObject(result + "");
                            if (jsonObj.get("RSPCOD").equals("000000")) {
                                Toast.makeText(AppraiseActivity.this, jsonObj.get("RSPMSG") + "",
                                        Toast.LENGTH_SHORT).show();

                                if (!jsonObj.get("PICURL").equals("")) {
                                    Log.e("PICURL", "PICURL" + jsonObj.get("PICURL"));
                                    if (Imgurls.equals("")) {
                                        Imgurls = jsonObj.get("PICURL") + "";
                                    } else {
                                        Imgurls = Imgurls + "|" + jsonObj.get("PICURL");
                                    }
                                    final ImageView imgView = new ImageView(AppraiseActivity.this);
                                    imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    Glide.with(AppraiseActivity.this).load(file + "").asBitmap().into(imgView);
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
                                    lp.setMargins(10, 0, 0, 0);
                                    imgView.setLayoutParams(lp);

                                    imgView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            twoButton = new TwoButtonDialogTitleWhite(AppraiseActivity.this, "提示", "是否要删除该图片？", "确定", "取消", new OnMyDialogClickListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.M)
                                                @Override
                                                public void onClick(View v) {
                                                    switch (v.getId()) {
                                                        case R.id.btn_left:
                                                            addImgLl.removeView(imgView);
                                                            imgNum--;
                                                            twoButton.dismiss();
                                                            break;
                                                        case R.id.btn_right:
                                                            twoButton.dismiss();
                                                            break;
                                                    }

                                                }
                                            });
                                            twoButton.show();
                                        }
                                    });
                                    addImgLl.addView(imgView);
                                    imgNum++;

                                } else {
                                    Toast.makeText(AppraiseActivity.this, "上传失败，请重新上传！", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(AppraiseActivity.this, "上传失败，请重新上传！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        // TODO Auto-generated method stub
                        loadingDialogWhole.dismiss();
                        Toast.makeText(AppraiseActivity.this, "上传失败，请重新上传！", Toast.LENGTH_SHORT).show();
                    }

                });
    }



    private void upLoadAli(final String key, final String path) {
        showLoadingDialog("正在上传照片中。。。");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = new PutObjectSamples(oss, MyConstant.ALI_PUBLIC_BUCKET_PUBLIC, key, path).putObjectFromLocalFile();
                if (flag) {//上传成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialogWhole.dismiss();
                            Toast.makeText(AppraiseActivity.this,  "上传成功",
                                    Toast.LENGTH_SHORT).show();
                            Imgurls = MyConstant.ALI_PUBLIC_URL+key;
                            final ImageView imgView = new ImageView(AppraiseActivity.this);
                            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                            Glide.with(AppraiseActivity.this).load(path).asBitmap().into(imgView);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
                            lp.setMargins(10, 0, 0, 0);
                            imgView.setLayoutParams(lp);

                            imgView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    twoButton = new TwoButtonDialogTitleWhite(AppraiseActivity.this, "提示", "是否要删除该图片？", "确定", "取消", new OnMyDialogClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        @Override
                                        public void onClick(View v) {
                                            switch (v.getId()) {
                                                case R.id.btn_left:
                                                    addImgLl.removeView(imgView);
                                                    imgNum--;
                                                    twoButton.dismiss();
                                                    break;
                                                case R.id.btn_right:
                                                    twoButton.dismiss();
                                                    break;
                                            }

                                        }
                                    });
                                    twoButton.show();
                                }
                            });
                            addImgLl.addView(imgView);
                            imgNum++;

                        }

                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialogWhole.dismiss();
                            Toast.makeText(getApplicationContext(), "上传失败，请重新上传！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }





    public void uploadpraise() {

        showLoadingDialog("提交中。。。");
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, File> mapfile = new HashMap<>();

        //  mercId //发起评论的用户ID(必填), 例如：M0081997,
        //         associationId // 被评论的企业ID(必填), 例如"20161202113010569.5516033072907",
        //  description //评论内容(必填)
        //          images // 图片URI集合，多个图片用"|"符号分隔
        //  evaluationType // 评论类型(必填)：0是商家,1是头条
        //          grade // 评论给的星级 1-5级 整数

        map.put("mobile", MOBILE);
        map.put("mercId", mercId);
        map.put("associationId", orgcode);
        map.put("description", contentTv.getText() + "");
        map.put("images", Imgurls);
        map.put("evaluationType", "0");
        map.put("grade", grade);

        OkHttpClientManager.getInstance(AppraiseActivity.this).upload(AppraiseActivity.this, HttpUrls.XHX_Appraise, map, mapfile, 0,
                new OkHttpClientManager.ReqCallBack() {

                    @Override
                    public void onReqSuccess(Object result) {
                        // TODO Auto-generated method stub
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        if (result != null) {
                            JSONObject jsonObj = new JSONObject().parseObject(result + "");
                            if (jsonObj.get("RSPCOD").equals("000000")) {
                                Toast.makeText(getApplicationContext(), jsonObj.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AppraiseActivity.this, jsonObj.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(AppraiseActivity.this, "提交失败，请重新提交！", Toast.LENGTH_SHORT).show();
                        }


                    }


                    @Override
                    public void onReqFailed(String errorMsg) {
                        // TODO Auto-generated method stub
                        loadingDialogWhole.dismiss();
                        Toast.makeText(AppraiseActivity.this, "提交失败，请重新提交！", Toast.LENGTH_SHORT).show();
                    }

                });


    }

}
