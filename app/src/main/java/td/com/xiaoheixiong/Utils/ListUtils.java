package td.com.xiaoheixiong.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;


/**
 * Created by 11832 on 2018/3/21.
 */

public class ListUtils {

    public static ArrayList<String> getList(String detailImg) {
        ArrayList list = new ArrayList();
        if(!StringUtils.isEmpty(detailImg)){
            String[] temp = detailImg.split("[|]");

            for (int i = 0; i < temp.length; i++) {
                list.add(temp[i]);
            }
        }
        return list;

    }
}
