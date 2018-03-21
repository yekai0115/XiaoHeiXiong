package td.com.xiaoheixiong.activity;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import butterknife.Bind;
import butterknife.ButterKnife;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.views.StatusBarUtil;


public class VersionMsgActivity extends BaseActivity {
    @Bind(R.id.version_tv)
    TextView versionTv;
    private TextView title_tv;
    private ImageView back_img, right_img;
    private RelativeLayout title_right_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        ButterKnife.bind(this);

        intiView();
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

    private void intiView() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        back_img = (ImageView) findViewById(R.id.back_img);
        title_right_rl = (RelativeLayout) findViewById(R.id.title_right_rl);
        right_img = (ImageView) findViewById(R.id.right_img);
        title_right_rl.setVisibility(View.GONE);
        title_tv.setText("版本信息");

        back_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        versionTv.setText("当前版本： " + getVersion());
    }


    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
