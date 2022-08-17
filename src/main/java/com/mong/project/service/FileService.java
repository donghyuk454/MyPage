package com.mong.project.service;

import com.mong.project.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.nio.file.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

import static com.mong.project.exception.ErrorCode.FAIL_TO_WRITE_FILE;

@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class FileService {

    private final String imageDirectory;

    public FileService(@Value("${spring.image.directory}") String imageDirectory){
        this.imageDirectory = imageDirectory;
    }

    public File convertToFile(@NotNull MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String fileName = imageDirectory + uuid
                + extractExtension(Objects.requireNonNull(file.getOriginalFilename()));
        File newFile = new File(fileName);

        try {
            if (newFile.createNewFile()) {
                writeFileByFOS(newFile, file);

                log.info("파일을 생성하였습니다. 경로 = {}", newFile.getAbsolutePath());
                return newFile;
            }
        } catch (IOException e) {
            log.error("파일을 생성에 실패하였습니다. 경로 = {}", newFile.getAbsolutePath());

            throw new IllegalArgumentException(FAIL_TO_WRITE_FILE);
        }
        return null;
    }

    private String extractExtension(String fileName) {
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(FAIL_TO_WRITE_FILE);
        }
    }

    private void writeFileByFOS(File file, MultipartFile multipartFile) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        }
    }

    public boolean removeFileByPath(@NotNull String path) {
        try {
            Files.delete(Path.of(path));
        } catch (NoSuchFileException e) {
            throw new IllegalStateException(ErrorCode.FAIL_TO_REMOVE_FILE);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean removeFileByFile(@NotNull File file) {
        return removeFileByPath(file.getAbsolutePath());
    }
}
