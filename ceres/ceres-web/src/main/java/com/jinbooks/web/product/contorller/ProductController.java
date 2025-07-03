package com.jinbooks.web.product.contorller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.product.Product;
import com.jinbooks.entity.product.dto.ProductPageDto;
import com.jinbooks.persistence.service.ProductService;
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
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * 分页查询列表
     */
    @GetMapping("/fetch")
    public Message<Page<Product>> fetch(@ParameterObject ProductPageDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productService.pageList(dto));
    }

    /**
     * 获取合同详情
     */
    @GetMapping("/get/{id}")
    public Message<Product> getById(@PathVariable("id") String id) {
        log.debug("get {}", id);
        return new Message<>(Message.SUCCESS, productService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public Message<Boolean> save(@Validated(AddGroup.class) @RequestBody Product dto,@CurrentUser UserInfo currentUser) {
        log.debug("save {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productService.save(dto));
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Message<Boolean> update(@Validated(EditGroup.class) @RequestBody Product dto,@CurrentUser UserInfo currentUser) {
        log.debug("update {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productService.update(dto));
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Message<Boolean> delete(@Validated @RequestBody ListIdsDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("delete {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productService.deleteByIds(dto.getListIds()));
    }
}
