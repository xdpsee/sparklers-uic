package com.zhenhui.demo.sparklers.service;

import java.util.Optional;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhenhui.demo.sparklers.domain.exception.UserAlreadyExistException;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.domain.interactor.QueryUserById;
import com.zhenhui.demo.sparklers.domain.model.User;
import com.zhenhui.demo.sparklers.security.JsonWebTokenAuthentication;
import com.zhenhui.demo.sparklers.service.results.ErrorCode;
import com.zhenhui.demo.sparklers.service.params.CreateUserParams;
import com.zhenhui.demo.sparklers.service.results.Result;
import io.reactivex.observers.DefaultObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=utf-8")
public class UserController {

    @Autowired
    private QueryUserById queryUserById;

    @Autowired
    private CreateUser createUser;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public void createUser(@RequestBody CreateUserParams body,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        final AsyncContext context = request.startAsync();
        context.setTimeout(10000);

        createUser.execute(new Params(body.getPhone()
                        , passwordEncoder.encode(body.getSecret())
                        , body.getAuthorities()),
                new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean success) {
                        Result.newBuilder().error(ErrorCode.NONE).data(true).write(response);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (throwable instanceof UserAlreadyExistException) {
                            Result.newBuilder().error(ErrorCode.DATA_EXISTED).message("用户已存在").write(response);
                        } else {
                            Result.newBuilder().error(ErrorCode.INTERNAL_ERROR).write(response);
                        }

                        context.complete();
                    }

                    @Override
                    public void onComplete() {
                        context.complete();
                    }
                });
    }

    @RequestMapping(path = "/me", method = RequestMethod.GET)
    public void currentUser(HttpServletRequest request, HttpServletResponse response) {

        final AsyncContext context = request.startAsync();
        context.setTimeout(10000);

        final JsonWebTokenAuthentication authentication = (JsonWebTokenAuthentication) SecurityContextHolder.getContext()
                .getAuthentication();

        queryUserById.execute(authentication.principal.getUserId(), new DefaultObserver<Optional<User>>() {
            @Override
            public void onNext(Optional<User> user) {
                if (!user.isPresent()) {
                    Result.newBuilder().error(ErrorCode.DATA_NOT_FOUND).message("未知用户").write(response);
                } else {
                    Result.newBuilder().error(ErrorCode.NONE).message("ok").data(user.get()).write(response);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Result.newBuilder().error(ErrorCode.INTERNAL_ERROR).write(response);
                context.complete();
            }

            @Override
            public void onComplete() {
                context.complete();
            }
        });
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public void getUser(@PathVariable("id") long userId, HttpServletRequest request, HttpServletResponse response) {

        final AsyncContext context = request.startAsync();
        context.setTimeout(10000);

        queryUserById.execute(userId, new DefaultObserver<Optional<User>>() {
            @Override
            public void onNext(Optional<User> user) {
                if (!user.isPresent()) {
                    Result.newBuilder().error(ErrorCode.DATA_NOT_FOUND).message("未知用户").write(response);
                } else {
                    Result.newBuilder().error(ErrorCode.NONE).message("ok").data(user.get()).write(response);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Result.newBuilder().error(ErrorCode.INTERNAL_ERROR).write(response);
                context.complete();
            }

            @Override
            public void onComplete() {
                context.complete();
            }
        });
    }

}




