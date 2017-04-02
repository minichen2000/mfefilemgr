package com.mfe.mfefilemgr.api;

import com.mfe.mfefilemgr.business.QnResMgr;
import com.mfe.mfefilemgr.business.intf.IMfeFileMgr;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeDirInfo;
import com.mfe.mfefilemgr.restful.mfefilemgrserver.api.DirApiService;
import com.mfe.mfefilemgr.restful.mfefilemgrserver.api.NotFoundException;
import com.mfe.mfefilemgr.utils.MfeFileMgrPool;
import com.mfe.mfefilemgr.utils.Utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-03-31T18:11:08.535+08:00")
public class DirApiServiceImpl extends DirApiService {
    @Override
    public Response listDir(String provider, String endpoint, String bucket, String directory, SecurityContext securityContext) throws NotFoundException {
        IMfeFileMgr mgr= MfeFileMgrPool.getMgr(provider);
        if(null==mgr) return Utils.noProviderFoundResponse(provider);
        try {
            MfeDirInfo dirInfo= mgr.getDir(endpoint, bucket, directory);
            return Response.ok().entity(dirInfo).build();
        } catch (Exception e) {
            return mgr.rsErrorResponse(Utils.toMfeFileMgrErrorInfo(e));
        }
    }

    @Override
    public Response listRootDir(String provider, String endpoint, String bucket, SecurityContext securityContext) throws NotFoundException {
        IMfeFileMgr mgr= MfeFileMgrPool.getMgr(provider);
        if(null==mgr) return Utils.noProviderFoundResponse(provider);
        try {
            MfeDirInfo dirInfo= mgr.getDir(endpoint, bucket,"");
            return Response.ok().entity(dirInfo).build();
        } catch (Exception e) {
            return mgr.rsErrorResponse(Utils.toMfeFileMgrErrorInfo(e));
        }
    }

    @Override
    public Response deleteDir(String provider, String endpoint, String bucket, String directory, SecurityContext securityContext) throws NotFoundException {
        IMfeFileMgr mgr= MfeFileMgrPool.getMgr(provider);
        if(null==mgr) return Utils.noProviderFoundResponse(provider);
        try {
            mgr.deleteDir(endpoint, bucket, directory);
            return Response.ok().build();
        } catch (Exception e) {
            return mgr.rsErrorResponse(Utils.toMfeFileMgrErrorInfo(e));
        }
    }
}
