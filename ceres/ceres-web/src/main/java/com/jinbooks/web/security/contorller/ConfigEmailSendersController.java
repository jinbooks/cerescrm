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
 





package com.jinbooks.web.security.contorller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.crypto.password.PasswordReciprocal;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.config.ConfigEmailSenders;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.service.ConfigEmailSendersService;

@RestController
@RequestMapping(value={"/security/emailsenders"})
public class ConfigEmailSendersController {
	static final Logger logger = LoggerFactory.getLogger(ConfigEmailSendersController.class);

	@Autowired
	ConfigEmailSendersService configEmailSendersService;

	@GetMapping(value={"/get"})
	public Message<ConfigEmailSenders> get(@CurrentUser UserInfo currentUser){
		ConfigEmailSenders emailSenders = configEmailSendersService.getById(currentUser.getWorkspaceId());
		if(emailSenders != null && StringUtils.isNotBlank(emailSenders.getCredentials())) {
			emailSenders.setCredentials(PasswordReciprocal.getInstance().decoder(emailSenders.getCredentials()));
		}else {
			emailSenders =new ConfigEmailSenders();
			emailSenders.setProtocol("smtp");
			emailSenders.setEncoding("utf-8");
		}
		return new Message<>(emailSenders);
	}

	@PutMapping(value={"/update"})
	public Message<ConfigEmailSenders> update( @RequestBody ConfigEmailSenders emailSenders,@CurrentUser UserInfo currentUser,BindingResult result) {
		logger.debug("update emailSenders : {}",emailSenders);
		emailSenders.setBookId(currentUser.getWorkspaceId());
		emailSenders.setCredentials(PasswordReciprocal.getInstance().encode(emailSenders.getCredentials()));
		if(StringUtils.isBlank(emailSenders.getId())) {
			emailSenders.setId(emailSenders.getBookId());
			if(configEmailSendersService.save(emailSenders)) {
				return new Message<>(Message.SUCCESS);
			}else {
				return new Message<>(Message.ERROR);
			}
		}else {
			if(configEmailSendersService.updateById(emailSenders)) {
				return new Message<>(Message.SUCCESS);
			}else {
				return new Message<>(Message.ERROR);
			}
		}

	}
}
