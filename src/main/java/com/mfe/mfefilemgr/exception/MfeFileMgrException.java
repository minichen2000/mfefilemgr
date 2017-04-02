package com.mfe.mfefilemgr.exception;


import com.mfe.mfefilemgr.restful.model.mfefilemgr.Provider;

public class MfeFileMgrException extends Exception {
	private static final long serialVersionUID = 479829418798909619L;
	public Provider provider;
	public String errorReason_;
	public int errorCode;

	public MfeFileMgrException(Provider provider, int code, String errorReason) {
		super("Exception code:" + code + " and errorReason:" + errorReason);
		this.provider=provider;
		this.errorCode=code;
		this.errorReason_ = errorReason;
	}

	public MfeFileMgrException(Provider provider, int code, String errorReason, Throwable cause) {
		super(cause);
		this.provider=provider;
		errorCode=code;
		errorReason_ = errorReason;
	}
}
