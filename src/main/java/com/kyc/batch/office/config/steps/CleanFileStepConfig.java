package com.kyc.batch.office.config.steps;

import com.kyc.core.batch.tasklets.CleanFilesTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.kyc.batch.office.constants.KycBatchExecutiveConstants.CLEAN_FILE_TASK;

@Configuration
public class CleanFileStepConfig {

    @Value("${kyc.batch.offices.base-path}")
    private String basePath;

    @Bean
    public Step cleanFileStep(JobRepository jobRepository,
                              PlatformTransactionManager platformTransactionManager){

        return new StepBuilder(CLEAN_FILE_TASK,jobRepository)
                .tasklet(cleanFileTasklet(),platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet cleanFileTasklet(){
        return new CleanFilesTasklet(CLEAN_FILE_TASK,basePath);
    }
}
