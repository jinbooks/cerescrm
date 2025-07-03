package com.jinbooks.entity.opportunity;

import com.baomidou.mybatisplus.annotation.*;
import com.jinbooks.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/19 17:17
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("jbx_opportunity")
public class Opportunity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 5319188877725227815L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 商机编号
     */
    private String opportunityCode;

    /**
     * 工作区ID
     */
    private String workspaceId;

    /**
     * 商机名称
     */
    private String name;

    /**
     * 商机描述
     */
    private String description;

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
//    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String leadId;

    /**
     * 商机类型：1-新业务，2-续约，3-升级，4-交叉销售
     */
    private Integer type;

    /**
     * 来源类型：1-线索转化，2-客户推荐，3-市场活动，4-直接开发
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
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 商机金额
     */
    private BigDecimal amount;

    /**
     * 预期金额
     */
    private BigDecimal expectedAmount;

    /**
     * 加权金额（金额*成功概率）
     */
    private BigDecimal weightedAmount;

    /**
     * 预估成本
     */
    private BigDecimal cost;

    /**
     * 预估利润
     */
    private BigDecimal profit;

    /**
     * 当前阶段ID
     */
    private String stageId;

    /**
     * 成功概率（0-100）
     */
    private Integer probability;

    /**
     * 状态：1-进行中，2-已赢单，3-已输单，4-已暂停
     */
    private Integer status;

    /**
     * 预计成交日期
     */
    private Date expectedCloseDate;

    /**
     * 实际成交日期
     */
    private Date actualCloseDate;

    /**
     * 首次接触日期
     */
    private Date firstContactDate;

    /**
     * 最后活动日期
     */
    private Date lastActivityDate;

    /**
     * 竞争对手（JSON格式）
     */
    private String competitors;

    /**
     * 赢单原因
     */
    private String winReason;

    /**
     * 输单原因
     */
    private String loseReason;

    /**
     * 负责人ID
     */
    private String ownerId;

    /**
     * 分配时间
     */
    private Date assignedAt;

    /**
     * 优先级：1-低，2-中，3-高
     */
    private Integer priority;

    /**
     * 质量评分1-5
     */
    private Integer qualityScore;

    /**
     * 姓名
     */
    private String peopleName;

    /**
     * 公司
     */
    private String company;

    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    @TableField(exist = false)
    private String ownerName;

    @TableField(exist = false)
    private String leadName;

    @TableField(exist = false)
    private String stageName;

    @TableField(exist = false)
    private String customerName;

    @TableField(exist = false)
    private String contactName;
}
