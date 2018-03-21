package td.com.xiaoheixiong.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.AppManager;
import td.com.xiaoheixiong.Utils.DataCleanManager;
import td.com.xiaoheixiong.Utils.LQRPhotoSelectUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.views.StatusBarUtil;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.OneButtonDialogWhite;
import td.com.xiaoheixiong.dialogs.SelectPicPopupWindow;
import td.com.xiaoheixiong.dialogs.TwoButtonDialogTitleWhite;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class SettingActivity extends BaseActivity {

    @Bind(R.id.exit_tv)
    TextView exitTv;
    @Bind(R.id.cache_tv)
    TextView cacheTv;
    @Bind(R.id.Clear_cache_rl)
    RelativeLayout ClearCacheRl;
    @Bind(R.id.Version_information_rl)
    RelativeLayout VersionInformationRl;
    @Bind(R.id.change_passwrod_rl)
    RelativeLayout ChangePasswordRl;
    @Bind(R.id.real_name_rl)
    RelativeLayout realNameRl;
    @Bind(R.id.bind_user_rl)
    RelativeLayout bindUserRl;
    @Bind(R.id.ll_main)
    LinearLayout llMain;
    @Bind(R.id.set_version_tv)
    TextView setVersionTv;
    @Bind(R.id.user_information_rl)
    RelativeLayout userInformationRl;
    @Bind(R.id.real_name_tv)
    TextView realNameTv;
    @Bind(R.id.bank_manage_rl)
    RelativeLayout bankManageRl;
    @Bind(R.id.change_payword_rl)
    RelativeLayout changePaywordRl;
    @Bind(R.id.get_payword_rl)
    RelativeLayout getPaywordRl;
    @Bind(R.id.company_rl)
    RelativeLayout companyRl;
    @Bind(R.id.privacy_rl)
    RelativeLayout privacyRl;
    @Bind(R.id.help_rl)
    RelativeLayout helpRl;

    private TextView title_tv;
    private ImageView back_img, right_img;
    private RelativeLayout title_right_rl;
    private String name = "", phone = "", enterpriseName = "", lastLoginRealm = "", STS, mercId;
    private TwoButtonDialogTitleWhite TwoButtonDialog;
    private SharedPreferences.Editor editor;
    private SelectPicPopupWindow menuWindow;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final String PHOTO_FILE_NAME = "photo.jpg";
    private String idcardpic = "", userid = "";
    private File tempFile, cardPicFile;
    private int mBorderWidth = 2;
    private int mBorderColor = Color.parseColor("#f2f2f2");
    private String code = "U99999", headImgSet;
    private OneButtonDialogWhite button;
    private Uri imageUri;
    private File outputImage;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        editor = MyCacheUtil.setshared(this);
        name = MyCacheUtil.getshared(this).getString("name", "");
        phone = MyCacheUtil.getshared(this).getString("phone", "");
        mercId = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        enterpriseName = MyCacheUtil.getshared(this).getString("enterpriseName", "");
        lastLoginRealm = MyCacheUtil.getshared(this).getString("lastLoginRealm", "");
        headImgSet = MyCacheUtil.getshared(this).getString("headImgSet", "");
        userid = MyCacheUtil.getshared(this).getString("userid", "");
        STS = MyCacheUtil.getshared(this).getString("STS", "");
        Log.e("STS", STS + "");
        intiView();
        init();
        //  settitle(R.color.transparent);
    }

    @Override
    protected void setStatusBar() {
        // super.setStatusBar();
        //  StatusBarUtil.setTransparent(this);
        //   StatusBarUtil.setTranslucent(this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
        //   StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.red), 0);
    }

    private void init() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
                //    Glide.with(MainActivity.this).load(outputUri).into(mIvPic);
                Log.e("outputFile", outputFile + "");
                //   String str = outputUri.getPath();
                //    upload(new File(str));
                Luban.get(SettingActivity.this)
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
                            }

                            @Override
                            public void onError(Throwable e) {
                                //TODO 当压缩过去出现问题时调用
                            }
                        }).launch();    //启动压缩

            }
        }, true);//true裁剪，false不裁剪

    }

    private void intiView() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        back_img = (ImageView) findViewById(R.id.back_img);
        title_right_rl = (RelativeLayout) findViewById(R.id.title_right_rl);
        right_img = (ImageView) findViewById(R.id.right_img);
        title_right_rl.setVisibility(View.GONE);
        title_tv.setText("设置");
        back_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        setVersionTv.setText("V " + getVersion());
        Log.e("++", "jin .....");
        if (STS.equals("0")) {
            Log.e("", "jin .....");
            realNameTv.setText("已认证");
        } else if (STS.equals("1")) {
            realNameTv.setText("已提交待认证");
        } else if (STS.equals("2")) {
            realNameTv.setText("未认证");
        } else if (STS.equals("-1")) {
            realNameTv.setText("审核未通过");
        }


        try {
            cacheTv.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @OnClick({R.id.exit_tv, R.id.Clear_cache_rl, R.id.Version_information_rl
            , R.id.change_passwrod_rl, R.id.set_img, R.id.real_name_rl, R.id.bind_user_rl, R.id.user_information_rl,
            R.id.bank_manage_rl, R.id.change_payword_rl, R.id.get_payword_rl, R.id.help_rl,R.id.company_rl, R.id.privacy_rl})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {

            case R.id.exit_tv:
                TwoButtonDialog = new TwoButtonDialogTitleWhite(this, "提示", "确定退出吗！", "退出应用", "退出账号",


                        new OnMyDialogClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.btn_left:
                                        TwoButtonDialog.dismiss();
                                        AppManager.getAppManager().AppExit(getApplicationContext());
                                        break;
                                    case R.id.btn_right:
                                        editor.clear();
                                        editor.commit();
                                        TwoButtonDialog.dismiss();
                                        Intent it = new Intent(SettingActivity.this, LoginActivity.class);
                                        startActivity(it);
                                        AppManager.getAppManager().finishAllActivity();
                                        //   finish();
                                        break;
                                }
                            }
                        });
                TwoButtonDialog.show();

                break;
            case R.id.Clear_cache_rl:
                TwoButtonDialog = new TwoButtonDialogTitleWhite(this, "清除缓存", "确定清除缓存吗！", "确定", "取消",
                        new OnMyDialogClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.btn_left:
                                        TwoButtonDialog.dismiss();
                                        try {
                                            DataCleanManager.clearAllCache(getApplicationContext());
                                            cacheTv.setText("0");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        break;
                                    case R.id.btn_right:
                                        TwoButtonDialog.dismiss();
                                        break;
                                }
                            }
                        });
                TwoButtonDialog.show();

                break;

            case R.id.Version_information_rl:
                Intent it = new Intent(this, VersionMsgActivity.class);
                startActivity(it);
                break;

/*
            case R.id.head_rl:
                if (PermissionsCheckerUtil.lacksPermissions(this, Manifest.permission.CAMERA) || PermissionsCheckerUtil.lacksPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    button = new OneButtonDialogWhite(SettingActivity.this, "为保证应用正常使用，需开启应用相机和存储权限！", "前往设置", new OnMyDialogClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Uri packageURI = Uri.parse("package:" + "com.beixiang.points");
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                            button.dismiss();
                        }

                    });
                    button.setCancelable(false);
                    button.setCanceledOnTouchOutside(false);
                    button.show();
                    return;
                }
                ;

                setheadimg();

                break;*/

            case R.id.change_passwrod_rl:
                Intent itPwd = new Intent(this, ChangePasswordActivity.class);
                startActivity(itPwd);

                break;
            case R.id.set_img:
                // Intent its = new Intent(this, ChangePasswordActivity.class);
                //   startActivity(its);
                break;
            case R.id.right_img:
                break;

            case R.id.real_name_rl:
                if (STS.equals("0")) {//实名认证状态-1:审核未通过 0:提交已通过 1已提交待认证 2未提交 3银行卡信息已提交
                    button = new OneButtonDialogWhite(SettingActivity.this, "实名认证已通过！", "确定", new OnMyDialogClickListener() {

                        @Override
                        public void onClick(View v) {
                            button.dismiss();
                        }

                    });
                    button.show();
                    return;
                } else if (STS.equals("1")) {
                    button = new OneButtonDialogWhite(SettingActivity.this, "已提交审核，请耐心等待！", "确定", new OnMyDialogClickListener() {

                        @Override
                        public void onClick(View v) {
                            button.dismiss();
                        }

                    });
                    button.show();
                    return;
                }
                Intent its = new Intent(this, TiedCardRealNameActivity.class);
                startActivity(its);

                break;
            case R.id.bind_user_rl:
                Intent itw = new Intent(this, LoginActivity.class);
                startActivity(itw);
                finish();
                break;
            case R.id.user_information_rl:
                Intent itInfo = new Intent(this, BussinessInfoActivity.class);
                startActivity(itInfo);
                break;
            case R.id.bank_manage_rl:
                if (STS.equals("0")) {
                    Intent itbank = new Intent(this, BankInfoChangeActivity.class);
                    startActivity(itbank);
                } else if (STS.equals("1")) {
                    Toast.makeText(this, "请您等待实名认证通过后再操作！", Toast.LENGTH_LONG).show();
                } else if (STS.equals("2")) {
                    Toast.makeText(this, "请您先实名认证！", Toast.LENGTH_LONG).show();
                } else if (STS.equals("-1")) {
                    Toast.makeText(this, "实名认证审核未通过！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.change_payword_rl:
                JugdeSetpayPWD(0);

                break;
            case R.id.get_payword_rl:
                JugdeSetpayPWD(1);

                break;

            case R.id.company_rl:
                intent.setClass(this, IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_aboutCompany);
                startActivity(intent);
                break;
            case R.id.privacy_rl:
                intent.setClass(this, IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_privacyPolicy);
                startActivity(intent);
                break;

            case R.id.help_rl:
                intent.setClass(this, IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_opinions);
                startActivity(intent);
                break;

        }
    }

    private void JugdeSetpayPWD(final int code) {
        showLoadingDialog("检测中...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        HashMap<String, Object> maps = new HashMap<>();
        maps.put("mercNum", mercId);

        OkHttpClientManager.getInstance(this).requestAsyn(HttpUrls.XHX_jugde_setPaypwd, OkHttpClientManager.TYPE_GET, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");
                        loadingDialogWhole.dismiss();
                        JSONObject oJSON = JSON.parseObject(result + "");

                        if (oJSON.get("RSPCOD").equals("000000")) {
                            if (code == 0) {
                                Intent it = new Intent(SettingActivity.this, SetOrChangePayPwdActivity.class);
                                it.putExtra("code", "1");
                                startActivity(it);
                            } else if (code == 1) {
                                Intent it = new Intent(SettingActivity.this, GetbackPayPasswordActivity.class);
                                startActivity(it);
                            }

                        } else if (oJSON.get("RSPCOD").equals("111111")) {
                            TwoButtonDialog = new TwoButtonDialogTitleWhite(SettingActivity.this, "提示", "您还没设置支付密码，点击确定设置密码！", "确定", "取消",
                                    new OnMyDialogClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            switch (v.getId()) {
                                                case R.id.btn_left:
                                                    Intent it = new Intent(SettingActivity.this, SetOrChangePayPwdActivity.class);
                                                    it.putExtra("code", "0");
                                                    startActivity(it);
                                                    TwoButtonDialog.dismiss();
                                                    break;
                                                case R.id.btn_right:
                                                    TwoButtonDialog.dismiss();
                                                    break;
                                            }
                                        }
                                    });
                            TwoButtonDialog.show();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        loadingDialogWhole.dismiss();
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(SettingActivity.this, "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void setheadimg() {
        menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
        //  LinearLayout lin = (LinearLayout) findViewById(R.id.fm_main);
        menuWindow.showAtLocation(llMain, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        // WindowManager.LayoutParams params=this.getWindow().getAttributes();
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
                    PermissionGen.with(SettingActivity.this)
                            .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                            ).request();

                    break;
                case R.id.btn_pick_photo:
                    // 3、调用从图库选取图片方法
                    PermissionGen.needPermission(SettingActivity.this,
                            LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
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
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public void showDialog() {
        button = new OneButtonDialogWhite(SettingActivity.this, "为保证应用正常使用，需开启应用相机和存储权限！", "前往设置", new OnMyDialogClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Uri packageURI = Uri.parse("package:" + "com.beixiang.points");
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                startActivity(intent);
                button.dismiss();
            }

        });
        button.setCancelable(false);
        button.setCanceledOnTouchOutside(false);
        button.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        STS = MyCacheUtil.getshared(this).getString("STS", "");
        if (STS.equals("0")) {
            Log.e("", "jin .....");
            realNameTv.setText("已认证");
        } else if (STS.equals("1")) {
            realNameTv.setText("已提交待认证");
        } else if (STS.equals("2")) {
            realNameTv.setText("未认证");
        } else if (STS.equals("-1")) {
            realNameTv.setText("审核未通过");
        }
    }



}
