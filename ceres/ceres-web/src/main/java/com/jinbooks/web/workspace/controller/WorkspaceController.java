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
 

package com.jinbooks.web.workspace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.workspace.Workspace;
import com.jinbooks.entity.workspace.dto.WorkspaceChangeDto;
import com.jinbooks.entity.workspace.dto.WorkspacePageDto;
import com.jinbooks.entity.workspace.vo.WorkspaceVo;
import com.jinbooks.persistence.service.WorkspaceService;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/31 11:18
 */

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceController.class);

    @Autowired
    WorkspaceService workspaceService;

    @GetMapping(value = { "/fetch" })
    public Message<Page<Workspace>> fetch(@ParameterObject WorkspacePageDto dto) {
        logger.debug("fetch {}",dto);
        return workspaceService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<Workspace> getById(@PathVariable(name="id") String id) {

        logger.debug("get {}",id);

        return new Message<>(Message.SUCCESS, workspaceService.getById(id));
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody WorkspaceChangeDto dto) {
        logger.debug("save {}",dto);
        return workspaceService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody WorkspaceChangeDto dto) {
        logger.debug("update {}",dto);
        return workspaceService.update(dto);
    }

    @DeleteMapping(value = { "/delete" })
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {

        logger.debug("delete {}",dto);

        return workspaceService.delete(dto);
    }

    @GetMapping("/fetchAll")
    public Message<List<WorkspaceVo>> listStore(@CurrentUser UserInfo currentUser) {
        return Message.ok(workspaceService.listWorkspaces(currentUser.getId()));
    }
}
