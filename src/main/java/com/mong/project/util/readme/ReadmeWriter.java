package com.mong.project.util.readme;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class ReadmeWriter {

    private static final String[] testResultNames = {"Instruction", "Branch", "Line", "Complexity", "Method"};
    private static final String NEXT_LINE_CHAR = "\r\n";
    private static final String TITLE = "## \uD83D\uDCCB 테스트 커버리지";
    private static final String AUTO_WRITE_TITLE = "**(해당 내용은 코드로 자동 작성되었습니다.)**";

    public boolean writeReadme (String readmePath, String csvPath) {

        log.info("README 경로: {}, CSV 경로: {}", readmePath, csvPath);

        Map<String, Integer> testResult;

        try {
            testResult = getTestResult(csvPath);
            log.info("test 결과: {}", testResult);
        } catch (IOException e) {
            return false;
        }

        return write(readmePath, testResult);
    }

    private Map<String, Integer> getTestResult(String csvPath) throws IOException {
        Map<String, Integer> testResult = new HashMap<>();

        Integer[] testCnt = new Integer[10];
        Arrays.fill(testCnt, 0);

        try(BufferedReader reader = Files.newBufferedReader(Paths.get(csvPath))) {
            
            readLineAndSetTestCount(testCnt, reader);

            Arrays.stream(testCnt)
                    .forEach(t -> log.info("count: {}", t));

            setTestResultByTestCount(testResult, testCnt);
        }

        return testResult;
    }

    private static void setTestResultByTestCount(Map<String, Integer> testResult, Integer[] testCnt) {
        for (int i = 0; i < testCnt.length; i += 2) {
            testResult.put(testResultNames[i/2], testCnt[i+1]*100/(testCnt[i]+ testCnt[i+1]));
        }
    }

    private void readLineAndSetTestCount(Integer[] testCnt, BufferedReader reader) throws IOException {
        String line;
        
        while ((line = reader.readLine()) != null) {
            Integer[] result = changeToInteger(line.split(","));

            for (int i = 0; i < 10; i++)
                testCnt[i] += result[i];
        }
    }

    private Integer[] changeToInteger(String[] line) {
        Integer[] result = new Integer[10];

        for (int i = 0; i < 10; i++) {
            result[i] = Integer.parseInt(line[i+3]);
        }

        return result;
    }

    private boolean write(String readmePath, Map<String, Integer> testResult) {
        try (BufferedReader reader = new BufferedReader(new FileReader(readmePath, StandardCharsets.UTF_8))) {
            
            StringBuilder dummy = new StringBuilder();

            copyUntilAutoWriteTitle(reader, dummy);
            
            dummy.append(AUTO_WRITE_TITLE)
                    .append(NEXT_LINE_CHAR + "\n");
            
            dummy.append(testCoverageForm(testResult));

            writeReadMeFile(readmePath, dummy);
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    private static void writeReadMeFile(String readmePath, StringBuilder dummy) throws IOException {
        try (FileWriter writer = new FileWriter(readmePath, StandardCharsets.UTF_8)) {
            writer.write(dummy.toString());
        }
    }

    private static void copyUntilAutoWriteTitle(BufferedReader reader, StringBuilder dummy) throws IOException {
        String line;

        while ((line = reader.readLine()) != null && !line.equals(TITLE)) {
            dummy.append(line)
                    .append(NEXT_LINE_CHAR);
            log.info(line);
        }

        dummy.append(line)
                .append(NEXT_LINE_CHAR);
    }

    private String testCoverageForm(Map<String, Integer> testResult) {
        String[] result = {"","",""};
        String separator = "|";

        testResult.forEach((name, value) -> {
            result[0] += separator + name + ", %";
            result[1] += separator + "---";
            result[2] += separator + value;
        });

        return result[0] + separator + NEXT_LINE_CHAR +
                result[1] + separator + NEXT_LINE_CHAR +
                result[2] + separator;
    }
}
