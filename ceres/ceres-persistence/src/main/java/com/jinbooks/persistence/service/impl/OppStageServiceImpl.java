package com.jinbooks.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.opportunity.OppStage;
import com.jinbooks.entity.opportunity.Opportunity;
import com.jinbooks.entity.opportunity.dto.OppStageChangeDto;
import com.jinbooks.entity.opportunity.dto.OppStagePageDto;
import com.jinbooks.exception.BusinessException;
import com.jinbooks.persistence.mapper.OppStageMapper;
import com.jinbooks.persistence.mapper.OpportunityMapper;
import com.jinbooks.persistence.service.OppStageService;
import lombok.RequiredArgsConstructor;
import org.aspectj.org.eclipse.jdt.core.IField;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/20 16:23
 */

@Service
@RequiredArgsConstructor
public class OppStageServiceImpl extends ServiceImpl<OppStageMapper, OppStage> implements OppStageService {

    private final OpportunityMapper opportunityMapper;

    @Override
    public Message<OppStage> getById(String id) {
        OppStage oppStage = super.getById(id);
        return Message.ok(oppStage);
    }

    @Override
    public Message<Page<OppStage>> pageList(OppStagePageDto dto) {
        LambdaQueryWrapper<OppStage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OppStage::getWorkspaceId, dto.getWorkspaceId());
        wrapper.eq(OppStage::getIsSystem, 1);
        wrapper.orderByAsc(OppStage::getSortOrder);
        Page<OppStage> page = super.page(dto.build(), wrapper);

        return Message.ok(page);
    }

    @Override
    public Message<String> save(OppStageChangeDto dto) {
        checkCodeAndName(dto, false);

        OppStage oppStage = BeanUtil.copyProperties(dto, OppStage.class);

        boolean result = super.save(oppStage);

        return result ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    public Message<String> update(OppStageChangeDto dto) {
        checkCodeAndName(dto, false);

        OppStage oppStage = BeanUtil.copyProperties(dto, OppStage.class);

        boolean result = super.updateById(oppStage);

        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    @Override
    public Message<String> delete(ListIdsDto dto) {
        List<Opportunity> opportunities = opportunityMapper.selectList(Wrappers.<Opportunity>lambdaQuery()
                .in(Opportunity::getStageId, dto.getListIds()));

        if (ObjectUtils.isNotEmpty(opportunities)) {
            throw new BusinessException(50001, "该阶段已被商机使用暂时无法删除");
        }

        boolean result = super.removeBatchByIds(dto.getListIds());

        return result ? Message.ok("删除成功") : Message.failed("删除失败");
    }

    private void checkCodeAndName(OppStageChangeDto dto, boolean isEdit) {
        String workspaceId = dto.getWorkspaceId();
        String code = dto.getCode();
        String name = dto.getName();
        String currentId = dto.getId();

        // 校验编码唯一
        LambdaQueryWrapper<OppStage> codeWrapper = new LambdaQueryWrapper<>();
        codeWrapper.eq(OppStage::getWorkspaceId, workspaceId)
                .eq(OppStage::getCode, code);
        if (isEdit) {
            codeWrapper.ne(OppStage::getId, currentId);
        }
        if (super.count(codeWrapper) > 0) {
            throw new BusinessException(50001, "操作失败，编码已存在，请修改");
        }

        // 校验名称唯一
        LambdaQueryWrapper<OppStage> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(OppStage::getWorkspaceId, workspaceId)
                .eq(OppStage::getName, name);
        if (isEdit) {
            nameWrapper.ne(OppStage::getId, currentId);
        }
        if (super.count(nameWrapper) > 0) {
            throw new BusinessException(50001, "操作失败，名称已存在，请修改");
        }
    }

    @Override
    public Message<List<OppStage>> getAll(OppStage oppStage) {
        LambdaQueryWrapper<OppStage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OppStage::getWorkspaceId, oppStage.getWorkspaceId());
        wrapper.eq(OppStage::getIsActive, oppStage.getIsActive());
        wrapper.eq(OppStage::getIsSystem, oppStage.getIsSystem());
        wrapper.orderByAsc(OppStage::getSortOrder);

        return Message.ok(super.list(wrapper));
    }
}
