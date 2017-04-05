package com.mfe.mfefilemgr.business;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.mfe.mfefilemgr.business.defaultImpl.DefaultMgr;
import com.mfe.mfefilemgr.constants.ConfLoader;
import com.mfe.mfefilemgr.constants.ConfigKey;
import com.mfe.mfefilemgr.exception.MfeFileMgrException;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeDirInfo;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFile;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFileMgrErrorInfo;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.Provider;
import com.mfe.mfefilemgr.utils.aliyun.AliyunUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by minichen on 2017/4/3.
 */
public class AliyunResMgr extends DefaultMgr {

    private final static Logger log = LogManager.getLogger(AliyunResMgr.class);
    private static AliyunResMgr ourInstance = new AliyunResMgr();

    public static AliyunResMgr getInstance() {
        return ourInstance;
    }

    private AliyunResMgr() {
    }

    public OSSClient getOSSClient(String endpoint) {
        return new OSSClient(endpoint, getAK(), getSK());
    }

    @Override
    public String getAK() {
        return AliyunUtils.getAK();
    }

    @Override
    public String getSK() {
        return AliyunUtils.getSK();
    }

    @Override
    public Response rsErrorResponse(MfeFileMgrErrorInfo ef) {
        switch (ef.getProvider()) {
            case ALIYUN:
                switch (ef.getCode()) {
                    case "NoSuchKey":
                        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(ef).build();
                    default:
                        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).entity(ef).build();
                }
            default:
                return null;
        }
    }

    @Override
    public char getDirDelimiter() {
        return ConfLoader.getInstance().getConf(ConfigKey.ALIYUN_DIR_DELIMITER, ConfigKey.DEFAULT_ALIYUN_DIR_DELIMITER).charAt(0);
    }

    @Override
    public MfeFile getFile(String endpoint, String bucket, String key) throws Exception {
        OSSClient client = getOSSClient(endpoint);
        MfeFile rlt = AliyunUtils.toMfeFile(key, client.getObjectMetadata(bucket, key));
        client.shutdown();
        return rlt;
    }

    @Override
    public MfeDirInfo getFilesAndCommonPrefix(String endpoint, String bucket, String prefix, String delimiter) throws Exception {
        MfeDirInfo rlt = null;
        OSSClient client = getOSSClient(endpoint);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucket);
        if (null != prefix && 0 != prefix.length()) {
            listObjectsRequest.setPrefix(prefix);
        }
        if (null != delimiter && 0 != delimiter.length()) {
            listObjectsRequest.setDelimiter(delimiter);
        }
        listObjectsRequest.setMaxKeys(1);
        String nextMarker = null;
        ObjectListing objectListing;
        rlt = new MfeDirInfo();
        do {
            objectListing = client.listObjects(listObjectsRequest.withMarker(nextMarker));
            for (OSSObjectSummary s : objectListing.getObjectSummaries()) {
                if (null != delimiter && delimiter.equalsIgnoreCase("" + getDirDelimiter()) && (null != prefix && !prefix.isEmpty() && prefix.equals(s.getKey()))) {
                    //bug of aliyun? we need exclude the given directory(prefix) itself from the file list in case getDir.
                } else {
                    rlt.addFilesItem(AliyunUtils.toMfeFile(s));
                }
            }
            for (String commonPrefix : objectListing.getCommonPrefixes()) {
                rlt.addSubdirsItem(commonPrefix);
            }
            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
        client.shutdown();
        return rlt;
    }

    @Override
    public void deleteFile(String endpoint, String bucket, String key) throws Exception {
        OSSClient client = getOSSClient(endpoint);
        client.deleteObject(bucket, key);
        client.shutdown();
    }

    @Override
    public void deleteFiles(String endpoint, String bucket, List<String> keys) throws Exception {
        OSSClient client = getOSSClient(endpoint);
        DeleteObjectsResult result = client.deleteObjects(new DeleteObjectsRequest(bucket).withKeys(keys).withQuiet(true));
        client.shutdown();
        if (null == result || null == result.getDeletedObjects() || 0 < result.getDeletedObjects().size()) {
            throw new MfeFileMgrException(Provider.ALIYUN, "DeleteFilesFailed", "some file(s) were failed to delete.");
        }
    }


}
