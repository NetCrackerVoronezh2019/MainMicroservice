package Models;

public class FriendshipNotifications {
    private Integer notificationId;
    private Long outgoingId;
    private String outgoingName;

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Long getOutgoingId() {
        return outgoingId;
    }

    public void setOutgoingId(Long outgoingId) {
        this.outgoingId = outgoingId;
    }

    public String getOutgoingName() {
        return outgoingName;
    }

    public void setOutgoingName(String outgoingName) {
        this.outgoingName = outgoingName;
    }
}
