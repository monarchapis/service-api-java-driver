package com.monarchapis.driver.exception;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

@XmlRootElement(name = "error")
public class ApiError {
	private int code = 500;
	private String reason;
	private String message;
	private String developerMessage;
	private String errorCode;
	private String moreInfo;
	private DateTime time;

	public ApiError() {
		this.time = DateTime.now();
	}

	public ApiError(int status, String reason, String message, String developerMessage, String errorCode) {
		this(status, reason, message, developerMessage, errorCode, null, DateTime.now());
	}

	public ApiError(int status, String reason, String message, String developerMessage, String errorCode,
			String moreInfo) {
		this(status, reason, message, developerMessage, errorCode, moreInfo, DateTime.now());
	}

	public ApiError(int status, String reason, String message, String developerMessage, String errorCode,
			String moreInfo, DateTime time) {
		this.code = status;
		this.reason = reason;
		this.message = message;
		this.developerMessage = developerMessage;
		this.errorCode = errorCode;
		this.moreInfo = moreInfo;
		this.time = time;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ApiError code(int code) {
		setCode(code);
		return this;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public ApiError reason(String reason) {
		setReason(reason);
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ApiError message(String message) {
		setMessage(message);
		return this;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public ApiError developerMessage(String developerMessage) {
		setDeveloperMessage(developerMessage);
		return this;
	}

	public DateTime getTime() {
		return time;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public ApiError errorCode(String errorCode) {
		setErrorCode(errorCode);
		return this;
	}

	public String getMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

	public ApiError moreInfo(String moreInfo) {
		setMoreInfo(moreInfo);
		return this;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
