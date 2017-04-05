package com.mfe.mfefilemgr.exception;


import com.mfe.mfefilemgr.restful.model.mfefilemgr.Provider;

public class MfeFileMgrException extends Exception {
    private static final long serialVersionUID = 479829418798909619L;
    public Provider provider;
    public String errorReason_;
    public String errorCode;
    public static MfeFileMgrException AK_SK_EMPTY = new MfeFileMgrException(Provider.MFEFILEMGR, "1", "AK or SK is empty.");

    public MfeFileMgrException(Provider provider, String code, String errorReason) {
        super("Exception code:" + code + " and errorReason:" + errorReason);
        this.provider = provider;
        this.errorCode = code;
        this.errorReason_ = errorReason;
    }

    public MfeFileMgrException(Provider provider, String code, String errorReason, Throwable cause) {
        super(cause);
        this.provider = provider;
        errorCode = code;
        errorReason_ = errorReason;
    }
}
