package com.jinbooks.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.followup.FollowUp;
import com.jinbooks.entity.followup.dto.FollowUpChangeDto;
import com.jinbooks.entity.followup.dto.FollowUpPageDto;
import com.jinbooks.entity.lead.Lead;
import com.jinbooks.entity.lead.dto.LeadChangeDto;
import com.jinbooks.entity.lead.dto.LeadPageDto;
import com.jinbooks.entity.lead.vo.LeadVo;
import com.jinbooks.enums.FollowUpCategoryEnum;
import com.jinbooks.persistence.mapper.FollowUpMapper;
import com.jinbooks.persistence.mapper.LeadMapper;
import com.jinbooks.persistence.service.FollowUpService;
import com.jinbooks.web.WebContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/26 15:12
 */

@Service
@RequiredArgsConstructor
public class FollowUpServiceImpl extends ServiceImpl<FollowUpMapper, FollowUp> implements FollowUpService {

    private final FollowUpMapper followUpMapper;

    private final LeadMapper leadMapper;

    @Override
    public Message<Page<FollowUp>> pageList(FollowUpPageDto dto) {
        Page<FollowUp> followUpPage = followUpMapper.pageList(dto.build(), dto);

        return Message.ok(followUpPage);
    }

    @Override
    @Transactional
    public Message<String> save(FollowUpChangeDto dto) {
        FollowUp followUp = BeanUtil.copyProperties(dto, FollowUp.class);

        if (FollowUpCategoryEnum.LEAD.equals(followUp.getCategory())) {
            Lead lead = leadMapper.selectById(followUp.getRelationId());
            lead.setFollowUpTimes(Optional.ofNullable(lead.getFollowUpTimes()).orElse(0) + 1);
            leadMapper.updateById(lead);
        }

        boolean save = super.save(followUp);


        return save ? Message.ok(WebContext.getI18nValue("common.add.success")) : Message.failed("新增失败");
    }

    @Override
    public Message<String> delete(ListIdsDto dto) {
        boolean result = super.removeBatchByIds(dto.getListIds());

        return result ? Message.ok("删除成功") : Message.failed("删除失败");
    }
}
