package td.com.xiaoheixiong.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import td.com.xiaoheixiong.R;

/**
 * 一按钮带titel(中间textView无限制下拉)Dialog样式2
 *
 * @author liangge
 */
public class HongbaoButtonDialog extends Dialog implements
        View.OnClickListener {
    private TextView bt_left, bt_right, msg_tv, title_tv;
    /**
     * 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消)
     */
    private String title, content, affirmStr, strLeft = "", strRight = "";
    private ImageView imageView;
    private LinearLayout hb_bg_ll, openHB_ll;
    private RelativeLayout mainImg_rl;
    private Context context;

    /**
     * dialog监听器
     */
    public OnMyDialogClickListener onMyDialogClickListener;


    public HongbaoButtonDialog(Context context, boolean cancelable,
                               OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public HongbaoButtonDialog(Context context, int theme,
                               String content,
                               OnMyDialogClickListener onMyDialogClickListener) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.content = content;
        this.onMyDialogClickListener = onMyDialogClickListener;
    }


    public HongbaoButtonDialog(Context context,
                               String content,
                               OnMyDialogClickListener onMyDialogClickListener) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.content = content;
        this.onMyDialogClickListener = onMyDialogClickListener;
    }

    private void initView() {
        //   out_img = (ImageView) findViewById(R.id.out_img);
        imageView = (ImageView) findViewById(R.id.imageView);
        hb_bg_ll = (LinearLayout) findViewById(R.id.hb_bg_ll);
        openHB_ll = (LinearLayout) findViewById(R.id.openHB_ll);
        mainImg_rl = (RelativeLayout) findViewById(R.id.mainImg_rl);

        //   Glide.with(context).load("file:///android_asset/hbao.gif").asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        imageView.setOnClickListener(this);
        mainImg_rl.setOnClickListener(this);
        hb_bg_ll.setOnClickListener(this);
        openHB_ll.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_hongbao);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initView();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        onMyDialogClickListener.onClick(v);
    }
}
