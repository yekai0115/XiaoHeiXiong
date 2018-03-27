package td.com.xiaoheixiong.beans.HeadLineDetal;

/**
 * Created by 11832 on 2018/3/22.
 */

public class CommentBean {

    private String id;
    private String headline_id;
    private String mer_id;
    private String comments;
    private String add_time;
    private String reply_id;
    private String headImg;
    private String nickName;
    private String headlineReplyVO;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setHeadline_id(String headline_id) {
        this.headline_id = headline_id;
    }
    public String getHeadline_id() {
        return headline_id;
    }

    public void setMer_id(String mer_id) {
        this.mer_id = mer_id;
    }
    public String getMer_id() {
        return mer_id;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getComments() {
        return comments;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
    public String getAdd_time() {
        return add_time;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }
    public String getReply_id() {
        return reply_id;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
    public String getHeadImg() {
        return headImg;
    }

    public void setHeadlineReplyVO(String headlineReplyVO) {
        this.headlineReplyVO = headlineReplyVO;
    }
    public String getHeadlineReplyVO() {
        return headlineReplyVO;
    }

    public String getNickName() {
        return nickName;
    }
}
