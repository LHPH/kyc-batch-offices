package com.kyc.batch.office.mappers;

import com.kyc.batch.office.model.ExecutiveOfficeRelation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecutiveOfficeRelationRowMapper implements RowMapper<ExecutiveOfficeRelation> {

    @Override
    public ExecutiveOfficeRelation mapRow(ResultSet rs, int rowNum) throws SQLException {

        ExecutiveOfficeRelation row = new ExecutiveOfficeRelation();
        row.setIdBranch(rs.getInt("ID_BRANCH"));
        row.setIdExecutive(rs.getInt("ID"));
        return row;
    }
}
