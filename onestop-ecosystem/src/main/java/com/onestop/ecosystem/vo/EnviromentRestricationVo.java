package com.onestop.ecosystem.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: EnviromentRestrication的Vo
 * @author: chenq
 * @date: 2019/9/29 8:21
 */

@Data
public class EnviromentRestricationVo {
    private String country;
    private BigDecimal countryArea;
    private BigDecimal forest = new BigDecimal(0);
    private BigDecimal grassland = new BigDecimal(0);
    private BigDecimal wetland = new BigDecimal(0);
    private BigDecimal plowland = new BigDecimal(0);
    private BigDecimal constructionLand = new BigDecimal(0);
    private BigDecimal unusedLand = new BigDecimal(0);
    private BigDecimal index;
}
