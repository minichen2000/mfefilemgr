package com.mfe.mfefilemgr.business.intf;

import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeDirInfo;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFile;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFileMgrErrorInfo;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by minichen on 2017/4/2.
 */
public interface IMfeFileMgr {
    public String getAK();
    public String getSK();
    public Response rsErrorResponse(MfeFileMgrErrorInfo ef);
    public char getDirDelimiter();
    public MfeFile getFile(String endpoint, String bucket, String key) throws Exception;
    public MfeDirInfo getFilesAndCommonPrefix(String endpoint, String bucket, String prefix, String delimiter) throws Exception;
    public void deleteFile(String endpoint, String bucket, String key) throws Exception;
    public void deleteFiles(String endpoint, String bucket, List<String> keys) throws Exception;

    public List<MfeFile> getAllFilesInBucket(String endpoint, String bucket) throws Exception;
    public MfeDirInfo getDir(String endpoint, String bucket, String directory) throws Exception;
    public List<MfeFile> getAllFilesStartWithPrefix(String endpoint, String bucket, String prefix) throws Exception;
    public void deleteFilesStartWithPrefix(String endpoint, String bucket, String prefix) throws Exception;
    public void deleteDir(String endpoint, String bucket, String dir) throws Exception;
}
