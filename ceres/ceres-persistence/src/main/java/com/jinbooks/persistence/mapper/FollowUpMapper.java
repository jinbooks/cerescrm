package com.jinbooks.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.followup.FollowUp;
import com.jinbooks.entity.followup.dto.FollowUpPageDto;
import com.jinbooks.entity.lead.Lead;
import com.jinbooks.entity.lead.dto.LeadPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FollowUpMapper extends BaseMapper<FollowUp> {
    Page<FollowUp> pageList(Page page, @Param("Dto") FollowUpPageDto dto);
}
