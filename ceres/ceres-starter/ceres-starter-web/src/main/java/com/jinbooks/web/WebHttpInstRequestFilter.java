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

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import com.jinbooks.configuration.ApplicationConfig;
import com.jinbooks.constants.ConstsHttpHeader;
import com.jinbooks.entity.Institutions;
import com.jinbooks.persistence.service.InstitutionsService;

/**
 * 多租户机构读取Filter
 */
public class WebHttpInstRequestFilter  extends GenericFilterBean {
	static final  Logger logger = LoggerFactory.getLogger(WebHttpInstRequestFilter.class);

	InstitutionsService institutionsService;

	ApplicationConfig applicationConfig;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		logger.trace("WebHttpInstRequestFilter");
		HttpServletRequest request= ((HttpServletRequest)servletRequest);

		if(request.getSession().getAttribute(WebConstants.CURRENT_INST) == null) {
			if(logger.isTraceEnabled()) {WebContext.printRequest(request);}
			String host = request.getHeader(ConstsHttpHeader.HEADER_HOSTNAME);
			logger.trace("hostname {}",host);
			if(StringUtils.isEmpty(host)) {
				host = request.getHeader(ConstsHttpHeader.HEADER_HOST);
				logger.trace("host {}",host);
			}
			if(host.indexOf(":")> -1 ) {
				host = host.split(":")[0];
				logger.trace("domain split {}",host);
			}
			Institutions institution = institutionsService.getByInstIdOrDomain(host);
			logger.trace("institution {}" ,institution);
			request.getSession().setAttribute(WebConstants.CURRENT_INST, institution);

			String origin = request.getHeader(ConstsHttpHeader.HEADER_ORIGIN);
			if(StringUtils.isEmpty(origin)) {
				origin = applicationConfig.getFrontendUri();
			}
			logger.trace("origin {}" ,origin);
		}
        chain.doFilter(servletRequest, servletResponse);
	}

	public WebHttpInstRequestFilter(InstitutionsService institutionsService,ApplicationConfig applicationConfig) {
		super();
		this.institutionsService = institutionsService;
		this.applicationConfig = applicationConfig;
	}

}
