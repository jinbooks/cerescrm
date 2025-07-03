package com.jinbooks.entity.customer;

import com.baomidou.mybatisplus.annotation.*;
import com.jinbooks.entity.BaseEntity;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 客户分层表 jbx_customer_segment
 *
 * @author wuyan
 * {@code @date} 2025-06-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_customer_segment")
public class CustomerSegment extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @NotBlank(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 客户分层名称（如：VIP客户、潜力客户）
     */
    @NotBlank(message = "客户分层名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String segmentName;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 分层规则条件（SQL表达式）
     */
    private String ruleCondition;

    /**
     * 显示颜色代码（如：#FF5733）
     */
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "颜色代码格式错误")
    private String colorCode;

    String workspaceId;
    
    /**
     * 删除标记：y-已删, n-正常
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
