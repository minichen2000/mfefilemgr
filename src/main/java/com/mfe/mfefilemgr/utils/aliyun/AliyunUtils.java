package com.mfe.mfefilemgr.utils.aliyun;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectMetadata;
import com.mfe.mfefilemgr.ConfLoader;
import com.mfe.mfefilemgr.business.AliyunResMgr;
import com.mfe.mfefilemgr.constants.ConfigKey;
import com.mfe.mfefilemgr.exception.MfeFileMgrException;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFile;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.Provider;
import com.mfe.mfefilemgr.utils.Utils;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.model.FileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by minichen on 2017/3/30.
 */
public class AliyunUtils {
    private final static Logger log = LogManager.getLogger(AliyunUtils.class);
    static public String getAK(){
        return ConfLoader.getInstance().getConf(ConfigKey.ALIYUN_AK, "");
    }

    static public String getSK(){
        return ConfLoader.getInstance().getConf(ConfigKey.ALIYUN_SK, "");
    }


    static public MfeFile toMfeFile(String key, ObjectMetadata omd){
        MfeFile rlt=null;
        if(null!=omd && null!=key){
            rlt=new MfeFile();
            //rlt.setEndUser(fileInfo.endUser);
            rlt.setFsize(omd.getContentLength());
            rlt.setHash(omd.getETag());
            rlt.setKey(key);
            rlt.setMimeType(omd.getContentType());
            rlt.setPutTime(omd.getLastModified().getTime());
        }
        return rlt;
    }
    static public MfeFile toMfeFile(OSSObjectSummary s){
        MfeFile rlt=null;
        if(null!=s){
            rlt=new MfeFile();
            rlt.setEndUser(s.getOwner().getDisplayName());
            rlt.setFsize(s.getSize());
            rlt.setHash(s.getETag());
            rlt.setKey(s.getKey());
            rlt.setMimeType(Utils.genContentType(AliyunResMgr.getInstance(), s.getKey()));
            rlt.setPutTime(s.getLastModified().getTime());
        }
        return rlt;
    }

    static public MfeFileMgrException toMfeFileMgrException(OSSException e){
        if(null==e) return null;
        return new MfeFileMgrException(Provider.ALIYUN, e.getErrorCode(), e.getMessage());
    }
    static public MfeFileMgrException toMfeFileMgrException(ClientException e){
        if(null==e) return null;
        return new MfeFileMgrException(Provider.ALIYUN, e.getErrorCode(), e.getMessage());
    }

    static public MfeFileMgrException buildQiniuExceptionFileNotFound(){
        return new MfeFileMgrException(Provider.ALIYUN, "NoSuchKey", "NoSuchKey");
    }


}
