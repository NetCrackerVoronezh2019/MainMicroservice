package Models;

import java.util.Date;

public class CommentModel {
    private Long commentId;
    private String text;
    private Long postId;
    private UserAndGroupUserModel sender;
    private Date date;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public UserAndGroupUserModel getSender() {
        return sender;
    }

    public void setSender(UserAndGroupUserModel sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
