/**
  * Copyright 2018 bejson.com 
  */
package td.com.xiaoheixiong.beans.TuanTuan;
import java.util.List;



/**
 * 团团券对象
 */
public class TTDetalBean {

    private String RSPCOD;
    private String RSPMSG;
    private List<TTBean> RSPDATA;

    public String getRSPCOD() {
        return RSPCOD;
    }

    public String getRSPMSG() {
        return RSPMSG;
    }

    public List<TTBean> getRSPDATA() {
        return RSPDATA;
    }
}