package com.mfe.mfefilemgr.utils.qn;

import com.mfe.mfefilemgr.ConfLoader;
import com.mfe.mfefilemgr.constants.ConfigKey;
import com.mfe.mfefilemgr.exception.MfeFileMgrException;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFile;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFileMgrErrorInfo;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.Provider;
import com.mfe.mfefilemgr.utils.Utils;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.model.FileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Response;

/**
 * Created by minichen on 2017/3/30.
 */
public class QnUtils {
    private final static Logger log = LogManager.getLogger(QnUtils.class);
    static public String get_QiNiu_AK(){
        return ConfLoader.getInstance().getConf(ConfigKey.QINIU_AK, "");
    }

    static public String get_QiuNiu_SK(){
        return ConfLoader.getInstance().getConf(ConfigKey.QINIU_SK, "");
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

    static public MfeFile toMfeFile(FileInfo fileInfo){
        MfeFile rlt=null;
        if(null!=fileInfo){
            rlt=new MfeFile();
            rlt.setEndUser(fileInfo.endUser);
            rlt.setFsize(fileInfo.fsize);
            rlt.setHash(fileInfo.hash);
            rlt.setKey(fileInfo.key);
            rlt.setMimeType(fileInfo.mimeType);
            rlt.setPutTime(fileInfo.putTime);
        }
        return rlt;
    }

    static public MfeFileMgrException toMfeFileMgrException(QiniuException e){
        if(null==e) return null;
        return new MfeFileMgrException(Provider.QINIU, e.code(), e.error());
    }

    static public MfeFileMgrException buildQiniuExceptionFileNotFound(){
        return new MfeFileMgrException(Provider.QINIU, 612, "no such file or directory");
    }


}
