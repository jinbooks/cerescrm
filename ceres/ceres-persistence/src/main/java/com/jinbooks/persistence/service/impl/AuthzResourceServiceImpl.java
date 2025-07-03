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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.entity.dto.QueryAppResourceDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.permissions.Roles;
import com.jinbooks.entity.permissions.Resources;
import com.jinbooks.persistence.mapper.AuthzResourceMapper;
import com.jinbooks.persistence.service.AuthzResourceService;

@Repository
public class AuthzResourceServiceImpl   extends ServiceImpl<AuthzResourceMapper,UserInfo> implements AuthzResourceService {
	private static final Logger logger = LoggerFactory.getLogger(AuthzResourceServiceImpl.class);

	@Autowired
	AuthzServiceImpl authzService;

	@Autowired
	AuthzResourceMapper authzResourceMapper;

	@Autowired
    FileStorageServiceImpl fileStorageService;

	public AuthzResourceMapper getMapper() {
		return authzResourceMapper;
	}

	/**
	 * 根据主体获取用户对应得应用资源清单
	 * @param user
	 * @param app
	 * @return 资源清单列表
	 */
	@Override
    public Set<Resources> getResourcesBySubject(UserInfo user){
    	logger.debug("user {} , app {}",user);

    	List<CompletableFuture<List<Resources>>> futures = new ArrayList<>();
    	//根据用户读取应用资源
    	QueryAppResourceDto dto = new QueryAppResourceDto(user.getId());

    	//根据用户组获取应用资源
    	List<Roles> listRole = authzService.queryRolesByMembers(user);
    	for(Roles r : listRole) {
    		dto.getRoleIds().add(r.getId());
    	}
    	if (CollectionUtils.isNotEmpty(dto.getRoleIds())) {
	    	CompletableFuture<List<Resources>> subjectRoleResourcesFuture = CompletableFuture.supplyAsync(() -> {
	    		return queryResourcesByRoleId(dto);
	    	});
	    	futures.add(subjectRoleResourcesFuture);
    	}

        @SuppressWarnings("unchecked")
		CompletableFuture<List<Resources>>[] completableFutures = futures.toArray(new CompletableFuture[futures.size()]);

        //合并数据并去重
        CompletableFuture<Set<Resources>> completableFuture =
        		CompletableFuture.allOf(completableFutures).thenApply(result -> {
        			Set<Resources> resourcesList = new HashSet<>();
                	for (CompletableFuture<List<Resources>> future : completableFutures) {
                		resourcesList.addAll(future.join());
                	}
                	return  resourcesList;
                });

    	return completableFuture.join();
    }


	/**
	 * 根据组列表获取资源清单
	 * @param dto
	 * @return
	 */
	@Override
	public List<Resources> queryResourcesByRoleId(QueryAppResourceDto dto) {
		return getMapper().queryResourcesByRoleId(dto);
	}

}
