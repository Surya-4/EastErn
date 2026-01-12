package com.ecommerce.user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import com.ecommerce.bean.UserBean;
import com.ecommerce.service.ServiceInterface;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private ServiceInterface userService;

    @Test
    public void testAddUser() {
//        UserBean user = new UserBean();
//        user.setUserName("junituser2");
//        user.setPassword("testpass2");
//        user.setRoles(Arrays.asList("ROLE_USER"));
//
//        String userId = userService.addUser(user);
//        assertNotNull(userId, "User ID should not be null after save");
//
//        UserBean fetchedUser = userService.findUser("junituser");
//        assertNotNull(fetchedUser, "User must be found after save");
//        assertEquals("junituser", fetchedUser.getUserName());
    }
}
