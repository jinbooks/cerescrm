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

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.opportunity.OppStage;
import com.jinbooks.entity.workspace.Workspace;
import com.jinbooks.entity.workspace.dto.WorkspaceChangeDto;
import com.jinbooks.entity.workspace.dto.WorkspacePageDto;
import com.jinbooks.entity.workspace.vo.WorkspaceVo;
import com.jinbooks.enums.BookBusinessExceptionEnum;
import com.jinbooks.exception.BusinessException;
import com.jinbooks.persistence.mapper.OppStageMapper;
import com.jinbooks.persistence.mapper.WorkspaceMapper;
import com.jinbooks.persistence.service.*;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/31 11:15
 */

@Service
public class WorkspaceServiceImpl extends ServiceImpl<WorkspaceMapper, Workspace> implements WorkspaceService {
    @Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    WorkspaceMapper workspaceMapper;

    @Autowired
    OppStageMapper oppStageMapper;

    @Override
    public Message<Page<Workspace>> pageList(WorkspacePageDto dto) {
        Page<Workspace> page = workspaceMapper.pageList(dto.build(), dto);

        return new Message<>(Message.SUCCESS, page);
    }

    @Override
    @Transactional
    public Message<String> save(WorkspaceChangeDto dto) {
        //校验工作区名称是否重复
        checkIfTheNameExists(dto, false);
        dto.setWorkspaceId(identifierGenerator.nextId(dto).toString());
        //新增工作区
        Workspace newBook = new Workspace();
        BeanUtil.copyProperties(dto, newBook);
        boolean saveResult = super.save(newBook);

        //新增阶段（内置模板）
        addOpportunityStage(dto.getWorkspaceId());
        return saveResult ? new Message<>(Message.SUCCESS, "新增成功") : new Message<>(Message.FAIL, "新增失败");
    }

    @Override
    @Transactional
    public Message<String> update(WorkspaceChangeDto dto) {
        checkIfTheNameExists(dto, true);

        //更新工作区
        Workspace booksUpdate = new Workspace();
        BeanUtil.copyProperties(dto, booksUpdate);
        //新增阶段（内置模板）
        addOpportunityStage(dto.getWorkspaceId());
        boolean result = super.updateById(booksUpdate);
        return result ? new Message<>(Message.SUCCESS, "修改成功") : new Message<>(Message.FAIL, "修改失败");
    }

    private void checkIfTheNameExists(WorkspaceChangeDto dto, boolean isEdit) {
        LambdaQueryWrapper<Workspace> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Workspace::getName, dto.getName());
        if (isEdit) {
            wrapper.ne(Workspace::getWorkspaceId, dto.getWorkspaceId());
        }
        List<Workspace> list = super.list(wrapper);
        if (ObjectUtils.isNotEmpty(list)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DUPLICATE_SETNAME_EXIST.getCode(),
                    BookBusinessExceptionEnum.DUPLICATE_SETNAME_EXIST.getMsg()
            );
        }
    }


    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> workspaceIds = dto.getListIds();

        //校验是否为活跃状态
        LambdaQueryWrapper<Workspace> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Workspace::getStatus, 1);
        wrapper.in(Workspace::getWorkspaceId, workspaceIds);
        List<Workspace> books = workspaceMapper.selectList(wrapper);
        if (ObjectUtils.isNotEmpty(books)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getCode(),
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getMsg()
            );
        }

        //删除工作区数据
        boolean result = super.removeByIds(workspaceIds);

        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    @Override
    public List<WorkspaceVo> listWorkspaces(String userId) {
        return workspaceMapper.listWorkspaces(userId);
    }

    private void addOpportunityStage(String workSpaceId) {
        List<OppStage> oppStages = oppStageMapper.selectList(
                new LambdaQueryWrapper<OppStage>()
                        .eq(OppStage::getWorkspaceId, workSpaceId)
        );

        if (oppStages == null || oppStages.isEmpty()) {
            List<OppStage> defaultStages = oppStageMapper.selectList(
                    new LambdaQueryWrapper<OppStage>()
                            .eq(OppStage::getIsSystem, 0)
                            .isNull(OppStage::getWorkspaceId)
            );

            if (defaultStages == null || defaultStages.isEmpty()) {
                throw new BusinessException(50001, "请导入商机阶段内置数据");
            }

            List<OppStage> newStages = defaultStages.stream()
                    .map(stage -> {
                        OppStage copy = new OppStage();
                        BeanUtil.copyProperties(stage, copy, "id");
                        copy.setWorkspaceId(workSpaceId);
                        copy.setIsSystem(1);
                        return copy;
                    })
                    .toList();

            oppStageMapper.insert(newStages);
        }
    }


}
