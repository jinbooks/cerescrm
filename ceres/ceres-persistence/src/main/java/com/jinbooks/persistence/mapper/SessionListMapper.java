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
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinbooks.entity.access.SessionList;
import com.jinbooks.entity.idm.UserInfo;

/**
 * @author Crystal.sea
 *
 */

@Mapper
public interface SessionListMapper extends BaseMapper<SessionList> {

	@Select("select * from jbx_session_list  where session_id = #{sessionId}")
	public SessionList getBySessionId(@Param ("sessionId") String sessionId) ;

	@Update("delete from jbx_session_list  where session_id = #{sessionId}")
	public void removeById(@Param ("sessionId") String sessionId);

	@Update("update jbx_userinfo set last_logoff_time = #{lastLogoffTime} , is_online = 0  where id = #{id}")
	public void updateLastLogoffTime(UserInfo user);

	@Select("select * from jbx_session_list")
	public List<SessionList> listAll() ;

	@Select("select * from jbx_session_list where style = #{style}")
	public List<SessionList> listByStyle(@Param ("style") String style) ;


}
