package com.jinbooks.entity.lead.vo;

import com.jinbooks.entity.followup.FollowUp;
import com.jinbooks.entity.lead.Lead;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/6/30 11:01
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadFollowUpVo {
    private Lead lead;

    private List<FollowUp> followUp;
}
