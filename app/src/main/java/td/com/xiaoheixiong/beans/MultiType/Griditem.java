package td.com.xiaoheixiong.beans.MultiType;

import android.support.annotation.NonNull;

/**
 * Created by lenovo on 2017/10/27.
 */

public class Griditem {

    public int ImgId;
    public
    @NonNull
    String text;


    public Griditem(int coverResId, @NonNull final String text) {
        this.ImgId = coverResId;
        this.text = text;
    }
}
