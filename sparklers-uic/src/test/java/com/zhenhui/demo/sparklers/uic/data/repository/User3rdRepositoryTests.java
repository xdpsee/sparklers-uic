package com.zhenhui.demo.sparklers.uic.data.repository;

import com.zhenhui.demo.sparklers.uic.TestBase;
import com.zhenhui.demo.sparklers.uic.common.SocialType;
import com.zhenhui.demo.sparklers.uic.domain.model.User3rd;
import com.zhenhui.demo.sparklers.uic.domain.repository.User3rdRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class User3rdRepositoryTests extends TestBase {

    @Autowired
    private User3rdRepository user3rdRepository;

    @Test
    public void testNormal() {

        User3rd user3rd = new User3rd(SocialType.WEIBO, "1", "", "", 1L);
        user3rdRepository.create3rdUser(user3rd);

        user3rd = user3rdRepository.get3rdUser(SocialType.WEIBO, "1");
        assertNotNull(user3rd);
    }

    @Test
    public void testDuplicate() {
        User3rd user3rd = new User3rd(SocialType.WEIBO, "1", "", "", 1L);
        user3rdRepository.create3rdUser(user3rd);

        user3rd = new User3rd(SocialType.WEIBO, "1", "", "", 1L);
        user3rdRepository.create3rdUser(user3rd);
    }

    @Test
    public void testGet3rdUser() {

        User3rd user3rd = new User3rd(SocialType.WEIBO, "1", "", "", 1L);
        user3rdRepository.create3rdUser(user3rd);

        List<User3rd> user3rds = user3rdRepository.get3rdUsers(1);

        assertEquals(1, user3rds.size());

    }

}