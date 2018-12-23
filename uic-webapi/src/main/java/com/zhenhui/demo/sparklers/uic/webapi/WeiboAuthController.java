package com.zhenhui.demo.sparklers.uic.webapi;

import com.zhenhui.demo.sparklers.common.Error;
import com.zhenhui.demo.sparklers.common.Result;
import com.zhenhui.demo.sparklers.uic.domain.exception.*;
import com.zhenhui.demo.sparklers.uic.domain.interactor.SigninWithSocial;
import com.zhenhui.demo.sparklers.uic.domain.interactor.UserBindSocial;
import com.zhenhui.demo.sparklers.uic.webapi.params.UserBindingParams;
import com.zhenhui.demo.sparklers.uic.utils.ResultUtils;
import com.zhenhui.demo.uic.api.enums.SocialType;
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
@RequestMapping("/auth/weibo")
public class WeiboAuthController {

    @Autowired
    private SigninWithSocial signinWithSocial;
    @Autowired
    private UserBindSocial userBindSocial;

    @RequestMapping(path = "", method = RequestMethod.POST)
    public void createToken(@RequestBody String token,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        signinWithSocial.execute(new SigninWithSocial.Params(SocialType.WEIBO, token), new DefaultObserver<String>() {
            @Override
            public void onNext(String jwt) {
                ResultUtils.write(Result.success(jwt), response);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof Auth3rdUserException) {
                    ResultUtils.write(Result.error(Error.DATA_INVALID.error(), e.getMessage()), response);
                } else if (e instanceof Auth3rdNoUserBondException) {
                    ResultUtils.write(Result.error(Error.DATA_NOT_FOUND.error(), e.getMessage()), response);
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

    @RequestMapping(path = "/bind", method = RequestMethod.POST)
    public void bindUser(@RequestBody UserBindingParams params,
                         HttpServletRequest request,
                         HttpServletResponse response) {

        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        userBindSocial.execute(new UserBindSocial.Params(SocialType.WEIBO
                , params.getToken()
                , params.getPhone()
                , params.getCaptcha()), new DefaultObserver<String>() {
            @Override
            public void onNext(String jwt) {
                ResultUtils.write(Result.success(jwt), response);
            }

            @Override
            public void onError(Throwable e) {

                if (e instanceof CaptchaNotFoundException) {
                    ResultUtils.write(Result.error(Error.DATA_INVALID.name(), "验证码无效"), response);
                } else if (e instanceof CaptchaExpireException) {
                    ResultUtils.write(Result.error(Error.DATA_INVALID.name(), "验证码已过期"), response);
                } else if (e instanceof CaptchaMismatchException) {
                    ResultUtils.write(Result.error(Error.DATA_INVALID.name(), "验证码错误, 不匹配"), response);
                } else if (e instanceof UserNotFoundException) {
                    ResultUtils.write(Result.error(Error.DATA_NOT_FOUND.name(), "用户不存在"), response);
                } else if (e instanceof Auth3rdUserException) {
                    ResultUtils.write(Result.error(Error.DATA_INVALID.name(), e.getMessage()), response);
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

}
