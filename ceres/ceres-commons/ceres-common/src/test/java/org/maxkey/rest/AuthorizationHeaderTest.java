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
 

 

 

package org.maxkey.rest;

import org.junit.Test;

import com.jinbooks.util.AuthorizationHeader;
import com.jinbooks.util.AuthorizationHeaderUtils;

public class AuthorizationHeaderTest {
	
	@Test
	public void test()  {
		
		String basic =AuthorizationHeaderUtils.createBasic("Aladdin", "open sesame");
		System.out.println(basic);
		
		String ahc_basic ="Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==";
		System.out.println(AuthorizationHeaderUtils.resolve(ahc_basic));
		
		AuthorizationHeader ahc =new AuthorizationHeader("Aladdin");
		System.out.println(ahc.transform());
		
		System.out.println(AuthorizationHeaderUtils.resolve(ahc.transform()));
		
	}
}
