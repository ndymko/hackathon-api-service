package io.neverending25.api.controller;

import io.neverending25.api.service.APIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Floor Plan API", description = "AI-powered floor plan analysis and validation")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class APIController {
    private final APIService apiService;

    @PostMapping("/parse")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Parse floor plan from image",
            description = "Extracts walls, doors, and rooms from a base64-encoded floor plan image using computer vision"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Base64-encoded floor plan image",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Test Image",
                                    summary = "Base64 image test",
                                    value = """
                    {
                      "image": "xyzzzz..."
                    }
                    """
                            ),
                    }
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully parsed floor plan",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Parsed Plan",
                            value = """
                {
                  "walls": [
                    {"position": [[158,12],[248,12]]},
                    {"position": [[248,12],[248,46]]}
                  ],
                  "doors": [
                    {"bbox": [[102,40],[137,40],[137,51],[102,51]]}
                  ],
                  "rooms": [
                    {"id": "5", "x": 258, "y": 370}
                  ],
                  "area": 157212,
                  "perimeter": 6753.35
                }
                """
                    )
            )
    )
    public Map<String, Object> parse(@RequestBody Map<String, Object> body) {
        return apiService.parse(body);
    }

    @PostMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Validate floor plan with AI",
            description = "Validates floor plan, adds missing doors, identifies load-bearing walls using Llama 3"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Floor plan JSON from /parse endpoint",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Floor Plan JSON",
                            summary = "Parsed plan structure",
                            value = """
                {
                  "walls": [
                    {"position": [[158,12],[248,12]]},
                    {"position": [[248,12],[248,46]]},
                    {"position": [[76,46],[158,46]]},
                    {"position": [[20,209],[416,209]]}
                  ],
                  "doors": [
                    {"bbox": [[102,40],[137,40],[137,51],[102,51]]},
                    {"bbox": [[185,41],[207,41],[207,51],[185,51]]}
                  ],
                  "rooms": [
                    {"id": "5", "x": 258, "y": 370},
                    {"id": "16", "x": 201, "y": 251},
                    {"id": "22", "x": 158, "y": 209}
                  ],
                  "area": 157212,
                  "perimeter": 6753.35
                }
                """
                    )
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Validation results with enhanced plan",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Validation Report",
                            value = """
                {
                  "valid": false,
                  "issues": [
                    {
                      "description": "Room 5 at (258,370) isolated - no door access",
                      "location": [[258,370],[258,407]]
                    }
                  ],
                  "good": [
                    {
                      "description": "Main entrance properly positioned",
                      "location": [[102,40],[137,51]]
                    }
                  ],
                  "suggestions": [
                    {
                      "description": "Add door to Room 5",
                      "move_door": {"from": null, "to": [[203,370],[228,370]]},
                      "remove_wall": null
                    }
                  ]
                }
                """
                    )
            )
    )
    public Map<String, Object> validate(@RequestBody Map<String, Object> body) {
        return apiService.validatePlanViaLlama(body);
    }

    @PostMapping("/llm")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Chat with Llama 3",
            description = "Free-form conversation with Llama 3 AI"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User prompt for AI chat",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Simple Question",
                                    value = """
                                            {"prompt": "Hello! How are you?"}"""
                            ),
                    }
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "AI response",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Chat Response",
                            value = """
                {
                  "response": "Hello! I'm Llama 3. I can help with architecture, building codes, and floor plans. What would you like to know?",
                }
                """
                    )
            )
    )
    public Map<String, Object> llm(@RequestBody Map<String, Object> body) {
        return apiService.llm(body);
    }
}