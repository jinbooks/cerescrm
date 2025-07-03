package com.jinbooks.web.contract.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.contract.Contract;
import com.jinbooks.entity.contract.dto.ContractPageDto;
import com.jinbooks.entity.contract.vo.ContractVo;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.service.ContractService;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 合同控制器
 *
 * @author Wuyan
 */
@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
@Slf4j
public class ContractController {

    private final ContractService contractService;

    /**
     * 分页查询合同列表
     */
    @GetMapping("/fetch")
    public Message<Page<ContractVo>> fetch(@ParameterObject ContractPageDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(contractService.pageList(dto));
    }

    /**
     * 获取合同详情
     */
    @GetMapping("/get/{id}")
    public Message<ContractVo> getById(@PathVariable("id") String id,@CurrentUser UserInfo currentUser) {
        log.debug("get {}", id);
        return new Message<>(Message.SUCCESS, contractService.getById(id,currentUser.getWorkspaceId()));
    }

    /**
     * 新增合同
     */
    @PostMapping("/save")
    public Message<Boolean> save(@Validated(AddGroup.class) @RequestBody Contract dto,@CurrentUser UserInfo currentUser) {
        log.debug("save {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(contractService.save(dto));
    }

    /**
     * 更新合同
     */
    @PutMapping("/update")
    public Message<Boolean> update(@Validated(EditGroup.class) @RequestBody Contract dto,@CurrentUser UserInfo currentUser) {
        log.debug("update {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(contractService.update(dto));
    }

    /**
     * 删除合同
     */
    @DeleteMapping("/delete")
    public Message<Boolean> delete(@Validated @RequestBody ListIdsDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("delete {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(contractService.deleteByIds(dto.getListIds()));
    }
}
