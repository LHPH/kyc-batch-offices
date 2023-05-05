package com.kyc.batch.office.tasklets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FileDeleteTaskletTest {

    @Mock
    private Resource resource;

    @InjectMocks
    private FileDeleteTasklet tasklet;

    @BeforeAll
    public static void init(){
        MockitoAnnotations.openMocks(FileDeleteTaskletTest.class);
    }

    @BeforeEach
    public void setUp(){
        tasklet.afterPropertiesSet();
    }

    @Test
    public void execute_deletingFile_fileDeleted() throws IOException {

        File file = mock(File.class);

        given(resource.exists()).willReturn(true);
        given(resource.getFile()).willReturn(file);
        given(file.delete()).willReturn(true);

        RepeatStatus result = tasklet.execute(mock(StepContribution.class),mock(ChunkContext.class));
        Assertions.assertEquals(RepeatStatus.FINISHED,result);
        verify(file,times(1)).delete();
    }

    @Test
    public void execute_nonExistentFile_nothingToDo() throws IOException {

        File file = mock(File.class);

        given(resource.exists()).willReturn(false);

        RepeatStatus result = tasklet.execute(mock(StepContribution.class),mock(ChunkContext.class));
        Assertions.assertEquals(RepeatStatus.FINISHED,result);
        verify(file,times(0)).delete();
    }

    @Test
    public void execute_fileCouldNotBeDeleted_nothingToDo() throws IOException {

        File file = mock(File.class);

        given(resource.exists()).willReturn(true);
        given(resource.getFile()).willReturn(file);
        given(file.delete()).willReturn(false);

        RepeatStatus result = tasklet.execute(mock(StepContribution.class),mock(ChunkContext.class));
        Assertions.assertEquals(RepeatStatus.FINISHED,result);
        verify(file,times(1)).delete();
    }

    @Test
    public void execute_deletingFileButIOException_nothingToDo() throws IOException {

        File file = mock(File.class);

        given(resource.exists()).willReturn(true);
        given(resource.getFile()).willThrow(new IOException("test"));

        RepeatStatus result = tasklet.execute(mock(StepContribution.class),mock(ChunkContext.class));
        Assertions.assertEquals(RepeatStatus.FINISHED,result);
        verify(file,times(0)).delete();
    }

}
