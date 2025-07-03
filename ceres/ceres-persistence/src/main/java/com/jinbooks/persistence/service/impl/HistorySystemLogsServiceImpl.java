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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.entity.ChangePassword;
import com.jinbooks.entity.Institutions;
import com.jinbooks.entity.SocialsProvider;
import com.jinbooks.entity.history.HistorySystemLogs;
import com.jinbooks.entity.idm.Organizations;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.permissions.RoleMember;
import com.jinbooks.entity.permissions.Roles;
import com.jinbooks.entity.permissions.Permission;
import com.jinbooks.entity.permissions.Resources;
import com.jinbooks.persistence.mapper.HistorySystemLogsMapper;
import com.jinbooks.persistence.service.HistorySystemLogsService;
import com.jinbooks.util.JsonUtils;

import org.springframework.stereotype.Service;

@Service
public class HistorySystemLogsServiceImpl  extends ServiceImpl<HistorySystemLogsMapper,HistorySystemLogs> implements HistorySystemLogsService {
	static final Logger logger = LoggerFactory.getLogger(HistorySystemLogsServiceImpl.class);

	@Autowired
	HistorySystemLogsMapper historySystemLogsMapper;

	public HistorySystemLogsMapper getMapper() {
		return historySystemLogsMapper;
	}

	public void log(String topic,Object entity,String action,String result,UserInfo operator) {
		String message = "";
		String targetId = "";
		String targetName = "";
		String cipherText = "";
		if(entity != null) {
			if(entity instanceof UserInfo model) {
				targetId = model.getId();
				targetName =  model.getUsername();
				message = buildMsg(model);
			}else if(entity instanceof Organizations model) {
				targetId = model.getId();
				targetName =  model.getOrgName();
				message = buildMsg(model);
			}else if(entity instanceof ChangePassword model) {
				targetId = model.getId();
				targetName =  model.getUsername();
				cipherText = model.getPassword();
				message = buildMsg(model);
			} else if(entity instanceof Roles model) {
				targetId = model.getId();
				targetName =  model.getRoleName();
				message = buildMsg(model);
			}else if(entity instanceof RoleMember model) {
				targetId = model.getId();
				targetName =  model.getRoleName();
				message = buildMsg(model);
			}else if(entity instanceof Resources model) {
				message = buildMsg(model);
			}else if(entity instanceof SocialsProvider model) {
				targetId = model.getId();
				targetName =  model.getProviderName();
				message = buildMsg(model);
			}else if(entity instanceof Permission model) {
				targetId = model.getId();
				targetName =  model.getRoleId();
				message = buildMsg(model);
			}else if(entity instanceof Institutions model) {
				targetId = model.getId();
				targetName =  model.getFullName();
				message = buildMsg(model);
			}else if(entity instanceof String) {
				message = entity.toString();
			}else {
				message = entity.toString();
			}

		}

		log(topic,targetId,targetName,cipherText,message,action,result,operator, entity);
	}

	public void log(String topic,String targetId,String targetName,String cipherText ,String message,String action,String result,UserInfo operator,Object entity) {
		HistorySystemLogs systemLog = new HistorySystemLogs();
		//systemLog.setId(systemLog.generateId());
		systemLog.setTargetId(targetId);
		systemLog.setTargetName(targetName);
		systemLog.setCipherText(cipherText);
		systemLog.setTopic(topic);
		systemLog.setMessage(message);
		systemLog.setMessageAction(action);
		systemLog.setMessageResult(result);
		systemLog.setUserId(operator.getId());
		systemLog.setUsername(operator.getUsername());
		systemLog.setDisplayName(operator.getDisplayName());
		systemLog.setBookId(operator.getWorkspaceId());
		systemLog.setJsonCotent(JsonUtils.toString(entity));
		systemLog.setExecuteTime(new Date());
		logger.trace("System Log {}" ,systemLog);
		getMapper().insert(systemLog);
	}

	public String buildMsg(UserInfo userInfo) {
		return new StringBuilder()
				.append(userInfo.getDisplayName())
				.append("[")
				.append(userInfo.getUsername())
				.append("]")
				.toString();
	}

	public String buildMsg(Organizations org) {
		return new StringBuilder()
				.append(org.getOrgName())
				.append("[")
				.append(org.getOrgCode())
				.append("]")
				.toString();
	}

	public String buildMsg(ChangePassword changePassword) {
		return new StringBuilder()
				.append(changePassword.getDisplayName())
				.append("[")
				.append(changePassword.getUsername())
				.append("]")
				.toString();
	}

	public String buildMsg(Roles g) {
		return new StringBuilder()
				.append(g.getRoleName())
				.toString();
	}


	public String buildMsg(RoleMember rm) {
		return new StringBuilder()
				.append(rm.getRoleName())
				.append("[")
				.append(rm.getUsername()).append(",")
				.append(rm.getDisplayName())
				.append("]")
				.toString();
	}

	public String buildMsg(Permission privilege) {
		return new StringBuilder()
				.append(privilege.getRoleId())
				.append("[")
				.append(privilege.getResourceId())
				.append("]")
				.toString();
	}


	public String buildMsg(Resources r) {
		return new StringBuilder()
				.append(r.getResName())
				.append("[")
				.append(r.getClassify())
				.append("]")
				.toString();
	}


	public String buildMsg(SocialsProvider s) {
		return new StringBuilder()
				.append(s.getProviderName())
				.append("[")
				.append(s.getProvider())
				.append("]")
				.toString();
	}


	public String buildMsg(Institutions inst) {
		return new StringBuilder()
				.append(inst.getFullName())
				.append("[")
				.append(inst.getId())
				.append(inst.getDomain())
				.append("]")
				.toString();
	}
}
