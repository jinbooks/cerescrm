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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.entity.access.SessionList;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.mapper.SessionListMapper;
import com.jinbooks.persistence.service.SessionListService;
import com.jinbooks.web.WebContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SessionListServiceImpl  extends ServiceImpl<SessionListMapper,SessionList> implements SessionListService {
	 private static Logger logger = LoggerFactory.getLogger(SessionListServiceImpl.class);

	@Autowired
	SessionListMapper sessionListMapper;

	public SessionListMapper getMapper() {
		return sessionListMapper;
	}


    public void insertOnline(SessionList sessionList) {
    	sessionList.setId(WebContext.genId());

        new Thread(new SessionInsertRunnable(this,sessionList)).start();
    }

	public SessionList getBySessionId(String sessionId) {
		return getMapper().getBySessionId(sessionId);
	}

	public void removeById(String sessionId) {
		getMapper().removeById(sessionId);
	}

	public void updateLastLogoffTime(String userId) {
		UserInfo user = new UserInfo();
		user.setId(userId);
		user.setLastLogoffTime(new Date());
		getMapper().updateLastLogoffTime(user);
	}

	public List<SessionList> list(String style) {
		if(StringUtils.isBlank(style)){
			return getMapper().listAll();
		}else {
			return getMapper().listByStyle(style);
		}
	}

	public class SessionInsertRunnable implements Runnable{

		SessionListServiceImpl service;

		SessionList sessionList;

		public SessionInsertRunnable(SessionListServiceImpl sessionListService, SessionList sessionList) {
			super();
			this.service = sessionListService;
			this.sessionList = sessionList;
		}

		@Override
	    public void run() {
			logger.debug(" sessionList {}" , sessionList);
			sessionList.setOperateTime(new Date());
			service.save(sessionList);
		}
	}

}
