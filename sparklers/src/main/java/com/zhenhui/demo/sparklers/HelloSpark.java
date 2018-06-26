package com.zhenhui.demo.sparklers;

import com.zhenhui.sparklers.boot.starter.Spark;
import org.springframework.stereotype.Component;

import static spark.Spark.get;

@Component
public class HelloSpark implements Spark {

    @Override
    public void init() {
        get("/hello", (request, response) -> {
            return "hello,spark!";
        });
    }
}

