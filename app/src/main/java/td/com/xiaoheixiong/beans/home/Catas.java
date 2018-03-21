/**
  * Copyright 2018 bejson.com 
  */
package td.com.xiaoheixiong.beans.home;

/**
 * 分类对象
 */
public class Catas {

    private int id;
    private String iconBigIos;
    private String iconMidIos;
    private String iconBigAndroid;
    private String iconMidAndroid;
    private String subCataName;
    private long createTime;
    private long updateTime;
    private String createBy;
    private int state;
    private String cataType;
    private int sort;

    private int resourceId;//制造本地资源icon


    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

    public void setIconBigIos(String iconBigIos) {
         this.iconBigIos = iconBigIos;
     }
     public String getIconBigIos() {
         return iconBigIos;
     }

    public void setIconMidIos(String iconMidIos) {
         this.iconMidIos = iconMidIos;
     }
     public String getIconMidIos() {
         return iconMidIos;
     }

    public void setIconBigAndroid(String iconBigAndroid) {
         this.iconBigAndroid = iconBigAndroid;
     }
     public String getIconBigAndroid() {
         return iconBigAndroid;
     }

    public void setIconMidAndroid(String iconMidAndroid) {
         this.iconMidAndroid = iconMidAndroid;
     }
     public String getIconMidAndroid() {
         return iconMidAndroid;
     }

    public void setSubCataName(String subCataName) {
         this.subCataName = subCataName;
     }
     public String getSubCataName() {
         return subCataName;
     }

    public void setCreateTime(long createTime) {
         this.createTime = createTime;
     }
     public long getCreateTime() {
         return createTime;
     }

    public void setUpdateTime(long updateTime) {
         this.updateTime = updateTime;
     }
     public long getUpdateTime() {
         return updateTime;
     }

    public void setCreateBy(String createBy) {
         this.createBy = createBy;
     }
     public String getCreateBy() {
         return createBy;
     }

    public void setState(int state) {
         this.state = state;
     }
     public int getState() {
         return state;
     }

    public void setCataType(String cataType) {
         this.cataType = cataType;
     }
     public String getCataType() {
         return cataType;
     }

    public void setSort(int sort) {
         this.sort = sort;
     }
     public int getSort() {
         return sort;
     }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}