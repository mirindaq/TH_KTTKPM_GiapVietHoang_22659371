package iuh.fit.product_service.controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/test")
public class ResilienceTestController {

    // Đếm số lần đã thử trong phiên retry hiện tại
    private final AtomicInteger retryAttemptCount = new AtomicInteger(0);

    /**
     * Test RETRY: mỗi lần gọi sẽ fail 2 lần đầu, lần 3 mới thành công.
     * Xem log console để thấy từng lần retry.
     * GET /test/retry
     */
    @GetMapping("/retry")
    @Retry(name = "retryTest", fallbackMethod = "retryFallback")
    public Map<String, Object> testRetry() {
        int attempt = retryAttemptCount.incrementAndGet();
        log.warn("[RETRY] Lần thử #{}", attempt);

        if (attempt < 3) {
            log.error("[RETRY] Lần thử #{} FAILED - giả lập lỗi", attempt);
            throw new RuntimeException("Giả lập lỗi lần " + attempt);
        }

        log.info("[RETRY] Lần thử #{} SUCCESS", attempt);
        retryAttemptCount.set(0); // reset cho lần gọi tiếp theo
        return Map.of("message", "Thành công sau 3 lần thử", "attempts", attempt);
    }

    private Map<String, Object> retryFallback(Throwable t) {
        log.error("[RETRY] Đã thử 3 lần vẫn lỗi, fallback: {}", t.getMessage());
        retryAttemptCount.set(0);
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Thất bại sau 3 lần retry: " + t.getMessage());
    }

    /**
     * Test RATE LIMITER: chỉ cho 3 request / 10 giây.
     * Gọi quá sẽ nhận 429.
     * GET /test/rate-limit
     */
    @GetMapping("/rate-limit")
    @RateLimiter(name = "rateLimitTest", fallbackMethod = "rateLimitFallback")
    public Map<String, Object> testRateLimit() {
        log.info("[RATE LIMITER] Request được phép đi qua");
        return Map.of("message", "Request được xử lý", "limit", "10 request / 1 giây");
    }

    private Map<String, Object> rateLimitFallback(Throwable t) {
        log.warn("[RATE LIMITER] Vượt quá giới hạn: {}", t.getMessage());
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                "Quá nhiều request, vui lòng thử lại sau 10 giây");
    }

    /**
     * Test TIME LIMITER: giả lập xử lý chậm 5 giây, timeout sau 2 giây.
     * GET /test/time-limit
     */
    @GetMapping("/time-limit")
    @TimeLimiter(name = "timeLimitTest", fallbackMethod = "timeLimitFallback")
    public CompletableFuture<Map<String, Object>> testTimeLimit() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("[TIME LIMITER] Bắt đầu xử lý (giả lập 5 giây)...");
                Thread.sleep(5000); // giả lập tác vụ chậm
                log.info("[TIME LIMITER] Hoàn thành (sẽ không thấy log này vì đã timeout)");
            } catch (InterruptedException e) {
                log.warn("[TIME LIMITER] Thread bị cancel do timeout");
                Thread.currentThread().interrupt();
            }
            return Map.<String, Object>of("message", "Xử lý xong");
        });
    }

    private CompletableFuture<Map<String, Object>> timeLimitFallback(Throwable t) {
        log.error("[TIME LIMITER] Timeout! {}", t.getMessage());
        return CompletableFuture.failedFuture(
                new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Xử lý quá 2 giây, timeout!"));
    }
}
