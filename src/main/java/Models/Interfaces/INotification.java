package Models.Interfaces;
import Models.Enums.NotificationType;

public interface INotification {
    public NotificationType getNotificationType();
    public String getUserName();
}
