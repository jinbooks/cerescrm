package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.followup.FollowUp;
import com.jinbooks.entity.followup.dto.FollowUpChangeDto;
import com.jinbooks.entity.followup.dto.FollowUpPageDto;
import com.jinbooks.entity.lead.Lead;
import com.jinbooks.entity.lead.dto.LeadChangeDto;
import com.jinbooks.entity.lead.dto.LeadPageDto;
import com.jinbooks.entity.lead.vo.LeadVo;

public interface FollowUpService extends IService<FollowUp> {

    Message<Page<FollowUp>> pageList(FollowUpPageDto dto);

    Message<String> save(FollowUpChangeDto dto);

    Message<String> delete(ListIdsDto dto);
}
