package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
/*
- 문제 : 각 api 엔드포인트 url이 불규칙적입니다.(가독성)
- 원인 :
  - /get/product (동사 포함)
  - /create/product (동사 포함)
  - /update/product (동사 포함)
  - /delete/product (동사 포함)
  - /product/list (동사 미포함)
  - /product/category/list (동사 미포함)
- 개선안(대안) :
@RequestMapping("/products") 로 베이스 경로를 통일 하고
@GetMapping("/{productId}")과 같이 동사 대신 http메소드로 행동을 표현하도록 url을 수정해야 합니다.

- 선택 근거:
RESTful api naming 컨벤션 준수로 API 직관성이 좋아집니다.
HTTP 메서드로 액션을 구분할 수 있습니다.
 */
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = "/get/product/by/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/create/product")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest dto){
        Product product = productService.create(dto);
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/delete/product/{productId}") // 아래 updateProduct()와 같은 문제로 주석 하나로 대체합니다.
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteById(productId);
        return ResponseEntity.ok(true);
    }

    @PostMapping(value = "/update/product")
    public ResponseEntity<Product> updateProduct(@RequestBody UpdateProductRequest dto){
        Product product = productService.update(dto);
        return ResponseEntity.ok(product);
    }
    /*
    - 문제 : http 메소드를 잘못 사용하고 있습니다.
    - 원인 : delete, update 작업에 PostMapping을 사용하고 있습니다. post 메소드는 비멱등 메소드로 새로운 리소스를 생성할 때 사용합니다.
    - 개선안(대안) :
    데이터를 삭제할 때는 @DeleteMapping,
    수정이 필요할 때는 @PutMapping을 사용 하도록 합니다.
    - 선택 근거 :
    적절한 http 메소드를 사용해 api 의도가 명확해집니다.
    또 멱등한 연산을 할 경우 put을 사용하는 것이 의미적으로 적절하다고 생각합니다.
     */
    @PostMapping(value = "/product/list")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    @GetMapping(value = "/product/category/list")
    public ResponseEntity<List<String>> getProductListByCategory(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}