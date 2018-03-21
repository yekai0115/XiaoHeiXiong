package td.com.xiaoheixiong.fragments.Merchants;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.sdk.android.oss.OSS;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
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
import td.com.xiaoheixiong.activity.FuzzySearchActivity;
import td.com.xiaoheixiong.activity.IntegralWebViewActivity;
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
public class MerchartCFragment extends BaseFragment implements OnClickListener {

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.khzm_img)
    ImageView khzmImg;
    @Bind(R.id.kzm_img)
    ImageView kzmImg;
    @Bind(R.id.kbm_img)
    ImageView kbmImg;
    @Bind(R.id.bankCard_ll)
    LinearLayout bankCard_ll;
    @Bind(R.id.bank_gs_tv)
    TextView bankGsTv;
    @Bind(R.id.bank_number_et)
    EditText bankNumberEt;
    @Bind(R.id.open_bank_tv)
    TextView openBankTv;
    @Bind(R.id.provinces_tv)
    TextView provincesTv;
    @Bind(R.id.city_tv)
    TextView cityTv;
    @Bind(R.id.branch_tv)
    TextView branchTv;
    @Bind(R.id.phone_et)
    EditText phoneEt;
    @Bind(R.id.go_btn)
    Button goBtn;
    @Bind(R.id.khh_tv)
    TextView khhTv;
    @Bind(R.id.khszd_tv)
    TextView khszdTv;
    @Bind(R.id.khzh_tv)
    TextView khzhTv;
    @Bind(R.id.num_tv)
    TextView numTv;
    @Bind(R.id.read_cb)
    CheckBox readCb;
    @Bind(R.id.bank_name_tv)
    TextView bankNameTv;
    @Bind(R.id.bank_name_et)
    EditText bankNameEt;
    @Bind(R.id.read_tv)
    TextView readTv;

    private OptionsPickerView<String> mOpv;
    public MerchantsGatheringActivity parentAct = null;
    private String cashCard_Z = "", cashCard_B = "", permitPic = "", cardtype, princeid, banktotalid, num = "0", MOBILE;
    ArrayList<String> provinceNameList = new ArrayList<String>();
    ArrayList<String> cityNameList = new ArrayList<String>();
    ArrayList<String> speciesNameList = new ArrayList<String>();
    private List<Map<String, Object>> PrinceList;
    private ArrayList<Map<String, Object>> CityList;
    private int Options1, Option2;
    private int khzm_img = 0, kzm_img = 1, kbm_img = 2, Code;
    private OneButtonDialogWhite button;
    private SelectPicPopupWindow menuWindow;
    private LQRPhotoSelectFragmentUtils mLqrPhotoSelectUtils;
    private View view;
    private ArrayList<String> bankNameList = new ArrayList<String>();
    private ArrayList<Map> BankList;
    private int Open_Bank = 0, ProvinceId = 1, cityId = 2, branchId = 3;
    private HashMap<String, Object> maps = new HashMap<>();


    public OSS oss;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_merchart_c, null);
        ButterKnife.bind(this, view);
        parentAct = (MerchantsGatheringActivity) getActivity();
        oss=((MerchantsGatheringActivity) getActivity()).oss;
        MOBILE = MyCacheUtil.getshared(getActivity()).getString("PHONENUMBER", "");
        cardtype = parentAct.cardtype;
        initview();
        initPic();
      /*  if (bankNameList == null || bankNameList.size() == 0) {
            getbankData();
        }*/

        return view;
    }

    private void initview() {
        PrinceList = new ArrayList<Map<String, Object>>();

// 创建选项选择器对象
        mOpv = new OptionsPickerView<String>(getActivity());
        if (cardtype.equals("0")) {//企业
            khzmImg.setVisibility(View.VISIBLE);
            ;// 对公开户许可证的数字账号
            bankCard_ll.setVisibility(View.GONE);
            bankGsTv.setText("对公银行账号：");
        } else if (cardtype.equals("1")) {//个体
            khzmImg.setVisibility(View.GONE); // 对私开户许可证的数字账号
            bankCard_ll.setVisibility(View.VISIBLE);
            bankGsTv.setText("对私银行账号：");
            Drawable dra = getResources().getDrawable(R.drawable.xing_icon);
            dra.setBounds(0, 0, 25, 25);
            bankNameTv.setCompoundDrawables(dra, null, null, null);
            bankGsTv.setCompoundDrawables(dra, null, null, null);
            khhTv.setCompoundDrawables(dra, null, null, null);
            khszdTv.setCompoundDrawables(dra, null, null, null);
            khzhTv.setCompoundDrawables(dra, null, null, null);

        }
        readCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("read_cb", isChecked + "");
                if (isChecked) {
                    num = "1";
                    Log.e("num", num);
                } else {
                    num = "0";
                    Log.e("num", num);
                }
            }
        });
    }

    private void initPic() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectFragmentUtils(MerchartCFragment.this, new LQRPhotoSelectFragmentUtils.PhotoSelectListener() {
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
                                if (Code == khzm_img) {
                                    Glide.with(getActivity()).load(file + "").asBitmap().into(khzmImg);
                                //    uploadPicture(file);
                                } else if (Code == kzm_img) {
                                    Glide.with(getActivity()).load(file + "").asBitmap().into(kzmImg);
                               //     uploadPicture(file);
                                } else if (Code == kbm_img) {
                                    Glide.with(getActivity()).load(file + "").asBitmap().into(kbmImg);
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


    @OnClick({R.id.back_img, R.id.khzm_img, R.id.kzm_img, R.id.kbm_img, R.id.open_bank_tv, R.id.provinces_tv, R.id.city_tv, R.id.branch_tv, R.id.go_btn,R.id.read_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                switchContent(parentAct.Cfragment, parentAct.Bfragment, R.id.fragment, 0);
                break;
            case R.id.read_tv:
                Intent it = new Intent(getActivity(), IntegralWebViewActivity.class);
                it.putExtra("url", HttpUrls.XHX_privacyPolicy);
                startActivity(it);

                break;

            case R.id.khzm_img:
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
                Code = khzm_img;
                setPic();

                break;
            case R.id.kzm_img:
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
                Code = kzm_img;
                setPic();
                break;
            case R.id.kbm_img:
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
                Code = kbm_img;
                setPic();
                break;

            case R.id.open_bank_tv:
                if (bankNameList == null || bankNameList.size() == 0) {
                    getbankData();
                } else {
                    selectPicker(bankNameList, "选择开户行", Open_Bank);

                }

                break;

            case R.id.provinces_tv:
                if (provinceNameList == null || provinceNameList.size() == 0) {
                    provincesData();

                } else {
                    Log.e("jinzheli ..", "44444444");
                    selectPicker(provinceNameList, "选择省份", ProvinceId);

                }
                break;
            case R.id.city_tv:
                if (provincesTv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "请先填写省份！", Toast.LENGTH_SHORT).show();
                    return;
                }
                CityData();

                break;
            case R.id.branch_tv://选择开户支行
                if (provincesTv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "请先填写省份！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (cityTv.getText().length() <= 0) {
                    Toast.makeText(getContext(), "请先填写市区！", Toast.LENGTH_SHORT).show();
                    return;
                }
                getBranchdata();
                break;


            case R.id.go_btn:

                if (cardtype.equals("0")) {
                    if (permitPic.length() <= 0) {
                        Toast.makeText(getContext(), "开户许可证照不能为空！", Toast.LENGTH_SHORT).show();
                        // 对公开户许可证
                        return;
                    }
                } else if (cardtype.equals("1")) {//个体
                    if (cashCard_Z.length() <= 0) {
                        Toast.makeText(getContext(), "储蓄卡正面照不能为空！", Toast.LENGTH_SHORT).show();
                        // 对私开户许可证
                        return;
                    } else if (cashCard_B.length() <= 0) {
                        Toast.makeText(getContext(), "储蓄卡背面照不能为空！", Toast.LENGTH_SHORT).show();
                        // 对私开户许可证
                        return;
                    }


                    if (bankNameEt.getText().length() <= 0) {
                        Toast.makeText(getContext(), "开户行姓名不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (bankNumberEt.getText().length() <= 0) {
                        Toast.makeText(getContext(), "银行账号不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (openBankTv.getText().length() <= 0) {
                        Toast.makeText(getContext(), "开户行不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (provincesTv.getText().length() <= 0) {
                        Toast.makeText(getContext(), "省份不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (cityTv.getText().length() <= 0) {
                        Toast.makeText(getContext(), "城市不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (branchTv.getText().length() <= 0) {
                        Toast.makeText(getContext(), "开户银行支行不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (phoneEt.getText().length() <= 0) {
                    Toast.makeText(getContext(), "预留手机号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //隐藏隐私条款
//                else if (num.equals("0")) {
//                    Toast.makeText(getContext(), "请勾选阅读条款", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                String name = bankNameEt.getText() + "";//卡帐号
                String cardNum = bankNumberEt.getText() + "";//卡帐号
                String bankName = openBankTv.getText() + "";//开户行
                String province = provincesTv.getText() + "";
                String city = cityTv.getText() + "";
                String branch = branchTv.getText() + "";//支行
                String phone = phoneEt.getText() + "";

                parentAct.entbankInf.setOpnbank(bankName);
                parentAct.entbankInf.setName(name);
                if (cardtype.equals("0")) {
                    parentAct.entbankInf.setComaccnum(cardNum);// 对公开户许可证的数字账号
                } else if (cardtype.equals("1")) {
                    parentAct.entbankInf.setPriaccount(cardNum);// 对私开户许可证的数字账号
                }
                parentAct.entbankInf.setCardtype(cardtype);
                parentAct.entbankInf.setProvince(province);
                parentAct.entbankInf.setCity(city);
                parentAct.entbankInf.setPonaccname(branch);
                parentAct.entbankInf.setCardphone(phone);
                if (cardtype.equals("0")) {//对公
                    parentAct.enterpriseInf.setOpenlicense(permitPic);//开户许可证
                } else if (cardtype.equals("1")) {//对私
                    parentAct.enterpriseInf.setBalancecardone(cashCard_Z);//结算卡正面
                    parentAct.enterpriseInf.setBalancecardtwo(cashCard_B); //结算卡反面
                }


                Log.e("parentAct.qrmerinf", parentAct.qrmerinf + "");
                Log.e("parentAct.enterpriseInf", parentAct.enterpriseInf + "");
                Log.e("parentAct.entAddress", parentAct.entAddress + "");
                Log.e("parentAct.entbankInf", parentAct.entbankInf + "");
                try {
                    Map<String, String> map =reflect(parentAct.qrmerinf, "");
                    maps.putAll(map);
                    maps.putAll(reflect(parentAct.enterpriseInf, "enterpriseInf"));
                    maps.putAll(reflect(parentAct.entAddress, "entAddress"));
                    maps.putAll(reflect(parentAct.entbankInf, "entbankInf"));
                    maps.put("phone", parentAct.phone);//银行预留手机号
                    maps.put("typeId", parentAct.enterpriseInf.industryid + "");//行业类型id
                    Log.e("maps++++", "maps" + maps);
                    Log.e("phone", parentAct.phone);
                    UploadInfo();


                } catch (Exception e) {

                    e.printStackTrace();
                    Log.e("Exception", e.getMessage());
                }
                break;
        }
    }


    private void provincesData() {
        long str = System.currentTimeMillis();
        int timestamp = (int) (str / 1000);

        HashMap<String, Object> maps = new HashMap<>();
        HashMap<String, Object> Headmaps = new HashMap<>();
        maps.put("parentCode", "1");

        //  requestPostByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.Bank_address, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");
                        JSONObject jsonObj = new JSONObject().parseObject(result + "");
                        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                        list = JSON.parseObject(jsonObj.get("detail") + "", new TypeReference<List<Map<String, Object>>>() {
                        });
                        Log.e("list", list + "");

                        if (list != null) {
                            PrinceList.clear();
                            for (int i = 0; i < list.size(); i++) {
                                PrinceList.add(list.get(i));
                            }

                            Log.e("PrinceList", PrinceList.toString());
                            for (int i = 0; i < PrinceList.size(); i++) {
                                provinceNameList.add(PrinceList.get(i).get("areaname") + "");
                            }
                            Log.e("provinceName", provinceNameList + "");
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
        maps.put("parentCode", princeid);
        //  requestPostByAsyn(String actionUrl, HashMap<String, Object> paramsMap,
        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.Bank_address, OkHttpClientManager.TYPE_POST_JSON, maps,
                OkHttpClientManager.HOST_javaMpay, new OkHttpClientManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.e("result", result + "");
                        JSONObject jsonObj = new JSONObject().parseObject(result + "");
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
                        selectPicker(cityNameList, "选择城市", cityId);

                        Log.e("cityNameList", cityNameList + "");
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e("errorMsg", errorMsg + "");
                        Toast.makeText(getActivity(), "网络不给力！", Toast.LENGTH_SHORT).show();
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

                                    if (Code == khzm_img) {
                                        permitPic = jsonObj.get("PICURL") + "";
                                    } else if (Code == kzm_img) {
                                        cashCard_Z = jsonObj.get("PICURL") + "";
                                    } else if (Code == kbm_img) {
                                        cashCard_B = jsonObj.get("PICURL") + "";
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
                            if (Code == khzm_img) {
                                permitPic =MyConstant.ALI_PUBLIC_URL+key;
                            } else if (Code == kzm_img) {
                                cashCard_Z = MyConstant.ALI_PUBLIC_URL+key;
                            } else if (Code == kbm_img) {
                                cashCard_B = MyConstant.ALI_PUBLIC_URL+key;
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




    /**
     * 获取开户行数据
     */
    private void getbankData() {
        HashMap<String, Object> map = new HashMap<>();
        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.Head_Bank, 1, map, 0, new OkHttpClientManager.ReqCallBack() {

            @Override
            public void onReqSuccess(Object result) {
                // TODO Auto-generated method stub
                BankList = new ArrayList<>();
                if (result != null) {
                    JSONObject jsonObj = JSONObject.parseObject(result + "");
                    Log.e("jsonObj", jsonObj.getString("RSPCOD"));
                    if (jsonObj.getString("RSPCOD").equals("000000")) {
                        String bankTotal = jsonObj.getString("bankTotal");
                        Log.e("bankTotal", bankTotal);
                        List<Map> lists = new ArrayList<Map>();
                        lists = JSONObject.parseArray(bankTotal, Map.class);
                        Log.e("list", lists.toString());
                        BankList.addAll(lists);
                        Log.e("BankList", BankList.toString());
                        if (lists != null) {
                            for (int i = 0; i < lists.size(); i++) {
                                bankNameList.add(lists.get(i).get("banktotalname").toString());
                            }
                            Log.e("bankNameList", bankNameList + "");

                        }
                        selectPicker(bankNameList, "选择开户行", Open_Bank);
                    }

                } else {
                    Toast.makeText(getContext(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "系统错误", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 获取开户支行数据
     */
    private void getBranchdata() {
        HashMap<String, Object> map = new HashMap<>();
        String city = cityTv.getText() + "";
        String citys = city.substring(0, city.length() - 1);
        Log.e("citys", "citys" + citys);
        map.put("banktotalid", banktotalid);//开户行ID
        map.put("city", citys);

        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.branch_Bank, 1, map, 0, new OkHttpClientManager.ReqCallBack() {

            @Override
            public void onReqSuccess(Object result) {
                // TODO Auto-generated method stub
                String city = cityTv.getText() + "";
                String citys = city.substring(0, city.length() - 1);
                Log.e("citys", "citys" + citys);
                String[] values = {banktotalid, citys};

                if (!result.equals("")) {
                    Intent intent = new Intent(getActivity(), FuzzySearchActivity.class);
                    intent.putExtra("json", result + "");
                    // startActivity(intent);
                    startActivityForResult(intent, 0);

                } else {
                    Toast.makeText(getContext(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                // TODO Auto-generated method stub
                Toast.makeText(getContext(), "系统错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void selectPicker(ArrayList<String> list, String title, final int types) {
        // 设置标题
        mOpv.setTitle(title);

        // 设置三级联动效果
        // mOpv.setPicker(speciesNameList1, speciesNameList2,
        // speciesNameList3, true);

        mOpv.setPicker(list, null, null, true);

        // 设置是否循环滚动
        mOpv.setCyclic(false, false, false);

        // 设置默认选中的三级项目
        mOpv.setSelectOptions(0, 0);

        // 监听确定选择按钮
        mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                // 返回的分别是三个级别的选中位置
                if (types == Open_Bank) {
                    banktotalid = BankList.get(options1).get("banktotalid") + "";//开户行ID
                    String tx = bankNameList.get(options1) + "";
                    Log.e("banktotalid", banktotalid + "...");
                    openBankTv.setText(tx);

                } else if (types == ProvinceId) {
                    Log.e("PrinceList+++", PrinceList + "");
                    princeid = PrinceList.get(options1).get("areacode") + "";
                    String tx = provinceNameList.get(options1) + "";
                    cityTv.setText("");
                    provincesTv.setText(tx);
                } else if (types == cityId) {
                    String tx = cityNameList.get(options1) + "";
                    cityTv.setText(tx);
                } else if (types == branchId) {
                    String tx = cityNameList.get(options1) + "";
                    branchTv.setText(tx);
                }

            }
        });
        mOpv.show();
    }

    @SuppressWarnings("unchecked")
    public void UploadInfo() {
        showLoadingDialog("正在上传资料中。。。");
        OkHttpClientManager.getInstance(getActivity()).requestAsyn(HttpUrls.UploadData, 1, maps, 0, new OkHttpClientManager.ReqCallBack() {

            @Override
            public void onReqSuccess(Object result) {
                // TODO Auto-generated method stub
                loadingDialogWhole.dismiss();
                DataDispose(result);
            }

            @Override
            public void onReqFailed(String errorMsg) {
                // TODO Auto-generated method stub
                loadingDialogWhole.dismiss();
                button = new OneButtonDialogWhite(getActivity(), "上传资料失败，请重新提交！", "确定", new OnMyDialogClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        button.dismiss();
                    }

                });
                button.setCancelable(false);
                button.setCanceledOnTouchOutside(false);
                button.show();
            }
        });
    }

    protected void DataDispose(Object result) {
        // TODO Auto-generated method stub
        if (result != null && !result.equals("")) {
            HashMap<String, Object> map = new HashMap<>();
            map = (HashMap<String, Object>) JSON.parseObject(result + "", new TypeReference<Map<String, Object>>() {
            });
            if (map != null) {
                if (map.get("RSPCOD").equals("000000")) {
                    //   switchContent(parentAct.Fragment4, parentAct.Fragment5, R.id.fragment, 1);
                    Toast.makeText(getContext(), map.get("RSPMSG") + "", Toast.LENGTH_SHORT).show();
                    switchContent(parentAct.Cfragment, parentAct.Dfragment, R.id.fragment, 1);

                } else {
                    button = new OneButtonDialogWhite(getActivity(), map.get("RSPMSG") + "", "确定", new OnMyDialogClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            button.dismiss();
                        }

                    });
                    button.setCancelable(false);
                    button.setCanceledOnTouchOutside(false);
                    button.show();
                }
            } else {
                button = new OneButtonDialogWhite(getActivity(), "上传资料失败，请重新提交！", "确定", new OnMyDialogClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        button.dismiss();
                    }

                });
                button.setCancelable(false);
                button.setCanceledOnTouchOutside(false);
                button.show();

            }

        }
    }

    public void setPic() {
        menuWindow = new SelectPicPopupWindow(getActivity(), itemsOnClick);
        //  LinearLayout lin = (LinearLayout) findViewById(R.id.fm_main);
        menuWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        // WindowManager.LayoutParams params=this.getWindow().getAttributes();
        // params.alpha=0.7f;
        // this.getWindow().setAttributes(params);
    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    //  camera();
                    // 3、调用拍照方法
                    PermissionGen.with(MerchartCFragment.this)
                            .addRequestCode(LQRPhotoSelectFragmentUtils.REQ_TAKE_PHOTO)
                            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                            ).request();

                    break;
                case R.id.btn_pick_photo:
                    // 3、调用从图库选取图片方法
                    PermissionGen.needPermission(MerchartCFragment.this,
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
        PermissionGen.onRequestPermissionsResult(MerchartCFragment.this, requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联

        if (resultCode == 6) {
            String getData = data.getStringExtra("bankname");
            branchTv.setText(getData);
            return;
        } else {
            mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
