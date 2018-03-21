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
 * 
 */
public class OneButtonDialogWhite extends Dialog implements
		View.OnClickListener {
	/** 标题、内容 */
	private TextView tv_title, tv_content;
	/** 确认、取消 */
	private TextView bt_affirm;
	/** 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消) */
	private String title, content, affirmStr;
	/** dialog监听器 */
	public OnMyDialogClickListener onMyDialogClickListener;

	public OneButtonDialogWhite(Context context, boolean cancelable,
                                OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public OneButtonDialogWhite(Context context, int theme,
                                String content, String affirmStr,
                                OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.content = content;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}
	
	
	public OneButtonDialogWhite(Context context, String content,
                                String affirmStr, OnMyDialogClickListener onMyDialogClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.content = content;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}


	private void initView() {
		tv_content = (TextView) findViewById(R.id.tv_prompt);
		bt_affirm = (TextView) findViewById(R.id.btn_left);
		tv_content.setText(content);
		bt_affirm.setText(affirmStr);
		bt_affirm.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_onebuttonwhite);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onMyDialogClickListener.onClick(v);
	}
}
