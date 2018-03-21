/**
 * Copyright 2018 bejson.com
 */
package td.com.xiaoheixiong.beans.TuanTuan;


import java.io.Serializable;

/**
 * 团团券对象
 */
public class TTBean  implements Serializable{

    private String id;
    private String name;
    private String price;
    private String enterNum;//团团券参团人数
    private String cardNum;//秒秒券数量

    private String mainImg;
    private String detailImg;
    private String mercId;
    private String orgCode;
    private String createBy;
    private String createTime;
    private String endTime;
    private String state;
    private String isDel;
    private String description;


    private String merAddress;
    private String merLabel;
    private String phone;
    private String popularity;
    private String shopName;
    private String orgcode;



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getEnterNum() {
        return enterNum;
    }

    public String getMainImg() {
        return mainImg;
    }

    public String getDetailImg() {
        return detailImg;
    }

    public String getMercId() {
        return mercId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getState() {
        return state;
    }

    public String getIsDel() {
        return isDel;
    }

    public String getDescription() {
        return description;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getMerAdderess() {
        return merAddress;
    }

    public String getMerLabel() {
        return merLabel;
    }

    public String getPhone() {
        return phone;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getShopName() {
        return shopName;
    }

    public String getOrgcode() {
        return orgcode;
    }
}