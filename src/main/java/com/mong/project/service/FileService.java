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

        if (createNewFile(newFile)) {
            writeFileByFOS(newFile, file);

            log.info("파일을 생성하였습니다. 경로 = {}", newFile.getAbsolutePath());
            return newFile;
        }
        return null;
    }

    private String extractExtension(String fileName) {
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(ErrorCode.FAIL_TO_WRITE_FILE);
        }
    }

    private boolean createNewFile(File file) {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            log.info("파일 생성에 실패했습니다. 파일 이름 = {}, 경로 = {}", file.getName(), file.getAbsolutePath());
            throw new IllegalArgumentException(ErrorCode.FAIL_TO_WRITE_FILE);
        }
    }

    private void writeFileByFOS(File file, MultipartFile multipartFile) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            if(removeFileByFile(file))
                log.info("파일 작성에 실패하여 삭제하였습니다. 파일 이름 = {}", file.getName());
            else
                log.info("파일 작성에 실패하였고 삭제에도 실패하였습니다. 파일 이름 = {}", file.getName());
            throw new IllegalArgumentException(ErrorCode.FAIL_TO_WRITE_FILE);
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
