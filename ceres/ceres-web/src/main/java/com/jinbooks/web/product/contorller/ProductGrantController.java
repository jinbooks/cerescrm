package com.jinbooks.web.product.contorller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.product.ProductGrant;
import com.jinbooks.entity.product.dto.ProductGrantPageDto;
import com.jinbooks.persistence.service.ProductGrantService;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/productGrant")
@RequiredArgsConstructor
@Slf4j
public class ProductGrantController {

    private final ProductGrantService productGrantService;

    /**
     * 分页查询列表
     */
    @GetMapping("/fetch")
    public Message<Page<ProductGrant>> fetch(@ParameterObject ProductGrantPageDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productGrantService.pageList(dto));
    }

    /**
     * 获取详情
     */
    @GetMapping("/get/{id}")
    public Message<ProductGrant> getById(@PathVariable("id") String id) {
        log.debug("get {}", id);
        return new Message<>(Message.SUCCESS, productGrantService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public Message<Boolean> save(@Validated(AddGroup.class) @RequestBody ProductGrant dto,@CurrentUser UserInfo currentUser) {
        log.debug("save {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productGrantService.save(dto));
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Message<Boolean> update(@Validated(EditGroup.class) @RequestBody ProductGrant dto,@CurrentUser UserInfo currentUser) {
        log.debug("update {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productGrantService.update(dto));
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Message<Boolean> delete(@Validated @RequestBody ListIdsDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("delete {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productGrantService.deleteByIds(dto.getListIds()));
    }
    
    /**
     * 获取详情
     */
    @GetMapping("/license/{license}")
    public Message<ProductGrant> getByLicense(@PathVariable("license") String license) {
        log.debug("get {}", license);
        return new Message<>(Message.SUCCESS, productGrantService.getByLicense(license));
    }
}
