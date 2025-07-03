package com.jinbooks.entity.dashboard;

import lombok.*;

import java.math.BigDecimal;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/02 18:48:44
 * {@code @version} 1.0
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ContractAmountTotal {
    private BigDecimal contractTotalAmount;
    private BigDecimal receivedTotalAmount;
    private BigDecimal unreceivedTotalAmount;
}
