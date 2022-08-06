package com.mong.project.service;

import com.mong.project.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@TestPropertySource("classpath:test.properties")
class FileServiceTest {

    private final FileService fileService;

    private final String imageDirectory;

    private File newFile;

    public FileServiceTest(@Value("${spring.image.directory}") String imageDirectory) {
        this.imageDirectory = imageDirectory;
        fileService = new FileService(imageDirectory);
    }

    @AfterEach
    void afterEach(){
        if(newFile != null && newFile.exists()){
            newFile.delete();
        }
    }

    @Test
    @DisplayName("파일을 생성합니다.")
    void createFile(){
        //given
        MultipartFile file = new MockMultipartFile("test", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));

        //when
        newFile = fileService.convertToFile(file);

        //then
        assertThat(newFile).exists();
    }

    @Test
    @DisplayName("파일의 확장자가 없는 경우 오류가 발생합니다.")
    void createInvalidExtensionFile(){
        //given
        MultipartFile file = new MockMultipartFile("test", "test", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> newFile = fileService.convertToFile(file));

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.FAIL_TO_WRITE_FILE);
    }

    @Test
    @DisplayName("file 을 통해 파일을 삭제합니다.")
    void removeFileByFile(){
        //given
        MultipartFile file = new MockMultipartFile("test", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));
        newFile = fileService.convertToFile(file);

        //when
        boolean result = fileService.removeFileByFile(newFile);

        //then
        assertThat(result).isTrue();
        assertThat(newFile).doesNotExist();
    }

    @Test
    @DisplayName("path 를 통해 파일을 삭제합니다.")
    void removeFileByPath(){
        //given
        MultipartFile file = new MockMultipartFile("test", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));
        newFile = fileService.convertToFile(file);

        //when
        boolean result = fileService.removeFileByPath(newFile.getAbsolutePath());

        //then
        assertThat(result).isTrue();
        assertThat(newFile).doesNotExist();
    }

    @Test
    @DisplayName("잘못된 path 를 통해 파일을 삭제합니다. IllegalStateException 이 발생합니다.")
    void removeFileByInvalidPath(){
        //given
        MultipartFile file = new MockMultipartFile("test", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));
        newFile = fileService.convertToFile(file);

        //when
        IllegalStateException exception = assertThrows(IllegalStateException.class, ()->fileService.removeFileByPath("not/exist/path/file.PNG"));

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.FAIL_TO_REMOVE_FILE);
    }
}