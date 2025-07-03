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

import java.util.List;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.permissions.PermissionWorkspace;
import com.jinbooks.entity.permissions.RoleMember;
import com.jinbooks.entity.permissions.dto.PermissionWorkspaceDto;
import com.jinbooks.entity.permissions.dto.PermissionWorkspacePageDto;
import com.jinbooks.persistence.service.HistorySystemLogsService;
import com.jinbooks.persistence.service.PermissionWorkspaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value={"/permissions/permissionWorkspace"})
public class PermissionWorkspaceController {
	static final Logger logger = LoggerFactory.getLogger(PermissionWorkspaceController.class);

	@Autowired
	PermissionWorkspaceService permissionWorkspaceService;

	@Autowired
	HistorySystemLogsService historySystemLogsService;

	@Autowired
	IdentifierGenerator identifierGenerator;

	@GetMapping(value = { "/userAccessWorkspace" })
	public Message<Page<PermissionWorkspace>> userAccessWorkspace(@ParameterObject PermissionWorkspacePageDto dto,
													@CurrentUser UserInfo currentUser) {
		logger.debug("userAccessBook : {}",dto);
		return Message.ok(permissionWorkspaceService.userAccessWorkspace(dto.build(), dto));
	}

	@GetMapping(value = { "/userNotAccessWorkspace" })
	public Message<Page<PermissionWorkspace>> userNotAccessWorkspace(@ParameterObject PermissionWorkspacePageDto dto,
													   @CurrentUser UserInfo currentUser) {
		logger.debug("userNotAccessBook : {}",dto);
		return Message.ok(permissionWorkspaceService.userNotAccessWorkspace(dto.build(), dto));
	}
	
	
	/**
	 * Members add to the Group
	 * @param currentUser
	 * @return
	 */
	@PostMapping(value = {"/add"})
	public Message<PermissionWorkspace> add(@Validated @RequestBody PermissionWorkspaceDto dto,@CurrentUser UserInfo currentUser) {
		boolean result = true;
		for (int i = 0; i < dto.workspaceIds().size(); i++) {
			PermissionWorkspace newPermission =
					new PermissionWorkspace(
							dto.userId(),
							dto.workspaceIds().get(i));
			result = permissionWorkspaceService.save(newPermission);
		}
		if(result) {
			return new Message<>(Message.SUCCESS);
		}
		return new Message<>(Message.FAIL);
	}
	
	@DeleteMapping(value={"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<RoleMember> delete(@RequestParam("ids") List<String> ids,@CurrentUser UserInfo currentUser) {
		logger.debug("-delete ids : {}" , ids);
		if (permissionWorkspaceService.removeBatchByIds(ids)) {
			 return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

}
