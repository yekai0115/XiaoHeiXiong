package td.com.xiaoheixiong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.DimenUtils;
import td.com.xiaoheixiong.Utils.ListUtils;
import td.com.xiaoheixiong.Utils.MyCacheUtil;
import td.com.xiaoheixiong.Utils.views.GlideRoundTransform;
import td.com.xiaoheixiong.httpNet.HttpUrls;
import td.com.xiaoheixiong.httpNet.OkHttpClientManager;

/**
 * 转发头条
 */

public class TransmitHeadlinActivity extends BaseActivity {


    private TextView tv_cancle;
    private TextView tv_transmit;
    private EditText ed_content;
    private TextView tv_nick;
    private TextView tv_content;
    private ImageView img_pic;
    private ArrayList<String> imagekeyList = new ArrayList<>();
    private String mainImg; //    头像
    private String description;//描述
    private String nick;//昵称
    private Context mContext;
    private String MERCNUM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit_headline);
        mContext = this;
        MERCNUM = MyCacheUtil.getshared(this).getString("MERCNUM", "");
        nick = getIntent().getStringExtra("mercName");
        mainImg = getIntent().getStringExtra("mainImg");
        description = getIntent().getStringExtra("description");
        String imgs=getIntent().getStringExtra("imageList");
        imagekeyList = ListUtils.getList(imgs);
        initViews();

    }

    protected void initViews() {
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_transmit = (TextView) findViewById(R.id.tv_transmit);
        ed_content = (EditText) findViewById(R.id.ed_content);
        tv_nick = (TextView) findViewById(R.id.tv_nick);
        tv_content = (TextView) findViewById(R.id.tv_content);
        img_pic = (ImageView) findViewById(R.id.img_pic);
        tv_nick.setText(nick);
        tv_content.setText(description);
        if(null==imagekeyList||imagekeyList.isEmpty()){
            Glide.with(mContext).load(mainImg)
                    .centerCrop()
                    //  .override(DimenUtils.dip2px(context, 100), DimenUtils.dip2px(context, 100))
                    .placeholder(R.drawable.pic_nomal_loading_style)
                    .error(R.drawable.pic_nomal_loading_style)
                    .into(img_pic);
        }else{
            Glide.with(mContext).load(imagekeyList.get(0))
                    .centerCrop()
                    //  .override(DimenUtils.dip2px(context, 100), DimenUtils.dip2px(context, 100))
                    .placeholder(R.drawable.pic_nomal_loading_style)
                    .error(R.drawable.pic_nomal_loading_style)
                    .into(img_pic);
        }

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String content=ed_content.getText().toString();
                if(StringUtils.isEmpty(content)){
                    Toast.makeText(getApplicationContext(), "请输入转发内容！", Toast.LENGTH_SHORT).show();
                }else{
                    StringBuilder sb = new StringBuilder();
                    for (String key : imagekeyList) {
                        if(!StringUtils.isEmpty(key)){
                            sb.append(key).append("|");
                        }
                    }
                    String images = sb.deleteCharAt(sb.length() - 1).toString();
                    addHeadLine(content,images);
                }
            }
        });
    }


    private void addHeadLine(String description, String images) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("sign", "");
        maps.put("mercId", MERCNUM);
        maps.put("mercName", "");
        maps.put("description", description);
        maps.put("images", images);
        showLoadingDialog("...");

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

                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        // TODO Auto-generated method stub
                        loadingDialogWhole.dismiss();
                    }
                });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
