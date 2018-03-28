package td.com.xiaoheixiong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;

public class selectMechatsActivity extends BaseActivity {

    @Bind(R.id.gt_img)
    ImageView gtImg;
    @Bind(R.id.qy_img)
    ImageView qyImg;
    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.title_right_rl)
    RelativeLayout titleRightRl;
    private String Mine = "";//自己申请、帮人申请

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mechats);
        ButterKnife.bind(this);
        Intent it = getIntent();
        Mine = it.getStringExtra("Mine");
        initview();
    }

    private void initview() {
        titleTv.setText("商家进件");
        titleRightRl.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.gt_img, R.id.qy_img, R.id.back_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.gt_img://个体
                Intent intent = new Intent(this, MerchantsGatheringActivity.class);
                intent.putExtra("cardtype", "1");
                intent.putExtra("Mine", Mine);
                startActivity(intent);
                finish();
                break;
            case R.id.qy_img://企业
                Intent intent1 = new Intent(this, MerchantsGatheringActivity.class);
                intent1.putExtra("cardtype", "0");
                intent1.putExtra("Mine", Mine);
                startActivity(intent1);
                finish();

                break;

            case R.id.back_img:
                finish();

                break;
        }
    }
}
