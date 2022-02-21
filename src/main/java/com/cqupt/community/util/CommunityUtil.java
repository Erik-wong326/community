package com.cqupt.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
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


    /**
     * 获取 JSON 字符串
     * 将 map 中的业务数据转换为 JSON 字符串
     * @param code 编码
     * @param msg 提示信息
     * @param map 业务数据
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    /**
     * 因为有的时候没有业务数据 map
     * 因此重载 getJSON方法
     * 重载方法2
     * @param code 编码
     * @param msg 提示信息
     * @return
     */
    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    /**
     * 重载方法3
     * @param code
     * @return
     */
    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

    /**
     * 测试JSON方法
     * @param args
     */
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        map.put("age", 25);
        System.out.println(getJSONString(0, "ok", map));
    }

}
