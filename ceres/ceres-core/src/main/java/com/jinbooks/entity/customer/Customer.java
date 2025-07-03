package com.jinbooks.entity.customer;

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
 * 客户表 jbx_customer
 *
 * @author wuyan
 * {@code @date} 2023/9/5 15:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_customer")
public class Customer extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @NotBlank(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 工作空间ID
     */
    private String workspaceId;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String customerName;

    /**
     * 客户类型：个人、企业
     */
    @NotBlank(message = "客户类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String customerType;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 地址信息
     */
    private String address;

    /**
     * 电子邮箱
     */
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式不正确")
    private String email;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 官网网址
     */
    private String website;
    
    
    String  province;
    
    String  city;
    
    String content;
    
    /**
     * 开票信息
     * 
     */
    
    private String taxNumber;
    
    private String taxContact;
    
    private String taxBank;
    
    private String taxBankAccount;
    
    private String taxBankAccountNumber;

    /**
     * 最近购买时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate lastPurchaseTime;

    /**
     * 购买频次
     */
    private Integer purchaseFrequency;

    /**
     * 累计消费金额
     */
    private BigDecimal totalSpending;

    /**
     * 服务满意度(1-5)
     */
    private Integer serviceSatisfaction;

    /**
     * 客户分组ID
     */
    private String segmentId;

    /**
     * 客户负责人
     */
    private String customerHead;

    /**
     * 来源渠道
     */
    private String customerFrom;

    /**
     * 删除标记：y-已删, n-正常
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
