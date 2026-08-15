package com.kyc.batch.office.config.steps;

import com.kyc.batch.office.constants.KycBatchExecutiveConstants;
import com.kyc.batch.office.mappers.ExecutiveOfficeRelationRowMapper;
import com.kyc.batch.office.model.ExecutiveOfficeRelation;
import com.kyc.core.batch.BatchStepListener;
import com.kyc.core.properties.KycMessages;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class BackupExecutiveOfficeConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("queriesProps")
    private Properties queriesProps;

    @Value("${kyc.batch.offices.backup-executive-office.path}")
    private String backupPath;

    @Bean
    public Step backupExecutiveOfficesStep(JobRepository jobRepository,
                                           PlatformTransactionManager platformTransactionManager,
                                           KycMessages kycMessages
    ){
        return new StepBuilder(KycBatchExecutiveConstants.BACKUP_EXECUTIVES_OFFICES_STEP,jobRepository)
                .listener(backupExecutiveOfficeBatchStepListener(kycMessages))
                .<ExecutiveOfficeRelation, ExecutiveOfficeRelation>chunk(10)
                .transactionManager(platformTransactionManager)
                .reader(backupExecutiveOfficeReader())
                .writer(backupExecutiveOfficeWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<ExecutiveOfficeRelation> backupExecutiveOfficeReader(){

       return new JdbcCursorItemReader<>(
                dataSource,
                queriesProps.get("backupExecutiveAndOffice").toString(),
                new ExecutiveOfficeRelationRowMapper()
                );
    }

    @Bean
    public FlatFileItemWriter<ExecutiveOfficeRelation> backupExecutiveOfficeWriter(){

        BeanWrapperFieldExtractor<ExecutiveOfficeRelation> fieldExtractor = new BeanWrapperFieldExtractor<>(new String[]{"idExecutive","idBranch"});

        DelimitedLineAggregator<ExecutiveOfficeRelation> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FlatFileItemWriter<ExecutiveOfficeRelation> flatFileItemWriter = new FlatFileItemWriter<>(
                new FileSystemResource(backupPath),
                lineAggregator
        );
        flatFileItemWriter.setHeaderCallback((writer) -> writer.write("idExecutive,idBranch"));

        return flatFileItemWriter;
    }

    @Bean
    public BatchStepListener<ExecutiveOfficeRelation, ExecutiveOfficeRelation> backupExecutiveOfficeBatchStepListener(KycMessages kycMessages){
        return new BatchStepListener<>(KycBatchExecutiveConstants.BACKUP_EXECUTIVES_OFFICES_STEP,kycMessages.getMessage("001"));
    }
}
