package com.jinbooks.entity.followup.dto;

import com.jinbooks.validate.AddGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/26 14:55
 */

@Data
public class FollowUpChangeDto {

    /**
     * 业务类别：1-线索，2-客户，3-商机，4-合同，5-售后
     */
    @NotNull(message = "业务类别不能为空", groups = {AddGroup.class})
    private Integer category;

    /**
     * 关联业务ID
     */
    @NotEmpty(message = "关联业务ID不能为空", groups = {AddGroup.class})
    private String relationId;

    /**
     * 工作区ID
     */
    private String workspaceId;

    /**
     * 跟进方式：1-电话，2-邮件，3-微信，4-面访，5-短信，6-QQ，7-其他
     */
    @NotNull(message = "请选择跟进方式", groups = {AddGroup.class})
    private Integer followType;

    /**
     * 跟进标题
     */
    @NotEmpty(message = "请填写跟进标题", groups = {AddGroup.class})
    private String title;

    /**
     * 跟进内容
     */
    @NotEmpty(message = "请填写跟进内容", groups = {AddGroup.class})
    private String content;

    /**
     * 跟进结果：1-成功联系，2-未接通，3-拒绝，4-有兴趣，5-待确认，6-已确认
     */
    @NotNull(message = "请选择跟进结果", groups = {AddGroup.class})
    private Integer result;

    /**
     * 跟进时间
     */
    @NotNull(message = "请填写跟进时间", groups = {AddGroup.class})
    private Date followAt;

    /**
     * 下次跟进时间
     */
    private Date nextFollowTime;

    /**
     * 提醒时间
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
}
