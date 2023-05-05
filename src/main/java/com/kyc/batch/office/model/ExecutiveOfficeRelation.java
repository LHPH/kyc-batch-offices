package com.kyc.batch.office.model;

import com.kyc.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExecutiveOfficeRelation extends BaseModel {

    private Integer idExecutive;
    private Integer idBranch;
}
