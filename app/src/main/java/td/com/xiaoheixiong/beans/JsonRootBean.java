/**
  * Copyright 2018 bejson.com 
  */
package td.com.xiaoheixiong.beans;

import java.io.Serializable;

/**
 * Auto-generated: 2018-03-02 17:15:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean implements Serializable{

    private Detail RSPDATA;
    private String RSPCOD;
    private String RSPMSG;
    public void setDetail(Detail detail) {
         this.RSPDATA = detail;
     }
     public Detail getDetail() {
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