package iuh.fit.product_service.controller;

import iuh.fit.product_service.entity.Product;
import iuh.fit.product_service.service.ProductService;
import iuh.fit.product_service.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /** Bật/tắt fake lỗi: GET /products/fake-fail?enable=true|false */
    @GetMapping("/fake-fail")
    public Map<String, Object> toggleFakeFail(@RequestParam boolean enable) {
        ProductServiceImpl.fakeFail = enable;
        return Map.of(
                "fakeFail", enable,
                "message", enable ? "Product-service sẽ throw lỗi 500" : "Product-service hoạt động bình thường"
        );
}

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") Long id) {   
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(productService.create(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable("id") Long id,
            @RequestBody Product product
    ) {
        return ResponseEntity.ok(productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}