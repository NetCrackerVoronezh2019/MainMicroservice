package Models;

public class GroupModel {
    private Long groupId;
    private String name;
    private Long dialogId;
    private Long creatorId;
    private String subjectName;
    private String image;
    private boolean isNotificationsOn;
    private Integer countNot;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isNotificationsOn() {
        return isNotificationsOn;
    }

    public Integer getCountNot() {
        return countNot;
    }

    public void setCountNot(Integer countNot) {
        this.countNot = countNot;
    }

    public void setNotificationsOn(boolean notificationsOn) {
        isNotificationsOn = notificationsOn;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDialogId() {
        return dialogId;
    }

    public void setDialogId(Long dialogId) {
        this.dialogId = dialogId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}