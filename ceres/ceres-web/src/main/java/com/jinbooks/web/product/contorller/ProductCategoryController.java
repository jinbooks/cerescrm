package com.jinbooks.web.product.contorller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.product.ProductCategory;
import com.jinbooks.entity.product.dto.ProductCategoryPageDto;
import com.jinbooks.persistence.service.ProductCategoryService;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.dromara.hutool.core.tree.MapTree;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/productCategory")
@RequiredArgsConstructor
@Slf4j
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    /**
     * 分页查询列表
     */
    @GetMapping("/fetch")
    public Message<Page<ProductCategory>> fetch(@ParameterObject ProductCategoryPageDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productCategoryService.pageList(dto));
    }

    /**
     * 获取详情
     */
    @GetMapping("/get/{id}")
    public Message<ProductCategory> getById(@PathVariable("id") String id) {
        log.debug("get {}", id);
        return new Message<>(Message.SUCCESS, productCategoryService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public Message<Boolean> save(@Validated(AddGroup.class) @RequestBody ProductCategory dto,@CurrentUser UserInfo currentUser) {
        log.debug("save {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productCategoryService.save(dto));
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Message<Boolean> update(@Validated(EditGroup.class) @RequestBody ProductCategory dto,@CurrentUser UserInfo currentUser) {
        log.debug("update {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productCategoryService.update(dto));
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Message<Boolean> delete(@Validated @RequestBody ListIdsDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("delete {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(productCategoryService.deleteByIds(dto.getListIds()));
    }
    
    @GetMapping(value = {"/tree"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<List<MapTree<String>>> tree(@CurrentUser UserInfo currentUser) {
        List<MapTree<String>> tree = productCategoryService.tree(currentUser.getWorkspaceId());
        return new Message<>(Message.SUCCESS, tree);
    }
}
