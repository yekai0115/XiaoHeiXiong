/**
  * Copyright 2018 bejson.com 
  */
package td.com.xiaoheixiong.beans.HeadLineDetal;

import java.io.Serializable;
import java.util.List;

import td.com.xiaoheixiong.beans.Detail;

/**
 * Auto-generated: 2018-03-02 17:15:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CommentBase implements Serializable{

    private List<CommentBean> RSPDATA;
    private String RSPCOD;
    private String RSPMSG;

    public List<CommentBean> getRSPDATA() {
        return RSPDATA;
    }

    public void setRSPCOD(String RSPCOD) {
         this.RSPCOD = RSPCOD;
     }
     public String getRSPCOD() {
         return RSPCOD;
     }

    public String getRSPMSG() {
        return RSPMSG;
    }
}