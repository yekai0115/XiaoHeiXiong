package td.com.xiaoheixiong.beans;

import java.io.Serializable;

/**
 * 网络返回基类 支持泛型
 *
 * @param <T>
 * @author yg
 */
public class BaseResponse<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6453824795394683688L;

    private String RSPCOD;
    private T RSPDATA;
    private String RSPMSG;

    public String getRSPCOD() {
        return RSPCOD;
    }

    public void setRSPCOD(String RSPCOD) {
        this.RSPCOD = RSPCOD;
    }

    public T getRSPDATA() {
        return RSPDATA;
    }

    public void setRSPDATA(T RSPDATA) {
        this.RSPDATA = RSPDATA;
    }

    public String getRSPMSG() {
        return RSPMSG;
    }

    public void setRSPMSG(String RSPMSG) {
        this.RSPMSG = RSPMSG;
    }
}
