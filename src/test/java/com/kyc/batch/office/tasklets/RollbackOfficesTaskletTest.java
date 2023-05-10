package com.kyc.batch.office.tasklets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Properties;

@ExtendWith(MockitoExtension.class)
public class RollbackOfficesTaskletTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private Properties queryProps;

    @InjectMocks
    private RollbackOfficesTasklet rollbackOfficesTasklet;

    @BeforeAll
    public static void init(){
        MockitoAnnotations.openMocks(RollbackOfficesTasklet.class);
    }

    @Test
    public void execute_rollbackOffice_successfulExecution(){

        RepeatStatus result = rollbackOfficesTasklet.execute(Mockito.mock(StepContribution.class),Mockito.mock(ChunkContext.class));
        Assertions.assertEquals(RepeatStatus.FINISHED,result);
    }
}
