package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
/* entity class에 setter를 사용하면 의도를 파악하기 어렵습니다.
   필요할 때 사용하는 비즈니스 메소드를 만들어 사용하는 것이 좋은 방법 같습니다.
 *
 * public void setCategory(String category) {
 *   this.category = category;
 * } x
 *
 * public void modifyCategory(String category) {
 *   this.category = category;
 * } o
 *
 * 처럼 명확한 사용처를 알 수 있도록 하는 것이 좋을 것 같습니다.
 */
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    protected Product() {
    }

    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
