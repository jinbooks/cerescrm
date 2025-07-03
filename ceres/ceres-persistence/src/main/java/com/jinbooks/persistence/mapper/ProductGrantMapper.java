package com.jinbooks.persistence.mapper;

import com.jinbooks.entity.dashboard.BaseGroupVo;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.product.ProductGrant;
import com.jinbooks.entity.product.dto.ProductGrantPageDto;
import com.jinbooks.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 
 */
public interface ProductGrantMapper extends BaseMapperPlus<ProductGrantMapper, ProductGrant, ProductGrant> {
    
    Page<ProductGrant> pageList(Page<ProductGrant> page, @Param("dto") ProductGrantPageDto dto);

    List<BaseGroupVo<Long>> countRecentProductGrant(String workspaceId);

    Long countCurrentYear(String workspaceId);
}
