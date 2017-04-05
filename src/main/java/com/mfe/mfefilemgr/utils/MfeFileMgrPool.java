package com.mfe.mfefilemgr.utils;

import com.mfe.mfefilemgr.business.AliyunResMgr;
import com.mfe.mfefilemgr.business.QnResMgr;
import com.mfe.mfefilemgr.business.intf.IMfeFileMgr;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by minichen on 2017/4/2.
 */
public class MfeFileMgrPool {
    static private Map<Provider, IMfeFileMgr> pool = null;

    static private void init() {
        pool = new HashMap<Provider, IMfeFileMgr>();
        pool.put(Provider.QINIU, QnResMgr.getInstance());
        pool.put(Provider.ALIYUN, AliyunResMgr.getInstance());
    }
    static public IMfeFileMgr getMgr(Provider provider) {
        if (null == pool) {
            init();
        }
        return pool.get(provider);
    }
    static public IMfeFileMgr getMgr(String provider) {
        try {
            Provider p = Provider.valueOf(provider.toUpperCase());
            if (null != p) {
                return getMgr(p);
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }


}
