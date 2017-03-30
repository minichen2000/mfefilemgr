package com.mfe.qnmgr.api;

import com.mfe.qnmgr.business.QnResMgr;
import com.mfe.qnmgr.exception.QnMgrException;
import com.mfe.qnmgr.restful.model.qnmgr.QnDirInfo;
import com.mfe.qnmgr.restful.model.qnmgr.QnFile;
import com.mfe.qnmgr.restful.model.qnmgr.QnmgrErrorInfo;
import com.mfe.qnmgr.restful.qnmgrserver.api.ApiResponseMessage;
import com.mfe.qnmgr.restful.qnmgrserver.api.FilesApiService;
import com.mfe.qnmgr.restful.qnmgrserver.api.NotFoundException;

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
            QnmgrErrorInfo errorInfo=new QnmgrErrorInfo();
            errorInfo.setCode("-1");
            errorInfo.setMessage(e.toString());
            return Response.serverError().entity(errorInfo).build();
        }
    }
    @Override
    public Response listDir(String zone, String bucket, String directory, SecurityContext securityContext) throws NotFoundException {
        try {
            QnDirInfo dirInfo= QnResMgr.getInstance().getDir(zone, bucket,directory, "/");
            return Response.ok().entity(dirInfo).build();
        } catch (Exception e) {
            e.printStackTrace();
            QnmgrErrorInfo errorInfo=new QnmgrErrorInfo();
            errorInfo.setCode("-1");
            errorInfo.setMessage(e.toString());
            return Response.serverError().entity(errorInfo).build();
        }
    }
}
