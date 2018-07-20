package com.zhenhui.demo.sparklers.restful;

import com.zhenhui.demo.sparklers.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTests extends TestBase {

    private MockMvc mvcMock;

    @Before
    public void setup() {
        mvcMock = MockMvcBuilders.standaloneSetup(new UserController()).build();
    }

    @Test
    public void testCreateUser() throws Exception {

        RequestBuilder request = post("/user")
                .param("phone", "18336499180")
                .param("secret", "123456789")
                .param("captcha", "1275");
        mvcMock.perform(request)
                .andExpect(status().is(400));
    }

}
