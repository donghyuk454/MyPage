package com.mong.project.controller;

import com.mong.util.readme.ReadmeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Profile("default")
@PropertySource("classpath:application.properties")
public class ReadmeController {

    private final ReadmeWriter readmeWriter;

    private final String csvPath;

    private final String readmePath;

    public ReadmeController(ReadmeWriter readmeWriter,
                            @Value("${spring.readme.path}") String readmePath,
                            @Value("${spring.test.csv.path}") String csvPath){
        this.readmeWriter = readmeWriter;
        this.readmePath = readmePath;
        this.csvPath = csvPath;
    }

    @GetMapping("/readme")
    public ResponseEntity<Void> writeReadme() {
        if (!readmeWriter.writeReadme(readmePath, csvPath))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }
}