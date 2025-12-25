package com.charitytrades.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RateLimiter {

    private final int requestsPerSecond;
    private final long minimumIntervalMs;
    private long lastRequestTime = 0;

    public RateLimiter(@Value("${globalgiving.rate-limit.requests-per-second:2}") int requestsPerSecond) {
        this.requestsPerSecond = requestsPerSecond;
        this.minimumIntervalMs = 1000L / requestsPerSecond;
    }

    public synchronized void acquire() {
        long now = System.currentTimeMillis();
        long timeSinceLastRequest = now - lastRequestTime;

        if (timeSinceLastRequest < minimumIntervalMs) {
            try {
                Thread.sleep(minimumIntervalMs - timeSinceLastRequest);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        lastRequestTime = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        if (now - lastRequestTime >= minimumIntervalMs) {
            lastRequestTime = now;
            return true;
        }
        return false;
    }

    public int getRequestsPerSecond() {
        return requestsPerSecond;
    }
}
