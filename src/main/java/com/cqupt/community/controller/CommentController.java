package com.cqupt.community.controller;

import com.cqupt.community.entity.Comment;
import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.entity.Event;
import com.cqupt.community.event.EventProducer;
import com.cqupt.community.service.CommentService;
import com.cqupt.community.service.DiscussPostService;
import com.cqupt.community.util.CommunityConstant;
import com.cqupt.community.util.HostHolder;
import com.cqupt.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/21 14:40
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * /add/{discussPostId} -> 添加之后跳转到当前帖子的页面
     * @param discussPostId 帖子id
     * @param comment 评论
     * @return
     */
    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        //设置哪个user发的comment,status,creatTime
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //Kafka显示系统通知功能
        // 触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId); //点击查看的链接 - postId (帖子id)

        //根据不同类型(帖子/评论) 设置不同的 entityUserId
        if (comment.getEntityType() == ENTITY_TYPE_POST) { //entityType是帖子discussPost
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());//找到对应帖子
            event.setEntityUserId(target.getUserId());//设置实体作者id
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) { //entityType是comment
            Comment target = commentService.findCommentById(comment.getEntityId()); //找到对应Comment
            event.setEntityUserId(target.getUserId());
        }

        eventProducer.fireEvent(event);

        //ES搜素功能
        //增加评论时，将帖子异步的提交到Elasticsearch服务器。
        if (comment.getEntityType() == ENTITY_TYPE_POST) { //对帖子的评论才提交到ES
            // 触发发帖事件
            event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);

            // 计算帖子分数
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, discussPostId);
        }

        return "redirect:/discuss/detail/" + discussPostId;
    }

}
