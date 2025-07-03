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

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jinbooks.entity.BaseEntity;

/**
 * @author 24096
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("jbx_permission_workspace")
public class PermissionWorkspace  extends BaseEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1406417974520137150L;

	@TableId(type = IdType.ASSIGN_ID)
    String id;

    String userId;

    String workspaceId;
    
    @TableField(exist = false)
    String name;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value="n",delval="y")
	String deleted;

    public PermissionWorkspace(String userId, String workspaceId) {
        this.userId = userId;
        this.workspaceId = workspaceId;
    }

}
