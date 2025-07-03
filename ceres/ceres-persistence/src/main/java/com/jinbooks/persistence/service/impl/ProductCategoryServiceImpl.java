package com.jinbooks.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.product.ProductCategory;
import com.jinbooks.entity.product.dto.ProductCategoryPageDto;
import com.jinbooks.persistence.mapper.ProductCategoryMapper;
import com.jinbooks.persistence.service.ProductCategoryService;

import org.dromara.hutool.core.tree.MapTree;
import org.dromara.hutool.core.tree.TreeNode;
import org.dromara.hutool.core.tree.TreeUtil;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
	
    private final ProductCategoryMapper baseMapper;

    @Override
    public Page<ProductCategory> pageList(ProductCategoryPageDto dto) {
        return baseMapper.pageList(dto.build(), dto);
    }

    @Override
    public ProductCategory getById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Boolean save(ProductCategory dto) {
        return baseMapper.insert(dto) > 0;
    }

    @Override
    public Boolean update(ProductCategory dto) {
        return baseMapper.updateById(dto) > 0;
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public List<MapTree<String>> tree(String workspaceId) {
    	 LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
    	 wrapper.eq(ProductCategory::getWorkspaceId, workspaceId);
        List<ProductCategory> categorys = baseMapper.selectList(wrapper);

        List<TreeNode<String>> treeNode = new ArrayList<>();
        categorys.forEach(temp -> {
            TreeNode<String> stringTreeNode = new TreeNode<>(
                    temp.getId(),
                    temp.getParentId(),
                    String.format("%s-%s", temp.getCode(), temp.getName()),
                    temp.getCode()
            );

            // 为每个节点创建独立的 extraMap
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("id", temp.getId());
            extraMap.put("name", temp.getName());
            extraMap.put("code", temp.getCode());
            if(StringUtils.isBlank(temp.getParentId())) {
            	extraMap.put("parentId", null);
            }else {
            	extraMap.put("parentId", temp.getParentId());
            }
            extraMap.put("displayName", temp.getName());
            stringTreeNode.setExtra(extraMap);

            treeNode.add(stringTreeNode);
        });

        List<MapTree<String>> tree = TreeUtil.build(treeNode, null);

        if (ObjectUtils.isEmpty(tree)) {
            tree = new ArrayList<>();
        }
        return tree;
    }
}
