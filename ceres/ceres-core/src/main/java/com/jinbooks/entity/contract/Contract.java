package com.jinbooks.entity.contract;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinbooks.entity.BaseEntity;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 合同表 jbx_contract
 *
 * @author wuyan
 * {@code @date} 2025-06-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_contract")
public class Contract extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @NotBlank(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 关联合同模板ID
     */
    private String contractTemplateId;

    /**
     * 合同编码
     */
    private String code;
    
    /**
     * 关联客户ID
     */
    @NotBlank(message = "客户ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String customerId;

    /**
     * 合同名称
     */
    @NotBlank(message = "合同名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String contractName;

    /**
     * 合同金额
     */
    private BigDecimal amount;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 合同状态：草稿、待审批、审批通过、审批拒绝、履行中、已完成、已终止
     */
    @Pattern(regexp = "^(草稿|待审批|审批通过|审批拒绝|履行中|已完成|已终止)$", message = "合同状态格式错误")
    private String status;

    /**
     * 签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate signingDate;

    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate effectiveDate;

    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate expirationDate;

    /**
     * 税率
     */
    private Float taxRate;

    /**
     * 合同内容
     */
    private String content;

    String workspaceId;
    /**
     * 删除标记：y-已删, n-正常
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
