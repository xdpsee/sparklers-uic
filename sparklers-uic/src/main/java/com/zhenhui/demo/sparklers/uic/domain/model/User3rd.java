package com.zhenhui.demo.sparklers.uic.domain.model;

import com.zhenhui.demo.sparklers.uic.common.SocialType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User3rd {

    private SocialType type;

    private String openId;

    private String nickname = "";

    private String avatar = "";

    private Long userId = 0L;

}
