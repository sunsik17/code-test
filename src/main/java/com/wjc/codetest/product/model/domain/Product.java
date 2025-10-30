package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
/* 문제 : entity class에 setter를 사용하고 있어 의도를 파악하기 어렵습니다.
   원인 : product 수정을 실행 할 때 setter를 사용하고 있습니다.
   개선안(대안) :
   public void modifyCategory(String category) {
     this.category = category;
   }
   public void modifyName(String name) {
     this.name = name;
   }
   으로 수정되어야 하고, category, name 등 수정하는 파라미터 검증 메소드도 추가하면 좋을것 같습니다.(null 체크, 글자 수 제한 등)

   - 선택 근거 :
   메서드 이름으로 의도를 표현할 수 있습니다.
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
