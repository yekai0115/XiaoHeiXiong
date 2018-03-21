/**
  * Copyright 2018 bejson.com 
  */
package td.com.xiaoheixiong.beans;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2018-03-02 17:15:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TouTiaoBean implements Serializable {

    private int id;
    private String createBy;
    private long createDate;
    private String updateBy;
    private long updateDate;
    private boolean delFlag;
    private String mercId;
    private String mercName;
    private String description;
    private String images;
    private int basePraise;
    private int realPraise;
    private int status;
    private String lngLat;
    private String locationDesc;
    private int noPraise;
    private List<String> imageList;
    private String mercImg;
    private String identityDesc;
    private String publishTime;
    private String statusName;
    private String firstImg;

    public boolean isShowAll = false;


    public int getId() {
        return id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public long getCreateDate() {
        return createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public boolean isDelFlag() {
        return delFlag;
    }

    public String getMercId() {
        return mercId;
    }

    public String getMercName() {
        return mercName;
    }

    public String getDescription() {
        return description;
    }

    public String getImages() {
        return images;
    }

    public int getBasePraise() {
        return basePraise;
    }

    public int getRealPraise() {
        return realPraise;
    }

    public int getStatus() {
        return status;
    }

    public String getLngLat() {
        return lngLat;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public int getNoPraise() {
        return noPraise;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public String getMercImg() {
        return mercImg;
    }

    public String getIdentityDesc() {
        return identityDesc;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getFirstImg() {
        return firstImg;
    }
}