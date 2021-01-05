专题产品定制流程

1. ProdOrderFileObserver 对扫描到的订单进行处理；

2. 前台通过调用ProdOrderController中的confirm方法进行确认：

   2.1 首先调用sendProdOrderAck向PSS发送订单接收情况；

   2.2 然后调用orderSplit对订单进行拆分；

   2.3 拆分后的订单分别向DMS发送数据获取订单，同时将拆分后子订单的个数写入Redis的BIZ:HASH:PRODORDER:COUNT中，将自订单的状态写入Redis的"BIZ:HASH:PRODORDER:RESULT:[TaskID]

3. ExtractReportFileObserver对扫到的数据提取订单完成报告EXTRACTREPORT进行处理：

   3.1 如果成功获取到数据，将获取到的数据地址和名字更新到数据库中；

   3.2 如果获取失败，调用preprocessOrderService的modifyTaskStatus方法，更新Redis中的自订单状态，同时更新本订单数据库中的状态；

   3.3 ？？？是否需要通过websocket向前台广播推送订单状态；

   3.4 当本条数据提取订单失败时，根据Redis中BIZ:HASH:PRODORDER:COUNT的个数，判断是否全部失败，如果全部失败，直接向PSS发送专题产品定制完成报告PRODORDERREPORT；

4. 通过consumMessage获取到处理结束的消息，并进行相应的处理：

   4.1 判断是否成功，如果成功，下发归档报告；

   4.2 如果不成功，调用modifyTaskStatus修改Redis中的状态，并判断所有子订单是否均已执行完，如果完成，则直接下发专题产品定制完成报告；

5. 根据第4步的执行结果，向DMS下发产品归档订单

6. 在ArchiveReportFileObserver中对归档结果进行处理：

   6.1 若归档成功，修改业务库中的子订单状态；

   6.2 如果归档失败，修改业务库中的子订单状态，并填写原因，同时调用modifyTaskStatus更新Redis中的子订单状态以及原任务中子订单的完成个数；

   6.3 判断原任务订单中是否均已完成所有操作，如果完成，向PSS下发专题产品定制完成报告PRODORDERREPORT