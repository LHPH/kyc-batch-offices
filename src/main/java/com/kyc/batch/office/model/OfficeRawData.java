package com.kyc.batch.office.model;

import com.kyc.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class OfficeRawData extends BaseModel {

    private Integer idBranch;
    private String name;
    private String street;
    private String neighborhood;
    private String city;
    private String state;
    private String postalCode;
    private String businessHours;
    private String latitude;
    private String longitude;
}
