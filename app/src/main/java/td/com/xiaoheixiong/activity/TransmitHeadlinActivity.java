package td.com.xiaoheixiong.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import td.com.xiaoheixiong.R;
/**
 * 转发头条
 */

public class TransmitHeadlinActivity extends BaseActivity  {


    private TextView tv_cancle;
    private TextView tv_transmit;
    private EditText ed_content;
    private TextView tv_nick;
    private TextView tv_content;

    private ArrayList<String> imagekeyList = new ArrayList<>();
    private String mainImg; //    主图
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit_headline);
        mContext = this;
        initViews();

    }

    protected void initViews() {
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_transmit = (TextView) findViewById(R.id.tv_transmit);
        ed_content = (EditText) findViewById(R.id.ed_content);
        tv_nick = (TextView) findViewById(R.id.tv_nick);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
