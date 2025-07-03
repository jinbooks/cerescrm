package com.jinbooks.entity.customer.vo;

import com.jinbooks.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 联系人表 jbx_customer_contact
 *
 * @author wuyan
 * {@code @date} 2025-06-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PeopleVo extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 关联客户ID
     */
    private String customerId;
    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 性别：男、女、未知
     */
    private String gender;

    /**
     * 职位名称
     */
    private String jobTitle;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 地址信息
     */
    private String address;

    /**
     * 备注信息
     */
    private String remark;
}
