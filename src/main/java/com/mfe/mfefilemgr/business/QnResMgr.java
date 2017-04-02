package com.mfe.mfefilemgr.business;

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
public class QnResMgr implements IMfeFileMgr {
    private final static Logger log = LogManager.getLogger(QnResMgr.class);
    private static QnResMgr ourInstance = new QnResMgr();

    public static QnResMgr getInstance() {
        return ourInstance;
    }

    private QnResMgr() {
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
                    case 612:
                        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(ef).build();
                    default:
                        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).entity(ef).build();
                }
            default:
                return null;
        }
    }



    public BucketManager genBucketManager(String ak, String sk, Zone zone, String bucket) throws Exception {
        Configuration cfg = new Configuration(zone);
        Auth auth = Auth.create(ak, sk);
        return new BucketManager(auth, cfg);
    }

    public BucketManager genBucketManager(String zone, String bucket) throws Exception {
        return genBucketManager(QnUtils.get_QiNiu_AK(), QnUtils.get_QiuNiu_SK(), QnUtils.getZone(zone), bucket);
    }

    public MfeDirInfo getFilesAndCommonPrefix(String ak, String sk, Zone zone, String bucket, String prefix, int limit, String delimiter) throws Exception {
        BucketManager bucketManager = genBucketManager(ak, sk, zone, bucket);
        String marker = null;
        FileListing f = null;
        MfeDirInfo rlt = new MfeDirInfo();
        for (; ; ) {
            f = bucketManager.listFiles(bucket, prefix, marker, limit, delimiter);
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

    public MfeDirInfo getFilesAndCommonPrefix(String zone, String bucket, String prefix, String delimiter) throws Exception {
        return getFilesAndCommonPrefix(QnUtils.get_QiNiu_AK(), QnUtils.get_QiuNiu_SK(), QnUtils.getZone(zone), bucket, prefix, 1000, delimiter);
    }

    public MfeFile getFile(String ak, String sk, Zone zone, String bucket, String key) throws Exception {
        BucketManager bucketManager = genBucketManager(ak, sk, zone, bucket);
        FileInfo fileInfo = bucketManager.stat(bucket, key);
        if (null == fileInfo) {
            throw new Exception("stat file return null");
        } else {
            return QnUtils.toMfeFile(fileInfo);
        }
    }

    public MfeFile getFile(String zone, String bucket, String key) throws Exception {
        return getFile(QnUtils.get_QiNiu_AK(), QnUtils.get_QiuNiu_SK(), QnUtils.getZone(zone), bucket, key);
    }

    public List<MfeFile> getAllFilesInBucket(String ak, String sk, Zone zone, String bucket) throws Exception {
        MfeDirInfo dir = getFilesAndCommonPrefix(ak, sk, zone, bucket, "", 1000, "");
        return null == dir ? null : dir.getFiles();
    }

    public List<MfeFile> getAllFilesInBucket(String zone, String bucket) throws Exception {
        return getAllFilesInBucket(QnUtils.get_QiNiu_AK(), QnUtils.get_QiuNiu_SK(), QnUtils.getZone(zone), bucket);
    }

    public MfeDirInfo getDir(String ak, String sk, Zone zone, String bucket, String directory) throws Exception {
        if(null==directory || 0==directory.trim().length()){
            return getFilesAndCommonPrefix(ak, sk, zone, bucket, "", 1000, ""+getDirDelimiter());
        }else {
            return getFilesAndCommonPrefix(ak, sk, zone, bucket, Utils.trimDirDelimiter(this, directory) + getDirDelimiter(), 1000, ""+getDirDelimiter());
        }
    }

    public MfeDirInfo getDir(String zone, String bucket, String directory) throws Exception {
        return getDir(QnUtils.get_QiNiu_AK(), QnUtils.get_QiuNiu_SK(), QnUtils.getZone(zone), bucket, directory);
    }

    public List<MfeFile> getAllFilesStartWithPrefix(String ak, String sk, Zone zone, String bucket, String prefix) throws Exception {
        MfeDirInfo dirInfo = getFilesAndCommonPrefix(ak, sk, zone, bucket, prefix, 1000, "");
        if (null == dirInfo) {
            throw new Exception("null dirInfo returned");
        } else {
            return dirInfo.getFiles();
        }
    }

    public List<MfeFile> getAllFilesStartWithPrefix(String zone, String bucket, String prefix) throws Exception {
        return getAllFilesStartWithPrefix(QnUtils.get_QiNiu_AK(), QnUtils.get_QiuNiu_SK(), QnUtils.getZone(zone), bucket, prefix);
    }


    public void deleteFile(String ak, String sk, Zone zone, String bucket, String key) throws Exception {
        genBucketManager(ak, sk, zone, bucket).delete(bucket, key);
    }

    public void deleteFile(String zone, String bucket, String key) throws Exception {
        deleteFile(QnUtils.get_QiNiu_AK(), QnUtils.get_QiuNiu_SK(), QnUtils.getZone(zone), bucket, key);
    }

    public void deleteFiles(String ak, String sk, Zone zone, String bucket, List<String> files) throws Exception {
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
        Response response = genBucketManager(ak, sk, zone, bucket).batch(batchOperations);
        BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
        for (int i = 0; i < keyList.length; i++) {
            BatchStatus status = batchStatusList[i];
            if (status.code == 612) {
                throw QnUtils.buildQiniuExceptionFileNotFound();
            }else if (status.code != 200) {
                throw new MfeFileMgrException(Provider.QINIU, status.code, "some file(s) were failed to delete.");
            }
        }
    }

    public void deleteFiles(String zone, String bucket, List<String> files) throws Exception {
        deleteFiles(QnUtils.get_QiNiu_AK(), QnUtils.get_QiuNiu_SK(), QnUtils.getZone(zone), bucket, files);
    }

    public void deleteFilesStartWithPrefix(String ak, String sk, Zone zone, String bucket, String prefix) throws Exception {
        List<MfeFile> files = getAllFilesStartWithPrefix(ak, sk, zone, bucket, prefix);
        List<String> keyList = new ArrayList<String>();
        for (MfeFile file : files) {
            keyList.add(file.getKey());
        }
        deleteFiles(ak, sk, zone, bucket, keyList);
    }

    public void deleteFilesStartWithPrefix(String zone, String bucket, String prefix) throws Exception {
        deleteFilesStartWithPrefix(QnUtils.get_QiNiu_AK(), QnUtils.get_QiuNiu_SK(), QnUtils.getZone(zone), bucket, prefix);
    }

    public void deleteDir(String ak, String sk, Zone zone, String bucket, String directory) throws Exception {
        deleteFilesStartWithPrefix(ak, sk, zone, bucket, Utils.trimDirDelimiter(this, directory) + getDirDelimiter());
    }

    public void deleteDir(String zone, String bucket, String dir) throws Exception {
        deleteDir(QnUtils.get_QiNiu_AK(), QnUtils.get_QiuNiu_SK(), QnUtils.getZone(zone), bucket, dir);
    }
}
