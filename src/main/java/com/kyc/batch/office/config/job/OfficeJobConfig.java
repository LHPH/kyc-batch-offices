package com.kyc.batch.office.config.job;

import com.kyc.batch.office.constants.KycBatchExecutiveConstants;
import com.kyc.core.batch.BatchJobExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.kyc.core.constants.BatchConstants.BATCH_COMPLETED;
import static com.kyc.core.constants.BatchConstants.BATCH_FAILED;

@Configuration
public class OfficeJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job officeManagementJob(Step backupExecutiveOfficesStep, Step backupOfficesStep,
                                   Step addOfficesStep, Step deleteOfficesStep,
                                   Step updateExecutiveOfficesStep, Step rollbackOfficesStep,
                                   Step cleanFileStep){

        return jobBuilderFactory.get(KycBatchExecutiveConstants.JOB_NAME)
                .listener(officeJobManagementListener())
                .start(backupExecutiveOfficesStep).on(BATCH_FAILED).fail()
                .from(backupExecutiveOfficesStep).on(BATCH_COMPLETED).to(backupOfficesStep)
                .from(backupOfficesStep).on(BATCH_FAILED).fail()
                .from(backupOfficesStep).on(BATCH_COMPLETED).to(deleteOfficesStep)
                .from(deleteOfficesStep).on(BATCH_FAILED).fail()
                .from(deleteOfficesStep).on(BATCH_COMPLETED).to(addOfficesStep)
                .from(addOfficesStep).on(BATCH_COMPLETED).to(updateExecutiveOfficesStep)
                .from(addOfficesStep).on(BATCH_FAILED).to(rollbackOfficesStep)
                .from(rollbackOfficesStep).on(BATCH_COMPLETED).to(updateExecutiveOfficesStep)
                .from(updateExecutiveOfficesStep).on(BATCH_COMPLETED).to(cleanFileStep)
                .end()
                .build();
    }

    @Bean
    public BatchJobExecutionListener officeJobManagementListener(){

        return new BatchJobExecutionListener(KycBatchExecutiveConstants.JOB_NAME);
    }

}
