package com.cqupt.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/17 13:16
 */
public class CommunityUtil {

    //生成随机字符串作为激活码
    public static String generateUUID(){
        //randomUUID会产生我们不需要的"-" -> replaceAll用 "" 替换
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5码加密 , MD5特点:只能加密不能解密
    //避免简单密码被破解 -> salt值
    //hello ->(md5) abc123def123
    //hello + 3e4a(salt) ->(md5) abc123def495
    public static String md5Encode(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        //用spring自带的工具类进行md5加密
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
