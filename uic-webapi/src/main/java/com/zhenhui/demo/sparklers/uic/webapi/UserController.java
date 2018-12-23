package com.zhenhui.demo.sparklers.uic.webapi;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.common.Error;
import com.zhenhui.demo.sparklers.common.Result;
import com.zhenhui.demo.sparklers.uic.domain.exception.*;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.uic.domain.interactor.QueryUserWithId;
import com.zhenhui.demo.sparklers.uic.domain.interactor.ResetSecret;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.webapi.params.CreateUserParams;
import com.zhenhui.demo.sparklers.uic.webapi.params.ResetSecretParams;
import com.zhenhui.demo.sparklers.uic.security.domain.SecurityToken;
import com.zhenhui.demo.sparklers.uic.utils.ResultUtils;
import io.reactivex.observers.DefaultObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=utf-8")
public class UserController {

    @Autowired
    private QueryUserWithId queryUserWithId;

    @Autowired
    private CreateUser createUser;

    @Autowired
    private ResetSecret resetSecret;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.POST)
    public void createUser(@RequestBody CreateUserParams params,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        createUser.execute(new Params(params.getPhone()
                        , passwordEncoder.encode(params.getSecret())
                        , Sets.newHashSet("USER")
                        , params.getCaptcha()),
                new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean success) {
                        ResultUtils.write(Result.success(success), response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof CaptchaNotFoundException) {
                            ResultUtils.write(Result.error(Error.DATA_INVALID.error(), "验证码无效"), response);
                        } else if (e instanceof CaptchaExpireException) {
                            ResultUtils.write(Result.error(Error.DATA_INVALID.error(), "验证码已过期"), response);
                        } else if (e instanceof CaptchaMismatchException) {
                            ResultUtils.write(Result.error(Error.DATA_INVALID.error(), "验证码错误, 不匹配"), response);
                        } else if (e instanceof UserAlreadyExistException) {
                            ResultUtils.write(Result.error(Error.DATA_EXISTED.error(), "用户已存在"), response);
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
    @RequestMapping(path = "/me", method = RequestMethod.POST)
    public void currentUser(HttpServletRequest request, HttpServletResponse response) {

        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        final SecurityToken authentication = (SecurityToken) SecurityContextHolder.getContext()
                .getAuthentication();

        queryUserWithId.execute(authentication.subject.getUserId(), new DefaultObserver<Optional<User>>() {
            @Override
            public void onNext(Optional<User> user) {
                if (!user.isPresent()) {
                    ResultUtils.write(Result.error(Error.DATA_NOT_FOUND.name(), "未知用户"), response);
                } else {
                    ResultUtils.write(Result.success(user.get()), response);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                ResultUtils.write(Result.error(Error.APPLICATION_ERROR), response);
                context.complete();
            }

            @Override
            public void onComplete() {
                context.complete();
            }
        });
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    public void getUser(@PathVariable("id") long userId, HttpServletRequest request, HttpServletResponse response) {

        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        queryUserWithId.execute(userId, new DefaultObserver<Optional<User>>() {
            @Override
            public void onNext(Optional<User> user) {
                if (!user.isPresent()) {
                    ResultUtils.write(Result.error(Error.DATA_NOT_FOUND.name(), "未知用户"), response);
                } else {
                    ResultUtils.write(Result.success(user.get()), response);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                ResultUtils.write(Result.error(Error.APPLICATION_ERROR), response);
                context.complete();
            }

            @Override
            public void onComplete() {
                context.complete();
            }
        });
    }

    @RequestMapping(path = "/reset-secret", method = RequestMethod.POST)
    public void resetSecret(@RequestBody ResetSecretParams params, HttpServletRequest request, HttpServletResponse response) {

        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        resetSecret.execute(new ResetSecret.Params(params.getPhone(), params.getCaptcha(), params.getSecret()), new DefaultObserver<Boolean>() {
            @Override
            public void onNext(Boolean success) {
                ResultUtils.write(Result.success(success), response);
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




