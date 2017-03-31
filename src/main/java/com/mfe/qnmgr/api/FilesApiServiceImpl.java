package com.mfe.qnmgr.api;

import com.mfe.qnmgr.business.QnResMgr;
import com.mfe.qnmgr.exception.QnMgrException;
import com.mfe.qnmgr.restful.model.qnmgr.QnDirInfo;
import com.mfe.qnmgr.restful.model.qnmgr.QnFile;
import com.mfe.qnmgr.restful.model.qnmgr.QnmgrErrorInfo;
import com.mfe.qnmgr.restful.qnmgrserver.api.ApiResponseMessage;
import com.mfe.qnmgr.restful.qnmgrserver.api.FilesApiService;
import com.mfe.qnmgr.restful.qnmgrserver.api.NotFoundException;
import com.mfe.qnmgr.utils.qn.Utils;
import com.qiniu.common.QiniuException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-03-30T20:44:57.384+08:00")
public class FilesApiServiceImpl extends FilesApiService {


    @Override
    public Response getAllFiles(String zone, String bucket, SecurityContext securityContext) throws NotFoundException {
        try {
            List<QnFile> files= QnResMgr.getInstance().getFiles(zone, bucket);
            return Response.ok().entity(files).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(Utils.toQnmgrErrorInfo(e)).build();
        }
    }
    @Override
    public Response listDir(String zone, String bucket, String directory, SecurityContext securityContext) throws NotFoundException {
        try {
            QnDirInfo dirInfo= QnResMgr.getInstance().getDir(zone, bucket,directory, "/");
            return Response.ok().entity(dirInfo).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(Utils.toQnmgrErrorInfo(e)).build();
        }
    }

    @Override
    public Response deleteFile(String zone, String bucket, String key, SecurityContext securityContext) throws NotFoundException {
        try {
            QnResMgr.getInstance().deleteFile(zone, bucket,key);
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(Utils.toQnmgrErrorInfo(e)).build();
        }
    }

    @Override
    public Response deleteFiles(String zone, String bucket, List<String> keys, SecurityContext securityContext) throws NotFoundException {
        return null;
    }
}
