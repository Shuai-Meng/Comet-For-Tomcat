package comet;

import manage.vo.MyMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class MyMessageListener implements MessageListener{
    @Autowired
    private MessageSender messageSender;

    @Override public void onMessage(Message message) {
        MyMessage myMessage = null;
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            myMessage = (MyMessage) objectMessage.getObject();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        messageSender.sendMessage(myMessage);
    }
}
