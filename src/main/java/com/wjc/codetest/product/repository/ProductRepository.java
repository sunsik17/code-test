package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.model.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByCategory(String name, Pageable pageable);
    /*
    - 문제 : 가독성 문제
    - 원인 : 쿼리에는 문제가 없지만, 첫 번째 파라미터 String name이 어떤 의미인지 명확하지 않음
    - 개선안 :
    String category 으로 변경
    - 선택 근거 :
    메서드명과 파라미터명이 일치하면 코드의 의도를 더 직관적으로 전달할 수 있습니다.
    다른 개발자가 봐도 'category를 기준으로 상품을 조회한다'는 것을 바로 이해할 수 있습니다.
     */

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
}
