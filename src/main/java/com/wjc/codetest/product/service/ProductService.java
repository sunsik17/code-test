package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(CreateProductRequest dto) {
        Product product = new Product(dto.getCategory(), dto.getName());
        return productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("product not found");
            // globalExceptionHandler 에서 리뷰
        }
        return productOptional.get();
    }
    /*
    - 문제 : Optional사용 방식
    - 원인 : isPresent() 이후 get() 사용
    - 개선안 :
    return productRepository.findById(productId).orElseThrow(
      () -> new RuntimeException("product not found");
    );
    - 선택 근거 :
    null check와 exception을 한줄로 표현할 수 있어 간결하고 명확합니다.
     */

    public Product update(UpdateProductRequest dto) {
        Product product = getProductById(dto.getId());
        product.setCategory(dto.getCategory());
        product.setName(dto.getName());
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;

    }
    /*
    - 문제 : 공백
    - 원인 : return updateProduct; 아래 의미 없는 줄바꿈
    - 개선안 : 삭제
    - 선택 근거 :
    팀단위 프로젝트라면 팀 코드 컨벤션이 있을 것이고 해당 컨벤션을 지켜주어야 합니다.
    필요 없는 공백을 없애고 코드 컨벤션을 지키려는 습관을 들여야 합니다.
    */
    public void deleteById(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    public Page<Product> getListByCategory(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
    }
    /*
    -문제 : 성능 낭비
    -원인 : findAllByCategory()는 이미 특정 카테고리의 상품들을 찾아 내지만 category로 정렬하는 효과 없는 정렬쿼리 발생
    -개선안 :
    pageRequest 객체를 생성할 때 Sort.by를 삭제하거나
    클라이언트가 선택한 기준에 맞게 정렬해야 합니다.
    -선택근거 :
    개발자가 직접 코드로 category를 이용한 정렬을 진행하기 때문에
    의미 없는 정렬 쿼리 ORDER BY 줄이 사라지거나 의미있게 정렬 할 수 있도록 바뀌게 됩니다.
     */

    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}