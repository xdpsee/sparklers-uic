package com.zhenhui.demo.sparklers.uic.domain.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhenhui.demo.uic.api.enums.SocialType;
import com.zhenhui.demo.sparklers.uic.security.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = -1L;

    private long id;

    @JsonIgnore
    private String phone;

    private String name;

    @JsonIgnore
    private String secret;

    private String avatar;

    private Set<String> authorities = new HashSet<>();

    public Subject toPrincipal() {

        Subject subject = new Subject();

        subject.setUserId(id);
        subject.setPhone(phone);
        subject.setAuthorities(authorities);
        subject.setType(SocialType.NONE);

        return subject;
    }

}




