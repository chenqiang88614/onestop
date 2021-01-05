package com.onestop.ecosystem.service;

import com.onestop.ecosystem.bean.ArchiveOrder;
import com.onestop.ecosystem.bean.ProdOrderAck;
import com.onestop.ecosystem.bean.ProdOrderReport;
import com.onestop.ecosystem.entity.PreprocessOrder;
import com.onestop.ecosystem.entity.ProdOrder;

/**
 * @description: 文件发送接口
 * @author: chenq
 * @date: 2019/9/4 15:10
 */
public interface IFileSendService {
    /**
     * @description 发送专题产品定制确认
     * @param prodOrderAck 专题产品订单
     * @param confirm 接收或拒绝
     * @return FTP发送反馈
     */
    String sendProdOrderAck(ProdOrderAck prodOrderAck, boolean confirm, String originator,
                            String recipient);

    /**
     * @description 下发数据提取单
     * @param preprocessOrder 与处理订单
     * @return
     */
    void sendExtractOrder(PreprocessOrder preprocessOrder);

    /**
     * @description 下发专题产品定制完成报告
     * @param prodOrderReport 专题产品定制完成报告
     * @param preprocessId 预处理订单中的ID
     */
    void sendProdOrderReport(ProdOrderReport prodOrderReport, long preprocessId);

    void sendArchiveOrder(ArchiveOrder archiveOrder, long preprocessId);
}
