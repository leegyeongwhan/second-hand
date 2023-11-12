package com.secondhand.domain.product;

import com.secondhand.domain.member.Member;
import lombok.*;

@Getter
public class CountInfo {

    private Integer chatCount;
    private  Integer likeCount;
    private Integer viewCount;

    public CountInfo(Integer countLike) {
        this.likeCount = countLike;
    }

    @Builder
    private CountInfo(Integer chatCount, Integer likeCount, Integer viewCount) {
        this.chatCount = chatCount;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
    }
}
