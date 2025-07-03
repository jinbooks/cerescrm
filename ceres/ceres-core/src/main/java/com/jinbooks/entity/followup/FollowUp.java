package com.jinbooks.entity.followup;

import com.baomidou.mybatisplus.annotation.*;
import com.jinbooks.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/26 14:29
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("jbx_follow_up")
public class FollowUp extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -2994269426309374693L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 业务类别：1-线索，2-客户，3-商机，4-合同，5-售后
     */
    private Integer category;

    /**
     * 关联业务ID
     */
    private String relationId;

    /**
     * 工作区ID
     */
    private String workspaceId;

    /**
     * 跟进方式：1-电话，2-邮件，3-微信，4-面访，5-短信，6-QQ，7-其他
     */
    private Integer followType;

    /**
     * 跟进标题
     */
    private String title;

    /**
     * 跟进内容
     */
    private String content;

    /**
     * 跟进结果：1-成功联系，2-未接通，3-拒绝，4-有兴趣，5-待确认，6-已确认
     */
    private Integer result;

    /**
     * 跟进时间
     */
    private Date followAt;

    /**
     * 下次跟进时间
     */
    private Date nextFollowTime;

    /**
     * 跟进时间
     */
    private Date remindTime;

    /**
     * 跟进人ID
     */
    private String followUserId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    @TableField(exist = false)
    private String followUserName;

    @TableField(exist = false)
    private String relationName;
}
