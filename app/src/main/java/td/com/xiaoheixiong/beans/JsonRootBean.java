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

    private Detail detail;
    private String RSPCOD;
    public void setDetail(Detail detail) {
         this.detail = detail;
     }
     public Detail getDetail() {
         return detail;
     }

    public void setRSPCOD(String RSPCOD) {
         this.RSPCOD = RSPCOD;
     }
     public String getRSPCOD() {
         return RSPCOD;
     }

}