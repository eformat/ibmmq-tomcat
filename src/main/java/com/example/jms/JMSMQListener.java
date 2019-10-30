package com.example.jms;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class JMSMQListener implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(JMSMQListener.class);
    private QueueConnectionFactory qConnFactory = null;
    private QueueConnection queueConnection = null;
    private QueueSession queueSession = null;
    private Queue queue = null;
    private Context ctx = null;
    private MessageConsumer messageConsumer = null;

    static {
        LOGGER.debug("static initialiser called");
    }

    public void init() throws Exception {
        try {
            ctx = (Context) new InitialContext().lookup("java:comp/env");
            qConnFactory = (QueueConnectionFactory) ctx.lookup("jms/MyQCF");
            LOGGER.debug("qConnFactory: " + qConnFactory);
            queueConnection = qConnFactory.createQueueConnection();
            LOGGER.debug("queueConnection: " + queueConnection);
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = (Queue) ctx.lookup("jms/MyQ");
            messageConsumer = queueSession.createConsumer(queue);
            queueConnection.start();
            messageConsumer.setMessageListener(this);
        } catch (NamingException ne) {
            LOGGER.error("JMS Message init() NamingException  " + ne.getMessage());
            throw ne;
        } catch (JMSException e) {
            LOGGER.error("JMS Message init() Exception  " + e.getMessage());
            throw e;
        }
    }

    public void onMessage(Message message) {
        String msgStr = null;

        try {
            LOGGER.info("onMessage start ");
            if (message instanceof TextMessage) {
                msgStr = ((TextMessage) message).getText();

            } else if (message instanceof BytesMessage) {
                LOGGER.debug("BytesMessage... ");
                // Read message in bytes
                BytesMessage byteMessage = (BytesMessage) message;
                byte[] byteArr = new byte[(int) byteMessage.getBodyLength()];
                for (int i = 0; i < (int) byteMessage.getBodyLength(); i++) {
                    byteArr[i] = byteMessage.readByte();
                }
                msgStr = new String(byteArr);
            }
            LOGGER.info("JMS Message received successfully: " + msgStr);
        } catch (Exception e) {
            LOGGER.error("JMS Message Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
