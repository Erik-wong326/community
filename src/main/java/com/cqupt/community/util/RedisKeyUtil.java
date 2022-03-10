package com.cqupt.community.util;

/**
 * Redis生成Key的工具类
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/22 9:02
 */
public class RedisKeyUtil {

    //Redis-关注,取关
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee"; //关注的目标
    private static final String PREFIX_FOLLOWER = "follower"; //粉丝
    //Redis存储验证码
    private static final String PREFIX_KAPTCHA = "kaptcha";
    //Redis存储登录凭证
    private static final String PREFIX_TICKET = "ticket";
    //Redis缓存用户信息
    private static final String PREFIX_USER = "user";
    //Redis存储独立访客Unique visitor
    private static final String PREFIX_UV = "uv";
    //Redis存储日活跃用户Daily Active User
    private static final String PREFIX_DAU = "dau";
    //Redis存储帖子分数
    private static final String PREFIX_POST = "post";

    // 某个实体的赞
    // 键 like:entity:entityType:entityId -> 值 set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // 某个用户关注的实体
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 登录验证码功能
     *
     * 为了给每个用户返回不一样的验证码
     * 但未登录状态又不能使用 userId 作为用户凭证
     * -> 建立一个短期的临时凭证存在 cookie 用于标记用户
     * @param owner 用户临时凭证
     * @return
     */
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录的凭证
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 用户
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    //单日UV:以天为单位记录
    public static String getUVKey(String date){
        return PREFIX_UV + SPLIT + date;
    }

    //区间UV 例如:查看一周的UV
    public static String getUVKey(String startDate,String endDate){
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    //单日DAU:单日活跃用户
    public static String getDAUKey(String date){
        return PREFIX_DAU + SPLIT + date;
    }
    //区间DAU
    public static String getDAUKey(String startDate,String endDate){
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    // 帖子分数
    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }
}
