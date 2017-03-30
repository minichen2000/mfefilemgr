package com.mfe.qnmgr;

import com.mfe.qnmgr.apiImpls.BucketsApiServiceImpl;
import com.mfe.qnmgr.constants.ConfigKey;
import com.mfe.qnmgr.exception.QnMgrException;
import com.mfe.qnmgr.utils.ClassThief;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Created by junyuel on 2017/3/30.
 */
public class Main {
    private static Logger log;
    public static void main(String[] args){

        registerQnmgrServerImpl();

        String confPath = loadConf();

        System.setProperty("log4j.configurationFile", confPath + "/qnmgr.log4j2.xml");
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        loggerContext.reconfigure();

        log = LogManager.getLogger(Main.class);

        String[] packages = new String[] {
                "com.mfe.qnmgr.restful.qnmgrserver.api"};

        ResourceConfig config = new ResourceConfig().packages(packages).register(JacksonFeature.class)
                .register(AccessControlFilter.class);

        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        int port = ConfLoader.getInstance().getInt(ConfigKey.QNMGR_PORT, ConfigKey.DEFAULT_QNMGR_PORT);
        final Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(server, "/qnmgr/*");
        context.addServlet(servlet, "/*");
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    try {
                        server.start();
                        log.info("Qnmgr server started@" + port);
                        server.join();
                    } catch (Exception e) {
                        log.error("Qnmgr server start@" + port + " failed", e);
                        shutDown();
                    }
                } finally {
                    log.error("Adapter server exit@" + port + " failed");
                    server.destroy();
                }

            }
        }).start();
    }

    private static String loadConf() {
        String confPath = System.getProperty("QNMGR_CONFPATH");
        if (null == confPath) {
            confPath = System.getenv("QNMGR_CONFPATH");
        }
        if (null == confPath) {
            confPath = System.getProperty("user.home");
        }

        try {
            ConfLoader.getInstance().loadConf(confPath + "/qnmgr.conf.properties");
        } catch (QnMgrException e) {
            e.printStackTrace();
            shutDown();
        }

        return confPath;
    }
    private static void shutDown() {
        log.info("Shutdown.");
        System.exit(1);
    }

    private static void registerQnmgrServerImpl() {
        try {
            ClassThief.setFinalStatic("com.mfe.qnmgr.restful.qnmgrserver.api.factories.BucketsApiServiceFactory",
                    "service", new BucketsApiServiceImpl());
        } catch (Exception e) {
            log.error("registerQnmgrServerImpl", e);
            shutDown();
        }
    }
}
