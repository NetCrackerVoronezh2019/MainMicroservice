package Models;

import java.util.List;

public class AttachmentsFromDialogModel {
    private Integer chatId;
    private List<FileModel> allFiles;
    public Integer getChatId() {
        return chatId;
    }
    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
    public List<FileModel> getAllFiles() {
        return allFiles;
    }
    public void setAllFiles(List<FileModel> allFiles) {
        this.allFiles = allFiles;
    }
}
