package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.contract.Contract;
import com.jinbooks.entity.contract.dto.ContractPageDto;
import com.jinbooks.entity.contract.vo.ContractVo;

import java.util.List;

/**
 * 合同服务接口
 *
 * @author Wuyan
 */
public interface ContractService {
    /**
     * 分页查询合同列表
     */
    Page<ContractVo> pageList(ContractPageDto dto);

    /**
     * 获取合同详情
     */
    ContractVo getById(String id,String workspaceId);

    /**
     * 新增合同
     */
    Boolean save(Contract dto);

    /**
     * 更新合同
     */
    Boolean update(Contract dto);

    /**
     * 删除合同
     */
    Boolean deleteByIds(List<String> ids);
}
