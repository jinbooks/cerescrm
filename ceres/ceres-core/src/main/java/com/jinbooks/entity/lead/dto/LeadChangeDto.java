package com.jinbooks.entity.lead.dto;

import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/13 14:06
 */

@Data
public class LeadChangeDto {

    @NotEmpty(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

//    @NotEmpty(message = "线索编码不能为空", groups = {AddGroup.class, EditGroup.class})
//    private String leadCode;

    private String workspaceId;

    private String name;

    private String company;

    private String title;

    private String phone;

    private String email;

    private String wechat;

    private Integer sourceType;

    private String sourceDetail;

    private Integer industry;

    private BigDecimal budget;

    private Integer status;

    private Integer qualityScore;

    private Integer priority;

    private Date nextFollowTime;

    private Date lastContactTime;

    private Integer followUpTimes;

    private String province;

    private String city;

    private String ownerId;

    private String demand;
}
