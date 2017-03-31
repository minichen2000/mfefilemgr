package com.mfe.qnmgr.utils.qn;

import com.mfe.qnmgr.ConfLoader;
import com.mfe.qnmgr.business.QnResMgr;
import com.mfe.qnmgr.constants.ConfigKey;
import com.mfe.qnmgr.exception.QnMgrException;
import com.mfe.qnmgr.restful.model.qnmgr.QnDirInfo;
import com.mfe.qnmgr.restful.model.qnmgr.QnFile;
import com.mfe.qnmgr.restful.model.qnmgr.QnmgrErrorInfo;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import okio.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by minichen on 2017/3/30.
 */
public class Utils {
    private final static Logger log = LogManager.getLogger(Utils.class);
    static public String getAK(){
        return ConfLoader.getInstance().getConf(ConfigKey.AK, "");
    }

    static public String getSK(){
        return ConfLoader.getInstance().getConf(ConfigKey.SK, "");
    }

    static public Zone getZone(String zone){
        if(zone.toLowerCase().equals("zone0")){
            return Zone.zone0();
        }else if(zone.toLowerCase().equals("zone1")){
            return Zone.zone1();
        }else if(zone.toLowerCase().equals("zone2")){
            return Zone.zone2();
        }else if(zone.toLowerCase().equals("zonena0")){
            return Zone.zoneNa0();
        }else {
            return Zone.autoZone();
        }
    }

    static public QnFile toQnFile(FileInfo fileInfo){
        QnFile rlt=null;
        if(null!=fileInfo){
            rlt=new QnFile();
            rlt.setEndUser(fileInfo.endUser);
            rlt.setFsize(fileInfo.fsize);
            rlt.setHash(fileInfo.hash);
            rlt.setKey(fileInfo.key);
            rlt.setMimeType(fileInfo.mimeType);
            rlt.setPutTime(fileInfo.putTime);
        }
        return rlt;
    }

    static public QnMgrException buildQnMgrExceptionFileNotFound(){
        return new QnMgrException(612, "no such file or directory");
    }


    static public QnMgrException toQnMgrException(Exception e){
        if(null==e) return null;
        //log.debug("toQnMgrException: \n"+e.toString());
        if(e instanceof QiniuException){
            QiniuException qe=(QiniuException)e;
            //return new QnMgrException(qe.code(), qe.getLocalizedMessage()+", "+qe.error()+(null==qe.response ? "" : "resp code: "+qe.response.statusCode+", resp error: "+qe.response.error));
            return new QnMgrException(qe.code(), qe.error());
        }else if(e instanceof QnMgrException){
            return (QnMgrException)e;
        }else{
            return new QnMgrException(-1, e.getLocalizedMessage());
        }
    }

    static public QnmgrErrorInfo toQnmgrErrorInfo(Exception e){
        if(null==e) return null;
        //log.debug("toQnmgrErrorInfo: \n"+e.toString());
        QnmgrErrorInfo rlt=new QnmgrErrorInfo();
        QnMgrException qne=toQnMgrException(e);
        rlt.setCode(""+qne.errorCode);
        rlt.setMessage(qne.errorReason_);
        return rlt;
    }

    static public Response rsErrorResponse(Exception e){
        e.printStackTrace();
        QnmgrErrorInfo errorInfo=Utils.toQnmgrErrorInfo(e);
        switch(Integer.parseInt(errorInfo.getCode())){
            case 612:
                return Response.status(Response.Status.NOT_FOUND).entity(errorInfo).build();
            default:
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorInfo).build();
        }
    }

    static public String trimForwardSlash(String dir){
        if(dir.startsWith("/")){
            dir=dir.substring(1);
        }
        if(dir.endsWith("/")){
            dir=dir.substring(0, dir.length()-1);
        }
        if(dir.startsWith("/") || dir.endsWith("/")){
            return trimForwardSlash(dir);
        }else{
            return dir;
        }
    }


}
