package td.com.xiaoheixiong.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import td.com.xiaoheixiong.R;


/**
 * 一按钮不带titel(中间textView无限制下拉)Dialog样式
 * 
 * @author liangge
 * 
 */
public class SuccessOrFailDialog extends Dialog implements
		View.OnClickListener {
	/** 标题、内容 */
	private TextView tv_title, tv_content,tv_response;
	private ImageView successfail_img;
	private Drawable drawable;
	/** 确认、取消 */
	private TextView bt_affirm;
	/** 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消) */
	private String title, content, affirmStr,response;
	private int id;
	/** dialog监听器 */
	public OnMyDialogClickListener onMyDialogClickListener;
	public Context context;

	public SuccessOrFailDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public SuccessOrFailDialog(Context context, int theme,
			String content, String affirmStr,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub	
		this.content = content;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}
	
	
	public SuccessOrFailDialog(Context context, String content, Drawable drawable, String response,
			String affirmStr, OnMyDialogClickListener onMyDialogClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.content = content;
		this.affirmStr = affirmStr;
	    this.drawable = drawable;
	    this.response = response;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	public SuccessOrFailDialog(Context context, String content, String response,
							   String affirmStr, OnMyDialogClickListener onMyDialogClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.content = content;
		this.affirmStr = affirmStr;
		this.response = response;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	public SuccessOrFailDialog(Context context, String content,String affirmStr,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.content = content;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	private void initView() {
		
		tv_content = (TextView) findViewById(R.id.tv_prompt);
		bt_affirm = (TextView) findViewById(R.id.btn_left);	
		tv_response = (TextView) findViewById(R.id.tv_response);
		successfail_img = (ImageView) findViewById(R.id.successfail_img);
		
		//Drawable drawable = context.getResources().getDrawable(id);
		//successfail_img.setImageDrawable(drawable);
		tv_content.setText(content);
		bt_affirm.setText(affirmStr);
		tv_response.setText(response);
		bt_affirm.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_success);
		setCanceledOnTouchOutside(false);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onMyDialogClickListener.onClick(v);
	}
}
