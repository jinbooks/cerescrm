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
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.jinbooks.authn.secretkey.SecretKeyManager;
import com.jinbooks.authn.secretkey.impl.InMemorySecretKeyManager;
import com.jinbooks.authn.secretkey.impl.RedisSecretKeyManager;
import com.jinbooks.authn.session.SessionManager;
import com.jinbooks.authn.session.impl.SessionManagerImpl;
import com.jinbooks.authn.web.HttpSessionListenerAdapter;
import com.jinbooks.authn.web.SavedRequestAwareAuthenticationSuccessHandler;
import com.jinbooks.configuration.ApplicationConfig;
import com.jinbooks.entity.config.ConfigLoginPolicy;
import com.jinbooks.persistence.redis.connection.RedisConnectionFactory;
import com.jinbooks.persistence.service.ConfigLoginPolicyService;
import com.jinbooks.persistence.service.HistoryLoginService;
import com.jinbooks.persistence.service.SessionListService;

/**
 * 会话自动配置
 *
 * @author Crystal.Sea
 *
 */
@AutoConfiguration
public class SessionAutoConfiguration {
    private static final  Logger logger = LoggerFactory.getLogger(SessionAutoConfiguration.class);

    @Bean(name = "savedRequestSuccessHandler")
    SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler();
    }

    /**
     * @Description:
     * @Param: [applicationConfig, configLoginPolicyService, redisConnFactory, sessionListService, historyLoginService]
     * @return: org.maxkey.authn.session.SessionManager
     * @Author: xZen
     * @Date: 2024/11/25 15:57
     */
    @Bean(name = "sessionManager")
    SessionManager sessionManager(
    		ApplicationConfig applicationConfig,
            ConfigLoginPolicyService configLoginPolicyService,
            RedisConnectionFactory redisConnFactory,
            SessionListService sessionListService,
            HistoryLoginService historyLoginService
    ) {
    	ConfigLoginPolicy configLoginPolicy = configLoginPolicyService.getConfigLoginPolicy();
    	logger.debug("session persistence {} , timeout {}" ,applicationConfig.getCached(), configLoginPolicy.getSessionValidity() * 3600);
    	return  new SessionManagerImpl(applicationConfig.getCached(),configLoginPolicy.getSessionValidity() * 3600,redisConnFactory, sessionListService,historyLoginService);
    }

    @Bean(name = "secretKeyManager")
    SecretKeyManager secretKeyManager(
    		ApplicationConfig applicationConfig,
            RedisConnectionFactory redisConnFactory) {
    	logger.debug("init secretKeyManager.");
    	SecretKeyManager secretKeyManager = null;
    	if (applicationConfig.isCachedRedis()) {
        	secretKeyManager = new RedisSecretKeyManager(redisConnFactory);
            logger.debug("RedisSecretKeyManager");
        }else {
        	secretKeyManager = new InMemorySecretKeyManager();
            logger.debug("InMemorySecretKeyManager");
        }

    	return  secretKeyManager;
    }




    @Bean
    HttpSessionListenerAdapter httpSessionListenerAdapter() {
        return new HttpSessionListenerAdapter();
    }
}
