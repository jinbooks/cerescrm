package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.contract.ContractProduct;
import com.jinbooks.entity.contract.dto.ContractProductPageDto;

import java.util.List;

/**
 * 服务接口
 *
 */
public interface ContractProductService {
    /**
     * 分页查询列表
     */
    Page<ContractProduct> pageList(ContractProductPageDto dto);

    /**
     * 获取详情
     */
    ContractProduct getById(String id);

    /**
     * 新增
     */
    Boolean save(ContractProduct dto);

    /**
     * 更新
     */
    Boolean update(ContractProduct dto);

    /**
     * 删除
     */
    Boolean deleteByIds(List<String> ids);
}
