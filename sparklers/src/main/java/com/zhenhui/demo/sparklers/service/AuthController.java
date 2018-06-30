package com.zhenhui.demo.sparklers.service;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhenhui.demo.sparklers.domain.exception.UserNotFoundException;
import com.zhenhui.demo.sparklers.domain.interactor.SigninByCaptcha;
import com.zhenhui.demo.sparklers.domain.interactor.SigninByCaptcha.Params;
import com.zhenhui.demo.sparklers.service.results.ErrorCode;
import com.zhenhui.demo.sparklers.service.params.SigninParams;
import com.zhenhui.demo.sparklers.service.results.Result;
import io.reactivex.observers.DefaultObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/auth/token")
public class AuthController {

    @Autowired
    private SigninByCaptcha signinByCaptcha;

    @RequestMapping(method = RequestMethod.POST)
    public void createToken(@RequestBody SigninParams body,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        final AsyncContext context = request.startAsync();
        context.setTimeout(10000);

        signinByCaptcha.execute(new Params(body.getPhone(), body.getCaptcha()), new DefaultObserver<String>() {
            @Override
            public void onNext(String token) {
                Result.newBuilder()
                    .error(ErrorCode.NONE)
                    .data(token)
                    .write(response);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UserNotFoundException) {
                    Result.newBuilder()
                        .error(ErrorCode.DATA_NOT_FOUND)
                        .message("用户不存在")
                        .write(response);
                    context.complete();
                } else {
                    Result.newBuilder()
                        .error(ErrorCode.INTERNAL_ERROR)
                        .write(response);
                    context.complete();
                }
            }

            @Override
            public void onComplete() {
                context.complete();
            }
        });
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String refreshToken() {
        return "";
    }
}






