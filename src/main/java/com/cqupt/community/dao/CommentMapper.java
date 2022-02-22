package com.cqupt.community.dao;

import com.cqupt.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/21 13:01
 */
@Mapper
public interface CommentMapper {

    /**
     * 根绝实体查询
     * 帖子(课程等)的评论
     * @param entityType 实体类型
     * @param entityId 实体id
     * @param offset
     * @param limit
     * @return
     */
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 查询数据的条目数
     * @param entityType
     * @param entityId
     * @return
     */
    int selectCountByEntity(int entityType, int entityId);


    int insertComment(Comment comment);

    /**
     * 根据 id 查询 comment
     * @param id
     * @return
     */
    Comment selectCommentById(int id);
}
