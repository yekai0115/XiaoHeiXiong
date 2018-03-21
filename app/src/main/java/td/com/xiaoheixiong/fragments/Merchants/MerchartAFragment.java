package td.com.xiaoheixiong.fragments.Merchants;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.OSS;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
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
import td.com.xiaoheixiong.Utils.permissionManager.PermissionsCheckerUtil;
import td.com.xiaoheixiong.activity.MerchantsGatheringActivity;
import td.com.xiaoheixiong.aliutil.PutObjectSamples;
import td.com.xiaoheixiong.beans.MyConstant;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;
import td.com.xiaoheixiong.dialogs.OneButtonDialogWhite;
import td.com.xiaoheixiong.dialogs.SelectPicPopupWindow;
import td.com.xiaoheixiong.dialogs.TwoButtonDialogTitleWhite;
import td.com.xiaoheixiong.fragments.BaseFragment;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.pickerList.OptionsPickerView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

@SuppressLint("NewApi")
public class MerchartAFragment extends BaseFragment {
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.phone_apply_et)
    EditText phoneApplyEt;
    @Bind(R.id.name_apply_et)
    EditText nameApplyEt;
    @Bind(R.id.responsibility_name_et)
    EditText responsibilityNameEt;

    @Bind(R.id.tv_idcard_type)
    TextView tv_idcard_type;


    @Bind(R.id.Idcard_number_et)
    EditText IdcardNumberEt;
    @Bind(R.id.Register_phone_et)
    EditText RegisterPhoneEt;
    @Bind(R.id.eMail_et)
    EditText eMailEt;
    @Bind(R.id.sfzm_img)
    ImageView sfzmImg;
    @Bind(R.id.sffm_img)
    ImageView sffmImg;
    @Bind(R.id.sfsc_img)
    ImageView sfscImg;
    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.go_btn)
    Button goBtn;
    @Bind(R.id.myMsg_ll)
    LinearLayout myMsgLl;

    public MerchantsGatheringActivity parentAct = null;
    @Bind(R.id.et_validity)
    EditText etValidity;
    @Bind(R.id.long_cb)
    CheckBox longCb;
    private SelectPicPopupWindow menuWindow;
    private String idcardone = "", idcardtwo = "", handidcardpic = "123", MOBILE, term = "0";
    private OneButtonDialogWhite button;
    private View view;
    private LQRPhotoSelectFragmentUtils mLqrPhotoSelectUtils;
    private int sfzm_img = 0, sffm_img = 1, sfsc_img = 2, Code, num;
    private String[] pic;
    private TwoButtonDialogTitleWhite twoButton;
    private OptionsPickerView<String> mOpv;
    private ArrayList<String> idcardTypeList = new ArrayList<String>();
    private String idcardType;
    public OSS oss;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_merchart_a, null);
        parentAct = (MerchantsGatheringActivity) getActivity();
        oss=((MerchantsGatheringActivity) getActivity()).oss;
        MOBILE = MyCacheUtil.getshared(getActivity()).getString("PHONENUMBER", "");
        ButterKnife.bind(this, view);
        initview();
        initPic();
        return view;
    }

    private void initPic() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectFragmentUtils(MerchartAFragment.this, new LQRPhotoSelectFragmentUtils.PhotoSelectListener() {
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
                                String imgKey = ImgSetUtil.getImgKeyString();
                                if (Code == sfzm_img) {
                                    Glide.with(getActivity()).load(file + "").asBitmap().into(sfzmImg);
                               //     uploadPicture(file);
                                } else if (Code == sffm_img) {
                                    Glide.with(getActivity()).load(file + "").asBitmap().into(sffmImg);
                                //    uploadPicture(file);
                                } else if (Code == sfsc_img) {
                                    Glide.with(getActivity()).load(file + "").asBitmap().into(sfscImg);
                                //    uploadPicture(file);
                                }
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
        if (parentAct.Mine.equals("0")) {
            myMsgLl.setVisibility(View.VISIBLE);

        } else if (parentAct.Mine.equals("1")) {
            myMsgLl.setVisibility(View.GONE);
        }
        pic = new String[5];

        longCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etValidity.setText("");
                    term = "1";
                    Log.e("term1", term);
                } else {
                    term = "0";
                    Log.e("term0", term);
                }
            }
        });
        etValidity.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    longCb.setChecked(false);
                    term = "0";
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mOpv = new OptionsPickerView<String>(getActivity());
        idcardTypeList.add("中国居民身份证");
        idcardTypeList.add("香港居民身份证");
        idcardTypeList.add("澳门居民身份证");
        idcardTypeList.add("台湾居民身份证");
    }

    @OnClick({R.id.back_img, R.id.go_btn, R.id.sfzm_img, R.id.sffm_img, R.id.sfsc_img, R.id.tv_idcard_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:

                twoButton = new TwoButtonDialogTitleWhite(getActivity(), "提示", "有未保存的资料，是否确认返回？", "确定", "取消", new OnMyDialogClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_left:
                                getActivity().finish();
                                twoButton.dismiss();
                                break;
                            case R.id.btn_right:
                                twoButton.dismiss();
                                break;
                        }
                    }
                });
                twoButton.setCancelable(false);
                twoButton.setCanceledOnTouchOutside(false);
                twoButton.show();

                break;
            case R.id.go_btn:
                if (parentAct.Mine.equals("0")) {
                    if (phoneApplyEt.getText().length() <= 0) {
                        Toast.makeText(getContext(), "申请人账号不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (nameApplyEt.getText().length() <= 0) {
                        Toast.makeText(getContext(), "申请人姓名不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (responsibilityNameEt.getText().length() <= 0 || responsibilityNameEt.getText().equals("")) {
                    Toast.makeText(getContext(), "负责人姓名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (IdcardNumberEt.getText().length() <= 0) {
                    Toast.makeText(getContext(), "身份证号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etValidity.getText().toString().equals("") && term.toString().equals("0")) {
                    Toast.makeText(getContext(), "请输入您本人的身份证有效期", Toast.LENGTH_SHORT).show();
                    return;
                } else if (RegisterPhoneEt.getText().length() <= 0) {
                    Toast.makeText(getContext(), "注册手机号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (eMailEt.getText().length() <= 0) {
                    Toast.makeText(getContext(), "邮箱不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (idcardone.length() <= 0 || idcardone.equals("")) {
                    Toast.makeText(getContext(), "身份证正面照不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (idcardtwo.length() <= 0 || idcardtwo.equals("")) {
                    Toast.makeText(getContext(), "身份证反面照不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
//                else if (handidcardpic.length() <= 0 || handidcardpic.equals("")) {
//                    Toast.makeText(getContext(), "手持身份证照不能为空！", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                // 法人信息
                String fname = responsibilityNameEt.getText() + "";//姓名
                String idcard = IdcardNumberEt.getText() + "";// 证件号
                String Rphone = RegisterPhoneEt.getText() + "";// 注册手机号
                String email = eMailEt.getText() + "";// 邮箱
                String validterm = "";
                if (term.equals("0")) {
                    validterm = etValidity.getText() + "";// 有效期
                } else {
                    validterm = "20990909";
                }

                parentAct.enterpriseInf.setName(fname);
                parentAct.enterpriseInf.setIdcard(idcard);
                parentAct.phone = Rphone;
                parentAct.qrmerinf.setEmail(email);
                if(idcardType==null||idcardType.length()==0){
                    idcardType=idcardTypeList.get(0);
                }
                parentAct.enterpriseInf.setIdcardType(idcardType);//证件类型
                parentAct.enterpriseInf.setIdcardone(idcardone);//身份证正面
                parentAct.enterpriseInf.setIdcardtwo(idcardtwo);//身份证反面
                parentAct.enterpriseInf.setHandidcardpic(handidcardpic);//手持身份证
                parentAct.enterpriseInf.setValidterm(validterm);
                switchContent(parentAct.Afragment, parentAct.Bfragment, R.id.fragment, 1);
                break;
            case R.id.sfzm_img:
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
                ;
                Code = sfzm_img;
                setPic();

                break;
            case R.id.sffm_img:
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
                ;
                Code = sffm_img;
                setPic();
                break;
            case R.id.sfsc_img:
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
                ;
                Code = sfsc_img;
                setPic();
                break;
            case R.id.tv_idcard_type:

                // 设置标题
                mOpv.setTitle("选择证件类型");

                // 设置三级联动效果
                // mOpv.setPicker(speciesNameList1, speciesNameList2,
                // speciesNameList3, true);
                mOpv.setPicker(idcardTypeList, null, null, true);
                // 设置是否循环滚动
                mOpv.setCyclic(false, false, false);

                // 设置默认选中的三级项目
                mOpv.setSelectOptions(0, 0);

                // 监听确定选择按钮
                mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        idcardType = idcardTypeList.get(options1) + "";
                        tv_idcard_type.setText(idcardType);
                    }
                });
                mOpv.show();
                break;
        }
    }

    public void setPic() {
        Log.e("menuWindow", "jin ......A");

        menuWindow = new SelectPicPopupWindow(getActivity(), itemsOnClick);
        //  LinearLayout lin = (LinearLayout) findViewById(R.id.fm_main);
        menuWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    PermissionGen.with(MerchartAFragment.this)
                            .addRequestCode(LQRPhotoSelectFragmentUtils.REQ_TAKE_PHOTO)
                            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                            ).request();

                    break;
                case R.id.btn_pick_photo:
                    // 3、调用从图库选取图片方法
                    PermissionGen.needPermission(MerchartAFragment.this,
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

    @SuppressWarnings("unchecked")
    public void uploadPicture(File file) {
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
                                Toast.makeText(getContext(), jsonObj.get("RSPMSG") + "",
                                        Toast.LENGTH_SHORT).show();

                                if (!jsonObj.get("PICURL").equals("")) {
                                    Log.e("PICURL", "PICURL" + jsonObj.get("PICURL"));

                                    if (Code == sfzm_img) {
                                        idcardone = jsonObj.get("PICURL") + "";
                                    } else if (Code == sffm_img) {
                                        idcardtwo = jsonObj.get("PICURL") + "";
                                    } else if (Code == sfsc_img) {
                                        handidcardpic = jsonObj.get("PICURL") + "";
                                    }

                                } else {
                                    Toast.makeText(getContext(), "上传失败，请重新上传！", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getContext(), "上传失败，请重新上传！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        // TODO Auto-generated method stub
                        loadingDialogWhole.dismiss();
                        Toast.makeText(getContext(), "上传失败，请重新上传！", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getContext(),  "上传成功", Toast.LENGTH_SHORT).show();
                            if (Code == sfzm_img) {
                                idcardone = MyConstant.ALI_PUBLIC_URL+key;
                            } else if (Code == sffm_img) {
                                idcardtwo = MyConstant.ALI_PUBLIC_URL+key;
                            } else if (Code == sfsc_img) {
                                handidcardpic =  MyConstant.ALI_PUBLIC_URL+key;
                            }
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
        PermissionGen.onRequestPermissionsResult(MerchartAFragment.this, requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.long_cb)
    public void onViewClicked() {
    }
}
