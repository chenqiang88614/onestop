package com.onestop.ecosystem.vo;

import lombok.Data;

/**
 * @description: ProdOrder确认参数
 * @author: chenq
 * @date: 2019/9/4 14:44
 */
@Data
public class ConfirmVo {
    private String id;

    private boolean confirm;

    private String reason;

    private String currentUser;
}
