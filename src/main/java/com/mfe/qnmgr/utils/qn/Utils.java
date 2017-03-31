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
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    static public QnMgrException toQnMgrException(Exception e){
        if(null==e) return null;
        if(e instanceof QiniuException){
            QiniuException qe=(QiniuException)e;
            return new QnMgrException(qe.code(), qe.getLocalizedMessage()+", "+qe.error());
        }else{
            return new QnMgrException(-1, e.getLocalizedMessage());
        }
    }

    static public QnmgrErrorInfo toQnmgrErrorInfo(Exception e){
        if(null==e) return null;
        QnmgrErrorInfo rlt=new QnmgrErrorInfo();
        if(e instanceof QnMgrException){
            QnMgrException qe=(QnMgrException)e;
            rlt.setCode(""+qe.errorCode);
            rlt.setMessage(qe.errorReason_);
        }else{
            rlt.setCode(""+(-1));
            rlt.setMessage(e.getLocalizedMessage());
        }
        return rlt;
    }

    static public QnDirInfo getDirInfo(BucketManager mgr, String bucket, String prefix, int limit, String delimiter) throws QnMgrException {
        String marker = null;
        FileListing f=null;
        QnDirInfo rlt=new QnDirInfo();
        for(;;){
            try {
                f = mgr.listFiles(bucket, prefix, marker, limit, delimiter);
                if(null==f){
                    return rlt;
                }
                marker = f.marker == null ? "" : f.marker;
                if(null!=f.items){
                    for (FileInfo item : f.items) {
                        rlt.addFilesItem(Utils.toQnFile(item));
                    }
                }
                if(null!=f.commonPrefixes){
                    //rlt.getSubdirs().clear();
                    for (String item : f.commonPrefixes) {
                        rlt.addSubdirsItem(item);
                    }
                }
                if("".equals(marker)){
                    return rlt;
                }
            } catch (Exception e) {
                e.printStackTrace();
                QnMgrException ee=toQnMgrException(e);
                if(null!=ee){
                    throw ee;
                }
            }
        }

    }
}
