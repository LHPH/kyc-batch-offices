package com.kyc.batch.office.config.steps;

import com.kyc.batch.office.constants.KycBatchExecutiveConstants;
import com.kyc.batch.office.model.ExecutiveOfficeRelation;
import com.kyc.batch.office.model.OfficeRawData;
import com.kyc.core.batch.BatchSkipListener;
import com.kyc.core.batch.BatchStepListener;
import com.kyc.core.exception.handlers.KycBatchExceptionHandler;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.StepListenerSupport;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.util.Properties;

@Configuration
public class AddOfficesMainConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private KycBatchExceptionHandler exceptionHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("queriesProps")
    private Properties queriesProps;

    @Value("${kyc.batch.offices.offices-load.path}")
    private String officesFilePath;

    @Value("${kyc.batch.offices.offices-load.fields}")
    private String fields;

    @Bean
    public Step addOfficesStep(){
        return stepBuilderFactory
                .get(KycBatchExecutiveConstants.ADD_OFFICES_STEP)
                .listener(addOfficeBatchStepListener())
                .<OfficeRawData, OfficeRawData>chunk(10)
                .faultTolerant()
                .skip(Exception.class)
                .noSkip(FileNotFoundException.class)
                .skipLimit(10)
                .listener(addOfficeBatchSkipListener())
                .reader(addOfficesMainReader())
                .writer(addOfficeMainWriter())
                .exceptionHandler(exceptionHandler)
                .build();
    }

    @Bean
    public FlatFileItemReader<OfficeRawData> addOfficesMainReader(){

        return new FlatFileItemReaderBuilder<OfficeRawData>()
                .resource(new FileSystemResource(officesFilePath))
                .name(KycBatchExecutiveConstants.ADD_OFFICES_STEP+"-READER")
                .linesToSkip(1)
                .delimited()
                .names(fields.split(","))
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<OfficeRawData>(){{
                    setTargetType(OfficeRawData.class);
                }})
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<OfficeRawData> addOfficeMainWriter(){

        JdbcBatchItemWriter<OfficeRawData> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
        jdbcBatchItemWriter.setDataSource(dataSource);
        jdbcBatchItemWriter.setSql(queriesProps.get("addOfficesMain").toString());
        jdbcBatchItemWriter.setItemPreparedStatementSetter((item,ps)->{

            ps.setInt(1,item.getIdBranch());
            ps.setString(2,item.getName());
            ps.setString(3,item.getStreet());
            ps.setString(4,item.getNeighborhood());
            ps.setString(5,item.getCity());
            ps.setString(6,item.getState());
            ps.setString(7,item.getPostalCode());
            ps.setString(8,item.getBusinessHours());
            ps.setString(9,item.getLatitude());
            ps.setString(10,item.getLongitude());
            ps.setBoolean(11,true);
        });

        return jdbcBatchItemWriter;
    }

    @Bean
    public StepListenerSupport<OfficeRawData, OfficeRawData> addOfficeBatchStepListener(){
        return new BatchStepListener<>(KycBatchExecutiveConstants.ADD_OFFICES_STEP);
    }

    @Bean
    public SkipListener<OfficeRawData, OfficeRawData> addOfficeBatchSkipListener(){
        return new BatchSkipListener<>(KycBatchExecutiveConstants.ADD_OFFICES_STEP);
    }

}
