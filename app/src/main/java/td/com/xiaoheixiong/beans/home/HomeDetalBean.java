/**
  * Copyright 2018 bejson.com 
  */
package td.com.xiaoheixiong.beans.home;
import java.util.List;

/**
 * 生活圈数据对象
 */
public class HomeDetalBean {

    private String RSPCOD;
    private List<GroupMmerMarkList> groupMmerMarkList;
    private List<Detail> detail;
    private List<MerMarkList> merMarkList;
    private List<Catas> catas;
    private String RSPMSG;
    private List<Adlist> adlist;
    public void setRSPCOD(String RSPCOD) {
         this.RSPCOD = RSPCOD;
     }
     public String getRSPCOD() {
         return RSPCOD;
     }

    public void setGroupMmerMarkList(List<GroupMmerMarkList> groupMmerMarkList) {
         this.groupMmerMarkList = groupMmerMarkList;
     }
     public List<GroupMmerMarkList> getGroupMmerMarkList() {
         return groupMmerMarkList;
     }

    public void setDetail(List<Detail> detail) {
         this.detail = detail;
     }
     public List<Detail> getDetail() {
         return detail;
     }

    public void setMerMarkList(List<MerMarkList> merMarkList) {
         this.merMarkList = merMarkList;
     }
     public List<MerMarkList> getMerMarkList() {
         return merMarkList;
     }

    public void setCatas(List<Catas> catas) {
         this.catas = catas;
     }
     public List<Catas> getCatas() {
         return catas;
     }

    public void setRSPMSG(String RSPMSG) {
         this.RSPMSG = RSPMSG;
     }
     public String getRSPMSG() {
         return RSPMSG;
     }

    public void setAdlist(List<Adlist> adlist) {
         this.adlist = adlist;
     }
     public List<Adlist> getAdlist() {
         return adlist;
     }

}