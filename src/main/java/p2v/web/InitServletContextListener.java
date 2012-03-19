package p2v.web;

import p2v.jpa.JpaUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitServletContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        JpaUtil.init();
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
