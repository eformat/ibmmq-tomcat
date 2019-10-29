package com.example.jms;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 * The Class JMSMQSender.
 */
public class JMSMQSender {

    private static final Logger LOGGER = Logger.getLogger(JMSMQSender.class);
    private QueueConnectionFactory qConnFactory;
    private QueueConnection queueConnection;
    private QueueSession queueSession;
    private QueueSender queueSender;
    private Queue queue;
    private Context ctx = null;
    private TextMessage textMessage;

    public void sendMessage(String text, String jndiName) throws NamingException, JMSException, Exception {

        try {

            ctx = new InitialContext();
            qConnFactory = (QueueConnectionFactory) ctx.lookup("jms/MyQCF");
            LOGGER.debug("qConnFactory: " + qConnFactory);
            queueConnection = qConnFactory.createQueueConnection();
            LOGGER.debug("queueConnection: " + queueConnection);
            queueSession = queueConnection.createQueueSession(false, -1);
            queue = (Queue) ctx.lookup("jms/MyQ");
            LOGGER.debug("queue: " + queue.getQueueName());
            queueSender = queueSession.createSender(queue);
            queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            queueConnection.start();
            textMessage = queueSession.createTextMessage();
            textMessage.setText(text);
            LOGGER.debug("Sending the following message: " + textMessage.getText());
            queueSender.send(textMessage);
            LOGGER.debug("After the following message: " + textMessage.getText());

        } catch (NamingException ne) {
            LOGGER.error("sendMessage: NamingException  " + ne.getMessage());
            throw ne;
        } catch (Exception e) {
            LOGGER.error("sendMessage: Exception  " + e.getMessage());
            throw e;
        } finally {
            if (null != queueSession) {
                queueSession.close();
            }
            if (null != queueConnection) {
                queueConnection.close();
            }
        }
    }
}
