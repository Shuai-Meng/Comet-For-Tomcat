package manage.vo;

import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Created by m on 17-5-21.
 */
public class MessageType {
    @Id
    private int    id;
    private String name;
    @Transient
    private byte status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}
