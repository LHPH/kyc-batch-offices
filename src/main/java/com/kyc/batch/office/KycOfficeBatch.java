package com.kyc.batch.office;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class KycOfficeBatch implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(KycOfficeBatch.class);

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private Job job;

    public static void main(String[] args) {
        SpringApplication.run(KycOfficeBatch.class, args);
    }

    @Override
    public void run(String... args) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, InvalidJobParametersException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date-exec",new Date())
                .toJobParameters();

        JobExecution execution = jobOperator.start(job, jobParameters);
        LOGGER.info("Result execution [{}]",execution.getStatus());
    }
}
