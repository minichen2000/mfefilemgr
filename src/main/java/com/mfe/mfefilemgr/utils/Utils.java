package com.mfe.mfefilemgr.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.mfe.mfefilemgr.business.intf.IMfeFileMgr;
import com.mfe.mfefilemgr.exception.MfeFileMgrException;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFileMgrErrorInfo;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.Provider;
import com.mfe.mfefilemgr.utils.aliyun.AliyunUtils;
import com.mfe.mfefilemgr.utils.qn.QnUtils;
import com.qiniu.common.QiniuException;

import javax.ws.rs.core.Response;

/**
 * Created by minichen on 2017/4/2.
 */
public class Utils {

    static public String getFileExtention(String key){
        if(null==key || key.isEmpty()) return null;
        int idx=key.lastIndexOf('.');
        if(-1==idx || idx==key.length()-1) return "";
        return key.substring(idx+1);
    }
    static public String genContentType(IMfeFileMgr mgr, String key){
        if(null==key || key.isEmpty()) return null;
        if(key.endsWith(""+mgr.getDirDelimiter())){
            return "directory/directory";
        }else{
            String ext=getFileExtention(key);
            if(null==ext){
                return null;
            }else{
                return ContentType.fromFileExtension(ext).getMimeType();
            }
        }
    }

    static public Response noProviderFoundResponse(String provider){
        MfeFileMgrErrorInfo ei=new MfeFileMgrErrorInfo();
        ei.setProvider(Provider.MFEFILEMGR);
        ei.setCode("0");
        ei.setMessage("No provider found: "+provider);
        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(ei).build();
    }
    static public MfeFileMgrException toMfeFileMgrException(Exception e){
        if(e instanceof QiniuException){
            return QnUtils.toMfeFileMgrException((QiniuException)e);
        }else if(e instanceof OSSException){
            return AliyunUtils.toMfeFileMgrException((OSSException)e);
        }else if(e instanceof ClientException){
            return AliyunUtils.toMfeFileMgrException((ClientException)e);
        }else if(e instanceof MfeFileMgrException){
            return (MfeFileMgrException)e;
        }else{
            return new MfeFileMgrException(Provider.MFEFILEMGR, "-1", e.getLocalizedMessage());
        }
    }

    static public MfeFileMgrErrorInfo toMfeFileMgrErrorInfo(Exception e){
        if(null==e) return null;
        MfeFileMgrErrorInfo rlt=new MfeFileMgrErrorInfo();
        MfeFileMgrException mfefilemgr_e= toMfeFileMgrException(e);
        rlt.setProvider(mfefilemgr_e.provider);
        rlt.setCode(mfefilemgr_e.errorCode);
        rlt.setMessage(mfefilemgr_e.errorReason_);
        return rlt;
    }

    static public String trimDirDelimiter(IMfeFileMgr mgr, String dir){
        if(dir.startsWith(""+mgr.getDirDelimiter())){
            dir=dir.substring(1);
        }
        if(dir.endsWith(""+mgr.getDirDelimiter())){
            dir=dir.substring(0, dir.length()-1);
        }
        if(dir.startsWith(""+mgr.getDirDelimiter()) || dir.endsWith(""+mgr.getDirDelimiter())){
            return trimDirDelimiter(mgr, dir);
        }else{
            return dir;
        }
    }
}
