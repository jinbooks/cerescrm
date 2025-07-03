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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.entity.permissions.Permission;
import com.jinbooks.persistence.mapper.PermissionMapper;
import com.jinbooks.persistence.service.PermissionService;

@Repository
public class PermissionServiceImpl  extends ServiceImpl<PermissionMapper,Permission> implements PermissionService {
	static final  Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

	@Autowired
	PermissionMapper permissionMapper;

	public PermissionMapper getMapper() {
		return permissionMapper;
	}

	public boolean insertGroupPrivileges(List<Permission> permissionsList) {
	    return getMapper().insertPermissions(permissionsList)>0;
	}

	public boolean deleteGroupPrivileges(List<Permission> permissionsList) {
	     return getMapper().deletePermissions(permissionsList)>=0;
	 }

    public List<Permission> queryPermissions(Permission rolePermissions){
        return getMapper().queryPermissions(rolePermissions);
    }

}
