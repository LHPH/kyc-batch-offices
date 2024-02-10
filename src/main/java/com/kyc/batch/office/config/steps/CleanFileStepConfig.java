package com.kyc.batch.office.config.steps;

import com.kyc.core.batch.tasklets.CleanFilesTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.kyc.batch.office.constants.KycBatchExecutiveConstants.CLEAN_FILE_TASK;

@Configuration
public class CleanFileStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("${kyc.batch.offices.base-path}")
    private String basePath;

    @Bean
    public Step cleanFileStep(){

        return stepBuilderFactory.get(CLEAN_FILE_TASK)
                .tasklet(cleanFileTasklet())
                .build();
    }

    @Bean
    public Tasklet cleanFileTasklet(){
        return new CleanFilesTasklet(CLEAN_FILE_TASK,basePath);
    }
}
