package com.jinbooks.entity.customer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinbooks.entity.BaseEntity;
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
public class CustomerVo extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户类型：个人、企业
     */
    private String customerType;

    /**
     * 所属行业
     */
    private String industry;
    
    String province;
    
    String city;

    /**
     * 地址信息
     */
    private String address;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 官网网址
     */
    private String website;
    
    
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
     * 客户分组名称
     */
    private String segmentName;

    /**
     * 客户负责人
     */
    private String customerHead;

    /**
     * 来源渠道
     */
    private String customerFrom;
    
    /**
     * 微信号
     */
    private String wechat;

}
