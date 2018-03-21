package td.com.xiaoheixiong.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import td.com.xiaoheixiong.R;

/**
 * 一按钮带titel(中间textView无限制下拉)Dialog样式2
 *
 * @author liangge
 */
public class TwoButtonDialogTitleWhite extends Dialog implements
        View.OnClickListener {
    private TextView bt_left, bt_right, msg_tv, title_tv;
    /**
     * 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消)
     */
    private String title, content, affirmStr, strLeft="", strRight="";
    /**
     * dialog监听器
     */
    public OnMyDialogClickListener onMyDialogClickListener;

    public TwoButtonDialogTitleWhite(Context context, boolean cancelable,
                                     OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public TwoButtonDialogTitleWhite(Context context, int theme,
                                     String content, String affirmStr,
                                     OnMyDialogClickListener onMyDialogClickListener) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        this.content = content;
        this.affirmStr = affirmStr;
        this.onMyDialogClickListener = onMyDialogClickListener;
    }


    public TwoButtonDialogTitleWhite(Context context, String content,
                                     String affirmStr, OnMyDialogClickListener onMyDialogClickListener) {
        super(context);
        // TODO Auto-generated constructor stub
        this.content = content;
        this.affirmStr = affirmStr;
        this.onMyDialogClickListener = onMyDialogClickListener;
    }

    public TwoButtonDialogTitleWhite(Context context, String content,
                                     String affirmStr, String strLeft, String strRight, OnMyDialogClickListener onMyDialogClickListener) {
        super(context);
        // TODO Auto-generated constructor stub
        this.content = content;
        this.affirmStr = affirmStr;
        this.strLeft = strLeft;
        this.strRight = strRight;
        this.onMyDialogClickListener = onMyDialogClickListener;

    }

    private void initView() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        bt_left = (TextView) findViewById(R.id.btn_left);
        bt_right = (TextView) findViewById(R.id.btn_right);
        msg_tv = (TextView) findViewById(R.id.msg_tv);
        title_tv.setText(content);
        msg_tv.setText(affirmStr);
        bt_left.setText(strLeft);
        bt_right.setText(strRight);
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_twobuttonwhite);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initView();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        onMyDialogClickListener.onClick(v);
    }
}
