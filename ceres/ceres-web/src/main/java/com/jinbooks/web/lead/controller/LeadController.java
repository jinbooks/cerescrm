package com.jinbooks.web.lead.controller;

import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.lead.Lead;
import com.jinbooks.entity.lead.dto.LeadChangeDto;
import com.jinbooks.entity.lead.dto.LeadPageDto;
import com.jinbooks.entity.lead.vo.LeadFollowUpVo;
import com.jinbooks.entity.lead.vo.LeadVo;
import com.jinbooks.persistence.service.LeadService;
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
 * @time: 2025/6/13 14:03
 */

@RestController
@RequestMapping("/lead")
@RequiredArgsConstructor
public class LeadController {
    private static final Logger logger = LoggerFactory.getLogger(LeadController.class);

    private final LeadService leadService;

    @GetMapping(value = { "/fetch" })
    public Message<LeadVo> fetch(@ParameterObject LeadPageDto dto, @CurrentUser UserInfo userInfo) {

        logger.debug("fetch {}",dto);
        dto.setWorkspaceId(userInfo.getWorkspaceId());
        return leadService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<LeadFollowUpVo> getById(@PathVariable(name="id") String id) {
        logger.debug("get {}",id);
        return leadService.getById(id);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody LeadChangeDto dto, @CurrentUser UserInfo userInfo) {
        logger.debug("save {}",dto);
        dto.setWorkspaceId(userInfo.getWorkspaceId());
        return leadService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody LeadChangeDto dto) {
        logger.debug("update {}",dto);
        return leadService.update(dto);
    }

    @DeleteMapping(value = { "/delete" })
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {

        logger.debug("delete {}",dto);

        return leadService.delete(dto);
    }
}
