package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {
    private String category;
    private String name;

    public CreateProductRequest(String category) {
        this.category = category;
    }

    public CreateProductRequest(String category, String name) {
        this.category = category;
        this.name = name;
    }
}
/*
- 문제 : 보안 취약점 문제
- 원인 : 데이터 검증이 전혀 이루어 지지 않고 있습니다.
- 개선안 :
@NotNull, @Size 등 검증 규칙 추가
이후
Controller 레벨에서 @Valid 로 유효성검증
- 선택근거 :
null, 빈 값을 방어할 수 있습니다.
과도한 길이의 요청이나 비즈니스 요구사항을 지킬 수 있습니다.
 */