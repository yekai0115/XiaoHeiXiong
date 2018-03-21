package td.com.xiaoheixiong.Utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

public class UiUtils {

    /**
     * 获取上下文对象
     */
    public static Context getContext() {
        return BNaviDemoApplication.getContext();
    }

    /**
     * 填充布局
     */
    public static View inflate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    /**
     * 获取资源目录
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取String
     */
    public static String getString(int stringId) {
        return getResources().getString(stringId);
    }

    /**
     * 获取Drawable
     */
    public static Drawable getDrawable(int drawableId) {
        return getResources().getDrawable(drawableId);
    }

    /**
     * 获取StringArray
     */
    public static String[] getStringArray(int stringArrayId) {
        return getResources().getStringArray(stringArrayId);
    }

    /**
     * 根据颜色选择器id,获取颜色选择器对象
     */
    public static ColorStateList getColorStateList(int mTabTextColorResId) {
        return getResources().getColorStateList(mTabTextColorResId);
    }

    /**
     * 通过ID获取颜色
     */
    public static int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    /**
     * Dip 转 Px
     */
    public static int dip2px(int dip) {
        float d = getResources().getDisplayMetrics().density;
        return (int) (dip * d + 0.5);
    }

    /**
     * Px 转 Dip
     */
    public static int px2dip(int px) {
        float d = getResources().getDisplayMetrics().density;
        return (int) (px / d + 0.5);
    }
}
