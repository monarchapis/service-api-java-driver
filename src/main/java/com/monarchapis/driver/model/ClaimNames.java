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

package com.monarchapis.driver.model;

public interface ClaimNames {
	public static final String ID = "jti";
	public static final String SUBJECT = "sub";

	public static final String APPLICATION = "http://monarchapis.com/claims/application";
	public static final String CLIENT = "http://monarchapis.com/claims/client";
	public static final String PROVIDER = "http://monarchapis.com/claims/provider";
	public static final String TOKEN = "http://monarchapis.com/claims/token";
	public static final String PRINCIPAL = "http://monarchapis.com/claims/principal";
}
