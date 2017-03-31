package com.mfe.qnmgr.api;

import com.mfe.qnmgr.business.QnResMgr;
import com.mfe.qnmgr.restful.model.qnmgr.QnDirInfo;
import com.mfe.qnmgr.restful.qnmgrserver.api.DirApiService;
import com.mfe.qnmgr.restful.qnmgrserver.api.NotFoundException;
import com.mfe.qnmgr.utils.qn.Utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-03-31T18:11:08.535+08:00")
public class DirApiServiceImpl extends DirApiService {
    @Override
    public Response listDir(String zone, String bucket, String directory, SecurityContext securityContext) throws NotFoundException {
        try {
            QnDirInfo dirInfo= QnResMgr.getInstance().getDir(zone, bucket, directory);
            return Response.ok().entity(dirInfo).build();
        } catch (Exception e) {
            return Utils.rsErrorResponse(e);
        }
    }

    @Override
    public Response listRootDir(String zone, String bucket, SecurityContext securityContext) throws NotFoundException {
        try {
            QnDirInfo dirInfo= QnResMgr.getInstance().getFilesAndCommonPrefix(zone, bucket,"", "/");
            return Response.ok().entity(dirInfo).build();
        } catch (Exception e) {
            return Utils.rsErrorResponse(e);
        }
    }

    @Override
    public Response deleteDir(String zone, String bucket, String directory, SecurityContext securityContext) throws NotFoundException {
        try {
            QnResMgr.getInstance().deleteDir(zone, bucket, directory);
            return Response.ok().build();
        } catch (Exception e) {
            return Utils.rsErrorResponse(e);
        }
    }
}
