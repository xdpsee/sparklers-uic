package com.zhenhui.demo.sparklers.domain.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;

    private String name;

    private String avatar;

    private Set<String> authorities = new HashSet<>();

}




