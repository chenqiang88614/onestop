package com.onestop.ecosystem.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: VegetationalCover的Vo
 * @author: chenq
 * @date: 2019/9/29 8:31
 */
@Data
public class VegetationalCoverVo {
    private String country;
    private BigDecimal index;
}
