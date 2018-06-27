package com.zhenhui.demo.sparklers.service;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhenhui.demo.sparklers.domain.interactor.GetUser;
import com.zhenhui.demo.sparklers.domain.model.User;
import com.zhenhui.demo.sparklers.utils.ObserverAdapter;
import org.springframework.beans.factory.annotation.Autowired;

@WebServlet(urlPatterns = "/user/me", asyncSupported = true)
public class Profile extends HttpServlet {

    @Autowired
    private GetUser useCase;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        final AsyncContext asyncContext = request.startAsync(request, response);

        try {
            useCase.execute(1L, new ObserverAdapter<Optional<User>>() {
                @Override
                public void onSuccess(Optional<User> user) {
                    if (user.isPresent()) {
                        try {
                            response.getWriter().write(user.toString());
                        } catch (IOException e) {
                            // ignore
                        }
                    } else {
                        response.setStatus(404);
                    }

                    asyncContext.complete();
                }

                @Override
                public void onError(Throwable throwable) {
                    response.setStatus(500);
                    asyncContext.complete();
                }
            });
        } catch (Exception e) {
            response.setStatus(500);
            asyncContext.complete();
        }
    }
}




