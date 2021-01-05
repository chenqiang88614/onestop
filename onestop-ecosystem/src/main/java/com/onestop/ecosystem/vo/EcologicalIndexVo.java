package com.onestop.ecosystem.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: EcologicalIndex显示类
 * @author: chenq
 * @date: 2019/9/28 17:28
 */
@Data
public class EcologicalIndexVo {
    private String country;
    private String biologicalAbundance;
    private BigDecimal vegetationalCover;
    private BigDecimal riversDensity;
    private BigDecimal landStress;
    private BigDecimal pollutionLoad;
    private BigDecimal environmentRestriction;
    private BigDecimal ecologicalRegime;
    private String grade;
}
