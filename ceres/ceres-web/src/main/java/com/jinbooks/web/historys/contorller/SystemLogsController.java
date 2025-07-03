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
 





package com.jinbooks.web.historys.contorller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.history.HistorySystemLogs;
import com.jinbooks.entity.history.dto.HistorySystemLogsPageDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.service.HistorySystemLogsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统操作日志查询
 *
 * @author Crystal.sea
 *
 */

@RestController
@RequestMapping(value={"/historys"})
public class SystemLogsController {
	static final Logger logger = LoggerFactory.getLogger(SystemLogsController.class);

	@Autowired
	HistorySystemLogsService historySystemLogsService;

	/**
	 * 查询操作日志
	 * @param dto
	 * @return
	 */
	@GetMapping(value={"/systemLogs/fetch"})
	@ResponseBody
	public Message<Page<HistorySystemLogs>> fetch(@ParameterObject HistorySystemLogsPageDto dto,
												  @CurrentUser UserInfo currentUser){
		logger.debug("historys/historyLog/fetch {} ",dto);

		LambdaQueryWrapper<HistorySystemLogs> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(HistorySystemLogs::getBookId, currentUser.getWorkspaceId());

		return new Message<>(historySystemLogsService.page(dto.build(), wrapper));
	}

}
