package com.chandra.jms;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.log4j.Logger;

@WebListener
public class JMSContextListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(JMSContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // TODO Auto-generated method stub
        JMSMQListener mqListener = new JMSMQListener();

        try {
            mqListener.init();
        } catch (Exception e) {
            LOGGER.error("JMSMQListener init() Exception  " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("JMSContextListener destroyed");
    }

}


