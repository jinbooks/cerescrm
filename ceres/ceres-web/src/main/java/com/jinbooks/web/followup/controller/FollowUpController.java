package com.jinbooks.web.followup.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.followup.FollowUp;
import com.jinbooks.entity.followup.dto.FollowUpChangeDto;
import com.jinbooks.entity.followup.dto.FollowUpPageDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.lead.dto.LeadChangeDto;
import com.jinbooks.entity.lead.dto.LeadPageDto;
import com.jinbooks.entity.lead.vo.LeadVo;
import com.jinbooks.persistence.service.FollowUpService;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.web.lead.controller.LeadController;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/26 14:26
 */

@RestController
@RequestMapping("/follow-up")
@RequiredArgsConstructor
public class FollowUpController {

    private static final Logger logger = LoggerFactory.getLogger(FollowUpController.class);

    private final FollowUpService followUpService;

    @GetMapping(value = { "/fetch" })
    public Message<Page<FollowUp>> fetch(@ParameterObject FollowUpPageDto dto, @CurrentUser UserInfo userInfo) {
        logger.debug("fetch {}",dto);
        dto.setWorkspaceId(userInfo.getWorkspaceId());
        return followUpService.pageList(dto);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody FollowUpChangeDto dto, @CurrentUser UserInfo userInfo) {
        logger.debug("save {}",dto);
        dto.setWorkspaceId(userInfo.getWorkspaceId());
        return followUpService.save(dto);
    }

    @DeleteMapping(value = { "/delete" })
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {

        logger.debug("delete {}",dto);

        return followUpService.delete(dto);
    }
}
