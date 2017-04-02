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
    public Response rsErrorResponse(MfeFileMgrErrorInfo ef);
    public char getDirDelimiter();
    public MfeFile getFile(String zone, String bucket, String key) throws Exception;
    public List<MfeFile> getAllFilesInBucket(String zone, String bucket) throws Exception;
    public MfeDirInfo getDir(String zone, String bucket, String directory) throws Exception;
    public List<MfeFile> getAllFilesStartWithPrefix(String zone, String bucket, String prefix) throws Exception;
    public void deleteFile(String zone, String bucket, String key) throws Exception;
    public void deleteFiles(String zone, String bucket, List<String> files) throws Exception;
    public void deleteFilesStartWithPrefix(String zone, String bucket, String prefix) throws Exception;
    public void deleteDir(String zone, String bucket, String dir) throws Exception;
}
