package com.kyc.batch.office.config.steps;

import com.kyc.batch.office.constants.KycBatchExecutiveConstants;
import com.kyc.core.batch.BatchStepListener;
import com.kyc.core.exception.handlers.KycBatchExceptionHandler;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DeleteOfficesMainConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private KycBatchExceptionHandler exceptionHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("queriesProps")
    private Properties queriesProps;

    @Bean
    public Step deleteOfficesStep(){
        return stepBuilderFactory
                .get(KycBatchExecutiveConstants.DELETE_OFFICES_STEP)
                .listener(deleteOfficeBatchStepListener())
                .<Integer, Integer>chunk(10)
                .reader(deleteOfficesMainReader())
                .writer(deleteOfficeMainWriter())
                .exceptionHandler(exceptionHandler)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Integer> deleteOfficesMainReader(){

        JdbcCursorItemReader<Integer> reader = new JdbcCursorItemReader<>();

        reader.setSql(queriesProps.get("getCurrentOfficesId").toString());
        reader.setRowMapper((rs, rowNum) -> rs.getInt("ID"));
        reader.setDataSource(dataSource);
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
    public BatchStepListener<Integer, Integer> deleteOfficeBatchStepListener(){
        return new BatchStepListener<>(KycBatchExecutiveConstants.DELETE_OFFICES_STEP);
    }
}
