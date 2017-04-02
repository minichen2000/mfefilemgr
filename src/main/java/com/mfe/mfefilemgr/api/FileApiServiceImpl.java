package com.mfe.mfefilemgr.api;

import com.mfe.mfefilemgr.business.QnResMgr;
import com.mfe.mfefilemgr.business.intf.IMfeFileMgr;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.MfeFile;
import com.mfe.mfefilemgr.restful.mfefilemgrserver.api.FileApiService;
import com.mfe.mfefilemgr.restful.mfefilemgrserver.api.NotFoundException;
import com.mfe.mfefilemgr.utils.MfeFileMgrPool;
import com.mfe.mfefilemgr.utils.Utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-03-30T20:44:57.384+08:00")
public class FileApiServiceImpl extends FileApiService {


    @Override
    public Response getAllFilesInBucket(String provider, String endpoint, String bucket, SecurityContext securityContext) throws NotFoundException {
        IMfeFileMgr mgr= MfeFileMgrPool.getMgr(provider);
        if(null==mgr) return Utils.noProviderFoundResponse(provider);
        try {
            List<MfeFile> files= mgr.getAllFilesInBucket(endpoint, bucket);
            return Response.ok().entity(files).build();
        } catch (Exception e) {
            return mgr.rsErrorResponse(Utils.toMfeFileMgrErrorInfo(e));
        }
    }

    @Override
    public Response getAllFilesStartWithPrefix(String provider, String endpoint, String bucket, String prefix, SecurityContext securityContext) throws NotFoundException {
        IMfeFileMgr mgr= MfeFileMgrPool.getMgr(provider);
        if(null==mgr) return Utils.noProviderFoundResponse(provider);
        try {
            List<MfeFile> files= mgr.getAllFilesStartWithPrefix(endpoint, bucket, prefix);
            return Response.ok().entity(files).build();
        } catch (Exception e) {
            return mgr.rsErrorResponse(Utils.toMfeFileMgrErrorInfo(e));
        }
    }

    @Override
    public Response getFile(String provider, String endpoint, String bucket, String key, SecurityContext securityContext) throws NotFoundException {
        IMfeFileMgr mgr= MfeFileMgrPool.getMgr(provider);
        if(null==mgr) return Utils.noProviderFoundResponse(provider);
        try {
            MfeFile file= mgr.getFile(endpoint, bucket, key);
            return Response.ok().entity(file).build();
        } catch (Exception e) {
            return mgr.rsErrorResponse(Utils.toMfeFileMgrErrorInfo(e));
        }
    }

    @Override
    public Response deleteAllFilesStartWithPrefix(String provider, String endpoint, String bucket, String prefix, SecurityContext securityContext) throws NotFoundException {
        IMfeFileMgr mgr= MfeFileMgrPool.getMgr(provider);
        if(null==mgr) return Utils.noProviderFoundResponse(provider);
        try {
            mgr.deleteFilesStartWithPrefix(endpoint, bucket, prefix);
            return Response.ok().build();
        } catch (Exception e) {
            return mgr.rsErrorResponse(Utils.toMfeFileMgrErrorInfo(e));
        }
    }

    @Override
    public Response deleteFile(String provider, String endpoint, String bucket, String key, SecurityContext securityContext) throws NotFoundException {
        IMfeFileMgr mgr= MfeFileMgrPool.getMgr(provider);
        if(null==mgr) return Utils.noProviderFoundResponse(provider);
        try {
            mgr.deleteFile(endpoint, bucket,key);
            return Response.ok().build();
        } catch (Exception e) {
            return mgr.rsErrorResponse(Utils.toMfeFileMgrErrorInfo(e));
        }
    }

    @Override
    public Response deleteFiles(String provider, String endpoint, String bucket, List<String> keys, SecurityContext securityContext) throws NotFoundException {
        IMfeFileMgr mgr= MfeFileMgrPool.getMgr(provider);
        if(null==mgr) return Utils.noProviderFoundResponse(provider);
        try {
            mgr.deleteFiles(endpoint, bucket,keys);
            return Response.ok().build();
        } catch (Exception e) {
            return mgr.rsErrorResponse(Utils.toMfeFileMgrErrorInfo(e));
        }
    }
}
