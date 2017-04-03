package com.mfe.mfefilemgr.business.defaultImpl;

import com.mfe.mfefilemgr.ConfLoader;
import com.mfe.mfefilemgr.business.intf.IMfeFileMgr;
import com.mfe.mfefilemgr.constants.ConfigKey;
import com.mfe.mfefilemgr.exception.MfeFileMgrException;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeDirInfo;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFile;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFileMgrErrorInfo;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.Provider;
import com.mfe.mfefilemgr.utils.Utils;
import com.mfe.mfefilemgr.utils.qn.QnUtils;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minichen on 2017/3/30.
 */
public abstract class DefaultMgr implements IMfeFileMgr {
    private final static Logger log = LogManager.getLogger(DefaultMgr.class);


    @Override
    public List<MfeFile> getAllFilesInBucket(String endpoint, String bucket) throws Exception {
        MfeDirInfo dir = getFilesAndCommonPrefix(endpoint, bucket, "", "");
        return null == dir ? null : dir.getFiles();
    }

    @Override
    public MfeDirInfo getDir(String endpoint, String bucket, String directory) throws Exception {
        if(null==directory || 0==directory.length()){
            return getFilesAndCommonPrefix(endpoint, bucket, "", ""+getDirDelimiter());
        }else {
            return getFilesAndCommonPrefix(endpoint, bucket, Utils.trimDirDelimiter(this, directory) + getDirDelimiter(), ""+getDirDelimiter());
        }
    }

    @Override
    public List<MfeFile> getAllFilesStartWithPrefix(String endpoint, String bucket, String prefix) throws Exception {
        MfeDirInfo dirInfo = getFilesAndCommonPrefix(endpoint, bucket, prefix, "");
        if (null == dirInfo) {
            throw new Exception("null dirInfo returned");
        } else {
            return dirInfo.getFiles();
        }
    }

    @Override
    public void deleteFilesStartWithPrefix(String endpoint, String bucket, String prefix) throws Exception {
        List<MfeFile> files = getAllFilesStartWithPrefix(endpoint, bucket, prefix);
        List<String> keyList = new ArrayList<String>();
        for (MfeFile file : files) {
            keyList.add(file.getKey());
        }
        deleteFiles(endpoint, bucket, keyList);
    }

    @Override
    public void deleteDir(String endpoint, String bucket, String dir) throws Exception {
        deleteFilesStartWithPrefix(endpoint, bucket, Utils.trimDirDelimiter(this, dir) + getDirDelimiter());
    }
}
