package com.zhenhui.demo.sparklers.uic.restful;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.uic.common.Error;
import com.zhenhui.demo.sparklers.uic.common.Result;
import com.zhenhui.demo.sparklers.uic.domain.exception.*;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.uic.domain.interactor.QueryUserWithId;
import com.zhenhui.demo.sparklers.uic.domain.interactor.ResetSecret;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.restful.params.CreateUserParams;
import com.zhenhui.demo.sparklers.uic.restful.params.ResetSecretParams;
import com.zhenhui.demo.sparklers.uic.security.JsonWebTokenAuthentication;
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
                        Result.newBuilder().error(Error.NONE).data(true).write(response);
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
                        } else if (e instanceof UserAlreadyExistException) {
                            Result.newBuilder().error(Error.DATA_EXISTED).message("用户已存在").write(response);
                        } else {
                            Result.newBuilder().error(Error.INTERNAL_ERROR).write(response);
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

        final JsonWebTokenAuthentication authentication = (JsonWebTokenAuthentication) SecurityContextHolder.getContext()
                .getAuthentication();

        queryUserWithId.execute(authentication.principal.getUserId(), new DefaultObserver<Optional<User>>() {
            @Override
            public void onNext(Optional<User> user) {
                if (!user.isPresent()) {
                    Result.newBuilder().error(Error.DATA_NOT_FOUND).message("未知用户").write(response);
                } else {
                    Result.newBuilder().error(Error.NONE).message("ok").data(user.get()).write(response);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Result.newBuilder().error(Error.INTERNAL_ERROR).write(response);
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
                    Result.newBuilder().error(Error.DATA_NOT_FOUND).message("未知用户").write(response);
                } else {
                    Result.newBuilder().error(Error.NONE).message("ok").data(user.get()).write(response);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Result.newBuilder().error(Error.INTERNAL_ERROR).write(response);
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
                Result.newBuilder().error(Error.NONE).data(success).write(response);
            }

            @Override
            public void onError(Throwable e) {
                final Result.Builder result = Result.newBuilder();
                if (e instanceof CaptchaNotFoundException) {
                    result.error(Error.DATA_INVALID).message("验证码无效").write(response);
                } else if (e instanceof CaptchaExpireException) {
                    result.error(Error.DATA_INVALID).message("验证码已过期").write(response);
                } else if (e instanceof CaptchaMismatchException) {
                    result.error(Error.DATA_INVALID).message("验证码错误, 不匹配").write(response);
                } else if (e instanceof UserNotFoundException) {
                    result.error(Error.DATA_NOT_FOUND).message("用户不存在").write(response);
                } else {
                    result.error(Error.INTERNAL_ERROR).write(response);
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



