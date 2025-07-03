package com.jinbooks.persistence.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.contract.Contract;
import com.jinbooks.entity.contract.dto.ContractPageDto;
import com.jinbooks.entity.contract.vo.ContractVo;
import com.jinbooks.entity.dashboard.BaseGroupVo;
import com.jinbooks.entity.dashboard.ContractAmountTotal;
import com.jinbooks.entity.dashboard.DashBoardResultVo;
import com.jinbooks.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 合同 数据层
 *
 * @author Wuyan
 */
public interface ContractMapper extends BaseMapperPlus<ContractMapper, Contract, ContractVo> {
    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param dto  查询参数
     * @return 查询结果
     */
    Page<ContractVo> contractPageList(Page<Contract> page, @Param("dto") ContractPageDto dto);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 查询结果
     */
    ContractVo getContractById(@Param("id") String id, @Param("workspaceId") String workspaceId);

    List<BaseGroupVo<Long>> countRecentContract(String workspaceId);

    BigDecimal selectYearContractAmount(@Param("year") Integer year, @Param("workspaceId") String workspaceId);

    Long countCurrentYear(String workspaceId);

    List<BaseGroupVo<BigDecimal>> groupYearContractAmount(@Param("year") Integer year,
                                                          @Param("workspaceId") String workspaceId);

    ContractAmountTotal groupContractAmountTotal(String workspaceId);
}
