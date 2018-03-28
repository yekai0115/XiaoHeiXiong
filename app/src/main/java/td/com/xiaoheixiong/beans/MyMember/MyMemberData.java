package td.com.xiaoheixiong.beans.MyMember;

import java.util.List;

/**
 * Created by 11832 on 2018/3/28.
 */

public class MyMemberData {

    private List<MymemberBean>  weichat;

    private List<MymemberBean>  alipay;

    private List<MymemberBean>  unionpay;


    public List<MymemberBean> getWeichat() {
        return weichat;
    }

    public List<MymemberBean> getAlipay() {
        return alipay;
    }

    public List<MymemberBean> getUnionpay() {
        return unionpay;
    }
}
