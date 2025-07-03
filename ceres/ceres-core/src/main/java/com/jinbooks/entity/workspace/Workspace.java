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


package com.jinbooks.entity.workspace;

import com.baomidou.mybatisplus.annotation.*;
import com.jinbooks.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 工作区 实体类
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_workspace")
public class Workspace extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 4825104334666554378L;

    @TableId(type = IdType.ASSIGN_ID,  value = "workspace_id")
    String workspaceId;

    String name;

    String companyName;

    String address;

    Integer industry;

    Integer status;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    String deleted;
}
