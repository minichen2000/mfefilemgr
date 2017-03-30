package com.mfe.qnmgr.apiImpls;

import com.mfe.qnmgr.restful.qnmgrserver.api.ApiResponseMessage;
import com.mfe.qnmgr.restful.qnmgrserver.api.BucketsApiService;
import com.mfe.qnmgr.restful.qnmgrserver.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-03-30T14:56:29.035+08:00")
public class BucketsApiServiceImpl extends BucketsApiService {
    @Override
    public Response getAllBuckets(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getBucketByName(String bucketname, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
