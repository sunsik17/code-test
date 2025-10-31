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
    조회 할 때 @GetMapping,
    리소스를 생성 할 때 @PostMapping,
    리소스를 삭제할 때 @DeleteMapping,
    수정이 필요할 때 @PutMapping을 사용 합니다.
    - 선택 근거 :
    적절한 http 메소드를 사용해 api 의도가 명확해집니다.
    또 업데이트 기능과 같이 멱등한 연산을 할 경우 post 보다 put을 사용하는 것이 의미적으로 적절하다고 생각합니다.
     */
    @PostMapping(value = "/product/list")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }
    /*
    - 문제 : http 메소드를 잘못 사용하고 있으며 코드 한줄의 길이가 너무 긴듯 합니다.
    - 원인 : 조회 목적 api 입니다만 post 메소드 요청을 받고 있고, 화면을 벗어날 만큼 코드 길이가 깁니다.
    - 개선안 :
    @GetMapping
    public ResponseEntity<ProductListResponse> getProductListByCategory(
        @RequestParam(value = "page") int page,
        @RequestParam(value = "size") int size,
        @RequestParam(value = "sort") String sort ) {
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
     */

    @GetMapping(value = "/product/category/list")
    public ResponseEntity<List<String>> getProductListByCategory(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
    /*
    - 문제 : 메서드에서 불필요한 변수를 사용해 코드 퀄리티 저하
    - 원인 : 불필요한 중간 저장
    - 개선안 :
    단순 반환 시 return ResponseEntity.ok(service.method()) 형식으로 사용 하되,
    후처리 필요 시 변수에 담아 사용하면 좋을 것 같습니다.
    - 선택 근거 :
    현재는 후처리 없이 사용하지 않는 변수에 담아져 있기 때문에 즉시 반환 하는 방법을 사용하는 것이 더 깔끔하다고 생각합니다.
     */
}