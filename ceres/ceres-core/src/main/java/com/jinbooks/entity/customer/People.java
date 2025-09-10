package com.jinbooks.entity.customer;

import com.baomidou.mybatisplus.annotation.*;
import com.jinbooks.entity.BaseEntity;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 联系人表 jbx_customer_contact
 *
 * @author wuyan
 * {@code @date} 2025-06-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_people")
public class People extends BaseEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7116872586366905808L;

	/**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @NotBlank(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 关联客户ID
     */
    @NotBlank(message = "客户ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String customerId;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String contactName;

    /**
     * 性别：男、女、未知
     */
    @Pattern(regexp = "^(男|女|未知)$", message = "性别格式错误")
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
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式不正确")
    private String email;

    /**
     * 地址信息
     */
    private String address;

    /**
     * 备注信息
     */
    private String remark;
    
    /**
     * 微信号
     */
    private String wechat;
    
    String workspaceId;

    /**
     * 删除标记：y-已删, n-正常
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
