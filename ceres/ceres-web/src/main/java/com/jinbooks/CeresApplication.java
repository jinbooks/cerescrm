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
 





package com.jinbooks;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.jinbooks.web.InitializeContext;
import com.jinbooks.web.ProductEnvironment;
import com.jinbooks.web.WebContext;

/**
 * Ceres系统启动入口
 * @author JinBooks
 *
 */
@SpringBootApplication
public class CeresApplication extends SpringBootServletInitializer {
	private static final Logger _logger = LoggerFactory.getLogger(CeresApplication.class);

	public static void main(String[] args) {
		_logger.info("Start Ceres CRM Application ...");
	    ProductEnvironment.listEnvVars();

		new InitializeContext(SpringApplication.run(CeresApplication.class, args)).init();

		_logger.info("Ceres CRM at {}" , new DateTime());
		_logger.info("Ceres CRM Port {}",WebContext.getServerPort());
		_logger.info("Ceres CRM started.");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CeresApplication.class);
	}
	
}
