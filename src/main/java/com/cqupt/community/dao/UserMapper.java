package com.cqupt.community.dao;

import com.cqupt.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/15 14:35
 */
@Mapper
public interface UserMapper {

    User selectById(int id);
    User selectByName(String username);
    User selectByEmail(String email);

    int insertUser(User user);
    int updateStatus(int id,int status);
    int updateHeader(int id,String headerUrl);
    int updatePassword(int id,String password);

}
