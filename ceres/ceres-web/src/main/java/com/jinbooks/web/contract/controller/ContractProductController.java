package com.jinbooks.web.contract.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.contract.ContractProduct;
import com.jinbooks.entity.contract.dto.ContractProductPageDto;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.service.ContractProductService;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 合同购买产品
 *
 */
@RestController
@RequestMapping("/contractProduct")
@RequiredArgsConstructor
@Slf4j
public class ContractProductController {

    private final ContractProductService contractProductService;

    /**
     * 分页查询列表
     */
    @GetMapping("/fetch")
    public Message<Page<ContractProduct>> fetch(@ParameterObject ContractProductPageDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(contractProductService.pageList(dto));
    }

    /**
     * 获取详情
     */
    @GetMapping("/get/{id}")
    public Message<ContractProduct> getById(@PathVariable("id") String id) {
        log.debug("get {}", id);
        return new Message<>(Message.SUCCESS, contractProductService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public Message<Boolean> save(@Validated(AddGroup.class) @RequestBody ContractProduct dto,@CurrentUser UserInfo currentUser) {
        log.debug("save {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(contractProductService.save(dto));
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Message<Boolean> update(@Validated(EditGroup.class) @RequestBody ContractProduct dto,@CurrentUser UserInfo currentUser) {
        log.debug("update {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(contractProductService.update(dto));
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Message<Boolean> delete(@Validated @RequestBody ListIdsDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("delete {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(contractProductService.deleteByIds(dto.getListIds()));
    }
}
