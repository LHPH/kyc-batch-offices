package com.kyc.batch.office.config.steps;

import com.kyc.batch.office.constants.KycBatchExecutiveConstants;
import com.kyc.core.batch.BatchStepListener;
import com.kyc.core.properties.KycMessages;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DeleteOfficesMainConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("queriesProps")
    private Properties queriesProps;

    @Bean
    public Step deleteOfficesStep(JobRepository jobRepository,
                                  PlatformTransactionManager platformTransactionManager,
                                  KycMessages kycMessages
    ){
        return new StepBuilder(KycBatchExecutiveConstants.DELETE_OFFICES_STEP,jobRepository)
                .listener(deleteOfficeBatchStepListener(kycMessages))
                .<Integer, Integer>chunk(10)
                .transactionManager(platformTransactionManager)
                .reader(deleteOfficesMainReader())
                .writer(deleteOfficeMainWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Integer> deleteOfficesMainReader(){

        JdbcCursorItemReader<Integer> reader = new JdbcCursorItemReader<>(
                dataSource,
                queriesProps.get("getCurrentOfficesId").toString(),
                (rs, rowNum) -> rs.getInt("ID")
        );

        reader.setName(KycBatchExecutiveConstants.DELETE_OFFICES_STEP+"-READER");

        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<Integer> deleteOfficeMainWriter(){

        JdbcBatchItemWriter<Integer> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
        jdbcBatchItemWriter.setDataSource(dataSource);
        jdbcBatchItemWriter.setSql(queriesProps.get("deleteMainOfficeId").toString());
        jdbcBatchItemWriter.setItemPreparedStatementSetter((item,ps)->{
            ps.setInt(1,item);
        });

        return jdbcBatchItemWriter;
    }


    @Bean
    public BatchStepListener<Integer, Integer> deleteOfficeBatchStepListener(KycMessages kycMessages){
        return new BatchStepListener<>(KycBatchExecutiveConstants.DELETE_OFFICES_STEP,kycMessages.getMessage("001"));
    }
}
