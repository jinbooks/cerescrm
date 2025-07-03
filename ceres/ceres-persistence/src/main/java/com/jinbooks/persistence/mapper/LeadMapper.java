package com.jinbooks.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.dashboard.BaseGroupVo;
import com.jinbooks.entity.lead.Lead;
import com.jinbooks.entity.lead.dto.LeadPageDto;
import com.jinbooks.entity.lead.dto.LeadStatisticsDto;
import com.jinbooks.entity.lead.vo.LeadStatisticsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LeadMapper extends BaseMapper<Lead> {
    LeadStatisticsVo getLeadStatisticsBySQL(@Param("Dto") LeadStatisticsDto dto);

    Page<Lead> pageList(Page<Lead> page, @Param("Dto") LeadPageDto dto);

    List<BaseGroupVo<Long>> countRecentLeads(@Param("workspaceId") String workspaceId);

    Long countCurrentYear(String workspaceId);
}
