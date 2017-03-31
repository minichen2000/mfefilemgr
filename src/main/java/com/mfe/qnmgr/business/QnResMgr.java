package com.mfe.qnmgr.business;

import com.mfe.qnmgr.exception.QnMgrException;
import com.mfe.qnmgr.restful.model.qnmgr.QnDirInfo;
import com.mfe.qnmgr.restful.model.qnmgr.QnFile;
import com.mfe.qnmgr.utils.qn.Utils;
import com.qiniu.common.QiniuException;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by minichen on 2017/3/30.
 */
public class QnResMgr {
    private final static Logger log = LogManager.getLogger(QnResMgr.class);
    private static QnResMgr ourInstance = new QnResMgr();

    public static QnResMgr getInstance() {
        return ourInstance;
    }

    private QnResMgr() {
    }

    static public BucketManager genBucketManager(String ak, String sk, Zone zone, String bucket) throws Exception {
        Configuration cfg = new Configuration(zone);
        Auth auth = Auth.create(ak, sk);
        return new BucketManager(auth, cfg);
    }

    static public BucketManager genBucketManager(String zone, String bucket) throws Exception {
        return genBucketManager(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket);
    }

    static public List<QnFile> getFiles2(String ak, String sk, Zone zone, String bucket) throws Exception {
        int limit = 1000;
        List<QnFile> rlt = new LinkedList<QnFile>();

        BucketManager bucketManager = genBucketManager(ak, sk, zone, bucket);
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, "", limit, "");
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            if (null != items) {
                for (FileInfo item : items) {
                    rlt.add(Utils.toQnFile(item));
                }
            }
        }

        return rlt;
    }

    static public QnDirInfo getFilesAndCommonPrefix(String ak, String sk, Zone zone, String bucket, String prefix, int limit, String delimiter) throws Exception {
        BucketManager bucketManager = genBucketManager(ak, sk, zone, bucket);
        String marker = null;
        FileListing f = null;
        QnDirInfo rlt = new QnDirInfo();
        for (; ; ) {
            f = bucketManager.listFiles(bucket, prefix, marker, limit, delimiter);
            if (null == f) {
                return rlt;
            }
            marker = f.marker == null ? "" : f.marker;
            if (null != f.items) {
                for (FileInfo item : f.items) {
                    rlt.addFilesItem(Utils.toQnFile(item));
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

    static public QnDirInfo getFilesAndCommonPrefix(String zone, String bucket, String prefix, String delimiter) throws Exception {
        return getFilesAndCommonPrefix(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket, prefix, 1000, delimiter);
    }

    static public QnFile getFile(String ak, String sk, Zone zone, String bucket, String key) throws Exception {
        BucketManager bucketManager = genBucketManager(ak, sk, zone, bucket);
        FileInfo fileInfo = bucketManager.stat(bucket, key);
        if (null == fileInfo) {
            throw new Exception("stat file return null");
        } else {
            return Utils.toQnFile(fileInfo);
        }
    }

    static public QnFile getFile(String zone, String bucket, String key) throws Exception {
        return getFile(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket, key);
    }

    static public List<QnFile> getAllFilesInBucket(String ak, String sk, Zone zone, String bucket) throws Exception {
        QnDirInfo dir = getFilesAndCommonPrefix(ak, sk, zone, bucket, "", 1000, "");
        return null == dir ? null : dir.getFiles();
    }

    static public List<QnFile> getAllFilesInBucket(String zone, String bucket) throws Exception {
        return getAllFilesInBucket(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket);
    }

    static public QnDirInfo getDir(String ak, String sk, Zone zone, String bucket, String directory) throws Exception {
        return getFilesAndCommonPrefix(ak, sk, zone, bucket, Utils.trimForwardSlash(directory) + '/', 1000, "/");
    }

    static public QnDirInfo getDir(String zone, String bucket, String directory) throws Exception {
        return getDir(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket, directory);
    }

    static public List<QnFile> getAllFilesStartWithPrefix(String ak, String sk, Zone zone, String bucket, String prefix) throws Exception {
        QnDirInfo dirInfo = getFilesAndCommonPrefix(ak, sk, zone, bucket, prefix, 1000, "");
        if (null == dirInfo) {
            throw new Exception("null dirInfo returned");
        } else {
            return dirInfo.getFiles();
        }
    }

    static public List<QnFile> getAllFilesStartWithPrefix(String zone, String bucket, String prefix) throws Exception {
        return getAllFilesStartWithPrefix(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket, prefix);
    }


    static public void deleteFile(String ak, String sk, Zone zone, String bucket, String key) throws Exception {
        genBucketManager(ak, sk, zone, bucket).delete(bucket, key);
    }

    static public void deleteFile(String zone, String bucket, String key) throws Exception {
        deleteFile(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket, key);
    }

    static public void deleteFiles(String ak, String sk, Zone zone, String bucket, List<String> files) throws Exception {
        if (null == files) {
            throw new Exception("null file list");
        }
        if (0 >= files.size()) {
            throw Utils.buildQnMgrExceptionFileNotFound();
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
                throw Utils.buildQnMgrExceptionFileNotFound();
            }else if (status.code != 200) {
                throw new QnMgrException(status.code, "some file(s) were failed to delete.");
            }
        }
    }

    static public void deleteFiles(String zone, String bucket, List<String> files) throws Exception {
        deleteFiles(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket, files);
    }

    static public void deleteFilesStartWithPrefix(String ak, String sk, Zone zone, String bucket, String prefix) throws Exception {
        List<QnFile> files = getAllFilesStartWithPrefix(ak, sk, zone, bucket, prefix);
        List<String> keyList = new ArrayList<String>();
        for (QnFile file : files) {
            keyList.add(file.getKey());
        }
        deleteFiles(ak, sk, zone, bucket, keyList);
    }

    static public void deleteFilesStartWithPrefix(String zone, String bucket, String prefix) throws Exception {
        deleteFilesStartWithPrefix(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket, prefix);
    }

    static public void deleteDir(String ak, String sk, Zone zone, String bucket, String directory) throws Exception {
        deleteFilesStartWithPrefix(ak, sk, zone, bucket, Utils.trimForwardSlash(directory) + '/');
    }

    static public void deleteDir(String zone, String bucket, String dir) throws Exception {
        deleteDir(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket, dir);
    }
}
