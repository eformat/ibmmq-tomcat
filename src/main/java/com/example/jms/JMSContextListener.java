package com.example.jms;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

@WebListener
public class JMSContextListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(JMSContextListener.class);

    public void contextInitialized(ServletContextEvent sce) {
        // TODO Auto-generated method stub
        JMSMQListener mqListener = new JMSMQListener();

        try {
            mqListener.init();
        } catch (Exception e) {
            LOGGER.error("JMSMQListener init() Exception  " + e.getMessage());
        }
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("JMSContextListener destroyed");
    }

}


