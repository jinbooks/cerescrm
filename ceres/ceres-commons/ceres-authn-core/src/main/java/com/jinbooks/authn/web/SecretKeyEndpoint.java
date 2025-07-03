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
 



package com.jinbooks.authn.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jinbooks.authn.LoginSecretKey;
import com.jinbooks.authn.dto.LoginConfigDto;
import com.jinbooks.authn.jwt.service.AuthTokenService;
import com.jinbooks.authn.secretkey.SecretKeyManager;
import com.jinbooks.entity.Message;

@RestController
@RequestMapping(value = "/secretKey")
public class SecretKeyEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(SecretKeyEndpoint.class);

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	SecretKeyManager secretKeyManager;

	@GetMapping("/get")
	public Message<LoginConfigDto> get() {
		logger.debug("/secretKey.");
		LoginSecretKey  loginSecretKey = secretKeyManager.getSecretKey();
		LoginConfigDto conf = new LoginConfigDto();
		conf.setState(authTokenService.genRandomJwt());
		conf.setSecretKey(loginSecretKey.getSecretKey());
		conf.setSecretPublicKey(loginSecretKey.getPublicKey());
		return new Message<>(conf);
	}

}
