package com.kyc.batch.office.tasklets;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;

@AllArgsConstructor
public class FileDeleteTasklet implements Tasklet, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDeleteTasklet.class);

    private final Resource resource;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

        try{
            if(resource.exists()){
                File file = resource.getFile();
                boolean isDeleted = file.delete();
                if(isDeleted){
                   LOGGER.info("The file was deleted {}",file.getName());
                }
                else{
                    LOGGER.warn("The file {} could not be deleted",file.getName());
                }
            }
            else{
                LOGGER.warn("The resource to delete does not exists");
            }
        }
        catch(IOException ioex){
            LOGGER.warn("File was not deleted due: ",ioex);
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws IllegalArgumentException {
        Assert.notNull(resource,"Resource must not null");
    }
}
