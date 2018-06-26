package com.zhenhui.demo.sparklers.service.security;

import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public final class AuthenticationFilter implements Filter {

    @Override
    public void handle(Request request, Response response) throws Exception {

        halt(401);

    }
}

