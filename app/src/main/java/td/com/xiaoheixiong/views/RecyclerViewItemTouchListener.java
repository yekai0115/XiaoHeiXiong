package td.com.xiaoheixiong.views;

import android.view.View;

/**
 * Created by andry on 2016/9/26.
 */
public interface RecyclerViewItemTouchListener {

    public void onItemTouch(View view, int position);
    public void onTtemLongTouch(View view, int position);
}
