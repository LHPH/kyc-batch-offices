package com.kyc.batch.office.processor;

import com.kyc.batch.office.model.ExecutiveOfficeRelation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateExecutiveOfficeProcessorTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private Properties queryProps;

    @InjectMocks
    private UpdateExecutiveOfficeProcessor processor;

    @BeforeAll
    public static void init(){
        MockitoAnnotations.openMocks(UpdateExecutiveOfficeProcessorTest.class);
    }

    @Test
    public void process_processRecordWithExecutiveWithBranchDifferentZero_returnSameRecord(){

        ExecutiveOfficeRelation model = new ExecutiveOfficeRelation();
        model.setIdBranch(0);

        ExecutiveOfficeRelation result = processor.process(model);
        Assertions.assertEquals(model,result);
    }

    @Test
    public void process_processRecordWithExecutiveWithNoRecordedBranch_returnBranchZero(){

        ExecutiveOfficeRelation model = new ExecutiveOfficeRelation();
        model.setIdBranch(100);

        when(queryProps.getProperty(anyString()))
                .thenReturn("query");
        when(jdbcTemplate.queryForList(anyString(),any(),anyInt()))
                .thenReturn(new ArrayList<>());
        ExecutiveOfficeRelation result = processor.process(model);
        Assertions.assertEquals(0,model.getIdBranch());
    }

    @Test
    public void process_processRecordWithExecutiveWithRecordedBranch_returnSameRecord(){

        ExecutiveOfficeRelation model = new ExecutiveOfficeRelation();
        model.setIdBranch(10);

        when(queryProps.getProperty(anyString()))
                .thenReturn("query");
        when(jdbcTemplate.queryForList(anyString(),any(),anyInt()))
                .thenReturn(Collections.singletonList(10));
        ExecutiveOfficeRelation result = processor.process(model);
        Assertions.assertEquals(10,model.getIdBranch());
    }
}
