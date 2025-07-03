package com.jinbooks.entity.customer.dto;

import com.jinbooks.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 联系人分页查询参数
 *
 * @author Wuyan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PeoplePageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = 4938120957668783024L;


    /**
     * 关联客户ID
     */
    private String customerId;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 性别：男、女、未知
     */
    private String gender;

    /**
     * 联系电话
     */
    private String phone;
    
    String workspaceId;

}
