package com.mfe.mfefilemgr;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.mfe.mfefilemgr.api.DirApiServiceImpl;
import com.mfe.mfefilemgr.api.FileApiServiceImpl;
import com.mfe.mfefilemgr.constants.ConfLoader;
import com.mfe.mfefilemgr.constants.ConfLoaderException;
import com.mfe.mfefilemgr.constants.ConfigKey;
import com.mfe.mfefilemgr.utils.ClassThief;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by chenmin on 2017/3/30.
 */
public class Main {
    private static Logger log;
    public static void main(String[] args) throws Exception{

        loadConf(args);

        confLog();
        log = LogManager.getLogger(Main.class);

        configProxy();

        registerMfefilemgrServerImpl();
        ////////////////////////
        //swagger context
        String[] packages = new String[] {
                "com.mfe.mfefilemgr.restful.mfefilemgrserver.api"};

        ResourceConfig config = new ResourceConfig().packages(packages).register(JacksonFeature.class)
                .register(AccessControlFilter.class);

        ServletHolder swagger_servlet = new ServletHolder(new ServletContainer(config));


        ServletContextHandler swaggerContext = new ServletContextHandler();
        swaggerContext.setContextPath("/mfefilemgr/*");
        swaggerContext.addServlet(swagger_servlet, "/*");
        ////////////////////////
        //webapp context
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setDescriptor("webapp" + "/WEB-INF/web.xml");

        URL webAppDir = Thread.currentThread().getContextClassLoader().getResource("webapp");
        if (webAppDir == null) {
            throw new RuntimeException(String.format("No %s directory was found into the JAR file", "webapp"));
        }
        try {
            //log.debug("webAppDir: "+webAppDir.toString());
            //log.debug("webAppContext.setResourceBase: "+webAppDir.toURI().toString());
            webAppContext.setResourceBase(webAppDir.toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {swaggerContext, webAppContext});

        ////////////////////////

        final int port = ConfLoader.getInstance().getInt(ConfigKey.server_port);
        final Server server = new Server(port);

        //add https
        HttpConfiguration https_config = new HttpConfiguration();
        https_config.setSecureScheme("https");

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath("keystore.jks");
        // 私钥
        sslContextFactory.setKeyStorePassword("OBF:1apw1bpb1a4719ja1bb11bb31bb519iw1a4n1bpb1apm");
        // 公钥
        sslContextFactory.setKeyManagerPassword("OBF:1apw1bpb1a4719ja1bb11bb31bb519iw1a4n1bpb1apm");

        ServerConnector httpsConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory,"http/1.1"),
                new HttpConnectionFactory(https_config));
        // 设置访问端口
        httpsConnector.setPort(443);
        httpsConnector.setIdleTimeout(30000);
        server.addConnector(httpsConnector);
        ///////////////////////////////////
        server.setHandler(handlers);


        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    try {
                        server.start();
                        log.info("Mfefilemgr server started@" + port);
                        server.join();
                    } catch (Exception e) {
                        log.error("Mfefilemgr server start@" + port + " failed", e);
                        shutDown();
                    }
                } finally {
                    log.error("Mfefilemgr server exit@" + port + " failed");
                    server.destroy();
                }

            }
        }).start();
    }

    private static void confLog() throws Exception{
        String log_conf_file=ConfLoader.getInstance().getConf(ConfigKey.log_conf_file, null);
        if((new File(log_conf_file).canRead())){
            System.setProperty("log4j.configurationFile", log_conf_file);
            LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
            loggerContext.reconfigure();
        }else{
            LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
            loggerContext.getConfiguration().getRootLogger().setLevel(ConfLoader.getInstance().getBoolean(ConfigKey.debug) ? Level.DEBUG : Level.INFO);
        }
    }

    private static void loadConf(String[] args) {
        parseCommandLine(args);
        if(!ConfLoader.getInstance().getConf(ConfigKey.conf_file, "").isEmpty()){
            try {
                ConfLoader.getInstance().loadConf(ConfLoader.getInstance().getConf(ConfigKey.conf_file, ""));
            } catch (ConfLoaderException e) {
                e.printStackTrace();
            }
        }
    }

    private static void parseCommandLine(String[] _args){

        class Args{
            @Parameter(names={"-"+ConfigKey.server_port}, order = 0, description="Http server port")
            private int server_port=ConfigKey.default_server_port;

            @Parameter(names={"-"+ConfigKey.conf_file}, order = 1, description = "Configuation file")
            private String conf_file="";

            @Parameter(names={"-"+ConfigKey.log_conf_file}, order = 2, description ="Log4j2 configuration file")
            private String log_conf_file="";

            @Parameter(names={"-"+ConfigKey.proxy_host}, order = 3, description="Http proxy host")
            private String proxy_host="";

            @Parameter(names={"-"+ConfigKey.proxy_port}, order = 4, description = "Http proxy port")
            private int proxy_port=0;

            @Parameter(names={"-"+ConfigKey.debug}, order = 5, description = "Debug mode (only in case log_conf_file is not available)")
            private boolean debug=false;

            @Parameter(names={"-help"}, order = 6, help=true, description = "Show this help")
            private boolean help=false;
        }
        Args args=new Args();
        JCommander jc=new JCommander(args, _args);
        if(args.help){
            jc.usage();
            System.exit(0);
        }

        ConfLoader.getInstance().setInt(ConfigKey.server_port, args.server_port);
        ConfLoader.getInstance().setConf(ConfigKey.conf_file, args.conf_file);
        ConfLoader.getInstance().setConf(ConfigKey.log_conf_file, args.log_conf_file);
        ConfLoader.getInstance().setConf(ConfigKey.proxy_host, args.proxy_host);
        ConfLoader.getInstance().setInt(ConfigKey.proxy_port, args.proxy_port);
        ConfLoader.getInstance().setBoolean(ConfigKey.debug, args.debug);

    }
    private static void shutDown() {
        log.info("Shutdown.");
        System.exit(1);
    }


    private static void configProxy() throws Exception{
        String host = ConfLoader.getInstance().getConf(ConfigKey.proxy_host);
        int port = ConfLoader.getInstance().getInt(ConfigKey.proxy_port);
        if(null!=host && !host.isEmpty() && 0!=port){
            log.info("http proxy enabled: "+host+':'+port);
            System.setProperty("http.proxySet", "true");
            System.setProperty("http.proxyHost", host);
            System.setProperty("http.proxyPort", ""+port);
        }
    }

    private static void registerMfefilemgrServerImpl() {
        try {
            ClassThief.setFinalStatic("com.mfe.mfefilemgr.restful.mfefilemgrserver.api.factories.FileApiServiceFactory",
                    "service", new FileApiServiceImpl());
            ClassThief.setFinalStatic("com.mfe.mfefilemgr.restful.mfefilemgrserver.api.factories.DirApiServiceFactory",
                    "service", new DirApiServiceImpl());
        } catch (Exception e) {
            log.error("registerMfefilemgrServerImpl", e);
            shutDown();
        }
    }


}
