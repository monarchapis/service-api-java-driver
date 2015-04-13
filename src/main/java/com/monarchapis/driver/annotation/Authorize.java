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

package com.monarchapis.driver.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class or method as a secured operation and signals the API driver to
 * authorize the API request before it is processed. Annotated methods override
 * the class.
 * 
 * @author Phil Kedy
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Inherited
public @interface Authorize {
	/**
	 * An array of permissions that the client must have at least one of in
	 * order to invoke the annotated class or method.
	 * 
	 * @return the client permissions
	 */
	String[] client() default {};

	/**
	 * A flag that when true, forces the method to only be called by
	 * authenticated users. This is useful for when you don't require the user
	 * to have particular permissions. If delegated or claims are specified,
	 * this is not required to be set to true.
	 * 
	 * @return true if an authentication user is required
	 */
	boolean user() default false;

	/**
	 * An array of permissions that the user must have delegated at least one of
	 * to the application in order to invoke the annotated class or method. This
	 * would apply to asserting that a provided access token (e.g. bearer token)
	 * has the necessary permissions.
	 * 
	 * @return the client permissions
	 */
	String[] delegated() default {};

	/**
	 * An array of claims that the user must have at least one of in order to
	 * invoke the annotated class or method.
	 * 
	 * @return the client permissions
	 */
	Claim[] claims() default {};
}
