package td.com.xiaoheixiong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import td.com.xiaoheixiong.R;

public class SuccessOrFailActivity extends Activity {

    private LinearLayout success_ll, fail_ll;
    private TextView ensure_vt;
    private String tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successorfail);


        initview();
    }

    private void initview() {
        success_ll = (LinearLayout) findViewById(R.id.success_ll);
        fail_ll = (LinearLayout) findViewById(R.id.fail_ll);
        ensure_vt = (TextView) findViewById(R.id.ensure_vt);
        ensure_vt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                //	Intent intent = new Intent(SuccessOrFailActivity.this, PayMainActivity.class);// 跳转回支付中心选择银行卡的界面
                //	startActivity(intent);

            }
        });
    }

}
