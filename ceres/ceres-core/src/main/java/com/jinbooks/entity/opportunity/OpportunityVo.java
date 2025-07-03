package com.jinbooks.entity.opportunity;

import com.jinbooks.entity.followup.FollowUp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/30 9:47
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpportunityVo{

    private Opportunity opportunity;

    private List<FollowUp> followUps;
}
