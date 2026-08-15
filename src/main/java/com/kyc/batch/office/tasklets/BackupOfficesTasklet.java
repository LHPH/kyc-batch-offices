package com.kyc.batch.office.tasklets;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Properties;

@AllArgsConstructor
public class BackupOfficesTasklet implements Tasklet{

    private final JdbcTemplate jdbcTemplate;

    private final Properties queryProps;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

        jdbcTemplate.update(queryProps.getProperty("deleteTmpOffice"));
        jdbcTemplate.update(queryProps.getProperty("backupOffices"));

        return RepeatStatus.FINISHED;
    }
}
