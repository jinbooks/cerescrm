package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.lead.Lead;
import com.jinbooks.entity.lead.dto.LeadChangeDto;
import com.jinbooks.entity.lead.dto.LeadPageDto;
import com.jinbooks.entity.lead.vo.LeadFollowUpVo;
import com.jinbooks.entity.lead.vo.LeadVo;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/13 14:01
 */
public interface LeadService extends IService<Lead> {
    Message<LeadFollowUpVo> getById(String id);

    Message<LeadVo> pageList(LeadPageDto dto);

    Message<String> save(LeadChangeDto dto);

    Message<String> update(LeadChangeDto dto);

    Message<String> delete(ListIdsDto dto);
}
