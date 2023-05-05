package com.kyc.batch.office.config.steps;

import com.kyc.batch.office.constants.KycBatchExecutiveConstants;
import com.kyc.batch.office.mappers.ExecutiveOfficeRelationRowMapper;
import com.kyc.batch.office.model.ExecutiveOfficeRelation;
import com.kyc.core.batch.BatchStepListener;
import com.kyc.core.exception.handlers.KycBatchExceptionHandler;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class BackupExecutiveOfficeConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private KycBatchExceptionHandler exceptionHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("queriesProps")
    private Properties queriesProps;

    @Value("${kyc.batch.offices.backup-executive-office.path}")
    private String backupPath;

    @Bean
    public Step backupExecutiveOfficesStep(){
        return stepBuilderFactory
                .get(KycBatchExecutiveConstants.BACKUP_EXECUTIVES_OFFICES_STEP)
                .listener(backupExecutiveOfficeBatchStepListener())
                .<ExecutiveOfficeRelation, ExecutiveOfficeRelation>chunk(10)
                .reader(backupExecutiveOfficeReader())
                .writer(backupExecutiveOfficeWriter())
                .exceptionHandler(exceptionHandler)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<ExecutiveOfficeRelation> backupExecutiveOfficeReader(){

        JdbcCursorItemReader<ExecutiveOfficeRelation> reader = new JdbcCursorItemReader<>();

        reader.setSql(queriesProps.get("backupExecutiveAndOffice").toString());
        reader.setRowMapper(new ExecutiveOfficeRelationRowMapper());
        reader.setDataSource(dataSource);

        return reader;
    }

    @Bean
    public FlatFileItemWriter<ExecutiveOfficeRelation> backupExecutiveOfficeWriter(){

        BeanWrapperFieldExtractor<ExecutiveOfficeRelation> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"idExecutive","idBranch"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<ExecutiveOfficeRelation> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FlatFileItemWriter<ExecutiveOfficeRelation> flatFileItemWriter = new FlatFileItemWriter<>();
        flatFileItemWriter.setResource(new FileSystemResource(backupPath));
        flatFileItemWriter.setLineAggregator(lineAggregator);
        flatFileItemWriter.setHeaderCallback((writer) -> writer.write("idExecutive,idBranch"));

        return flatFileItemWriter;
    }

    @Bean
    public BatchStepListener<ExecutiveOfficeRelation, ExecutiveOfficeRelation> backupExecutiveOfficeBatchStepListener(){
        return new BatchStepListener<>(KycBatchExecutiveConstants.BACKUP_EXECUTIVES_OFFICES_STEP);
    }
}
