package com.mong.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final Environment environment;

    /**
     * 현재 실행중인 profile 정보
     * */

    @GetMapping("/profile")
    public String getProfile() {
        List<String> profile = Arrays.asList(environment.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("set1", "set2", "dev");
        String defaultProfile = profile.isEmpty() ? "default" : profile.get(0);

        return profile.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
