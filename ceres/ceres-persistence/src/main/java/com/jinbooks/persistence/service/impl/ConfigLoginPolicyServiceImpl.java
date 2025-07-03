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
 





package com.jinbooks.persistence.service.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jinbooks.entity.config.ConfigLoginPolicy;
import com.jinbooks.persistence.mapper.ConfigLoginPolicyMapper;
import com.jinbooks.persistence.service.ConfigLoginPolicyService;

import org.springframework.stereotype.Service;

@Service
public class ConfigLoginPolicyServiceImpl extends ServiceImpl<ConfigLoginPolicyMapper,ConfigLoginPolicy> implements ConfigLoginPolicyService {
	private static final Logger logger = LoggerFactory.getLogger(ConfigLoginPolicyServiceImpl.class);

	static final String CONFIG_LOGIN_POLICY_KEY = "CONFIG_LOGIN_POLICY_KEY";

	@Autowired
	ConfigLoginPolicyMapper configLoginPolicyMapper;

    //Cache ConfigLoginPolicy in memory ONE_HOUR
    static final Cache<String,  ConfigLoginPolicy> configLoginPolicyStore =
            Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build();

	public ConfigLoginPolicyMapper getMapper() {
		return configLoginPolicyMapper;
	}

	@Override
	public ConfigLoginPolicy getConfigLoginPolicy() {
		ConfigLoginPolicy configLoginPolicy = configLoginPolicyStore.getIfPresent(CONFIG_LOGIN_POLICY_KEY);
        if (configLoginPolicy == null) {
			LambdaQueryWrapper<ConfigLoginPolicy> wrapper = new LambdaQueryWrapper<>();
			wrapper.isNotNull(ConfigLoginPolicy::getId);
			configLoginPolicy = super.getOne(wrapper);
            configLoginPolicyStore.put(CONFIG_LOGIN_POLICY_KEY,configLoginPolicy);
            logger.debug("get ConfigLoginPolicy : {}" , configLoginPolicy);
        }
        return configLoginPolicy;
    }



}
