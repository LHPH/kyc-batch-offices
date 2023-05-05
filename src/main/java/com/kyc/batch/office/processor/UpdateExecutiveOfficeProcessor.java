package com.kyc.batch.office.processor;

import com.kyc.batch.office.model.ExecutiveOfficeRelation;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Properties;

@AllArgsConstructor
public class UpdateExecutiveOfficeProcessor implements ItemProcessor<ExecutiveOfficeRelation, ExecutiveOfficeRelation> {

    private JdbcTemplate jdbcTemplate;
    private Properties queryProps;

    @Override
    public ExecutiveOfficeRelation process(ExecutiveOfficeRelation input) {

        if(input.getIdBranch()!=0){

            List<Integer> results = jdbcTemplate.queryForList(queryProps.getProperty("checkOfficeId"),Integer.class,input.getIdBranch());

            if(results.isEmpty()){
                input.setIdBranch(0); //DEFAULT
            }
        }
        return input;
    }
}
