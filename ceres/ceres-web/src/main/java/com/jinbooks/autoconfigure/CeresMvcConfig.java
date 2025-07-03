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
 

 

 

package com.jinbooks.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jinbooks.authn.provider.AbstractAuthenticationProvider;
import com.jinbooks.authn.web.interceptor.PermissionInterceptor;

@EnableWebMvc
@AutoConfiguration
public class CeresMvcConfig implements WebMvcConfigurer {
    private static final  Logger logger = LoggerFactory.getLogger(CeresMvcConfig.class);
    
    @Autowired
    AbstractAuthenticationProvider authenticationProvider ;
    
    @Autowired
    PermissionInterceptor permissionInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns 用于添加拦截规则 ， 先把所有路径都加入拦截， 再一个个排除
        //excludePathPatterns 表示改路径不用拦截
        logger.debug("add HttpJwtEntryPoint");

        permissionInterceptor.setMgmt(true);
        
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/dashboard/**")
                .addPathPatterns("/orgs/**")
                .addPathPatterns("/users/**")
                .addPathPatterns("/userposts/**")
                .addPathPatterns("/organizationUser/**")
                .addPathPatterns("/posts/**")
                .addPathPatterns("/session/**")
                .addPathPatterns("/apps/**")
                .addPathPatterns("/accounts/**")
                .addPathPatterns("/accountsUnited/**")
                .addPathPatterns("/idm/**")
                .addPathPatterns("/idm/**/**")
                
                
                .addPathPatterns("/access/**")
                .addPathPatterns("/access/**/**")
                
                .addPathPatterns("/permissions/**")
                .addPathPatterns("/permissions/**/**")
                
                
                .addPathPatterns("/openapi/resources/**")
                .addPathPatterns("/openapi/privileges/**")
                .addPathPatterns("/openapi/orgs/**")                
                
                .addPathPatterns("/security/**")
                .addPathPatterns("/security/configPasswordEncrypt/**")
                .addPathPatterns("/security/grading/**")
                .addPathPatterns("/security/grading/**/**")
                .addPathPatterns("/security/grading/**/**/**")
                
                .addPathPatterns("/config/**")
                .addPathPatterns("/config/**/**")
                
                .addPathPatterns("/historys/**")
                .addPathPatterns("/historys/**/**")
                
                .addPathPatterns("/institutions/**")
                .addPathPatterns("/localization/**")
                
                .addPathPatterns("/filestorage/upload/")
                
                .addPathPatterns("/logout")
                .addPathPatterns("/logout/**")
                
                //open
                .addPathPatterns("/open/func/**")
                .excludePathPatterns("/open/func/list")
                .excludePathPatterns("/productGrant/license/**")
                ;
        
        logger.debug("add PermissionAdapter");
        
    }
    
}
