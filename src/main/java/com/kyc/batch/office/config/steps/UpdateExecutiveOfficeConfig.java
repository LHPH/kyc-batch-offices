package com.kyc.batch.office.config.steps;

import com.kyc.batch.office.constants.KycBatchExecutiveConstants;
import com.kyc.batch.office.model.ExecutiveOfficeRelation;
import com.kyc.batch.office.processor.UpdateExecutiveOfficeProcessor;
import com.kyc.core.batch.BatchStepListener;
import com.kyc.core.exception.handlers.KycBatchExceptionHandler;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Properties;

@Configuration
public class UpdateExecutiveOfficeConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private KycBatchExceptionHandler exceptionHandler;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queriesProps")
    private Properties queriesProps;

    @Value("${kyc.batch.offices.backup-executive-office.path}")
    private String backupPath;

    @Value("${kyc.batch.offices.backup-executive-office.fields}")
    private String fields;

    @Bean
    public Step updateExecutiveOfficesStep(){
        return stepBuilderFactory
                .get(KycBatchExecutiveConstants.BACKUP_EXECUTIVES_OFFICES_STEP)
                .listener(updateExecutiveOfficeBatchStepListener())
                .<ExecutiveOfficeRelation, ExecutiveOfficeRelation>chunk(10)
                .reader(updateExecutiveOfficesMainReader())
                .processor(updateExecutiveOfficeProcessor())
                .writer(updateExecutiveOfficeMainWriter())
                .exceptionHandler(exceptionHandler)
                .build();
    }

    @Bean
    public FlatFileItemReader<ExecutiveOfficeRelation> updateExecutiveOfficesMainReader(){

        return new FlatFileItemReaderBuilder<ExecutiveOfficeRelation>()
                .resource(new FileSystemResource(backupPath))
                .name(KycBatchExecutiveConstants.UPDATE_EXECUTIVE_OFFICES_STEP+"-READER")
                .linesToSkip(1)
                .delimited()
                .names(fields.split(","))
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<ExecutiveOfficeRelation>(){{
                    setTargetType(ExecutiveOfficeRelation.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<ExecutiveOfficeRelation, ExecutiveOfficeRelation> updateExecutiveOfficeProcessor(){

        return new UpdateExecutiveOfficeProcessor(jdbcTemplate,queriesProps);
    }

    @Bean
    public JdbcBatchItemWriter<ExecutiveOfficeRelation> updateExecutiveOfficeMainWriter(){

        JdbcBatchItemWriter<ExecutiveOfficeRelation> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
        jdbcBatchItemWriter.setDataSource(jdbcTemplate.getDataSource());
        jdbcBatchItemWriter.setSql(queriesProps.get("updateExecutiveOffice").toString());
        jdbcBatchItemWriter.setItemPreparedStatementSetter((item,ps)->{
            ps.setInt(1,item.getIdBranch());
            ps.setInt(2,item.getIdExecutive());
        });

        return jdbcBatchItemWriter;
    }

    @Bean
    public BatchStepListener<ExecutiveOfficeRelation, ExecutiveOfficeRelation> updateExecutiveOfficeBatchStepListener(){
        return new BatchStepListener<>(KycBatchExecutiveConstants.UPDATE_EXECUTIVE_OFFICES_STEP);
    }
}
