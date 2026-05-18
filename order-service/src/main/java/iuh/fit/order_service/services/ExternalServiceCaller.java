package iuh.fit.order_service.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import iuh.fit.order_service.clients.CustomerFeignClient;
import iuh.fit.order_service.clients.ProductFeignClient;
import iuh.fit.order_service.dtos.response.CustomerResponse;
import iuh.fit.order_service.dtos.response.ProductResponse;
import iuh.fit.order_service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalServiceCaller {

    private final CustomerFeignClient customerFeignClient;
    private final ProductFeignClient productFeignClient;

    @CircuitBreaker(name = "customerService", fallbackMethod = "customerFallback")
    @Retry(name = "customerService")
    @RateLimiter(name = "customerService")
    public CustomerResponse getCustomerById(Long id) {
        try {
            return customerFeignClient.getCustomerById(id);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("Không tìm thấy người dùng: " + id);
        }
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "productFallback")
    @Retry(name = "productService")
    @RateLimiter(name = "productService")
    public ProductResponse getProductById(Long id) {
        try {
            return productFeignClient.getProductById(id);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("Không tìm thấy sản phẩm: " + id);
        }
    }

    private CustomerResponse customerFallback(Long id, Throwable t) {
        log.error("Fallback – customer-service không khả dụng, userId={}: {}", id, t.getMessage());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Customer-service không khả dụng, vui lòng thử lại sau");
    }

    private ProductResponse productFallback(Long id, Throwable t) {
        log.error("Fallback – product-service không khả dụng, productId={}: {}", id, t.getMessage());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Product-service không khả dụng, vui lòng thử lại sau");
    }
}
