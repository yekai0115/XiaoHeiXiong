package td.com.xiaoheixiong.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * Created by ZhuMing on 2017/7/12.
 * 自定义 填充屏幕的VideoView
 */
public class FullScreenVideoView extends VideoView {
    public FullScreenVideoView(Context context) {
        super(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //  WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        //  int width = wm.getDefaultDisplay().getWidth();
        // int height = wm.getDefaultDisplay().getHeight();
        //  Log.e("WindowManager",width+"   "+ height );
        //  setMeasuredDimension(width, height);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        Log.e("WindowManager", width + "   " + height + "  ");

      //  boolean NaviGationBar = GetNavigationBarUtil.checkDeviceHasNavigationBar(getContext());//通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
      //  boolean NaviGationBarIsShow = GetNavigationBarUtil.isNavigationBarShow();//是否显示返回键、菜单键
      //  if (NaviGationBar) {
      //      if (!NaviGationBarIsShow) {
      //        int BarH =  GetNavigationBarUtil.getNavigationBarHeight();
       //         Log.e("BarH", BarH + "  ");
       //         setMeasuredDimension(width, height+BarH);
      //          return;
        //    }
       // }
        setMeasuredDimension(width, height);
    }
}