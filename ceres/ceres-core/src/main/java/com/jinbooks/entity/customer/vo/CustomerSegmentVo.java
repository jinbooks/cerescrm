package com.jinbooks.entity.customer.vo;

import com.jinbooks.entity.BaseEntity;
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
public class CustomerSegmentVo extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 客户分层名称（如：VIP客户、潜力客户）
     */
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
    private String colorCode;

}
