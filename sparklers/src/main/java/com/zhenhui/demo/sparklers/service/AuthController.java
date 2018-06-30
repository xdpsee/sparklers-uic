package com.zhenhui.demo.sparklers.service;

import com.zhenhui.demo.sparklers.domain.exception.BadSecretException;
import com.zhenhui.demo.sparklers.domain.exception.CaptchaExpireException;
import com.zhenhui.demo.sparklers.domain.exception.CaptchaMismatchException;
import com.zhenhui.demo.sparklers.domain.exception.UserNotFoundException;
import com.zhenhui.demo.sparklers.domain.interactor.SigninWithCaptcha;
import com.zhenhui.demo.sparklers.domain.interactor.SigninWithSecret;
import com.zhenhui.demo.sparklers.service.params.SigninWithCaptchaParams;
import com.zhenhui.demo.sparklers.service.params.SigninWithSecretParams;
import com.zhenhui.demo.sparklers.service.results.Error;
import com.zhenhui.demo.sparklers.service.results.Result;
import io.reactivex.observers.DefaultObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/auth/token")
public class AuthController {

    @Autowired
    private SigninWithCaptcha signinWithCaptcha;
    @Autowired
    private SigninWithSecret signinWithSecret;

    @RequestMapping(method = RequestMethod.POST)
    public void createToken(@RequestBody SigninWithSecretParams body,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        signinWithSecret.execute(new SigninWithSecret.Params(body.getPhone(), body.getSecret()), new DefaultObserver<String>() {
            @Override
            public void onNext(String token) {
                Result.newBuilder()
                        .error(Error.NONE)
                        .data(token)
                        .write(response);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UserNotFoundException) {
                    Result.newBuilder().error(Error.DATA_NOT_FOUND).message("用户不存在")
                            .write(response);
                }
                if (e instanceof BadSecretException) {
                    Result.newBuilder().error(Error.LOGIN_REQUIRED).message("密码错误")
                            .write(response);
                } else {
                    Result.newBuilder().error(Error.INTERNAL_ERROR)
                            .write(response);
                }

                context.complete();
            }

            @Override
            public void onComplete() {
                context.complete();
            }
        });
    }

    @RequestMapping(path = "/with-captcha", method = RequestMethod.POST)
    public void createToken(@RequestBody SigninWithCaptchaParams body,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        signinWithCaptcha.execute(new SigninWithCaptcha.Params(body.getPhone(), body.getCaptcha()), new DefaultObserver<String>() {
            @Override
            public void onNext(String token) {
                Result.newBuilder().error(Error.NONE).data(token)
                        .write(response);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof CaptchaExpireException) {
                    Result.newBuilder().error(Error.DATA_INVALID).message("验证码无效或已过期")
                            .write(response);
                } else if (e instanceof CaptchaMismatchException) {
                    Result.newBuilder().error(Error.DATA_INVALID).message("验证码错误, 不匹配")
                            .write(response);
                } else if (e instanceof UserNotFoundException) {
                    Result.newBuilder().error(Error.DATA_NOT_FOUND).message("用户不存在")
                            .write(response);
                } else {
                    Result.newBuilder().error(Error.INTERNAL_ERROR)
                            .write(response);
                }

                context.complete();
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






