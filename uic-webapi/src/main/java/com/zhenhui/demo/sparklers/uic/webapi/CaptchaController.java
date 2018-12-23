package com.zhenhui.demo.sparklers.uic.webapi;

import com.zhenhui.demo.sparklers.common.Error;
import com.zhenhui.demo.sparklers.common.Result;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateCaptcha;
import com.zhenhui.demo.sparklers.uic.utils.ResultUtils;
import io.reactivex.observers.DefaultObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CreateCaptcha createCaptcha;

    @RequestMapping(method = RequestMethod.POST)
    public void getCaptcha(@RequestParam String phone, HttpServletRequest request, HttpServletResponse response) {

        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        createCaptcha.execute(phone, new DefaultObserver<String>() {
            @Override
            public void onNext(String captcha) {
                ResultUtils.write(Result.success(captcha), response);
            }

            @Override
            public void onError(Throwable t) {
                ResultUtils.write(Result.error(Error.APPLICATION_ERROR), response);
                context.complete();
            }

            @Override
            public void onComplete() {
                context.complete();
            }
        });

    }

}

