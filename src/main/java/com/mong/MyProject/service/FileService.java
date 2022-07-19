package com.mong.MyProject.service;

import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@PropertySource("/src/main/resources/application.properties")
public class FileService {


    private String imageDirectory;

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
            e.printStackTrace();
            throw new IllegalArgumentException("파일 작성에 실패하였습니다.");
        }
    }

    private boolean createNewFile(File file) {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            log.info("파일 생성에 실패했습니다. 파일 이름 = {}", file.getName());
            e.printStackTrace();
            throw new IllegalArgumentException("파일 생성에 실패했습니다.");
        }
    }

    private void writeFileByFOS(File file, MultipartFile multipartFile) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            removeFileByFile(file);
            log.info("파일 작성에 실패하였습니다. 파일 이름 = {}", file.getName());
            e.printStackTrace();
            throw new IllegalArgumentException("파일 작성에 실패하였습니다.");
        }
    }

    public void removeFileByPath(@NotNull String path) {
        File file = new File(path);
        file.delete();
    }

    public void removeFileByFile(@NotNull File file) {
        file.delete();
    }

}
