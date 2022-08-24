package com.mong.project.util.readme;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ReadmeWriter {

    private final String[] testResultNames = {"Instruction", "Branch", "Line", "Complexity", "Method"};

    public boolean writeReadme (String readmePath, String csvPath) {

        log.info("README 경로: {}, CSV 경로: {}", readmePath, csvPath);

        Map<String, Integer> testResult = null;

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
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                Integer[] result = changeToInteger(line.split(","));

                for (int i = 0; i < 10; i++)
                    testCnt[i] += result[i];
            }

            for (Integer t: testCnt) {
                log.info("count: {}", t);
            }
            for (int i = 0; i < testCnt.length; i += 2) {
                testResult.put(testResultNames[i/2], testCnt[i+1]*100/(testCnt[i]+testCnt[i+1]));
            }
        }

        return testResult;
    }

    private Integer[] changeToInteger(String[] line) {
        Integer[] result = new Integer[10];

        for (int i = 0; i < 10; i++) {
            result[i] = Integer.parseInt(line[i+3]);
        }

        return result;
    }

    private boolean write(String readmePath, Map<String, Integer> testResult) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(readmePath), StandardCharsets.UTF_8))) {
            String line = "";
            StringBuilder dummy = new StringBuilder();

            while ((line = reader.readLine()) != null && !line.equals("## 테스트 커버리지")) {
                dummy.append(line).append("\r\n");
                log.info(line);
            }

            dummy.append(line).append("\r\n");
            dummy.append("**(해당 내용은 코드로 자동 작성되었습니다.)**").append("\r\n\n");
            dummy.append(testCoverageForm(testResult));

            try (FileWriter fw = new FileWriter(readmePath)) {
                fw.write(dummy.toString());
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String testCoverageForm(Map<String, Integer> testResult) {
        String[] result = {"","",""};

        for (String testResultName : testResultNames) {
            result[0] += "|" + testResultName + ", %";
            result[1] += "|---";
            result[2] += "|" + testResult.get(testResultName);
        }

        for (int i = 0 ; i < 3; i++) {
            result[i] += "|";
        }

        return result[0]+"\r\n"+result[1]+"\r\n"+result[2];
    }
}