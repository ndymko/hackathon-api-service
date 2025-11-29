package io.neverending25.api.client;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

public interface ParserClient {
    @PostExchange("/plan_recognition")
    Map<String, Object> parse(@RequestPart("file") Resource file);
}