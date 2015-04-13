/*
 * Copyright (C) 2015 CapTech Ventures, Inc.
 * (http://www.captechconsulting.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monarchapis.driver.exception;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

/**
 * Represents an API error to be serialized back to the client.
 * 
 * @author Phil Kedy
 */
@XmlRootElement(name = "error")
public class ApiError {
	/**
	 * The HTTP status code.
	 */
	private int status = 500;

	/**
	 * The error reason.
	 */
	private String reason;

	/**
	 * The error message that could be display to the end user.
	 */
	private String message;

	/**
	 * The error message that is intended for the developer to troubleshoot.
	 */
	private String developerMessage;

	/**
	 * The error code to pinpoint the source of the error.
	 */
	private String errorCode;

	/**
	 * The optional URL the developer can visit for more information.
	 */
	private String moreInfo;

	/**
	 * The date and time of the error so the API developer can inspect
	 * application logs.
	 */
	private DateTime time;

	/**
	 * The default constructor.
	 */
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
		this.status = status;
		this.reason = reason;
		this.message = message;
		this.developerMessage = developerMessage;
		this.errorCode = errorCode;
		this.moreInfo = moreInfo;
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int code) {
		this.status = code;
	}

	public ApiError code(int code) {
		setStatus(code);
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
