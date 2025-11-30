package io.neverending25.api.client;

import io.neverending25.api.dto.PageResponse;
import io.neverending25.api.dto.PlanResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface PlanClient {
    @GetExchange("/plans")
    PageResponse<PlanResponse> getPlans(
            @RequestParam Integer page,
            @RequestParam Integer size
    );
}