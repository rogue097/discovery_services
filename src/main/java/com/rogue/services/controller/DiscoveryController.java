package com.rogue.services.controller;

import com.rogue.services.services.DiscoveryServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/discovery")
@AllArgsConstructor
public class DiscoveryController {
    private final DiscoveryServices discoveryServices;

    @GetMapping
    public String discovery() {
        return "Hello from discoveryS3Buckets";
    }
}
