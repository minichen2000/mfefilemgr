package com.mfe.qnmgr.utils.qn;

import com.mfe.qnmgr.ConfLoader;
import com.mfe.qnmgr.constants.ConfigKey;
import com.mfe.qnmgr.restful.model.qnmgr.QnDirInfo;
import com.mfe.qnmgr.restful.model.qnmgr.QnFile;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;

/**
 * Created by minichen on 2017/3/30.
 */
public class Utils {

    static public String getAK(){
        return ConfLoader.getInstance().getConf(ConfigKey.AK, "");
    }

    static public String getSK(){
        return ConfLoader.getInstance().getConf(ConfigKey.SK, "");
    }

    static public Zone getZone(String zone){
        if(zone.toLowerCase().equals("zone0")){
            return Zone.zone0();
        }else if(zone.toLowerCase().equals("zone1")){
            return Zone.zone1();
        }else if(zone.toLowerCase().equals("zone2")){
            return Zone.zone2();
        }else if(zone.toLowerCase().equals("zonena0")){
            return Zone.zoneNa0();
        }else {
            return Zone.autoZone();
        }
    }

    static public QnFile toQnFile(FileInfo fileInfo){
        QnFile rlt=null;
        if(null!=fileInfo){
            rlt=new QnFile();
            rlt.setEndUser(fileInfo.endUser);
            rlt.setFsize(fileInfo.fsize);
            rlt.setHash(fileInfo.hash);
            rlt.setKey(fileInfo.key);
            rlt.setMimeType(fileInfo.mimeType);
            rlt.setPutTime(fileInfo.putTime);
        }
        return rlt;
    }

    static public QnDirInfo getDirInfo(BucketManager mgr, String bucket, String prefix, int limit, String delimiter){
        String marker = null;
        FileListing f=null;
        QnDirInfo rlt=new QnDirInfo();
        for(;;){
            try {
                f = mgr.listFiles(bucket, prefix, marker, limit, delimiter);
                if(null==f){
                    return rlt;
                }
                marker = f.marker == null ? "" : f.marker;
                if(null!=f.items){
                    for (FileInfo item : f.items) {
                        rlt.addFilesItem(Utils.toQnFile(item));
                    }
                }
                if(null!=f.commonPrefixes){
                    //rlt.getSubdirs().clear();
                    for (String item : f.commonPrefixes) {
                        rlt.addSubdirsItem(item);
                    }
                }
                if("".equals(marker)){
                    return rlt;
                }
            } catch (QiniuException e) {
                e.printStackTrace();
                return rlt;
            }
        }

    }
}
