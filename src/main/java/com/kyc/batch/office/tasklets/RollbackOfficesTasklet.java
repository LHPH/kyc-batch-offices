package com.kyc.batch.office.tasklets;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Properties;

@AllArgsConstructor
public class RollbackOfficesTasklet implements Tasklet {

    private JdbcTemplate jdbcTemplate;

    private Properties queryProps;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

        jdbcTemplate.update(queryProps.getProperty("deleteAllMainOffice"));
        jdbcTemplate.update(queryProps.getProperty("rollbackMainOffices"));

        return RepeatStatus.FINISHED;
    }
}
