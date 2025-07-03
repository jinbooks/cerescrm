/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
 

 

 

package com.jinbooks.ldap.constants;

import java.util.Arrays;
import java.util.List;

/**
 * GroupOfNames objectclass attribute
 * top
 * @author shimingxy
 *
 */
public class GroupOfNames {
	
	public static final List<String> OBJECTCLASSES = Arrays.asList("top", GroupOfNames.OBJECTCLASS);
	public static final String OBJECTCLASS				 	 = "groupOfNames";
	
	public static final String DISTINGUISHEDNAME 			 = "distinguishedname";
	public static final String CN                            = "cn";
	public static final String MEMBER                  		 = "member";
	public static final String BUSINESSCATEGORY              = "businessCategory";
	public static final String SEEALSO                       = "seeAlso";
	public static final String OWNER                         = "owner";
	public static final String OU                            = "ou";
	public static final String O                           	 = "o";
	public static final String DESCRIPTION                   = "description";
}
