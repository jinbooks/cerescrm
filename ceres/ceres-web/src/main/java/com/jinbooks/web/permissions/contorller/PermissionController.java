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
 



package com.jinbooks.web.permissions.contorller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.permissions.Permission;
import com.jinbooks.entity.permissions.dto.PermissionDto;
import com.jinbooks.persistence.service.HistorySystemLogsService;
import com.jinbooks.persistence.service.PermissionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value={"/permissions/permission"})
public class PermissionController {
	static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

	@Autowired
	PermissionService permissionService;

	@Autowired
	HistorySystemLogsService historySystemLogsService;

	@Autowired
	IdentifierGenerator identifierGenerator;

	@PutMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Permission> update(
			@RequestBody PermissionDto dto,
			@CurrentUser UserInfo currentUser) {
		logger.debug("-update  : {}" , dto);
		//have
		Permission queryPermission =
				new Permission(
						dto.roleId(),
						currentUser.getWorkspaceId());
		List<Permission> permissionsList = permissionService.queryPermissions(queryPermission);

		HashMap<String,String >permissionsMap =new HashMap<>();
		for(Permission permission : permissionsList) {
			permissionsMap.put(permission.getUniqueId(),permission.getId());
		}
		//Maybe insert
		ArrayList<Permission> newPermissionsList =new ArrayList<>();
		HashMap<String,String >newPermissionsMap =new HashMap<>();
		for(String resourceId : dto.resourceIds()) {
		    Permission newPermission=new Permission(
		    		dto.roleId(),
                    resourceId,
                    currentUser.getId(),
                    currentUser.getWorkspaceId());
		    newPermission.setId(identifierGenerator.nextId(newPermission).toString());
		    newPermissionsMap.put(newPermission.getUniqueId(), dto.appId());

		    if(!dto.appId().equalsIgnoreCase(resourceId) &&
		            !permissionsMap.containsKey(newPermission.getUniqueId())) {
		    	newPermissionsList.add(newPermission);
		    }
		}

		//delete
		ArrayList<Permission> deletePermissionsList =new ArrayList<>();
		for(Permission deletePermission : permissionsList) {
           if(!newPermissionsMap.containsKey(deletePermission.getUniqueId())) {
        	   deletePermission.setBookId(currentUser.getWorkspaceId());
        	   deletePermissionsList.add(deletePermission);
           }
        }
		if (!deletePermissionsList.isEmpty()) {
			logger.debug("-remove  : {}" , deletePermissionsList);
			permissionService.deleteGroupPrivileges(deletePermissionsList);
		}

		if (!newPermissionsList.isEmpty() && permissionService.insertGroupPrivileges(newPermissionsList)) {
			logger.debug("-insert  : {}" , newPermissionsList);
			return new Message<>(Message.SUCCESS);

		} else {
			return new Message<>(Message.SUCCESS);
		}

	}

    @GetMapping(value={"/get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<List<Permission>> get(
    		@ModelAttribute Permission permission,
    		@CurrentUser UserInfo currentUser) {
        logger.debug("-get  : {}" , permission);
        //have
        Permission queryPermission =
        		new Permission(
        				permission.getRoleId(),
        				currentUser.getWorkspaceId());

        List<Permission> queryPermissionList = permissionService.queryPermissions(queryPermission);

        return new Message<>(queryPermissionList);
	}


}
