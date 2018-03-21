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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.sdk.android.oss.OSS;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
import td.com.xiaoheixiong.fragments.BaseFragment;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;
import td.com.xiaoheixiong.views.pickerList.OptionsPickerView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

@SuppressLint("NewApi")
public class MerchartBFragment extends BaseFragment {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.dptz_img)
    ImageView dptzImg;
    @Bind(R.id.yyzz_img)
    ImageView yyzzImg;

    @Bind(R.id.dnz_img)
    ImageView dnz_img;

    @Bind(R.id.syt_img)
    ImageView syt_img;


    @Bind(R.id.Merchants_jc_et)
    EditText MerchantsJcEt;
    @Bind(R.id.Enterprise_name_et)
    EditText EnterpriseNameEt;
    @Bind(R.id.Regist_number_et)
    EditText RegistNumberEt;
    @Bind(R.id.Organization_code_et)
    EditText OrganizationCodeEt;
    @Bind(R.id.provinces_tv)
    TextView provincesTv;
    @Bind(R.id.city_tv)
    TextView cityTv;
    @Bind(R.id.address_et)
    EditText addressEt;
    @Bind(R.id.Industry_categories_a_tv)
    TextView IndustryCategoriesATv;
    @Bind(R.id.Industry_categories_b_tv)
    TextView IndustryCategoriesBTv;
    @Bind(R.id.Industry_categories_c_tv)
    TextView IndustryCategoriesCTv;
    @Bind(R.id.go_btn)
    Button goBtn;

    @Bind(R.id.et_validity)
    EditText etValidity;
    @Bind(R.id.long_cb)
    CheckBox longCb;


    private View view;
    private SelectPicPopupWindow menuWindow;
    private LQRPhotoSelectFragmentUtils mLqrPhotoSelectUtils;
    public MerchantsGatheringActivity parentAct = null;
    private String gatepic = ""; //门头照
    private String busregimg = ""; //营业执照
    private String indoorpic = ""; //店内照
    private String shouyintaipic = ""; //收银台/前台照
    private int dptz_img = 0, yyzz_img = 1, dnz_image = 2, syt_image = 3, Code;
    private OneButtonDialogWhite button;

    ArrayList<HashMap<String, Object>> listData;
    ArrayList<String> provinceNameList = new ArrayList<String>();
    ArrayList<String> cityNameList = new ArrayList<String>();
    ArrayList<String> speciesNameList = new ArrayList<String>();
    // 行业分类集合1
    private ArrayList<String> speciesNameList1 = new ArrayList<String>();
    // 行业分类集合2
    private ArrayList<ArrayList<String>> speciesNameList2 = new ArrayList<ArrayList<String>>();
    // 行业分类集合3
    private ArrayList<ArrayList<ArrayList<String>>> speciesNameList3 = new ArrayList<ArrayList<ArrayList<String>>>();
    private ArrayList<ArrayList<ArrayList<Map<String, Object>>>> speciesNameList33 = new ArrayList<ArrayList<ArrayList<Map<String, Object>>>>();
    private ArrayList<String> speciesName = new ArrayList<String>();
    private ArrayList<Map<String, Object>> speciesList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> PrinceList;
    private ArrayList<Map<String, Object>> CityList;
    private ArrayList<HashMap<String, Object>> SpeciesList;
    private JSONObject mJsonObj;
    private OptionsPickerView<String> mOpv;
    private int Options1, Option2, tag1 = 0, tag2 = 0;
    private String industryid, cityId, branchId, MOBILE;
    private String licenseTerm = "0";

    public OSS oss;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_merchart_b, null);
        parentAct = (MerchantsGatheringActivity) getActivity();
        oss=((MerchantsGatheringActivity) getActivity()).oss;
        MOBILE = MyCacheUtil.getshared(getActivity()).getString("PHONENUMBER", "");
        ButterKnife.bind(this, view);
        initPic();
        provincesData();
        initview();
        if (speciesNameList3.size() <= 0) {
            getSpeciesData();
        }
        return view;

    }

    private void initview() {
        mOpv = new OptionsPickerView<String>(getActivity());

        longCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etValidity.setText("");
                    licenseTerm = "1";
                } else {
                    licenseTerm = "0";

                }
            }
        });
        etValidity.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    longCb.setChecked(false);
                    licenseTerm = "0";
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (parentAct.cardtype.equals("0")) {
            dnz_img.setVisibility(View.GONE);

        } else if (parentAct.cardtype.equals("1")) {
            dnz_img.setVisibility(View.VISIBLE);
        }


    }

    private void initPic() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectFragmentUtils(MerchartBFragment.this, new LQRPhotoSelectFragmentUtils.PhotoSelectListener() {
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
                                if (Code == dptz_img) {
                                    Glide.with(getActivity()).load(file + "").asBitmap().into(dptzImg);
                                 //   uploadPicture(file);
                                } else if (Code == yyzz_img) {
                                    Glide.with(getActivity()).load(file + "").asBitmap().into(yyzzImg);
                                 //   uploadPicture(file);
                                } else if (Code == dnz_image) {
                                    Glide.with(getActivity()).load(file + "").asBitmap().into(dnz_img);
                                 //   uploadPicture(file);
                                } else if (Code == syt_image) {
                                    Glide.with(getActivity()).load(file + "").asBitmap().into(syt_img);
                                 //   uploadPicture(file);
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

    @OnClick({R.id.back_img, R.id.dptz_img, R.id.yyzz_img, R.id.provinces_tv, R.id.city_tv,
            R.id.Industry_categories_a_tv, R.id.Industry_categories_b_tv, R.id.Industry_categories_c_tv, R.id.go_btn,
            R.id.dnz_img, R.id.syt_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                switchContent(parentAct.Bfragment, parentAct.Afragment, R.id.fragment, 0);
                break;
            case R.id.dptz_img:
                Log.e("dptz_img", "jin........");
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
                Code = dptz_img;
                setPic();

                break;
            case R.id.yyzz_img:
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
                Code = yyzz_img;
                setPic();
                break;

            case R.id.dnz_img:
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
                Code = dnz_image;
                setPic();
                break;

            case R.id.syt_img:
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
                Code = syt_image;
                setPic();
                break;
            case R.id.provinces_tv:
                if (provinceNameList == null || provinceNameList.size() == 0) {
                    provincesData();
                    tag1 = 1;

                } else {
                    // 设置标题
                    mOpv.setTitle("选择省份");

                    // 设置三级联动效果
                    // mOpv.setPicker(speciesNameList1, speciesNameList2,
                    // speciesNameList3, true);
                    mOpv.setPicker(provinceNameList, null, null, true);
                    // 设置是否循环滚动
                    mOpv.setCyclic(false, false, false);

                    // 设置默认选中的三级项目
                    mOpv.setSelectOptions(0, 0);

                    // 监听确定选择按钮
                    mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int option2, int options3) {
                            // 返回的分别是三个级别的选中位置
                            Options1 = options1;
                            String tx = provinceNameList.get(options1) + "";
                            provincesTv.setText(tx);
                            cityId = PrinceList.get(options1).get("areacode") + "";
                        }
                    });
                    mOpv.show();

                }

                break;
            case R.id.city_tv:
                if (provincesTv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "请先填写省份！", Toast.LENGTH_SHORT).show();
                    return;
                }

                CityData();

                break;
            case R.id.Industry_categories_a_tv://选择行业类别
                if (speciesNameList3.size() <= 0) {
                    getSpeciesData();
                    tag2 = 1;
                } else {
// 设置标题
                    mOpv.setTitle("选择行业类别");

                    // 设置三级联动效果
                    // mOpv.setPicker(speciesNameList1, speciesNameList2,
                    // speciesNameList3, true);
                    mOpv.setPicker(speciesNameList1, null, null, true);
                    // 设置是否循环滚动
                    mOpv.setCyclic(false, false, false);

                    // 设置默认选中的三级项目
                    mOpv.setSelectOptions(0, 0);

                    // 监听确定选择按钮
                    mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int option2, int options3) {
                            // 返回的分别是三个级别的选中位置
                            Options1 = options1;
                            String tx = speciesNameList1.get(options1) + "";

                            IndustryCategoriesATv.setText(tx);
                            IndustryCategoriesBTv.setText("");
                            IndustryCategoriesCTv.setText("");
                        }
                    });
                    mOpv.show();
                }

                break;
            case R.id.Industry_categories_b_tv:
                if (IndustryCategoriesATv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "请先填写第一个行业类别！", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 设置标题
                mOpv.setTitle("选择行业类别");

                // 设置三级联动效果
                // mOpv.setPicker(speciesNameList1, speciesNameList2,
                // speciesNameList3, true);
                mOpv.setPicker(speciesNameList2.get(Options1), null, null, true);
                // 设置是否循环滚动
                mOpv.setCyclic(false, false, false);

                // 设置默认选中的三级项目
                mOpv.setSelectOptions(0, 0);

                // 监听确定选择按钮
                mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        // 返回的分别是三个级别的选中位置
                        Option2 = options1;
                        String tx = speciesNameList2.get(Options1).get(options1) + "";
                        IndustryCategoriesBTv.setText(tx);

                    }
                });
                mOpv.show();

                break;
            case R.id.Industry_categories_c_tv:
                if (IndustryCategoriesATv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "请先填写第一个行业类别！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (IndustryCategoriesBTv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "请先填写第二个行业类别！", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 设置标题
                mOpv.setTitle("选择行业类别");

                // 设置三级联动效果
                // mOpv.setPicker(speciesNameList1, speciesNameList2,
                // speciesNameList3, true);
                mOpv.setPicker(speciesNameList3.get(Options1).get(Option2), null, null, true);
                // 设置是否循环滚动
                mOpv.setCyclic(false, false, false);

                // 设置默认选中的三级项目
                mOpv.setSelectOptions(0, 0);

                // 监听确定选择按钮
                mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        // 返回的分别是三个级别的选中位置
                        String tx = speciesNameList3.get(Options1).get(Option2).get(options1) + "";
                        IndustryCategoriesCTv.setText(tx);
                        industryid = speciesNameList33.get(Options1).get(Option2).get(options1).get("id") + "";
                        Log.e("industryid", industryid + "");
                    }
                });
                mOpv.show();
                break;
            case R.id.go_btn:

                if (gatepic.length() <= 0 || gatepic.equals("")) {
                    Toast.makeText(getContext(), "店铺门头照不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (shouyintaipic.length() <= 0 || shouyintaipic.equals("")) {
                    Toast.makeText(getContext(), "收银台照不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (busregimg.length() <= 0 || busregimg.equals("")) {
                    Toast.makeText(getContext(), "营业执照不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etValidity.getText().toString().equals("") && licenseTerm.equals("0")) {
                    Toast.makeText(getContext(), "请输入营业执照有效期", Toast.LENGTH_SHORT).show();
                    return;
                } else if (MerchantsJcEt.getText().length() <= 0) {
                    Toast.makeText(getContext(), "商户简称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (EnterpriseNameEt.getText().length() <= 0) {
                    Toast.makeText(getContext(), "企业名称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (RegistNumberEt.getText().length() <= 0) {
                    Toast.makeText(getContext(), "执照注册号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //不要组织机构代码
//                else if (OrganizationCodeEt.getText().length() <= 0) {
//                    Toast.makeText(getContext(), "组织机构代码不能为空！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                else if (provincesTv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "省份不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (cityTv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "城市不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (provincesTv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "省份不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (addressEt.getText().length() <= 0) {
                    Toast.makeText(getContext(), "详细地址不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (IndustryCategoriesATv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "行业类别不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (IndustryCategoriesBTv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "行业类别不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (IndustryCategoriesCTv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "行业类别不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (parentAct.cardtype.equals("1")) {//个人
                    if (indoorpic.length() <= 0 || indoorpic.equals("")) {
                        Toast.makeText(getContext(), "店内照不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                String enterpraiseLicenseTerm = "";
                if (licenseTerm.equals("0")) {
                    enterpraiseLicenseTerm = etValidity.getText() + "";// 有效期
                } else {
                    enterpraiseLicenseTerm = "20990909";
                }

                String entabb = MerchantsJcEt.getText() + "";//商户简称
                String Entname = EnterpriseNameEt.getText() + "";//企业名称
                String RegistNumber = RegistNumberEt.getText() + "";//注册号
                String orgcode = OrganizationCodeEt.getText() + "";//组织机构代码
                String province = provincesTv.getText() + "";
                String city = cityTv.getText() + "";
                String address = addressEt.getText() + "";

                parentAct.entAddress.setProvince(province);
                parentAct.entAddress.setCity(city);
                parentAct.entAddress.setDetails(address);//详细地址

                parentAct.enterpriseInf.setEntname(Entname);//企业名称
                parentAct.enterpriseInf.setEntabb(entabb);//商户简称
                parentAct.enterpriseInf.setIndustryid(industryid);//行业类型ID
                parentAct.enterpriseInf.setOrgcode(orgcode);
                parentAct.enterpriseInf.setBusregnum(RegistNumber);
                parentAct.enterpriseInf.setGatepic(gatepic);//门头照
                parentAct.enterpriseInf.setBusregimg(busregimg); //营业执照
                parentAct.enterpriseInf.setStorePic(indoorpic);//店内照
                parentAct.enterpriseInf.setCheckStandPic(shouyintaipic);//收银台照
                parentAct.enterpriseInf.setEnterpraiseLicenseTerm(enterpraiseLicenseTerm); //营业执照期限
                parentAct.enterpriseInf.setLicenseTerm(licenseTerm); //营业执照期限
                switchContent(parentAct.Bfragment, parentAct.Cfragment, R.id.fragment, 1);
                break;
        }
    }

    private void provincesData() {
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("parentCode", "1");
        PrinceList = new ArrayList<Map<String, Object>>();
        //  requestPostByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.Bank_address, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");
                        com.alibaba.fastjson.JSONObject jsonObj = new com.alibaba.fastjson.JSONObject().parseObject(result + "");
                        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                        if (StringUtils.isNotBlank(jsonObj.get("detail").toString())) {
                            list = JSON.parseObject(jsonObj.get("detail") + "", new TypeReference<List<Map<String, Object>>>() {
                            });
                            Log.e("list", list + "");
                            if (list != null) {
                                PrinceList.addAll(list);
                                Log.e("PrinceList", PrinceList.toString());
                                for (int i = 0; i < PrinceList.size(); i++) {
                                    provinceNameList.add(PrinceList.get(i).get("areaname") + "");
                                }
                                Log.e("provinceName", provinceNameList + "");
// 设置标题
                                if (tag1 == 1) {
                                    mOpv.setTitle("选择省份");

                                    // 设置三级联动效果
                                    // mOpv.setPicker(speciesNameList1, speciesNameList2,
                                    // speciesNameList3, true);
                                    mOpv.setPicker(provinceNameList, null, null, true);
                                    // 设置是否循环滚动
                                    mOpv.setCyclic(false, false, false);

                                    // 设置默认选中的三级项目
                                    mOpv.setSelectOptions(0, 0);

                                    // 监听确定选择按钮
                                    mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                                        @Override
                                        public void onOptionsSelect(int options1, int option2, int options3) {
                                            // 返回的分别是三个级别的选中位置
                                            Options1 = options1;
                                            String tx = provinceNameList.get(options1) + "";
                                            provincesTv.setText(tx);
                                            cityId = PrinceList.get(options1).get("areacode") + "";
                                        }
                                    });
                                    mOpv.show();
                                }

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

    private void CityData() {
        CityList = new ArrayList<Map<String, Object>>();
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("parentCode", cityId);
        //  requestPostByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.Bank_address, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");
                        com.alibaba.fastjson.JSONObject jsonObj = new com.alibaba.fastjson.JSONObject().parseObject(result + "");
                        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                        list = JSON.parseObject(jsonObj.get("detail") + "", new TypeReference<List<Map<String, Object>>>() {
                        });
                        Log.e("list", list + "");
                        Log.e("citylist", list.toString());
                        CityList.clear();
                        CityList.addAll(list);
                        cityNameList.clear();
                        for (int i = 0; i < CityList.size(); i++) {
                            cityNameList.add(CityList.get(i).get("areaname") + "");
                        }
                        Log.e("cityNameList", cityNameList + "");
                        // 设置标题
                        mOpv.setTitle("选择城市");

                        // 设置三级联动效果
                        // mOpv.setPicker(speciesNameList1, speciesNameList2,
                        // speciesNameList3, true);
                        mOpv.setPicker(cityNameList, null, null, true);
                        // 设置是否循环滚动
                        mOpv.setCyclic(false, false, false);

                        // 设置默认选中的三级项目
                        mOpv.setSelectOptions(0, 0);

                        // 监听确定选择按钮
                        mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int option2, int options3) {
                                // 返回的分别是三个级别的选中位置
                                Options1 = options1;
                                String tx = cityNameList.get(options1) + "";
                                cityTv.setText(tx);
                                //    cityId= PrinceList.get(options1).get("areacode")+"";
                            }
                        });
                        mOpv.show();

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getActivity(), "网络不给力！", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * 获取行业类别
     */
    private void getSpeciesData() {
        HashMap<String, Object> map = new HashMap<>();

        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.Industry_Categories, OkHttpClientManager.TYPE_GET, map, OkHttpClientManager.HOST_javaMpay,
                new OkHttpClientManager.ReqCallBack() {

                    @Override
                    public void onReqSuccess(Object result) {
                        // TODO Auto-generated method stub
                        speciesNameList1.clear();
                        speciesNameList2.clear();
                        Log.e("result", result + "");
                        // speciesNameList3.clear();
                        // 得到返回值的集合 String
                        SpeciesList = new ArrayList<HashMap<String, Object>>();
                        try {
                            mJsonObj = new JSONObject(result + "");
                            JSONArray jsonArray = mJsonObj.getJSONArray("list");
                            if (jsonArray != null) {
                                if (mJsonObj.getString("RSPCOD").equals("000000")) {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonP = jsonArray.getJSONObject(i);// 获取每个省的Json对象
                                        String species = jsonP.getString("name");
                                        Log.e("species", species.toString());
                                        ArrayList<String> options2Items_01 = new ArrayList<String>();
                                        ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
                                        ArrayList<ArrayList<Map<String, Object>>> options3Items = new ArrayList<ArrayList<Map<String, Object>>>();
                                        JSONArray jsonCs = jsonP.getJSONArray("list");
                                        for (int j = 0; j < jsonCs.length(); j++) {
                                            JSONObject jsonC = jsonCs.getJSONObject(j);// 获取每个市的Json对象
                                            String city = jsonC.getString("name");
                                            options2Items_01.add(city);// 添加市数据

                                            ArrayList<String> options3Items_01_02 = new ArrayList<String>();
                                            ArrayList<String> options3Items_01_01 = new ArrayList<String>();
                                            ArrayList<Map<String, Object>> options3Items02 = new ArrayList<Map<String, Object>>();

                                            JSONArray jsonAs = jsonC.getJSONArray("list");
                                            for (int k = 0; k < jsonAs.length(); k++) {
                                                HashMap<String, Object> maps = new HashMap<>();

                                                JSONObject jsonC3 = jsonAs.getJSONObject(k);
                                                options3Items_01_02.add(jsonAs.getString(k));// 添加区数据
                                                maps.put("id", jsonC3.getString("id"));
                                                maps.put("name", jsonC3.getString("name"));
                                                String eare = jsonC3.getString("name");
                                                options3Items_01_01.add(eare);
                                                options3Items02.add(maps);
                                            }
                                            options3Items_01.add(options3Items_01_01);
                                            options3Items.add(options3Items02);
                                        }
                                        speciesNameList1.add(species);// 添加省数据
                                        speciesNameList2.add(options2Items_01);
                                        speciesNameList3.add(options3Items_01);
                                        speciesNameList33.add(options3Items);
                                    }
                                    Log.e("speciesNameList1", speciesNameList1.toString());
                                    Log.e("speciesNameList2", speciesNameList2.toString());
                                    Log.e("speciesNameList3", speciesNameList3.toString());
                                    Log.e("speciesNameList33", speciesNameList33.toString());

// 设置标题
                                    if (tag2 == 1) {
                                        mOpv.setTitle("选择行业类别");

                                        // 设置三级联动效果
                                        // mOpv.setPicker(speciesNameList1, speciesNameList2,
                                        // speciesNameList3, true);
                                        mOpv.setPicker(speciesNameList1, null, null, true);
                                        // 设置是否循环滚动
                                        mOpv.setCyclic(false, false, false);

                                        // 设置默认选中的三级项目
                                        mOpv.setSelectOptions(0, 0);

                                        // 监听确定选择按钮
                                        mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                                            @Override
                                            public void onOptionsSelect(int options1, int option2, int options3) {
                                                // 返回的分别是三个级别的选中位置
                                                Options1 = options1;
                                                String tx = speciesNameList1.get(options1) + "";

                                                IndustryCategoriesATv.setText(tx);
                                                IndustryCategoriesBTv.setText("");
                                                IndustryCategoriesCTv.setText("");
                                            }
                                        });
                                        mOpv.show();
                                    }


                                } else {
                                    Toast.makeText(getContext(), mJsonObj.get("RSPMSG") + "",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getContext(), "网络不给力！",
                                        Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity(), "系统错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

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

                            com.alibaba.fastjson.JSONObject jsonObj = new com.alibaba.fastjson.JSONObject().parseObject(result + "");
                            if (jsonObj.get("RSPCOD").equals("000000")) {
                                Toast.makeText(getContext(), jsonObj.get("RSPMSG") + "",
                                        Toast.LENGTH_SHORT).show();

                                if (!jsonObj.get("PICURL").equals("")) {
                                    Log.e("PICURL", "PICURL" + jsonObj.get("PICURL"));

                                    if (Code == dptz_img) {
                                        gatepic = jsonObj.get("PICURL") + "";
                                    } else if (Code == yyzz_img) {
                                        busregimg = jsonObj.get("PICURL") + "";
                                    }
                                    if (Code == dnz_image) {
                                        indoorpic = jsonObj.get("PICURL") + "";
                                    } else if (Code == syt_image) {
                                        shouyintaipic = jsonObj.get("PICURL") + "";
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
                            Toast.makeText(getContext(),  "上传成功",
                                    Toast.LENGTH_SHORT).show();
                            if (Code == dptz_img) {
                                gatepic = MyConstant.ALI_PUBLIC_URL+key;
                            } else if (Code == yyzz_img) {
                                busregimg =  MyConstant.ALI_PUBLIC_URL+key;
                            }
                            if (Code == dnz_image) {
                                indoorpic =  MyConstant.ALI_PUBLIC_URL+key;
                            } else if (Code == syt_image) {
                                shouyintaipic =  MyConstant.ALI_PUBLIC_URL+key;
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




    public void setPic() {
        Log.e("menuWindow", "jin ......B");
        menuWindow = new SelectPicPopupWindow(getActivity(), itemsOnClick);
        //  LinearLayout lin = (LinearLayout) findViewById(R.id.fm_main);
        menuWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    PermissionGen.with(MerchartBFragment.this)
                            .addRequestCode(LQRPhotoSelectFragmentUtils.REQ_TAKE_PHOTO)
                            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                            ).request();

                    break;
                case R.id.btn_pick_photo:
                    // 3、调用从图库选取图片方法
                    PermissionGen.needPermission(MerchartBFragment.this,
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
        PermissionGen.onRequestPermissionsResult(MerchartBFragment.this, requestCode, permissions, grantResults);
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

    @OnClick(R.id.Industry_categories_c_tv)
    public void onViewClicked() {
    }
}
