/**
 * Copyright 2018 bejson.com
 */
package td.com.xiaoheixiong.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2018-03-02 17:15:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Detail implements Serializable {


    private int totalPage;//总页数
    private int currentPage;//当前页数
    private int size;//页数大小

    private List<TouTiaoBean> lists;


    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getSize() {
        return size;
    }

    public List<TouTiaoBean> getLists() {
        return lists;
    }




}