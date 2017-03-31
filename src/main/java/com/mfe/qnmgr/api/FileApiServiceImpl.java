package com.mfe.qnmgr.api;

import com.mfe.qnmgr.business.QnResMgr;
import com.mfe.qnmgr.restful.model.qnmgr.QnFile;
import com.mfe.qnmgr.restful.qnmgrserver.api.FileApiService;
import com.mfe.qnmgr.restful.qnmgrserver.api.NotFoundException;
import com.mfe.qnmgr.utils.qn.Utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-03-30T20:44:57.384+08:00")
public class FileApiServiceImpl extends FileApiService {


    @Override
    public Response getAllFilesInBucket(String zone, String bucket, SecurityContext securityContext) throws NotFoundException {
        try {
            List<QnFile> files= QnResMgr.getInstance().getAllFilesInBucket(zone, bucket);
            return Response.ok().entity(files).build();
        } catch (Exception e) {
            return Utils.rsErrorResponse(e);
        }
    }

    @Override
    public Response getAllFilesStartWithPrefix(String zone, String bucket, String prefix, SecurityContext securityContext) throws NotFoundException {
        try {
            List<QnFile> files= QnResMgr.getInstance().getAllFilesStartWithPrefix(zone, bucket, prefix);
            return Response.ok().entity(files).build();
        } catch (Exception e) {
            return Utils.rsErrorResponse(e);
        }
    }

    @Override
    public Response getFile(String zone, String bucket, String key, SecurityContext securityContext) throws NotFoundException {
        try {
            QnFile file= QnResMgr.getInstance().getFile(zone, bucket, key);
            return Response.ok().entity(file).build();
        } catch (Exception e) {
            return Utils.rsErrorResponse(e);
        }
    }

    @Override
    public Response deleteAllFilesStartWithPrefix(String zone, String bucket, String prefix, SecurityContext securityContext) throws NotFoundException {
        try {
            QnResMgr.getInstance().deleteFilesStartWithPrefix(zone, bucket, prefix);
            return Response.ok().build();
        } catch (Exception e) {
            return Utils.rsErrorResponse(e);
        }
    }

    @Override
    public Response deleteFile(String zone, String bucket, String key, SecurityContext securityContext) throws NotFoundException {
        try {
            QnResMgr.getInstance().deleteFile(zone, bucket,key);
            return Response.ok().build();
        } catch (Exception e) {
            return Utils.rsErrorResponse(e);
        }
    }

    @Override
    public Response deleteFiles(String zone, String bucket, List<String> keys, SecurityContext securityContext) throws NotFoundException {
        try {
            QnResMgr.getInstance().deleteFiles(zone, bucket,keys);
            return Response.ok().build();
        } catch (Exception e) {
            return Utils.rsErrorResponse(e);
        }
    }
}
