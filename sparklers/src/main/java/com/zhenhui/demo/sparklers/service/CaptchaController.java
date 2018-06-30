package com.zhenhui.demo.sparklers.service;

import com.zhenhui.demo.sparklers.domain.interactor.CreateCaptcha;
import com.zhenhui.demo.sparklers.service.results.Error;
import com.zhenhui.demo.sparklers.service.results.Result;
import io.reactivex.observers.DefaultObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CreateCaptcha createCaptcha;

    @RequestMapping(method = RequestMethod.GET)
    public void getCaptcha(@RequestParam String phone, HttpServletRequest request, HttpServletResponse response) {

        final AsyncContext context = request.startAsync();
        context.setTimeout(6000);

        createCaptcha.execute(phone, new DefaultObserver<String>() {
            @Override
            public void onNext(String captcha) {
                Result.newBuilder().error(Error.NONE).data(captcha).write(response);
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

}
