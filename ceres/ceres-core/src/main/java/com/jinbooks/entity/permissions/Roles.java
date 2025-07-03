/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
 





package com.jinbooks.entity.permissions;

import java.io.Serial;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.jinbooks.constants.ConstsRoles;
import com.jinbooks.entity.BaseEntity;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author 24096
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName( "JBX_ROLES")
public class Roles extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 4660258495864814777L;

    @TableId(type = IdType.ASSIGN_ID)
    String id;

    @NotEmpty(message = "用户组编码不能为空", groups = {AddGroup.class, EditGroup.class})
    String roleCode;

    @NotEmpty(message = "用户组名称不能为空", groups = {AddGroup.class, EditGroup.class})
    String roleName;

    String pattern;

    String category;

    String filters ;

    String orgIdsList;

    int isdefault;

    String description;

    int status;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value="n",delval="y")
    String deleted;

    @TableField(exist = false)
	private String instName;

    @TableField(exist = false)
	String gradingUserId;


    public Roles(String id) {
        this.id = id;
    }

    /**
     * Groups.
     * @param id String
     * @param groupName String
     * @param isdefault int
     */
    public Roles(String id, String roleName, int isdefault) {
        super();
        this.id = id;
        this.roleName = roleName;
        this.isdefault = isdefault;
    }

    public Roles(String id, String roleCode,String roleName, int isdefault) {
        super();
        this.id = id;
        this.roleCode = roleName;
        this.roleName = roleName;
        this.isdefault = isdefault;
    }

	/**
     * ROLE_ALL_USER must be
     * 		1, dynamic
     * 		2, all orgIdsList
	 *		3, not filters
     */
    public void setDefaultAllUser() {
    	this.pattern = ConstsRoles.Pattern.DYNAMIC;
    	this.orgIdsList ="";
		this.filters ="";
    }
}
