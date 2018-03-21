package td.com.xiaoheixiong.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.LQRPhotoSelectUtils;
import td.com.xiaoheixiong.aliutil.MyOSSConfig;
import td.com.xiaoheixiong.beans.Mechants.EntAddress;
import td.com.xiaoheixiong.beans.Mechants.EntbankInf;
import td.com.xiaoheixiong.beans.Mechants.EnterpriseInf;
import td.com.xiaoheixiong.beans.Mechants.QrmerInf;
import td.com.xiaoheixiong.beans.MyConstant;
import td.com.xiaoheixiong.fragments.Merchants.MerchartAFragment;
import td.com.xiaoheixiong.fragments.Merchants.MerchartBFragment;
import td.com.xiaoheixiong.fragments.Merchants.MerchartCFragment;
import td.com.xiaoheixiong.fragments.Merchants.MerchartDFragment;


public class MerchantsGatheringActivity extends FragmentActivity {
	public FrameLayout fragment;
	Context context;
	public String phone = "",cardtype="",Mine = "";
	public MerchartAFragment Afragment;
	public MerchartBFragment Bfragment;
	public MerchartCFragment Cfragment;
	public MerchartDFragment Dfragment;
	public QrmerInf qrmerinf;
	public EnterpriseInf enterpriseInf;
	public EntAddress entAddress;
	public EntbankInf entbankInf;
	private LQRPhotoSelectUtils mLqrPhotoSelectUtils;

	public OSS oss;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant);
		Intent it = getIntent();
		cardtype = it.getStringExtra("cardtype");
		Mine = it.getStringExtra("Mine");
		oss = new OSSClient(getApplicationContext(), MyConstant.ALI_ENDPOINT, MyOSSConfig.getProvider(), MyOSSConfig.getOSSConfig());
		fragment = (FrameLayout) findViewById(R.id.fragment);
		Afragment = new MerchartAFragment();
		Bfragment = new MerchartBFragment();
		Cfragment = new MerchartCFragment();
		Dfragment = new MerchartDFragment();
		qrmerinf = new QrmerInf();
		enterpriseInf = new EnterpriseInf();
		entAddress = new EntAddress();
		entbankInf = new EntbankInf();
		// 步骤一：添加一个FragmentTransaction
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 步骤二：用add()方法加上Fragment的对象rightFragment

		transaction.add(R.id.fragment, Afragment);
		// 步骤三：调用commit()方法使得FragmentTransaction改变生效
		transaction.commit();
	}

}
