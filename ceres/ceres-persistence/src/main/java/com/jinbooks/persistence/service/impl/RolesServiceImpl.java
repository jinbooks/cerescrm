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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.constants.ConstsRoles;
import com.jinbooks.constants.ConstsStatus;
import com.jinbooks.entity.Institutions;
import com.jinbooks.entity.permissions.Roles;
import com.jinbooks.persistence.mapper.RolesMapper;
import com.jinbooks.persistence.service.RolesService;
import com.jinbooks.util.StrUtils;

@Repository
public class RolesServiceImpl  extends ServiceImpl<RolesMapper,Roles>  implements RolesService {
    static final  Logger logger = LoggerFactory.getLogger(RolesServiceImpl.class);

    @Autowired
    RoleMemberServiceImpl groupMemberService;

    @Autowired
    InstitutionsServiceImpl institutionsService;

    @Autowired
    RolesMapper groupsMapper;

	public RolesMapper getMapper() {
		return groupsMapper;
	}


	public List<Roles> queryDynamicRoles(Roles groups){
	    return this.getMapper().queryDynamicRoles(groups);
	}

	public boolean deleteById(String groupId) {
	    this.removeById(groupId);
	    groupMemberService.deleteByRoleId(groupId);
	    return true;
	}

	public List<Roles> queryRolesByUserId(String userId){
		return this.getMapper().queryRolesByUserId(userId);
	}

	public void refreshDynamicRoles(Roles dynamicGroup){
	    if(dynamicGroup.getPattern().equals(ConstsRoles.Pattern.DYNAMIC)) {

	        if(StringUtils.isNotBlank(dynamicGroup.getOrgIdsList())) {
    	    	String []orgIds = dynamicGroup.getOrgIdsList().split(",");
    	    	StringBuffer orgIdFilters = new StringBuffer();
    	    	for(String orgId : orgIds) {
    	    		if(StringUtils.isNotBlank(orgId)) {
	    	    		if(orgIdFilters.length() > 0) {
	    	    			orgIdFilters.append(",");
	    	    		}
	    	    		orgIdFilters.append("'").append(orgId).append("'");
    	    		}
    	    	}
    	    	if(orgIdFilters.length() > 0) {
    	    		dynamicGroup.setOrgIdsList(orgIdFilters.toString());
    	    	}
    	    }

    	    String filters = dynamicGroup.getFilters();
    	    logger.debug("filters {}" , filters);
    	    if(StringUtils.isNotBlank(filters)) {
	    		if(StrUtils.filtersSQLInjection(filters.toLowerCase())) {
	    			logger.info("filters include SQL Injection Attack Risk.");
	    			return;
	    		}
	    		filters = filters.replace("&", " AND ").replace("\\|", " OR ");

	    		logger.debug("set filters {}" , filters);
	    	    dynamicGroup.setFilters(filters);
    	    }

	    	groupMemberService.deleteDynamicRoleMember(dynamicGroup);
	    	groupMemberService.addDynamicRoleMember(dynamicGroup);
	    }
    }

	public void refreshAllDynamicRoles(){
		 LambdaQueryWrapper<Institutions> queryWrapper = new LambdaQueryWrapper<Institutions>();
		 queryWrapper.eq(Institutions::getStatus, ConstsStatus.ACTIVE);
		List<Institutions> instList = institutionsService.list(queryWrapper);
		for(Institutions inst : instList) {
			Roles group = new Roles();
		    List<Roles>  groupsList = queryDynamicRoles(group);
	        for(Roles r : groupsList) {
	            logger.debug("group {}" , groupsList);
	            refreshDynamicRoles(r);
	        }
		}
	}

}
