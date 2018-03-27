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


    private String id;
    private String create_by;
    private String create_time;
    private String update_date;
    private String del_flag;
    private String merc_id;
    private String merc_name;
    private String description;
    private String images;
    private String headImg;
    private String replyNum;//    回复数量
    private String  forwardNum;//转发数量
    private String  nickName;
    private String base_praise;
    private String real_praise;
    private String status;
    private String location_desc;
    private String lng;
    private String lat;

    public boolean isShowAll = false;

    public boolean isPraise = false;


    public String getId() {
        return id;
    }

    public String getCreate_by() {
        return create_by;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public String getDel_flag() {
        return del_flag;
    }

    public String getMerc_id() {
        return merc_id;
    }

    public String getMerc_name() {
        return merc_name;
    }

    public String getDescription() {
        return description;
    }

    public String getImages() {
        return images;
    }

    public String getBase_praise() {
        return base_praise;
    }

    public String getReal_praise() {
        return real_praise;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation_desc() {
        return location_desc;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }

    public boolean isShowAll() {
        return isShowAll;
    }

    public String getHeadImg() {
        return headImg;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public String getForwardNum() {
        return forwardNum;
    }

    public String getNickName() {
        return nickName;
    }


    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    public void setReal_praise(String real_praise) {
        this.real_praise = real_praise;
    }
}