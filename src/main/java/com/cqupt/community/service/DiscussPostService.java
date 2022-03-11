package com.cqupt.community.service;

import com.cqupt.community.dao.DiscussPostMapper;
import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/15 17:36
 */
@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

//    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
//        return discussPostMapper.selectDiscussPost(userId,offset,limit);
//    }

    //findDiscussPosts重构
    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit, int orderMode){
        return discussPostMapper.selectDiscussPost(userId,offset,limit,orderMode);
    }

    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        //title 和 content 需要过滤敏感词,过滤前去除标签,例如:<script>abc</script>
        //HtmlUtils.htmlEscape 方法可以把标签转义
        // 转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

    public int updateType(int id, int type) {
        return discussPostMapper.updateType(id, type);
    }

    public int updateStatus(int id, int status) {
        return discussPostMapper.updateStatus(id, status);
    }

    public int updateScore(int id, double score) {
        return discussPostMapper.updateScore(id, score);
    }
}
