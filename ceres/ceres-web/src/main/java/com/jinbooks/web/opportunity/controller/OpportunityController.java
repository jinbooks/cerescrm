package com.jinbooks.web.opportunity.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.opportunity.Opportunity;
import com.jinbooks.entity.opportunity.OpportunityVo;
import com.jinbooks.entity.opportunity.dto.OppStageSetDto;
import com.jinbooks.entity.opportunity.dto.OpportunityChangeDto;
import com.jinbooks.entity.opportunity.dto.OpportunityPageDto;
import com.jinbooks.persistence.service.OpportunityService;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/19 17:37
 */

@RestController
@RequestMapping("/opportunity")
@RequiredArgsConstructor
public class OpportunityController {
    private static final Logger logger = LoggerFactory.getLogger(OpportunityController.class);

    private final OpportunityService opportunityService;

    @GetMapping(value = { "/fetch" })
    public Message<Page<Opportunity>> fetch(@ParameterObject OpportunityPageDto dto, @CurrentUser UserInfo userInfo) {

        logger.debug("fetch {}",dto);
        dto.setWorkspaceId(userInfo.getWorkspaceId());
        return opportunityService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<OpportunityVo> getById(@PathVariable(name="id") String id) {
        logger.debug("get {}",id);
        return opportunityService.getById(id);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody OpportunityChangeDto dto, @CurrentUser UserInfo userInfo) {
        logger.debug("save {}",dto);
        dto.setWorkspaceId(userInfo.getWorkspaceId());
        return opportunityService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody OpportunityChangeDto dto) {
        logger.debug("update {}",dto);
        return opportunityService.update(dto);
    }

    @DeleteMapping(value = { "/delete" })
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {

        logger.debug("delete {}",dto);

        return opportunityService.delete(dto);
    }

    @PutMapping("/updateOpportunityStage")
    public Message<String> updateOpportunityStage(@Validated @RequestBody OppStageSetDto dto, @CurrentUser UserInfo userInfo) {
        dto.setWorkspaceId(userInfo.getWorkspaceId());
        return opportunityService.updateOpportunityStage(dto);
    }
}
