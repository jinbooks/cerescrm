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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.entity.permissions.PermissionWorkspace;
import com.jinbooks.entity.permissions.dto.PermissionWorkspacePageDto;
import com.jinbooks.persistence.mapper.PermissionWorkspaceMapper;
import com.jinbooks.persistence.service.PermissionWorkspaceService;

@Repository
public class PermissionWorkspaceServiceImpl  extends ServiceImpl<PermissionWorkspaceMapper,PermissionWorkspace> implements PermissionWorkspaceService {
	static final  Logger logger = LoggerFactory.getLogger(PermissionWorkspaceServiceImpl.class);

	@Autowired
	PermissionWorkspaceMapper permissionWorkspaceMapper;

	@Override
	public Page<PermissionWorkspace> userAccessWorkspace(Page page, PermissionWorkspacePageDto dto) {
		return permissionWorkspaceMapper.userAccessWorkspace(page, dto);
	}

	
	@Override
	public Page<PermissionWorkspace> userNotAccessWorkspace(Page page, PermissionWorkspacePageDto dto) {
		return permissionWorkspaceMapper.userNotAccessWorkspace(page, dto);
	}


}
