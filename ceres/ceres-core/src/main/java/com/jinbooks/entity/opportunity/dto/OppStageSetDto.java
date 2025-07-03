package com.jinbooks.entity.opportunity.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/24 16:39
 */

@Data
public class OppStageSetDto {

    @NotEmpty(message = "商机ID不能为空")
    String oppId;

    @NotEmpty(message = "商机阶段ID不能为空")
    String stageId;

    String workspaceId;
}
