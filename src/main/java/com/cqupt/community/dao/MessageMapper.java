package com.cqupt.community.dao;

import com.cqupt.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/21 15:17
 */
@Mapper
public interface MessageMapper {
    // 1.私信页面
    // 查询当前用户的会话列表,针对每个会话只返回一条最新的私信.
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量.
    int selectConversationCount(int userId);

    // 2.详情页面
    // 2.1查询某个会话所包含的私信列表.
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 2.2查询某个会话所包含的私信数量.
    int selectLetterCount(String conversationId);

    // 3.查询未读私信的数量
    int selectLetterUnreadCount(int userId, String conversationId);

    // 4.新增消息
    int insertMessage(Message message);

    // 5.修改消息的状态:未读(status:0) -> 已读(status:1)
    int updateStatus(List<Integer> ids, int status);

    // 6.显示通知列表
    // 6.1查询某个 topic 下最新的通知(1个即可)
    Message selectLatestNotice(int userId, String topic);

    // 6.2查询某个 topic 所包含的通知数量(共x个会话)
    int selectNoticeCount(int userId,String topic);

    // 6.3查询未读的通知的数量
    int selectNoticeUnreadCount(int userId,String topic);

    // 7.查询某个 topic 所包含的通知列表
    List<Message> selectNotices(int userId, String topic, int offset, int limit);
}
