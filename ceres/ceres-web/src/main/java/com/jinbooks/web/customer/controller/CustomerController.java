package com.jinbooks.web.customer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.customer.Customer;
import com.jinbooks.entity.customer.dto.CustomerPageDto;
import com.jinbooks.entity.customer.vo.CustomerVo;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.service.CustomerService;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 客户控制器
 *
 * @author Wuyan
 */
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 分页查询客户列表
     */
    @GetMapping("/fetch")
    public Message<Page<CustomerVo>> fetch(@ParameterObject CustomerPageDto dto,@CurrentUser UserInfo currentUser) {
    	dto.setWorkspaceId(currentUser.getWorkspaceId());
    	log.debug("fetch {}", dto);
        return Message.ok(customerService.pageList(dto));
    }

    /**
     * 获取客户详情
     */
    @GetMapping("/get/{id}")
    public Message<CustomerVo> getById(@PathVariable("id") String id,@CurrentUser UserInfo currentUser) {
        log.debug("get {}", id);
        return new Message<>(Message.SUCCESS, customerService.getById(id,currentUser.getWorkspaceId()));
    }

    /**
     * 新增客户
     */
    @PostMapping("/save")
    public Message<Boolean> save(@Validated(AddGroup.class) @RequestBody Customer dto,@CurrentUser UserInfo currentUser) {
    	dto.setWorkspaceId(currentUser.getWorkspaceId());
    	log.debug("save {}", dto);
        return Message.ok(customerService.save(dto));
    }

    /**
     * 更新客户
     */
    @PutMapping("/update")
    public Message<Boolean> update(@Validated(EditGroup.class) @RequestBody Customer dto,@CurrentUser UserInfo currentUser) {
    	dto.setWorkspaceId(currentUser.getWorkspaceId());
    	log.debug("update {}", dto);
        return Message.ok(customerService.update(dto));
    }

    /**
     * 删除客户
     */
    @DeleteMapping("/delete")
    public Message<Boolean> delete(@Validated @RequestBody ListIdsDto dto,@CurrentUser UserInfo currentUser) {
    	dto.setWorkspaceId(currentUser.getWorkspaceId());
        log.debug("delete {}", dto);
        return Message.ok(customerService.deleteByIds(dto.getListIds()));
    }
}
