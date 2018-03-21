package td.com.xiaoheixiong.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.dialogs.MechantsButtonDialog;
import td.com.xiaoheixiong.dialogs.OnMyDialogClickListener;


public class SelectCollectionActivity extends BaseActivity implements OnClickListener {
    private TextView tv__MerchantsGathering, tv_Individual_credit, title_tv;
    private String mobile, attStr, sts;
    private RelativeLayout bt_main_title_right1;
    private Editor editor;
    private ImageView back_img, right_img;
    private MechantsButtonDialog mDialog;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcollection);
        DisplayMetrics metrics = new DisplayMetrics();
        editor = MyCacheUtil.setshared(this);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
        attStr = MyCacheUtil.getshared(this).getString("MERSTS", "");
        sts = MyCacheUtil.getshared(this).getString("STS", "");
        initview();

    }

    private void initview() {
        tv__MerchantsGathering = (TextView) findViewById(R.id.tv__MerchantsGathering);
        tv_Individual_credit = (TextView) findViewById(R.id.tv_Individual_credit);
        tv__MerchantsGathering.setOnClickListener(this);
        tv_Individual_credit.setOnClickListener(this);
        back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("聚合收款");
        right_img = (ImageView) findViewById(R.id.right_img);
        right_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv__MerchantsGathering:
                mDialog = new MechantsButtonDialog(this, "", new OnMyDialogClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_left:
                                Intent it = new Intent(SelectCollectionActivity.this, selectMechatsActivity.class);
                                it.putExtra("Mine","0");
                                startActivity(it);
                                mDialog.dismiss();
                                finish();
                                break;
                            case R.id.btn_right:
                                Intent it1 = new Intent(SelectCollectionActivity.this, selectMechatsActivity.class);
                                it1.putExtra("Mine","1");
                                startActivity(it1);
                                mDialog.dismiss();
                                finish();
                                break;
                            case R.id.out_img:
                                mDialog.dismiss();
                                break;
                        }
                    }
                });
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();

                break;
            case R.id.tv_Individual_credit:
                //Intent intent = new Intent(this, SelectEpayActivity.class);
                // Intent intent = new Intent(this, PersonalCollectionActivity.class);
                //  startActivity(intent);
                finish();
                break;

            case R.id.right_img:
                call("4006118163");
                break;

            default:
                break;
        }
    }


    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
