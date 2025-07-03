package com.jinbooks.entity.opportunity.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.entity.PageQuery;
import lombok.Data;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/20 16:24
 */

@Data
public class OppStagePageDto extends PageQuery {

    @Serial
    private static final long serialVersionUID = -997535797489256932L;

    String workspaceId;
}
