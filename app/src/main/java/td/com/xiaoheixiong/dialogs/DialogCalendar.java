package td.com.xiaoheixiong.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import td.com.xiaoheixiong.R;

/**
 * Created by StormShadow on 2017/3/18.
 * Knowledge is power.
 */
public class DialogCalendar {

    public void showDialog(Activity activity, final onConfirmClickedListener listener) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_calendar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        int screenWidth = dm.widthPixels;
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = screenWidth;
        layoutParams.height=WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        final TextView curMonth = (TextView) dialog.findViewById(R.id.dialog_cal_curmonth);
        TextView confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(selectedDate);
                dialog.dismiss();
            }
        });

        final CompactCalendarView compactCalendarView = (CompactCalendarView) dialog.findViewById(R.id.dialog_cal_calendar);
        compactCalendarView.setLocale(TimeZone.getDefault(), Locale.CHINESE);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);

        final DateFormat format = new SimpleDateFormat("yyyy年M月");
        Date curDate = new Date();
        curMonth.setText(format.format(curDate));

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
//                CustomLog.e("lion", "Day was clicked: " + dateClicked + " with events " + events);
                selectedDate = dateClicked;
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
//                CustomLog.e("lion", "Month was scrolled to: " + firstDayOfNewMonth);
                curMonth.setText(format.format(firstDayOfNewMonth));
                selectedDate = firstDayOfNewMonth;
            }
        });

        dialog.show();
    }

    private Date selectedDate = null;
    public interface onConfirmClickedListener {
        void onClick(Date selectedDate);
    }
}
