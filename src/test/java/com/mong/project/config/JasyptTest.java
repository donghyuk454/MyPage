package com.mong.project.config;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SpringBootTest
@TestPropertySource("classpath:test.properties")
class JasyptTest {

    JasyptConfig jasyptConfig = new JasyptConfig();

    // dong
    private final String jasyptTestString;

    public JasyptTest(@Value("${spring.jasypt.test}") String jasyptTestString) {
        this.jasyptTestString = jasyptTestString;
    }

    @Test
    @DisplayName("암호화 복호화를 테스트 합니다.")
    void jasyptTest(){
        String testText = "this is password";

        StringEncryptor stringEncoder = jasyptConfig.stringEncryptor();
        String encryptText = stringEncoder.encrypt(testText);
        System.out.println("암호화한 text = " + encryptText);
        String decryptText = stringEncoder.decrypt(encryptText);
        System.out.println("복호화한 암호 = " + decryptText);


        System.out.println(stringEncoder.encrypt("database-1.cfc4ih6iawnd.ap-northeast-2.rds.amazonaws.com"));
        System.out.println(stringEncoder.encrypt("db"));
        System.out.println(stringEncoder.encrypt("dong"));
        System.out.println(stringEncoder.encrypt("ehdgur12"));

        assertThat(encryptText).isNotEqualTo(testText);
        assertThat(decryptText).isEqualTo(testText);
    }

    @Test
    @DisplayName("properties 파일 내에 있는 내용을 읽을 수 있는지 테스트 합니다.")
    void encryptPropertiesTest(){
        assertThat(jasyptTestString).isEqualTo("dong");
    }
}