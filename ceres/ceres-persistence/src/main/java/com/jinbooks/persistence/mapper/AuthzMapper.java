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
 


package com.jinbooks.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinbooks.entity.dto.QueryGroupMembersDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.permissions.Roles;

@Mapper
public  interface AuthzMapper extends BaseMapper<UserInfo> {


	public List<Roles> queryRolesByMembers(QueryGroupMembersDto dto) ;
	
	@Select("select * from  jbx_userinfo where id = #{userId} and deleted = 'n'")
	public UserInfo findUserById(@Param ("userId") String userId ) ;

}
