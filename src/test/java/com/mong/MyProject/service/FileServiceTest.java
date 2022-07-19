package com.mong.MyProject.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    private FileService fileService;
    private String imageDirectory = "src/test/resources/images/";

    private File newFile;

    @BeforeEach
    void beforeEach(){
        this.fileService= new FileService(imageDirectory);
    }

    @AfterEach
    void afterEach(){
        if(newFile != null && newFile.exists()){
            newFile.delete();
        }
    }

    @Test
    @DisplayName("파일을 생성합니다.")
    void 파일_생성(){
        //given
        MultipartFile file = new MockMultipartFile("test", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));

        //when
        newFile = fileService.convertToFile(file);

        //then
        assertThat(newFile).exists();
    }

    @Test
    @DisplayName("파일의 확장자가 없는 경우 오류가 발생합니다.")
    void 파일_쌩성_확장자_오류(){
        //given
        MultipartFile file = new MockMultipartFile("test", "test", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> newFile = fileService.convertToFile(file));

        //then
        assertThat(exception.getMessage()).isEqualTo("파일 작성에 실패하였습니다.");
    }

    @Test
    @DisplayName("file 을 통해 파일을 삭제합니다.")
    void 파일_삭제_파일(){
        //given
        MultipartFile file = new MockMultipartFile("test", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));
        File convertedFile = fileService.convertToFile(file);

        //when
        fileService.removeFileByFile(convertedFile);

        //then
        assertThat(convertedFile).doesNotExist();
    }

    @Test
    @DisplayName("path 를 통해 파일을 삭제합니다.")
    void 파일_삭제_경로(){
        //given
        MultipartFile file = new MockMultipartFile("test", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));
        File convertedFile = fileService.convertToFile(file);

        //when
        fileService.removeFileByPath(convertedFile.getAbsolutePath());

        //then
        assertThat(convertedFile).doesNotExist();
    }
}