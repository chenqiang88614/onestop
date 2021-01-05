package com.onestop.ecosystem.init;

import com.onestop.fsf.spi.FsfActivator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
@Order(3)
public class FileScannerStarter implements CommandLineRunner {
    @Value("${Configuration.customer.fsf.scanner-file}")
    private String fsfScannerFile;
    @Override
    public void run(String... args) throws Exception {
        File file = new File(fsfScannerFile);
        if (!file.exists()) {
            log.error("文件扫描配置文件 {} 不存在", fsfScannerFile);
            return;
        }
        FsfActivator.start(fsfScannerFile);
        log.info("文件扫描系统启动成功！");
    }
}
