package com.zhenhui.demo.sparklers.uic.webapi;

import com.zhenhui.demo.sparklers.common.Error;
import com.zhenhui.demo.sparklers.common.Result;
import com.zhenhui.demo.sparklers.uic.domain.exception.*;
import com.zhenhui.demo.sparklers.uic.domain.interactor.SigninWithCaptcha;
import com.zhenhui.demo.sparklers.uic.domain.interactor.SigninWithSecret;
import com.zhenhui.demo.sparklers.uic.webapi.params.SigninWithCaptchaParams;
import com.zhenhui.demo.sparklers.uic.webapi.params.SigninWithSecretParams;
import com.zhenhui.demo.sparklers.uic.security.SecurityTokenProducer;
import com.zhenhui.demo.sparklers.uic.security.domain.SecurityToken;
import com.zhenhui.demo.sparklers.uic.utils.ResultUtils;
import com.zhenhui.demo.uic.api.service.SecurityBlacklistService;
import io.reactivex.observers.DefaultObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection,unchecked")
@RestController
@RequestMapping("/auth/token")
public class AuthController {

    @Autowired
    private SigninWithCaptcha signinWithCaptcha;
    @Autowired
    private SigninWithSecret signinWithSecret;
    @Autowired
    private SecurityTokenProducer tokenProducer;
    @Autowired
    private SecurityBlacklistService blacklistService;

    @RequestMapping(method = RequestMethod.POST)
    public void createToken(@RequestBody SigninWithSecretParams body,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        signinWithSecret.execute(new SigninWithSecret.Params(body.getPhone(), body.getSecret()), new DefaultObserver<String>() {
            @Override
            public void onNext(String token) {
                ResultUtils.write(Result.success(token), response);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UserNotFoundException) {
                    ResultUtils.write(Result.error(Error.DATA_NOT_FOUND.error(), "用户不存在"), response);
                } else if (e instanceof BadSecretException) {
                    ResultUtils.write(Result.error(Error.LOGIN_REQUIRED.error(), "密码错误"), response);
                } else {
                    ResultUtils.write(Result.error(Error.APPLICATION_ERROR), response);
                }

                context.complete();
            }

            @Override
            public void onComplete() {
                context.complete();
            }
        });
    }

    @RequestMapping(path = "/captcha", method = RequestMethod.POST)
    public void createToken(@RequestBody SigninWithCaptchaParams body,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        signinWithCaptcha.execute(new SigninWithCaptcha.Params(body.getPhone(), body.getCaptcha()), new DefaultObserver<String>() {
            @Override
            public void onNext(String token) {
                ResultUtils.write(Result.success(token), response);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof CaptchaNotFoundException) {
                    ResultUtils.write(Result.error(Error.DATA_INVALID.error(), "验证码无效"), response);
                } else if (e instanceof CaptchaExpireException) {
                    ResultUtils.write(Result.error(Error.DATA_INVALID.error(), "验证码已过期"), response);
                } else if (e instanceof CaptchaMismatchException) {
                    ResultUtils.write(Result.error(Error.DATA_INVALID.error(), "验证码错误, 不匹配"), response);
                } else if (e instanceof UserNotFoundException) {
                    ResultUtils.write(Result.error(Error.DATA_NOT_FOUND.error(), "用户不存在"), response);
                } else {
                    ResultUtils.write(Result.error(Error.APPLICATION_ERROR), response);
                }

                context.complete();
            }

            @Override
            public void onComplete() {
                context.complete();
            }
        });
    }

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(method = RequestMethod.PATCH)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {

        final SecurityToken authentication = (SecurityToken) SecurityContextHolder.getContext()
                .getAuthentication();

        try {
            final String token = (String) request.getAttribute("token");
            blacklistService.block(token);

            String jwt = tokenProducer.createToken(authentication.subject);
            ResultUtils.write(Result.success(jwt), response);
        } catch (Exception e) {
            ResultUtils.write(Result.error(Error.APPLICATION_ERROR), response);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(method = RequestMethod.DELETE)
    public void invalidateToken(HttpServletRequest request, HttpServletResponse response) {

        try {
            final String token = (String) request.getAttribute("token");
            blacklistService.block(token);

            ResultUtils.write(Result.success(true), response);
        } catch (Exception e) {
            ResultUtils.write(Result.error(Error.APPLICATION_ERROR), response);
        }
    }

}

