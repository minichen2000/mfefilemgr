package com.mfe.qnmgr;

import com.mfe.qnmgr.api.DirApiServiceImpl;
import com.mfe.qnmgr.api.FileApiServiceImpl;
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
 * Created by chenmin on 2017/3/30.
 */
public class Main {
    private static Logger log;
    public static void main(String[] args){

        String confPath = loadConf();

        System.setProperty("log4j.configurationFile", confPath + "/qnmgr.log4j2.xml");
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        loggerContext.reconfigure();

        log = LogManager.getLogger(Main.class);

        configProxy();

        registerQnmgrServerImpl();



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
            ClassThief.setFinalStatic("com.mfe.qnmgr.restful.qnmgrserver.api.factories.FileApiServiceFactory",
                    "service", new FileApiServiceImpl());
            ClassThief.setFinalStatic("com.mfe.qnmgr.restful.qnmgrserver.api.factories.DirApiServiceFactory",
                    "service", new DirApiServiceImpl());
        } catch (Exception e) {
            log.error("registerQnmgrServerImpl", e);
            shutDown();
        }
    }

    private static void configProxy(){
        String host = ConfLoader.getInstance().getConf(ConfigKey.HTTP_PROXYHOST, null);
        int port = ConfLoader.getInstance().getInt(ConfigKey.HTTP_PROXYPORT, 0);
        if(null!=host && 0!=port){
            log.info("http proxy enabled: "+host+':'+port);
            System.setProperty("http.proxySet", "true");
            System.setProperty("http.proxyHost", host);
            System.setProperty("http.proxyPort", ""+port);
        }

    }
}
