package com.zhenhui.demo.sparklers.uic.restful;

import com.zhenhui.demo.sparklers.uic.common.Error;
import com.zhenhui.demo.sparklers.uic.common.Result;
import com.zhenhui.demo.sparklers.uic.common.SocialType;
import com.zhenhui.demo.sparklers.uic.domain.exception.Auth3rdNoUserBondException;
import com.zhenhui.demo.sparklers.uic.domain.exception.Auth3rdUserException;
import com.zhenhui.demo.sparklers.uic.domain.interactor.SigninWithSocial;
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

    @RequestMapping(path = "", method = RequestMethod.POST)
    public void createToken(@RequestBody String token,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        signinWithSocial.execute(new SigninWithSocial.Params(SocialType.WEIBO, token), new DefaultObserver<String>() {
            @Override
            public void onNext(String jwt) {
                Result.newBuilder().error(Error.NONE).data(jwt).write(response);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof Auth3rdUserException) {
                    Result.newBuilder().error(Error.DATA_INVALID).message(e.getMessage()).write(response);
                } else if (e instanceof Auth3rdNoUserBondException) {
                    Result.newBuilder().error(Error.DATA_NOT_FOUND).message(e.getMessage()).write(response);
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

}
