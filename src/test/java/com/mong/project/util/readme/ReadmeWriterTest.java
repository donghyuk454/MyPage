package com.mong.project.util.readme;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:test.properties")
class ReadmeWriterTest {

    private final ReadmeWriter readmeWriter = new ReadmeWriter();

    private final String readmePath;

    private final String csvPath;

    public ReadmeWriterTest(@Value("${spring.readme.path}") String readmePath,
                            @Value("${spring.test.csv.path}") String csvPath) {
        this.readmePath = readmePath;
        this.csvPath = csvPath;
    }

    @Test
    @DisplayName("csvPath 에 있는 csv 파일을 읽어 readmePath 에 있는 readme 파일을 수정하고, true 를 반환합니다.")
    void writeReadme() {
        boolean result = readmeWriter.writeReadme(readmePath, csvPath);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("csvPath 에 있는 csv 파일을 읽는 데에 실패합니다. false 를 반환합니다.")
    void writeReadmeWithInvalidCsvPath() {
        boolean result = readmeWriter.writeReadme(readmePath, "NOTHING");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("readmePath 에 있는 readme 파일을 읽는 데에 실패합니다. false 를 반환합니다.")
    void writeReadmeWithInvalidReadmePath() {
        boolean result = readmeWriter.writeReadme("NOTHING", csvPath);

        assertThat(result).isFalse();
    }
}