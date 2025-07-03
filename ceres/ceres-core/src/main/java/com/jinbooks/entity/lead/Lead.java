package com.jinbooks.entity.lead;

import com.baomidou.mybatisplus.annotation.*;
import com.jinbooks.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/13 10:49
 */


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("jbx_lead")
public class Lead extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3957571951215316889L;


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 线索编码
     */
    private String leadCode;

    /**
     * 工作区ID
     */
    private String workspaceId;

    /**
     * 联系人姓名
     */
    private String name;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 职位
     */
    private String title;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 来源类型：1-网站，2-电话，3-推荐，4-展会，5-广告
     */
    private Integer sourceType;

    /**
     * 来源详情
     */
    private String sourceDetail;

    /**
     * 行业
     */
    private Integer industry;

    /**
     * 跟进次数
     */
    private Integer followUpTimes;

    /**
     * 预算
     */
    private BigDecimal budget;

    /**
     * 状态：1-新线索，2-已联系，3-有意向，4-已转商机，5-已流失
     */
    private Integer status;

    /**
     * 质量评分1-5
     */
    private Integer qualityScore;

    /**
     * 优先级：1-低，2-中，3-高
     */
    private Integer priority;

    /**
     * 负责人ID
     */
    private String ownerId;

    /**
     * 分配时间
     */
    private Date assignedAt;

    /**
     * 下次跟进时间
     */
    private Date nextFollowTime;

    /**
     * 最后联系时间
     */
    private Date lastContactTime;

    /**
     * 转化时间
     */
    private Date convertedAt;

    /**
     * 线索需求
     */
    private String demand;

    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    @TableField(exist = false)
    private String ownerName;
}
