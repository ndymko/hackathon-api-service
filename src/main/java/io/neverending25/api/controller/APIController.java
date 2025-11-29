package io.neverending25.api.controller;

import io.neverending25.api.service.APIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class APIController {
    private final APIService apiService;

    @PostMapping("/parse")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> parse(@RequestBody Map<String, Object> body) {
        return apiService.parse(body);
    }

    @PostMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> validate(@RequestBody Map<String, Object> body) {
        return apiService.validatePlanViaLlama(body);
    }

    @PostMapping("/llm")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> llm(@RequestBody Map<String, Object> body) {
        return apiService.llm(body);
    }
}