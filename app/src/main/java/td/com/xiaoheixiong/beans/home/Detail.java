/**
  * Copyright 2018 bejson.com 
  */
package td.com.xiaoheixiong.beans.home;

/**
 *优优商家对象
 */
public class Detail {

    private String infos;
    private String belongPlateName;
    private int belongPlateId;
    private String merInfos;
    private PageInfo pageInfo;
    public void setInfos(String infos) {
         this.infos = infos;
     }
     public String getInfos() {
         return infos;
     }

    public void setBelongPlateName(String belongPlateName) {
         this.belongPlateName = belongPlateName;
     }
     public String getBelongPlateName() {
         return belongPlateName;
     }

    public void setBelongPlateId(int belongPlateId) {
         this.belongPlateId = belongPlateId;
     }
     public int getBelongPlateId() {
         return belongPlateId;
     }

    public void setMerInfos(String merInfos) {
         this.merInfos = merInfos;
     }
     public String getMerInfos() {
         return merInfos;
     }

    public void setPageInfo(PageInfo pageInfo) {
         this.pageInfo = pageInfo;
     }
     public PageInfo getPageInfo() {
         return pageInfo;
     }

}