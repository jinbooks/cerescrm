package com.jinbooks.entity.opportunity.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/20 14:59
 */

@Data
public class OpportunityChangeDto{

    @NotEmpty(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    private String workspaceId;

    @NotEmpty(message = "商机名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    private String description;

    private Integer type;

    private Integer sourceType;

    private String sourceDetail;

    private Integer industry;

    private String province;

    private String city;

    private BigDecimal amount;

    private BigDecimal expectedAmount;

    private BigDecimal cost;

    private Integer probability;

    private Integer status;

    private Date expectedCloseDate;

    private Date firstContactDate;

    private String competitors;

    private String winReason;

    private String loseReason;


    private Integer priority;

    private Integer qualityScore;


    /**
     * 客户ID（关联客户表）
     */
    private String customerId;

    /**
     * 联系人ID（关联联系人表）
     */
    private String contactId;

    /**
     * 来源线索ID（关联线索表）
     */
    private String leadId;

    /**
     * 负责人ID
     */
    private String ownerId;

    private Boolean isLeadConvert;

    /**
     * 姓名
     */
    private String peopleName;

    /**
     * 公司
     */
    private String company;

}
