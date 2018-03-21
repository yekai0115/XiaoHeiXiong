package td.com.xiaoheixiong.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.Inflater;

/**
 * FUNCTION：我的缓存类
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GetDateUtils {
    Calendar mDate = Calendar.getInstance();
    int time = 0;// 时间差
    private String[] mDateDisplayValues = new String[7];
    private String[] week = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    private static String[] remind = new String[]{"准时提醒", "提前5分钟", "提前10分钟", "提前30分钟", "提前1个小时", "提前2个小时", "提前1天"};
    private Map<String, Integer> timeMap;

    /**
     * 获取两个日期的时间差
     */
    public static int getTimeInterval(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int interval = 0;
        try {
            Date currentTime = new Date();// 获取现在的时间
            Date beginTime = dateFormat.parse(data);
            interval = (int) ((beginTime.getTime() - currentTime.getTime()) / (1000));// 时间差 单位秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return interval;
    }

    /**
     * 获取提前提醒的秒数
     */
    public int getAlarmTiqian(String tiqian) {
        int time = 0;
        if (tiqian.equals(remind[0])) {
            time = 0;//准时提醒
        } else if (tiqian.equals(remind[1])) {
            time = 5 * 60;//提前5分钟
        } else if (tiqian.equals(remind[2])) {
            time = 10 * 60;//提前10分钟
        } else if (tiqian.equals(remind[3])) {
            time = 30 * 60;//提前30分钟
        } else if (tiqian.equals(remind[4])) {
            time = 60 * 60;//提前1个小时
        } else if (tiqian.equals(remind[5])) {
            time = 2 * 60 * 60;//提前2个小时
        } else {
            time = 24 * 60 * 60;//提前一天
        }
        return time;
    }

    /**
     * 设定显示文字
     */
    public static Map<String, Long> getIntervalMap(int time) {
        String txt = null;
        Map<String, Long> map = new HashMap<>();
        if (time >= 0) {
            long day = time / (24 * 3600);// 天
            long hour = time % (24 * 3600) / 3600;// 小时
            long minute = time % 3600 / 60;// 分钟
            long second = time % 60;// 秒

            map.put("day", day);
            map.put("hour", hour);
            map.put("minute", minute);
            map.put("second", second);

        } else {
            txt = "已过期";
        }
        return map;
    }

    /**
     * 设定显示文字
     */
    public static String getInterval(int time) {
        String txt = null;
        if (time >= 0) {
            long day = time / (24 * 3600);// 天
            long hour = time % (24 * 3600) / 3600;// 小时
            long minute = time % 3600 / 60;// 分钟
            long second = time % 60;// 秒

            txt = " 距离现在还有：" + day + "天" + hour + "小时" + minute + "分" + second + "秒";
        } else {
            txt = "已过期";
        }
        return txt;
    }
}

