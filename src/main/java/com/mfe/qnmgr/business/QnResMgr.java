package com.mfe.qnmgr.business;

import com.mfe.qnmgr.ConfLoader;
import com.mfe.qnmgr.constants.ConfigKey;
import com.mfe.qnmgr.exception.QnMgrException;
import com.mfe.qnmgr.restful.model.qnmgr.QnDirInfo;
import com.mfe.qnmgr.restful.model.qnmgr.QnFile;
import com.mfe.qnmgr.utils.qn.Utils;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    static public BucketManager genBucketManager(String ak, String sk, Zone zone, String bucket)  throws Exception {
        Configuration cfg = new Configuration(zone);
        Auth auth = Auth.create(ak, sk);
        return new BucketManager(auth, cfg);
    }

    static public BucketManager genBucketManager(String zone, String bucket)  throws Exception {
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
            if(null!=items) {
                for (FileInfo item : items) {
                    rlt.add(Utils.toQnFile(item));
                }
            }
        }


        return rlt;
    }
    static public List<QnFile> getFiles(String ak, String sk, Zone zone, String bucket) throws Exception {
        QnDirInfo dir=Utils.getDirInfo(genBucketManager(ak, sk, zone, bucket), bucket, "", 1000, "");
        return null==dir ? null : dir.getFiles();
    }

    static public List<QnFile> getFiles(String zone, String bucket) throws Exception {
        log.debug("zone: " + zone);
        return getFiles(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket);
    }

    static public QnDirInfo getDir(String zone, String bucket, String prefix, String delimiter) throws Exception {
        log.debug("zone: " + zone);
        return Utils.getDirInfo(genBucketManager(Utils.getAK(), Utils.getSK(), Utils.getZone(zone), bucket), bucket, prefix, 1000, delimiter);
    }

    static public void deleteFile(String zone, String bucket, String key) throws Exception{
        try {
            genBucketManager(zone, bucket).delete(bucket, key);
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.toQnMgrException(e);
        }
    }
}
