package com.cqupt.community.controller;

import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.entity.Page;
import com.cqupt.community.entity.User;
import com.cqupt.community.service.DiscussPostService;
import com.cqupt.community.service.LikeService;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/15 17:43
 */
@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page,@RequestParam(name = "orderMode", defaultValue = "0") int orderMode){
        //方法调用前，SpringMVC回自动实例化Model 和 Page,并将 Page 注入 Model
        //所以,在 thymeleaf 中可以直接访问
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index?orderMode=" + orderMode);


        List<DiscussPost> posts = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit(),orderMode);
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if (posts != null){
            for (DiscussPost post : posts) {
                Map<String ,Object> map = new HashMap<>();
                map.put("post",post );
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                map.put("likeCount",likeCount);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("orderMode", orderMode);
        return "index";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "/error/500";
    }

    //拒绝访问时跳转的页面
    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage(){
        return "/error/404";
    }
}
