package manage.vo;

import javax.persistence.Id;

public class Receive {
    @Id
    private int id;
    private int userId;
    private int messageId;
    /**
     * 表示该消息是否已读，true代表是，false代表否
     */
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}