package td.com.xiaoheixiong.beans.MyAccount;

import java.util.List;

import td.com.xiaoheixiong.beans.earns.MyEarnsBean;

/**
 * Created by 11832 on 2018/3/28.
 */

public class MyAccountData {

    private String merchantTotal;
    private String flowmeterTotal;
    private List<MyAccountBean> list;

    public String getMerchantTotal() {
        return merchantTotal;
    }

    public String getFlowmeterTotal() {
        return flowmeterTotal;
    }

    public List<MyAccountBean> getList() {
        return list;
    }
}
