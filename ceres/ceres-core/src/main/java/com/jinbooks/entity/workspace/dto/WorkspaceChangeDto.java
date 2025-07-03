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


package com.jinbooks.entity.workspace.dto;

import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.YearMonth;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/1/2 9:42
 */

@Data
public class WorkspaceChangeDto {
    @NotEmpty(message = "编辑对象不能为空", groups = {EditGroup.class})
    String workspaceId;

    @NotEmpty(message = "名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 21, message = "名称的长度不能超过21位", groups = {AddGroup.class, EditGroup.class})
    String name;

    @NotEmpty(message = "单位名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 85, message = "单位名称的长度不能超过85位", groups = {AddGroup.class, EditGroup.class})
    String companyName;

    String address;

    Integer industry;

    @NotNull(message = "状态不能为空", groups = {AddGroup.class, EditGroup.class})
    Integer status;
}
