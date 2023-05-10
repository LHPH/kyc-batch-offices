package com.kyc.batch.office.config.job;

import com.kyc.batch.office.constants.KycBatchExecutiveConstants;
import com.kyc.core.batch.BatchJobExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfficeJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job officeManagementJob(Step backupExecutiveOfficesStep, Step backupOfficesStep,
                                   Step addOfficesStep, Step deleteOfficesStep,
                                   Step updateExecutiveOfficesStep, Step rollbackOfficesStep){

        return jobBuilderFactory.get(KycBatchExecutiveConstants.JOB_NAME)
                .listener(officeJobManagementListener())
                .start(backupExecutiveOfficesStep).on("FAILED").fail()
                .from(backupExecutiveOfficesStep).on("COMPLETED").to(backupOfficesStep)
                .from(backupOfficesStep).on("FAILED").fail()
                .from(backupOfficesStep).on("COMPLETED").to(deleteOfficesStep)
                .from(deleteOfficesStep).on("FAILED").fail()
                .from(deleteOfficesStep).on("COMPLETED").to(addOfficesStep)
                .from(addOfficesStep).on("COMPLETED").to(updateExecutiveOfficesStep)
                .from(addOfficesStep).on("FAILED").to(rollbackOfficesStep)
                .from(rollbackOfficesStep).on("COMPLETED").to(updateExecutiveOfficesStep)
                .end()
                .build();
    }

    @Bean
    public BatchJobExecutionListener officeJobManagementListener(){

        return new BatchJobExecutionListener(KycBatchExecutiveConstants.JOB_NAME);
    }

}
