package com.kyc.batch.office.config.steps;

import com.kyc.batch.office.constants.KycBatchExecutiveConstants;
import com.kyc.batch.office.tasklets.BackupOfficesTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;

@Configuration
public class BackupOfficesConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queriesProps")
    private Properties queriesProps;

    @Bean
    public Step backupOfficesStep(JobRepository jobRepository,
                                  PlatformTransactionManager platformTransactionManager){

        return new StepBuilder(KycBatchExecutiveConstants.BACKUP_OFFICES_TASK,jobRepository)
                .tasklet(backupOfficesTasklet(),platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet backupOfficesTasklet(){
        return new BackupOfficesTasklet(jdbcTemplate,queriesProps);
    }
}
