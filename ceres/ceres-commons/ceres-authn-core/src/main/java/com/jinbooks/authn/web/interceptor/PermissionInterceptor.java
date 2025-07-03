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
 

 

 

package com.jinbooks.authn.web.interceptor;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.jinbooks.authn.SignedPrincipal;
import com.jinbooks.authn.jwt.service.AuthTokenService;
import com.jinbooks.authn.session.SessionManager;
import com.jinbooks.authn.web.AuthorizationUtils;
import com.jinbooks.configuration.ApplicationConfig;
/**
 * 登录认证判断
 * 
 * @author Crystal.Sea
 *
 */
@Component
public class PermissionInterceptor  implements AsyncHandlerInterceptor  {
	private static final Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);	
	/**
	 * 系统配置
	 */
	@Autowired
	ApplicationConfig applicationConfig;
	/**
	 * 会话管理
	 */
	@Autowired
	SessionManager sessionManager;
	/**
	 * 认证令牌服务
	 */
	@Autowired
	AuthTokenService authTokenService ;
	/**
	 * 是否管理端
	 */
	boolean mgmt = false;
	
	/*
	 * 请求前处理
	 *  (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		 logger.trace("Permission Interceptor .");
		 logger.trace("request URL {} ",request.getRequestURI());
		 //认证信息识别
		 AuthorizationUtils.authenticate(request, authTokenService, sessionManager);
		 SignedPrincipal principal = AuthorizationUtils.getPrincipal();//读取认证当事人
		//判断用户是否登录,判断用户是否登录用户
		if(principal == null){
			logger.trace("No Authentication ... forward to /auth/entrypoint , request URI {}" , request.getRequestURI());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/auth/entrypoint");
		    dispatcher.forward(request, response);
		    return false;
		}
		
		return true;
	}

	/**
	 * 设置管理
	 * @param mgmt
	 */
	public void setMgmt(boolean mgmt) {
		this.mgmt = mgmt;
		logger.debug("Permission for ADMINISTRATORS {}", this.mgmt);
	}
	
}
