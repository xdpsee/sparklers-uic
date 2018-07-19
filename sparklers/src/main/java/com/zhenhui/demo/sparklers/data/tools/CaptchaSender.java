package com.zhenhui.demo.sparklers.data.tools;

import org.springframework.stereotype.Component;

@Component
public class CaptchaSender {


    public boolean send(String phone, String captch) {

        try {

        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
