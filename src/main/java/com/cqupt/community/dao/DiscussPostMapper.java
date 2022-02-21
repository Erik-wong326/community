package com.cqupt.community.dao;

import com.cqupt.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/15 17:00
 */
@Mapper
public interface DiscussPostMapper {
    //首页查询功能1
    //分页查询 -> 返回的是一个集合 -> 用list装
    //首页查询所有人的帖子时不用userId
    //但是 用户个人主页->我发布的帖子需要使用userId -> userId我们称为动态条件
    //此时就需要写动态 SQL 语句
    //分页 offset:起始行行号 , limit:每页最多显示多少数据
    List<DiscussPost> selectDiscussPost(int userId,int offset,int limit);

    //首页查询功能2
    //查询帖子的行数
    //userId 是sql语句需要用到的动态条件,并且这个方法有且仅有这一个条件(userId),在<if>中使用
    //则参数之前必须取别名 -> @Param 给参数取别名
    int selectDiscussPostRows(@Param("userId")int userId);

    /**
     * 功能3:发布帖子
     * @param discussPost
     * @return
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 功能4:查询帖子详情
     * @param id 用户id
     * @return
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 功能5:更新评论数量
     * @param id 帖子id
     * @param commentCount 评论数量
     * @return
     */
    int updateCommentCount(int id,int commentCount);

}
