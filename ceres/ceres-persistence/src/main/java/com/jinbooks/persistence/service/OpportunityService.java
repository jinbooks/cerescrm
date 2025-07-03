package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.opportunity.Opportunity;
import com.jinbooks.entity.opportunity.OpportunityVo;
import com.jinbooks.entity.opportunity.dto.OppStageSetDto;
import com.jinbooks.entity.opportunity.dto.OpportunityChangeDto;
import com.jinbooks.entity.opportunity.dto.OpportunityPageDto;

public interface OpportunityService {
    Message<OpportunityVo> getById(String id);

    Message<Page<Opportunity>> pageList(OpportunityPageDto dto);

    Message<String> save(OpportunityChangeDto dto);

    Message<String> update(OpportunityChangeDto dto);

    Message<String> delete(ListIdsDto dto);

    Message<String> updateOpportunityStage(OppStageSetDto dto);
}
