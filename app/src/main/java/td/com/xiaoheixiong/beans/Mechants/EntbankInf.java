package td.com.xiaoheixiong.beans.Mechants;

import java.io.Serializable;

public class EntbankInf implements Serializable {
    // private String entorgcode;//企业id 组织机构代码
    private String name="";// 银行开户名
    private String opnbank="";// 开户行
    private String ponaccname="";// 开户支行名
    private String comaccnum="";// 对公银行账号
    private String province="";
    private String city="";
    private String pubbankurl="";// 对公许可证图
    private String priaccount="";// 对私银行账号
    private String cardtype="";// 银行账号类型
    private String cardphone="";//银行预留手机号


    public String getCardphone() {
        return cardphone;
    }

    public void setCardphone(String cardphone) {
        this.cardphone = cardphone;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getPriaccount() {
        return priaccount;
    }

    public void setPriaccount(String priaccount) {
        this.priaccount = priaccount;
    }

    /*
     * public String getEntorgcode() { return entorgcode; } public void
     * setEntorgcode(String entorgcode) { this.entorgcode = entorgcode; }
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpnbank() {
        return opnbank;
    }

    public void setOpnbank(String opnbank) {
        this.opnbank = opnbank;
    }

    public String getPonaccname() {
        return ponaccname;
    }

    public void setPonaccname(String ponaccname) {
        this.ponaccname = ponaccname;
    }

    public String getComaccnum() {
        return comaccnum;
    }

    public void setComaccnum(String comaccnum) {
        this.comaccnum = comaccnum;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPubbankurl() {
        return pubbankurl;
    }

    public void setPubbankurl(String pubbankurl) {
        this.pubbankurl = pubbankurl;
    }

    @Override
    public String toString() {
        return "EntbankInf [name=" + name + ", opnbank=" + opnbank + ", ponaccname=" + ponaccname + ", comaccnum="
                + comaccnum + ", province=" + province + ", city=" + city + ", pubbankurl=" + pubbankurl
                + ", priaccount=" + priaccount + ", cardtype=" + cardtype + "]";
    }

}