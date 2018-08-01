package com.zhenhui.demo.sparklers.uic.domain.model;

import com.zhenhui.demo.sparklers.uic.common.SocialType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User3rd implements Serializable {

    private static final long serialVersionUID = -1L;

    private SocialType type;

    private String openId;

    private String nickname = "";

    private String avatar = "";

    private Long userId = 0L;

}
