package com.kyc.batch.office.config.steps;

import com.kyc.batch.office.constants.KycBatchExecutiveConstants;
import com.kyc.batch.office.tasklets.FileDeleteTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class CleanFileStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("${kyc.batch.offices.backup-executive-office.path}")
    private String filePath;

    @Bean
    public Step cleanFileStep(){

        return stepBuilderFactory.get(KycBatchExecutiveConstants.CLEAN_FILE_TASK)
                .tasklet(fileDeleteTasklet())
                .build();
    }

    @Bean
    public Tasklet fileDeleteTasklet(){
        return new FileDeleteTasklet(new FileSystemResource(filePath));
    }
}
