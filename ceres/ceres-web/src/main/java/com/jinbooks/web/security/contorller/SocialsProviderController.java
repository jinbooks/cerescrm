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

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.crypto.password.PasswordReciprocal;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.SocialsProvider;
import com.jinbooks.entity.dto.SocialsProviderPageDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.service.SocialsProviderService;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value={"/security/socialsprovider"})
public class SocialsProviderController {
	static final  Logger logger = LoggerFactory.getLogger(SocialsProviderController.class);

	@Autowired
	SocialsProviderService socialsProviderService;

	@GetMapping(value = { "/fetch" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Page<SocialsProvider>> fetch(@ParameterObject SocialsProviderPageDto dto, @CurrentUser UserInfo currentUser) {
		logger.debug("fetch {}",dto);

		LambdaQueryWrapper<SocialsProvider> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SocialsProvider::getBookId, currentUser.getWorkspaceId());

		return new Message<>(Message.SUCCESS, socialsProviderService.page(dto.build(), wrapper));
	}

	@GetMapping(value={"/query"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<SocialsProvider> query(@ParameterObject SocialsProviderPageDto dto,@CurrentUser UserInfo currentUser) {
		logger.debug("-query  : {}" , dto);
		LambdaQueryWrapper<SocialsProvider> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SocialsProvider::getBookId, currentUser.getWorkspaceId());
		if (ObjectUtils.isNotEmpty(socialsProviderService.list(wrapper))) {
			 return new Message<>(Message.SUCCESS);
		} else {
			 return new Message<>(Message.FAIL);
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<SocialsProvider> get(@PathVariable("id") String id) {
		SocialsProvider socialsProvider=socialsProviderService.getById(id);
		socialsProvider.setClientSecret(PasswordReciprocal.getInstance().decoder(socialsProvider.getClientSecret()));
		return new Message<>(socialsProvider);
	}

	@PostMapping(value={"/add"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<SocialsProvider> insert(@RequestBody  SocialsProvider socialsProvider,@CurrentUser UserInfo currentUser) {
		logger.debug("-Add  : {}" , socialsProvider);
		socialsProvider.setBookId(currentUser.getWorkspaceId());
		socialsProvider.setClientSecret(PasswordReciprocal.getInstance().encode(socialsProvider.getClientSecret()));
		if (socialsProviderService.save(socialsProvider)) {
			return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

	@PutMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<SocialsProvider> update(@RequestBody  SocialsProvider socialsProvider,@CurrentUser UserInfo currentUser) {
		logger.debug("-update  : {}" , socialsProvider);
		socialsProvider.setBookId(currentUser.getWorkspaceId());
		socialsProvider.setClientSecret(PasswordReciprocal.getInstance().encode(socialsProvider.getClientSecret()));
		if (socialsProviderService.updateById(socialsProvider)) {
		    return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

	@DeleteMapping(value={"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<SocialsProvider> delete(@RequestParam("ids") List<String> ids,@CurrentUser UserInfo currentUser) {
		logger.debug("-delete  ids : {} " , ids);
		if (socialsProviderService.removeByIds(ids)) {
			 return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

}
