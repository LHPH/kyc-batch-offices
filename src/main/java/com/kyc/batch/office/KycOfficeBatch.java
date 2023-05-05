package com.kyc.batch.office;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class KycOfficeBatch {

    public static void main(String[] args) {
        SpringApplication.run(KycOfficeBatch.class, args);
    }
}
