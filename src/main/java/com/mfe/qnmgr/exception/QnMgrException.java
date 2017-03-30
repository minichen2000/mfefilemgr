package com.mfe.qnmgr.exception;


public class QnMgrException extends Exception {
	private static final long serialVersionUID = 479829418798909619L;
	public QnMgrExceptionType type_;

	public String errorReason_;

	public ErrorCode errorCode;

	public QnMgrException(QnMgrExceptionType type, String errorReason) {
		super("Exception type:" + type + " and errorReason:" + errorReason);
		type_ = type;
		errorReason_ = errorReason;
	}

	public QnMgrException(QnMgrExceptionType type, String errorReason, Throwable cause) {
		super(cause);
		type_ = type;
		errorReason_ = errorReason;
	}

	public QnMgrException(QnMgrExceptionType type, ErrorCode errorCode) {
		super(errorCode.getDescription());
		type_ = type;
		errorReason_ = errorCode.getDescription();
	}

	public QnMgrException(ErrorCode errorCode) {
		super(errorCode.getDescription());
		this.errorCode = errorCode;
		errorReason_ = errorCode.getDescription();
	}
}
