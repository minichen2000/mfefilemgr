package com.mfe.mfefilemgr.business;

import com.mfe.mfefilemgr.ConfLoader;
import com.mfe.mfefilemgr.business.defaultImpl.DefaultMgr;
import com.mfe.mfefilemgr.constants.ConfigKey;
import com.mfe.mfefilemgr.exception.MfeFileMgrException;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeDirInfo;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFile;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFileMgrErrorInfo;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.Provider;
import com.mfe.mfefilemgr.utils.qn.QnUtils;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by minichen on 2017/3/30.
 */
public class QnResMgr extends DefaultMgr {
    private final static Logger log = LogManager.getLogger(QnResMgr.class);
    private static QnResMgr ourInstance = new QnResMgr();

    public static QnResMgr getInstance() {
        return ourInstance;
    }

    private QnResMgr() {
    }

    @Override
    public String getAK() {
        return QnUtils.getAK();
    }

    @Override
    public String getSK() {
        return QnUtils.getSK();
    }

    public BucketManager genBucketManager(String zone) throws Exception {
        Configuration cfg = new Configuration(QnUtils.getZone(zone));
        Auth auth = Auth.create(getAK(), getSK());
        return new BucketManager(auth, cfg);
    }

    @Override
    public char getDirDelimiter() {
        return ConfLoader.getInstance().getConf(ConfigKey.QINIU_DIR_DELIMITER, ConfigKey.DEFAULT_QINIU_DIR_DELIMITER).charAt(0);
    }

    @Override
    public javax.ws.rs.core.Response rsErrorResponse(MfeFileMgrErrorInfo ef) {
        switch(ef.getProvider()){
            case QINIU:
                switch(ef.getCode()){
                    case "612":
                        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(ef).build();
                    default:
                        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).entity(ef).build();
                }
            default:
                return null;
        }
    }

    @Override
    public MfeDirInfo getFilesAndCommonPrefix(String zone, String bucket, String prefix, String delimiter) throws Exception {
        BucketManager bucketManager = genBucketManager(zone);
        String marker = null;
        FileListing f = null;
        MfeDirInfo rlt = new MfeDirInfo();
        for (; ; ) {
            f = bucketManager.listFiles(bucket, prefix, marker, 1000, delimiter);
            if (null == f) {
                return rlt;
            }
            marker = f.marker == null ? "" : f.marker;
            if (null != f.items) {
                for (FileInfo item : f.items) {
                    rlt.addFilesItem(QnUtils.toMfeFile(item));
                }
            }
            if (null != f.commonPrefixes) {
                //rlt.getSubdirs().clear();
                for (String item : f.commonPrefixes) {
                    rlt.addSubdirsItem(item);
                }
            }
            if ("".equals(marker)) {
                return rlt;
            }
        }

    }

    @Override
    public MfeFile getFile(String zone, String bucket, String key) throws Exception {
        BucketManager bucketManager = genBucketManager(zone);
        FileInfo fileInfo = bucketManager.stat(bucket, key);
        if (null == fileInfo) {
            throw new Exception("stat file return null");
        } else {
            return QnUtils.toMfeFile(fileInfo);
        }
    }

    @Override
    public void deleteFile(String zone, String bucket, String key) throws Exception {
        genBucketManager(zone).delete(bucket, key);
    }

    @Override
    public void deleteFiles(String zone, String bucket, List<String> files) throws Exception {
        if (null == files) {
            throw new Exception("null file list");
        }
        if (0 >= files.size()) {
            throw QnUtils.buildQiniuExceptionFileNotFound();
        }
        String[] keyList = new String[files.size()];
        for (int i = 0; i < keyList.length; i++) {
            keyList[i] = files.get(i);
        }
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        batchOperations.addDeleteOp(bucket, keyList);
        Response response = genBucketManager(zone).batch(batchOperations);
        BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
        for (int i = 0; i < keyList.length; i++) {
            BatchStatus status = batchStatusList[i];
            if (status.code == 612) {
                throw QnUtils.buildQiniuExceptionFileNotFound();
            }else if (status.code != 200) {
                throw new MfeFileMgrException(Provider.QINIU, ""+status.code, "some file(s) were failed to delete.");
            }
        }
    }
}
