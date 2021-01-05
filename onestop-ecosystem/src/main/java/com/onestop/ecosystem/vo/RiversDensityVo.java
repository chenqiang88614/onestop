package com.onestop.ecosystem.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: RiversDensity的Vo
 * @author: chenq
 * @date: 2019/9/29 8:29
 */
@Data
public class RiversDensityVo {
    private String country;
    private BigDecimal countryArea;
    private BigDecimal riverLength;
    private BigDecimal riverArea;
    private BigDecimal lake;
    private BigDecimal reservior;
    private BigDecimal snowfield;
    private BigDecimal waterArea;
    private BigDecimal waterResource;
    private BigDecimal index;
}
