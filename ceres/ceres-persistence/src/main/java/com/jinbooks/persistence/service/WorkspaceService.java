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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.workspace.Workspace;
import com.jinbooks.entity.workspace.dto.WorkspaceChangeDto;
import com.jinbooks.entity.workspace.dto.WorkspacePageDto;
import com.jinbooks.entity.workspace.vo.WorkspaceVo;

import java.util.List;

public interface WorkspaceService extends IService<Workspace> {
    Message<Page<Workspace>> pageList(WorkspacePageDto dto);

    Message<String> save(WorkspaceChangeDto dto);

    Message<String> update(WorkspaceChangeDto dto);

    Message<String> delete(ListIdsDto dto);

    List<WorkspaceVo> listWorkspaces(String userId);
}
