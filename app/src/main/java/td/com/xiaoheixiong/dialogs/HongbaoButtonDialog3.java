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
public class HongbaoButtonDialog3 extends Dialog implements
        View.OnClickListener {
    private TextView money_tv;
    /**
     * 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消)
     */
    private String title, content, affirmStr, strLeft = "", strRight = "";
    private Context context;

    /**
     * dialog监听器
     */
    public OnMyDialogClickListener onMyDialogClickListener;


    public HongbaoButtonDialog3(Context context, boolean cancelable,
                                OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public HongbaoButtonDialog3(Context context, int theme,
                                String content,
                                OnMyDialogClickListener onMyDialogClickListener) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.content = content;
        this.onMyDialogClickListener = onMyDialogClickListener;
    }


    public HongbaoButtonDialog3(Context context,
                                String content,
                                OnMyDialogClickListener onMyDialogClickListener) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.content = content;
        this.onMyDialogClickListener = onMyDialogClickListener;
    }

    private void initView() {
        money_tv = (TextView) findViewById(R.id.money_tv);
        money_tv.setText(content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_hongbao3);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initView();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        onMyDialogClickListener.onClick(v);
    }
}
