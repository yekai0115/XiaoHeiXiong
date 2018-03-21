/**
  * Copyright 2018 bejson.com 
  */
package td.com.xiaoheixiong.beans.home;

/**
 *轮播图片对象
 */
public class Adlist {

    private int id;
    private String position;
    private String title;
    private String imgUrl;
    private String content;
    private int status;
    private long createTime;
    private String createBy;
    private int adType;
    private String linkUrl;
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    public String getPosition() {
        return position;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getImgUrl() {
        return imgUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }
    public int getAdType() {
        return adType;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public String getLinkUrl() {
        return linkUrl;
    }

}