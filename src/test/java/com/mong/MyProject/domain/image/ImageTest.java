package com.mong.MyProject.domain.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:test.properties")
class ImageTest {

    private String path;

    public ImageTest(@Value("${spring.image.directory}") String path){
        this.path = path;
    }

    @Test
    @DisplayName("image 의 builder 가 정상적으로 작동하는 지 확입합니다. 특히 key 값이 정확히 입력됐는지 확인합니다.")
    void builder() {
        String uuid =  UUID.randomUUID().toString();
        Image image = Image.builder()
                .type(ImageType.BOARD)
                .url(path+uuid+".PNG")
                .build();

        assertThat(image.getKey()).isEqualTo(uuid);
    }
}