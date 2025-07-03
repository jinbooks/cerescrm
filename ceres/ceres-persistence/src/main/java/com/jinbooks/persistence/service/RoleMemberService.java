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
 

package com.jinbooks.persistence.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.permissions.RoleMember;
import com.jinbooks.entity.permissions.Roles;
import com.jinbooks.entity.permissions.dto.RoleMemberPageDto;

public interface RoleMemberService extends IService<RoleMember> {
	List<UserInfo> queryMemberByRoleId(String groupId);

	Page<Roles> rolesNoMember(Page page, RoleMemberPageDto dto);

	Page<RoleMember> memberInRole(Page page, RoleMemberPageDto dto);

	Page<RoleMember> memberNotInRole(Page page, RoleMemberPageDto dto);

}
