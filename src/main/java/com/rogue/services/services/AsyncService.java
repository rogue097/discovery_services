package com.rogue.services.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {
  @Async
  public void executeAsync() {
    System.out.println("Thread from the pool: " + Thread.currentThread().getName());
    System.out.println("Executing some async service");
  }
}
