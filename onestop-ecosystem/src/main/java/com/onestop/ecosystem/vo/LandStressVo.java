package com.onestop.ecosystem.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: LandStress的Vo
 * @author: chenq
 * @date: 2019/9/29 8:23
 */
@Data
public class LandStressVo {
    private String country;
    private BigDecimal countryArea;
    private BigDecimal severeErosion = new BigDecimal(0);
    private BigDecimal moderateErosion = new BigDecimal(0);
    private BigDecimal totalLand = new BigDecimal(0);
    private BigDecimal otherLand = new BigDecimal(0);
    private BigDecimal index;
}
