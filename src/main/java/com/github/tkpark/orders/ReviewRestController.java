package com.github.tkpark.orders;

import com.github.tkpark.errors.UnauthorizedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.github.tkpark.utils.ApiUtils.success;
import static com.github.tkpark.utils.ApiUtils.ApiResult;

@RestController
@RequestMapping("api/orders")
public class ReviewRestController {

    @PostMapping(path = "review")
    public ApiResult<ReviewResult> review(
            @Valid @RequestBody ReviewRequest request
    ) throws UnauthorizedException {

        // orders table 조회

        // state 체크

        // 동일한 주문에 대해 중복 리뷰 체크

        // insert reviews

        // `Product`의 `reviewCount` 값이 1 증가

        // return



        return null;
    }
}
