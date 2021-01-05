package com.onestop.ecosystem.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: PollutionLoad的Vo
 * @author: chenq
 * @date: 2019/9/29 8:26
 */
@Data
public class PollutionLoadVo {
    private String country;
    private BigDecimal countryArea = new BigDecimal(0);
    private BigDecimal oxygenDemand = new BigDecimal(0);
    private BigDecimal ammoniaNitrogen = new BigDecimal(0);
    private BigDecimal fulfurDioxide = new BigDecimal(0);
    private BigDecimal oxynirtide = new BigDecimal(0);
    private BigDecimal smokeDust = new BigDecimal(0);
    private BigDecimal solidWaste = new BigDecimal(0);
    private BigDecimal annualPrecipitation = new BigDecimal(0);
    private BigDecimal index;
}
