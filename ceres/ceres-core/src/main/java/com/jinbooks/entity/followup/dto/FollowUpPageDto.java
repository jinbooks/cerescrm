package com.jinbooks.entity.followup.dto;

import com.jinbooks.entity.PageQuery;
import lombok.Data;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/26 14:55
 */

@Data
public class FollowUpPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = 2734369339571884526L;

    String workspaceId;

    Integer category;

    Integer result;
}
