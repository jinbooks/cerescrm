package com.jinbooks.web.opportunity.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.opportunity.OppStage;
import com.jinbooks.entity.opportunity.Opportunity;
import com.jinbooks.entity.opportunity.dto.OppStageChangeDto;
import com.jinbooks.entity.opportunity.dto.OppStagePageDto;
import com.jinbooks.entity.opportunity.dto.OpportunityChangeDto;
import com.jinbooks.entity.opportunity.dto.OpportunityPageDto;
import com.jinbooks.persistence.service.OppStageService;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/20 16:21
 */

@RestController
@RequestMapping("/opp-stage")
@RequiredArgsConstructor
public class OppStageController {
    private static final Logger logger = LoggerFactory.getLogger(OppStageController.class);

    private final OppStageService oppStageService;

    @GetMapping(value = { "/fetch" })
    public Message<Page<OppStage>> fetch(@ParameterObject OppStagePageDto dto, @CurrentUser UserInfo userInfo) {

        logger.debug("fetch {}",dto);
        dto.setWorkspaceId(userInfo.getWorkspaceId());
        return oppStageService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<OppStage> getById(@PathVariable(name="id") String id) {
        logger.debug("get {}",id);
        return oppStageService.getById(id);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody OppStageChangeDto dto, @CurrentUser UserInfo userInfo) {
        logger.debug("save {}",dto);
        dto.setWorkspaceId(userInfo.getWorkspaceId());
        return oppStageService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody OppStageChangeDto dto) {
        logger.debug("update {}",dto);
        return oppStageService.update(dto);
    }

    @DeleteMapping(value = { "/delete" })
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {

        logger.debug("delete {}",dto);

        return oppStageService.delete(dto);
    }

    @GetMapping(value = { "/get-all" })
    public Message<List<OppStage>> getAll(@CurrentUser UserInfo userInfo) {

        OppStage oppStage = new OppStage();
        oppStage.setWorkspaceId(userInfo.getWorkspaceId());
        oppStage.setIsActive(0);
        oppStage.setIsSystem(1);
        return oppStageService.getAll(oppStage);
    }
}
