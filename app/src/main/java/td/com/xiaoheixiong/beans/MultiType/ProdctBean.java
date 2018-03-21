package td.com.xiaoheixiong.beans.MultiType;

import java.io.Serializable;

public class ProdctBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String Imgurl;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return Imgurl;
    }

    public void setImgurl(String imgurl) {
        Imgurl = imgurl;
    }

    public ProdctBean(String name, String url, long id) {
        super();
        this.name = name;
        this.Imgurl = url;
        this.id = id;
    }

}
