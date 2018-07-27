package com.zhenhui.demo.sparklers.uic.restful;

import com.zhenhui.demo.sparklers.uic.common.Error;
import com.zhenhui.demo.sparklers.uic.common.Result;
import com.zhenhui.demo.sparklers.uic.domain.exception.*;
import com.zhenhui.demo.sparklers.uic.domain.interactor.SigninWithCaptcha;
import com.zhenhui.demo.sparklers.uic.domain.interactor.SigninWithSecret;
import com.zhenhui.demo.sparklers.uic.restful.params.SigninWithCaptchaParams;
import com.zhenhui.demo.sparklers.uic.restful.params.SigninWithSecretParams;
import com.zhenhui.demo.sparklers.uic.security.JsonWebTokenBlacklistService;
import com.zhenhui.demo.sparklers.uic.security.JsonWebTokenAuthentication;
import com.zhenhui.demo.sparklers.uic.security.TokenUtils;
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
    private TokenUtils tokenUtils;
    @Autowired
    private JsonWebTokenBlacklistService blacklistService;

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
                } else if (e instanceof BadSecretException) {
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

    @RequestMapping(path = "/captcha", method = RequestMethod.POST)
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
                if (e instanceof CaptchaNotFoundException) {
                    Result.newBuilder().error(Error.DATA_INVALID).message("验证码无效")
                            .write(response);
                } else if (e instanceof CaptchaExpireException) {
                    Result.newBuilder().error(Error.DATA_INVALID).message("验证码已过期")
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

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(method = RequestMethod.PATCH)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {

        final JsonWebTokenAuthentication authentication = (JsonWebTokenAuthentication) SecurityContextHolder.getContext()
                .getAuthentication();

        try {
            final String token = (String) request.getAttribute("token");
            blacklistService.block(token);

            Result.newBuilder().error(Error.NONE)
                    .data(tokenUtils.createToken(authentication.principal))
                    .write(response);
        } catch (Exception e) {
            Result.newBuilder().error(Error.INTERNAL_ERROR).message("服务不可用").write(response);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(method = RequestMethod.DELETE)
    public void invalidateToken(HttpServletRequest request, HttpServletResponse response) {

        try {
            final String token = (String) request.getAttribute("token");
            blacklistService.block(token);

            Result.newBuilder().error(Error.NONE).data(true).write(response);
        } catch (Exception e) {
            Result.newBuilder().error(Error.INTERNAL_ERROR).message("服务不可用").write(response);
        }
    }
}

