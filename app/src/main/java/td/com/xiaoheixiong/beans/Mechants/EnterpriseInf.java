package td.com.xiaoheixiong.beans.Mechants;

import java.io.Serializable;

public class EnterpriseInf implements Serializable {
    private String entname; // 企业名称
    private String orgcode;//组织机构代码
    private String busregnum;//营业执照注册号
    private String idcardtwo;//身份证反面
    private String name;//法人姓名
    private String idcard;//证件号
    private String idcardType;//证件类型
    private String idcardone;//身份证正面
    private String orgcodeimg;//组织机构代码图片
    private String busregimg;//营业执照图片
    private String documenttype;//证件类型（0普通营业执照，1多证合一营业执照）
    private String validterm;//有效期限
    private String term;//长期（0，1长期）
    public  String industryid = "";  //行业类型id
    private String merlogourl;//二维码
    private String entabb; //企业简称

    private String balancecardone;  //结算卡正面
    private String balancecardtwo; //结算卡反面
    private String gatepic; //门头照
    private String openlicense; //开户许可证
    private String handidcardpic; //手持身份证照片

    private String enterpraiseLicenseTerm; //营业执照有效期
    private String licenseTerm;  //长期（0，1长期）

    private String checkStandPic; //前台照片/收银台照片

    private String storePic; //店内照

    public String getBalancecardone() {
        return balancecardone;
    }

    public void setBalancecardone(String balancecardone) {
        this.balancecardone = balancecardone;
    }

    public String getBalancecardtwo() {
        return balancecardtwo;
    }

    public void setBalancecardtwo(String balancecardtwo) {
        this.balancecardtwo = balancecardtwo;
    }

    public String getGatepic() {
        return gatepic;
    }

    public void setGatepic(String gatepic) {
        this.gatepic = gatepic;
    }

    public String getOpenlicense() {
        return openlicense;
    }

    public void setOpenlicense(String openlicense) {
        this.openlicense = openlicense;
    }

    public String getHandidcardpic() {
        return handidcardpic;
    }

    public void setHandidcardpic(String handidcardpic) {
        this.handidcardpic = handidcardpic;
    }


    public String getEntabb() {
        return entabb;
    }

    public void setEntabb(String entabb) {
        this.entabb = entabb;
    }

    public String getMerlogourl() {
        return merlogourl;
    }

    public void setMerlogourl(String merlogourl) {
        this.merlogourl = merlogourl;
    }

    public String getIndustryid() {
        return industryid;
    }

    public void setIndustryid(String industryid) {
        this.industryid = industryid;
    }

    public String getEntname() {
        return entname;
    }

    public void setEntname(String entname) {
        this.entname = entname;
    }

    public String getIdcardtwo() {
        return idcardtwo;
    }

    public void setIdcardtwo(String idcardtwo) {
        this.idcardtwo = idcardtwo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdcardone() {
        return idcardone;
    }

    public void setIdcardone(String idcardone) {
        this.idcardone = idcardone;
    }

    public String getOrgcodeimg() {
        return orgcodeimg;
    }

    public void setOrgcodeimg(String orgcodeimg) {
        this.orgcodeimg = orgcodeimg;
    }

    public String getBusregimg() {
        return busregimg;
    }

    public void setBusregimg(String busregimg) {
        this.busregimg = busregimg;
    }

    public String getDocumenttype() {
        return documenttype;
    }

    public void setDocumenttype(String documenttype) {
        this.documenttype = documenttype;
    }

    public String getValidterm() {
        return validterm;
    }

    public void setValidterm(String validterm) {
        this.validterm = validterm;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getBusregnum() {
        return busregnum;
    }

    public void setBusregnum(String busregnum) {
        this.busregnum = busregnum;
    }

    public String getIdcardType() {
        return idcardType;
    }

    public void setIdcardType(String idcardType) {
        this.idcardType = idcardType;
    }

    public String getEnterpraiseLicenseTerm() {
        return enterpraiseLicenseTerm;
    }

    public void setEnterpraiseLicenseTerm(String enterpraiseLicenseTerm) {
        this.enterpraiseLicenseTerm = enterpraiseLicenseTerm;
    }

    public String getLicenseTerm() {
        return licenseTerm;
    }

    public void setLicenseTerm(String licenseTerm) {
        this.licenseTerm = licenseTerm;
    }

    public String getCheckStandPic() {
        return checkStandPic;
    }

    public void setCheckStandPic(String checkStandPic) {
        this.checkStandPic = checkStandPic;
    }

    public String getStorePic() {
        return storePic;
    }

    public void setStorePic(String storePic) {
        this.storePic = storePic;
    }

    //    @Override
//    public String toString() {
//        return "EnterpriseInf [entname=" + entname + ", idcardtwo=" + idcardtwo + ", name=" + name + ", idcard="
//                + idcard + ", idcardone=" + idcardone + ", orgcodeimg=" + orgcodeimg + ", busregimg=" + busregimg
//                + ", documenttype=" + documenttype + ", validterm=" + validterm + ", term=" + term + ", industryid="
//                + industryid + ", merlogourl=" + merlogourl + ", entabb=" + entabb + "]";
//    }


    @Override
    public String toString() {
        return "EnterpriseInf{" +
                "entname='" + entname + '\'' +
                ", orgcode='" + orgcode + '\'' +
                ", busregnum='" + busregnum + '\'' +
                ", idcardtwo='" + idcardtwo + '\'' +
                ", name='" + name + '\'' +
                ", idcard='" + idcard + '\'' +
                ", idcardType='" + idcardType + '\'' +
                ", idcardone='" + idcardone + '\'' +
                ", orgcodeimg='" + orgcodeimg + '\'' +
                ", busregimg='" + busregimg + '\'' +
                ", documenttype='" + documenttype + '\'' +
                ", validterm='" + validterm + '\'' +
                ", term='" + term + '\'' +
                ", industryid='" + industryid + '\'' +
                ", merlogourl='" + merlogourl + '\'' +
                ", entabb='" + entabb + '\'' +
                ", balancecardone='" + balancecardone + '\'' +
                ", balancecardtwo='" + balancecardtwo + '\'' +
                ", gatepic='" + gatepic + '\'' +
                ", openlicense='" + openlicense + '\'' +
                ", handidcardpic='" + handidcardpic + '\'' +
                ", enterpraiseLicenseTerm='" + enterpraiseLicenseTerm + '\'' +
                ", licenseTerm='" + licenseTerm + '\'' +
                '}';
    }
}