package com.jinbooks.entity.opportunity.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/20 16:24
 */

@Data
public class OppStageChangeDto {


    @NotEmpty(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    private String workspaceId;

    @NotEmpty(message = "阶段名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    @NotEmpty(message = "阶段编码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String code;

    private String description;

    private Integer probability;

    private Integer sortOrder;

    private String color;

    private Integer isActive;

}
