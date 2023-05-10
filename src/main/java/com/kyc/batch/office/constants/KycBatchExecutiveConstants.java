package com.kyc.batch.office.constants;

import com.kyc.core.constants.BatchConstants;

public final class KycBatchExecutiveConstants {

    public static final String JOB_NAME = String.format(BatchConstants.KYC_BATCH_JOB_TEMPLATE,
            "OFFICES","UPD-OFFICES");
    public static final String BACKUP_EXECUTIVES_OFFICES_STEP = String.format(BatchConstants.KYC_BATCH_STEP_TEMPLATE,
            "OFFICES","BACKUP-EXECUTIVE-OFFICES");
    public static final String BACKUP_OFFICES_TASK = String.format(BatchConstants.KYC_BATCH_TASKLET_TEMPLATE,
            "OFFICES","BACKUP-OFFICES");

    public static final String ADD_OFFICES_STEP = String.format(BatchConstants.KYC_BATCH_STEP_TEMPLATE,
            "OFFICES","ADD-OFFICES");
    public static final String DELETE_OFFICES_STEP = String.format(BatchConstants.KYC_BATCH_STEP_TEMPLATE,
            "OFFICES","DELETE-OFFICES");
    public static final String UPDATE_EXECUTIVE_OFFICES_STEP = String.format(BatchConstants.KYC_BATCH_STEP_TEMPLATE,
            "OFFICES","UPDATE-EXECUTIVE-OFFICES");
    public static final String CLEAN_FILE_TASK = String.format(BatchConstants.KYC_BATCH_TASKLET_TEMPLATE,
            "OFFICES","CLEAN-FILE");
    public static final String ROLLBACK_OFFICES_TASK = String.format(BatchConstants.KYC_BATCH_TASKLET_TEMPLATE,
            "OFFICES","ROLLBACK-OFFICES");

}
