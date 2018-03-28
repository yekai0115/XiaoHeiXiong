package td.com.xiaoheixiong.fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

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
import td.com.xiaoheixiong.Utils.GlideCircleTransform;
import td.com.xiaoheixiong.Utils.ImgSetUtil;
import td.com.xiaoheixiong.Utils.LQRPhotoSelectFragmentUtils;
import td.com.xiaoheixiong.Utils.LQRPhotoSelectUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.permissionManager.PermissionsCheckerUtil;
import td.com.xiaoheixiong.activity.AgentEarnsActivity;
import td.com.xiaoheixiong.activity.CollectionCodeActivity;
import td.com.xiaoheixiong.activity.IntegralWebViewActivity;
import td.com.xiaoheixiong.activity.MarketingActivity;
import td.com.xiaoheixiong.activity.MyAccountActivity;
import td.com.xiaoheixiong.activity.MyHeadLineActivity;
import td.com.xiaoheixiong.activity.MyMemberActivity;
import td.com.xiaoheixiong.activity.SettingActivity;
import td.com.xiaoheixiong.activity.selectMechatsActivity;
import td.com.xiaoheixiong.aliutil.MyOSSConfig;
import td.com.xiaoheixiong.aliutil.PutObjectSamples;
import td.com.xiaoheixiong.beans.MyConstant;
import td.com.xiaoheixiong.dialogs.HongbaoButtonDialog;
import td.com.xiaoheixiong.dialogs.MechantsButtonDialog;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.OneButtonDialogWhite;
import td.com.xiaoheixiong.dialogs.SelectPicPopupWindow;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class TabEFragment extends BaseFragment {

    @Bind(R.id.head_img)
    ImageView headImg;
    @Bind(R.id.daili_tv)
    TextView dailiTv;
    @Bind(R.id.mechat_register_tv)
    TextView mechatRegisterTv;
    @Bind(R.id.my_mechat_tv)
    TextView myMechatTv;
    @Bind(R.id.shouyi)
    TextView shouyi;
    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.dengji_tv)
    TextView dengjiTv;
    @Bind(R.id.set_img)
    ImageView setImg;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.pay_code_tv)
    TextView payCodeTv;
    @Bind(R.id.my_books_tv)
    TextView myBooksTv;
    @Bind(R.id.sh_shouyi_tv)
    TextView shShouyiTv;
    @Bind(R.id.dpgl_tv)
    TextView dpglTv;
    @Bind(R.id.my_mvp_tv)
    TextView myMvpTv;
    @Bind(R.id.fans_tv)
    TextView fansTv;
    @Bind(R.id.yh_money_tv)
    TextView yhMoneyTv;
    @Bind(R.id.yh_points_tv)
    TextView yhPointsTv;
    @Bind(R.id.yh_zuji_tv)
    TextView yhZujiTv;
    @Bind(R.id.yh_guanzhu_tv)
    TextView yhGuanzhuTv;
    @Bind(R.id.yh_share_tv)
    TextView yhShareTv;
    @Bind(R.id.yh_toutiao_tv)
    TextView yhToutiaoTv;
    @Bind(R.id.sh_yx_tv)
    TextView shYxTv;
    @Bind(R.id.my_shop_tv)
    TextView myShopTv;
    @Bind(R.id.sh_dpkj_tv)
    TextView shDpkjTv;
    @Bind(R.id.sh_dphb_tv)
    TextView shDphbTv;
    @Bind(R.id.yh_kq_tv)
    TextView yhKqTv;
    @Bind(R.id.sh_yxtg_tv)
    TextView shYxtgTv;
    @Bind(R.id.yunying_ll)
    LinearLayout yunyingLl;
    @Bind(R.id.shanghu_ll)
    LinearLayout shanghuLl;
    @Bind(R.id.zhifu_img)
    ImageView zhifuImg;
    @Bind(R.id.weixin_img)
    ImageView weixinImg;
    @Bind(R.id.shangjia_img)
    ImageView shangjiaImg;
    @Bind(R.id.daili_img)
    ImageView dailiImg;
    @Bind(R.id.view_ll)
    LinearLayout viewLl;
    @Bind(R.id.tradingManagement_tv)
    TextView tradingManagementTv;
    private RequestManager glideRequest;
    private TextView title_tv;
    private ImageView back_img, right_img;
    private RelativeLayout title_right_rl;
    private View view;
    public LayoutInflater mInflater;
    private HongbaoButtonDialog hongbaoDialog1;
    private MechantsButtonDialog mDialog;
    private String ISAREAAGENT, ACTNAM, ISMER, isAgent, agentLevel, headImgUrl, MOBILE, mercId;
    private OneButtonDialogWhite onebutton;
    private LQRPhotoSelectFragmentUtils mLqrPhotoSelectUtils;
    private SelectPicPopupWindow menuWindow;
    private OneButtonDialogWhite button;

    public OSS oss;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_e, container, false);
        // Glide.with(getActivity()).load(R.drawable.bg_girl).into(headImg);
        ButterKnife.bind(this, view);
        ISAREAAGENT = MyCacheUtil.getshared(getActivity()).getString("ISAREAAGENT", "");
        ACTNAM = MyCacheUtil.getshared(getActivity()).getString("ACTNAM", "");
        ISMER = MyCacheUtil.getshared(getActivity()).getString("ISMER", "");
        isAgent = MyCacheUtil.getshared(getActivity()).getString("isAgent", "");
        agentLevel = MyCacheUtil.getshared(getActivity()).getString("agentLevel", "");
        headImgUrl = MyCacheUtil.getshared(getActivity()).getString("headImgUrl", "");
        MOBILE = MyCacheUtil.getshared(getActivity()).getString("PHONENUMBER", "");
        mercId = MyCacheUtil.getshared(getActivity()).getString("MERCNUM", "");
        mInflater = inflater;
        initview();
        initPic();
        Log.e("onCreate", "onCreate + ");
        oss = new OSSClient(getActivity().getApplicationContext(), MyConstant.ALI_ENDPOINT, MyOSSConfig.getProvider(), MyOSSConfig.getOSSConfig());
        return view;
    }

    private void initPic() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectFragmentUtils(TabEFragment.this, new LQRPhotoSelectFragmentUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
                //    Glide.with(MainActivity.this).load(outputUri).into(mIvPic);
                Log.e("outputFile", outputFile + "");
                //   String str = outputUri.getPath();
                //    upload(new File(str));
                Luban.get(getActivity())
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
//                               uploadPicture(file);
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
        // TODO Auto-generated method stub
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        back_img = (ImageView) view.findViewById(R.id.back_img);
        back_img.setVisibility(View.GONE);
        title_right_rl = (RelativeLayout) view.findViewById(R.id.title_right_rl);
        right_img = (ImageView) view.findViewById(R.id.right_img);
        title_tv.setText("我的");
        right_img.setImageResource(R.mipmap.ic_launcher);
        // title_right_rl.setVisibility(View.INVISIBLE);
        title_right_rl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //  Intent it = new Intent(getActivity(), settingActivity.class);
                //  startActivity(it);
            }
        });
        glideRequest = Glide.with(getActivity());
        Log.e("headImgUrl", headImgUrl + "");
        if (headImgUrl.equals("null") || headImgUrl.equals("")) {
            glideRequest.load(R.mipmap.app_icon).transform(new GlideCircleTransform(getActivity())).into(headImg);

        } else {
            glideRequest.load(headImgUrl).transform(new GlideCircleTransform(getActivity())).into(headImg);
        }
        Log.e("ACTNAM", ACTNAM + "");
        if (ACTNAM.equals("null")) {
            nameTv.setText("未实名");
        } else {
            nameTv.setText(ACTNAM);

        }
        if (ISMER.equals("true")) {//是否是商家 true 是  false不是
            if (isAgent.equals("true")) {
                yunyingLl.setVisibility(View.VISIBLE);
                shanghuLl.setVisibility(View.VISIBLE);
                if (agentLevel.equals("1")) {
                    dengjiTv.setText("服务商");
                    zhifuImg.setVisibility(View.VISIBLE);
                    weixinImg.setVisibility(View.VISIBLE);
                    shangjiaImg.setVisibility(View.VISIBLE);
                    dailiImg.setVisibility(View.VISIBLE);
                } else if (agentLevel.equals("2")) {
                    dengjiTv.setText("运营商");
                    zhifuImg.setVisibility(View.VISIBLE);
                    weixinImg.setVisibility(View.VISIBLE);
                    shangjiaImg.setVisibility(View.VISIBLE);
                    dailiImg.setVisibility(View.GONE);
                } else if (agentLevel.equals("3")) {
                    dengjiTv.setText("子公司");
                    zhifuImg.setVisibility(View.VISIBLE);
                    weixinImg.setVisibility(View.VISIBLE);
                    shangjiaImg.setVisibility(View.VISIBLE);
                    dailiImg.setVisibility(View.VISIBLE);
                }

            } else {
                dengjiTv.setText("商家");
                zhifuImg.setVisibility(View.VISIBLE);
                weixinImg.setVisibility(View.VISIBLE);
                shangjiaImg.setVisibility(View.VISIBLE);
                dailiImg.setVisibility(View.GONE);

                yunyingLl.setVisibility(View.GONE);
                shanghuLl.setVisibility(View.VISIBLE);
            }

        } else {
            dengjiTv.setText("用户");
            zhifuImg.setVisibility(View.GONE);
            weixinImg.setVisibility(View.GONE);
            shangjiaImg.setVisibility(View.GONE);
            dailiImg.setVisibility(View.GONE);
            yunyingLl.setVisibility(View.GONE);
            shanghuLl.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("TabEFragment", "onResume");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("hidden", hidden + "");
        if (!hidden) {

        }
    }


    @OnClick({R.id.daili_tv, R.id.mechat_register_tv, R.id.my_mechat_tv, R.id.shouyi, R.id.set_img,
            R.id.right_img, R.id.pay_code_tv, R.id.my_books_tv, R.id.sh_shouyi_tv, R.id.dpgl_tv, R.id.my_mvp_tv,
            R.id.fans_tv, R.id.yh_money_tv, R.id.yh_points_tv, R.id.yh_zuji_tv, R.id.yh_guanzhu_tv, R.id.yh_share_tv,
            R.id.yh_toutiao_tv, R.id.sh_yx_tv, R.id.my_shop_tv, R.id.sh_dpkj_tv,
            R.id.sh_dphb_tv, R.id.yh_kq_tv, R.id.sh_yxtg_tv, R.id.head_img,R.id.tradingManagement_tv,R.id.shouyi33})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.shouyi33://代理商收益
                intent.setClass(getActivity(), AgentEarnsActivity.class);
                startActivity(intent);
                break;
            case R.id.daili_tv:
                //  Intent it = new Intent(getActivity(), IntegralWebViewActivity.class);
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_agency);
                startActivity(intent);
                break;
            case R.id.mechat_register_tv:
            /*    Intent it = new Intent(getActivity(), SelectCollectionActivity.class);
                startActivity(it);*/
                mDialog = new MechantsButtonDialog(getActivity(), "", new OnMyDialogClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_left:
                                Intent it = new Intent(getActivity(), selectMechatsActivity.class);
                                it.putExtra("Mine", "0");
                                startActivity(it);
                                mDialog.dismiss();

                                break;
                            case R.id.btn_right:
                                Intent it1 = new Intent(getActivity(), selectMechatsActivity.class);
                                it1.putExtra("Mine", "1");
                                startActivity(it1);
                                mDialog.dismiss();

                                break;
                            case R.id.out_img:
                                mDialog.dismiss();
                                break;
                        }
                    }
                });
                // mDialog.setCancelable(false);
                //  mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();
                break;
            case R.id.my_mechat_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_Merchat);
                startActivity(intent);

                break;
            case R.id.shouyi:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_earnings);
                startActivity(intent);

                break;
            case R.id.set_img:
                Intent its1 = new Intent(getActivity(), SettingActivity.class);
                startActivity(its1);

                break;
            case R.id.right_img:

                break;
            case R.id.pay_code_tv:
                intent.setClass(getActivity(), CollectionCodeActivity.class);
                intent.putExtra("type", "0");
                startActivity(intent);

                //  intent.setClass(getActivity(), IntegralWebViewActivity.class);
                //  intent.putExtra("url", HttpUrls.XHX_PayCode);
                // startActivity(intent);
                break;
            case R.id.my_books_tv://我的账本
//                intent.setClass(getActivity(), IntegralWebViewActivity.class);
//                intent.putExtra("url", HttpUrls.XHX_Mybooks);
//                startActivity(intent);
                intent.setClass(getActivity(), MyAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.sh_shouyi_tv:
                onebutton = new OneButtonDialogWhite(getActivity(), "此功能即将开通！", "确定", new OnMyDialogClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_left:
                                onebutton.dismiss();
                                break;
                        }

                    }
                });
                onebutton.show();
                break;
            case R.id.dpgl_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_storeManagement);
                startActivity(intent);
                break;
            case R.id.my_mvp_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_mvp);
                startActivity(intent);

                break;
            case R.id.fans_tv://流量管理
//                intent.setClass(getActivity(), IntegralWebViewActivity.class);
//                intent.putExtra("url", HttpUrls.XHX_fans);
//                startActivity(intent);

                intent.setClass(getActivity(), MyMemberActivity.class);
                startActivity(intent);
                break;
            case R.id.yh_money_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_money);
                startActivity(intent);

                break;
            case R.id.yh_points_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_points);
                startActivity(intent);

                break;
            case R.id.yh_zuji_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_footmark);
                startActivity(intent);

                break;
            case R.id.yh_guanzhu_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_guanzhu);
                startActivity(intent);

                break;
            case R.id.yh_share_tv:
                intent.setClass(getActivity(), CollectionCodeActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                //   intent.setClass(getActivity(), IntegralWebViewActivity.class);
                //  intent.putExtra("url", HttpUrls.XHX_Dongtai + "/" + ACTNAM);
                //   startActivity(intent);

              /*  onebutton = new OneButtonDialogWhite(getActivity(), "此功能即将开通！", "确定", new OnMyDialogClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_left:

                                onebutton.dismiss();
                                break;
                        }

                    }
                });
                onebutton.show();*/
                break;
            case R.id.yh_toutiao_tv:
//                intent.setClass(getActivity(), IntegralWebViewActivity.class);
//                intent.putExtra("url", HttpUrls.XHX_Dongtai);
//                startActivity(intent);


                intent.setClass(getActivity(), MyHeadLineActivity.class);
                startActivity(intent);
                break;
            case R.id.sh_yx_tv://营销
//                intent.setClass(getActivity(), IntegralWebViewActivity.class);
//                intent.putExtra("url", HttpUrls.XHX_marketing);
//                startActivity(intent);

                intent.setClass(getActivity(), MarketingActivity.class);
                startActivity(intent);
                break;
            case R.id.my_shop_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_MyShop);
                startActivity(intent);

                break;
            case R.id.sh_dpkj_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_shopCard);
                startActivity(intent);

               /* onebutton = new OneButtonDialogWhite(getActivity(), "此功能即将开通！", "确定", new OnMyDialogClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_left:

                                onebutton.dismiss();
                                break;
                        }

                    }
                });
                onebutton.show();*/
                break;
            case R.id.sh_dphb_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_redEnvelope);
                startActivity(intent);
                break;
            case R.id.yh_kq_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_cardTicket);
                startActivity(intent);

                break;
            case R.id.sh_yxtg_tv:
                onebutton = new OneButtonDialogWhite(getActivity(), "此功能即将开通！", "确定", new OnMyDialogClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_left:

                                onebutton.dismiss();
                                break;
                        }

                    }
                });
                onebutton.show();
                break;
            case R.id.head_img:
                if (PermissionsCheckerUtil.lacksPermissions(getActivity(), Manifest.permission.CAMERA) || PermissionsCheckerUtil.lacksPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    button = new OneButtonDialogWhite(getActivity(), "为保证应用正常使用，需开启应用相机和存储权限！", "前往设置", new OnMyDialogClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Uri packageURI = Uri.parse("package:" + "td.com.xiaoheixiong");
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
                setPic();
                break;

            case R.id.tradingManagement_tv:
                intent.setClass(getActivity(), IntegralWebViewActivity.class);
                intent.putExtra("url", HttpUrls.XHX_tradingManagement);
                startActivity(intent);
                break;

        }
    }

    public void setPic() {
        menuWindow = new SelectPicPopupWindow(getActivity(), itemsOnClick);
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
                    PermissionGen.with(TabEFragment.this)
                            .addRequestCode(LQRPhotoSelectFragmentUtils.REQ_TAKE_PHOTO)
                            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                            ).request();

                    break;
                case R.id.btn_pick_photo:
                    // 3、调用从图库选取图片方法
                    PermissionGen.needPermission(TabEFragment.this,
                            LQRPhotoSelectFragmentUtils.REQ_SELECT_PHOTO,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    );
                    //   gallery();
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
        OkHttpClientManager.getInstance(getActivity()).upload(getActivity(), HttpUrls.upload_Pic, map, mapfile, 0,
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
                                Toast.makeText(getActivity(), jsonObj.get("RSPMSG") + "",
                                        Toast.LENGTH_SHORT).show();

                                if (!jsonObj.get("PICURL").equals("")) {
                                    Log.e("PICURL", "PICURL" + jsonObj.get("PICURL"));
                                    String headimgurl = jsonObj.get("PICURL") + "";
                                    ChangeImg(headimgurl);

                                } else {
                                    Toast.makeText(getActivity(), "上传失败，请重新上传！", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getActivity(), "上传失败，请重新上传！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        // TODO Auto-generated method stub
                        loadingDialogWhole.dismiss();
                        Toast.makeText(getActivity(), "上传失败，请重新上传！", Toast.LENGTH_SHORT).show();
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialogWhole.dismiss();
                            String headimgurl = MyConstant.ALI_PUBLIC_URL+key;
                            ChangeImg(headimgurl);
                        }
                    });

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialogWhole.dismiss();
                            Toast.makeText(getContext(), "上传失败，请重新上传！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }





    private void ChangeImg(final String picurl) {
        showLoadingDialog("...");
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        HashMap<String, Object> maps = new HashMap<>();
        //  "mercNum":"123456",  //商户号
        //           "headUrl":""            //用户头像的路径
        maps.put("mercNum", mercId);
        maps.put("headUrl", picurl);

        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.XHX_change_Headimg, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        loadingDialogWhole.dismiss();
                        Log.e("result", result + "");
                        Map<String, Object> map = new HashMap<String, Object>();

                        JSONObject oJSON = JSON.parseObject(result + "");
                        if (oJSON.get("RSPCOD").equals("000000")) {
                            Toast.makeText(getContext(), "更换头像成功！", Toast.LENGTH_SHORT).show();
                            glideRequest = Glide.with(getActivity());
                            glideRequest.load(picurl).transform(new GlideCircleTransform(getActivity())).into(headImg);

                        } else {
                            Toast.makeText(getContext(), "更换头像失败！", Toast.LENGTH_SHORT).show();
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



}
