package com.jinbooks.entity.opportunity;

import com.baomidou.mybatisplus.annotation.*;
import com.jinbooks.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/20 15:56
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("jbx_opportunity_stage")
public class OppStage extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -3246372181197550603L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 工作区ID
     */
    private String workspaceId;

    /**
     * 阶段名称
     */
    private String name;

    /**
     * 阶段编码
     */
    private String code;

    /**
     * 阶段描述
     */
    private String description;

    /**
     * 默认成功概率
     */
    private Integer probability;

    /**
     * 排序顺序
     */
    private Integer sortOrder;

    /**
     * 阶段颜色
     */
    private String color;

    /**
     * 是否启用:0-是;1-否
     */
    private Integer isActive;

    /**
     * 是否系统预设:0-是;1-否
     */
    private Integer isSystem;

    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
