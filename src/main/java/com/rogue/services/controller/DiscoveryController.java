package com.rogue.services.controller;

import com.rogue.services.dto.JobStatusDto;
import com.rogue.services.dto.S3ObjectCountDto;
import com.rogue.services.dto.ServiceDto;
import com.rogue.services.dto.UpdateServiceRequestDto;
import com.rogue.services.dto.projection.S3ObjectView;
import com.rogue.services.services.DiscoveryServices;
import lombok.AllArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/v1/discovery")
@AllArgsConstructor
public class DiscoveryController {
  private final DiscoveryServices discoveryServices;

  @PostMapping(produces = "application/json", consumes = "application/json")
  @ResponseBody
  public ResponseEntity<ProblemDetail> updateServices(@RequestBody UpdateServiceRequestDto updateServiceRequestDto) {
    return ResponseEntity.ok(discoveryServices.updateServices(updateServiceRequestDto));
  }

  @GetMapping(value = "/{jobId}", produces = "application/json")
  public ResponseEntity<JobStatusDto> getJobStatus(@PathVariable Long jobId) {
    return ResponseEntity.ok(discoveryServices.getJobResult(jobId));
  }

  @GetMapping(value = "/latest", produces = "application/json")
  public ResponseEntity<List<ServiceDto>> getLatestJobStatus(@RequestParam String serviceType) {
    return ResponseEntity.ok(discoveryServices.getDiscoveryResult(serviceType));
  }

  @GetMapping(value = "/objects", produces = "application/json")
  public ResponseEntity<ProblemDetail> getObjects(@RequestParam String bucketName) {
    return ResponseEntity.ok(discoveryServices.getObjects(bucketName));
  }

  @GetMapping(value = "/objects/count", produces = "application/json")
  public ResponseEntity<S3ObjectCountDto> countObjects(@RequestParam String bucketName) {
    return ResponseEntity.ok(discoveryServices.countObjects(bucketName));
  }

  @GetMapping(value = "/objects/like", produces = "application/json")
  public ResponseEntity<List<S3ObjectView>> getObjectsLike(@RequestParam String bucketName, @RequestParam String pattern) throws UnsupportedEncodingException {
    pattern = URLDecoder.decode(pattern, StandardCharsets.UTF_8);
    return ResponseEntity.ok(discoveryServices.getObjectsLike(bucketName, pattern));
  }
}
