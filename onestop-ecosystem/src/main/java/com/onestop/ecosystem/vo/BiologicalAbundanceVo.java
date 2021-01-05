package com.onestop.ecosystem.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: BiologicalAbundance的Vo
 * @author: chenq
 * @date: 2019/9/29 8:17
 */
@Data
public class BiologicalAbundanceVo {
    private String country;
    private BigDecimal environmentRestriction;
    private BigDecimal biodiversity;
    private BigDecimal index;
}
