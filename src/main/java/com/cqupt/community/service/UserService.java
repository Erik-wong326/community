package com.cqupt.community.service;

import com.cqupt.community.dao.UserMapper;
import com.cqupt.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/15 17:40
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }
}
