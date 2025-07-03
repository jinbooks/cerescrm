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
 

 

 

package com.jinbooks.web;

import java.io.IOException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.jinbooks.constants.ContentType;

@Component
public class HttpResponseAdapter {

    
    public void setContentType(
                        HttpServletResponse response,
                        String format) {
        
        if(format == null || format.equalsIgnoreCase("") || format.equalsIgnoreCase(HttpResponseConstants.FORMAT_TYPE.XML)) {
            response.setContentType(ContentType.APPLICATION_XML_UTF8);
        }else {
            response.setContentType(ContentType.APPLICATION_JSON_UTF8);
        }
    }
    
	public void write(HttpServletResponse response,String content, String format) {

		setContentType(response , format);
		
        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        
        ServletOutputStream out = null;
            try {
				out = response.getOutputStream();
            	// write the data out
                out.write(content.getBytes());
                out.flush();
            }catch (IOException e) {
				e.printStackTrace();
			} finally {
                try {
                	if(out != null) {
                		out.close();
                	}
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
	}
    
}
