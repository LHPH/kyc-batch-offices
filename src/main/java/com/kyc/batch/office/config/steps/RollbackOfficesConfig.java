package com.kyc.batch.office.config.steps;

import com.kyc.batch.office.constants.KycBatchExecutiveConstants;
import com.kyc.batch.office.tasklets.BackupOfficesTasklet;
import com.kyc.batch.office.tasklets.RollbackOfficesTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Properties;

@Configuration
public class RollbackOfficesConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queriesProps")
    private Properties queriesProps;

    @Bean
    public Step rollbackOfficesStep(){

        return stepBuilderFactory.get(KycBatchExecutiveConstants.ROLLBACK_OFFICES_TASK)
                .tasklet(rollbackOfficesTasklet())
                .build();
    }

    @Bean
    public Tasklet rollbackOfficesTasklet(){
        return new RollbackOfficesTasklet(jdbcTemplate,queriesProps);
    }
}
