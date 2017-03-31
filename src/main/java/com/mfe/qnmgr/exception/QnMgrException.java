package com.mfe.qnmgr.exception;


public class QnMgrException extends Exception {
	private static final long serialVersionUID = 479829418798909619L;
	public String errorReason_;

	public int errorCode;

	public QnMgrException(int code, String errorReason) {
		super("Exception code:" + code + " and errorReason:" + errorReason);
		errorCode=code;
		errorReason_ = errorReason;
	}

	public QnMgrException(int code, String errorReason, Throwable cause) {
		super(cause);
		errorCode=code;
		errorReason_ = errorReason;
	}
}
