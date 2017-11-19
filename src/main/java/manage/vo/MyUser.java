package manage.vo;

/**
 *
 * @author m
 * @date 17-5-13
 */
 public class MyUser {
    private int id;
    private String name;
    private String role;
    private String messageList;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessageList() {
        return messageList;
    }

    public void setMessageList(String messageList) {
        this.messageList = messageList;
    }
}
