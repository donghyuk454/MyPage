package com.mong.project.config;

import com.mong.project.util.readme.ReadmeWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("default")
public class ReadmeConfig {

    @Bean
    public ReadmeWriter readmeWriter(){
        return new ReadmeWriter();
    }
}
