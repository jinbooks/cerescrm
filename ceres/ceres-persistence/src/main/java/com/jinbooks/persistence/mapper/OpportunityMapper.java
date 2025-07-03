package com.jinbooks.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.dashboard.BaseGroupVo;
import com.jinbooks.entity.opportunity.Opportunity;
import com.jinbooks.entity.opportunity.dto.OpportunityPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OpportunityMapper extends BaseMapper<Opportunity> {
    Page<Opportunity> pageList(Page<Opportunity> page, @Param("Dto") OpportunityPageDto dto);

    List<BaseGroupVo<Long>> countRecentOpportunity(String workspaceId);

    Long countCurrentYear(String workspaceId);
}
