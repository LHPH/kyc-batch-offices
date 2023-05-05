package com.kyc.batch.office.config;

import com.kyc.core.config.LoadSimpleSqlConfig;
import com.kyc.core.exception.handlers.KycBatchExceptionHandler;
import com.kyc.core.properties.KycMessages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(value = {LoadSimpleSqlConfig.class,KycMessages.class})
@Configuration
public class CommonConfig {

    @Bean
    public KycBatchExceptionHandler exceptionHandler(KycMessages kycMessages){
        return new KycBatchExceptionHandler(kycMessages.getMessage("001"));
    }
}
