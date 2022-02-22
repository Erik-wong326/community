package com.cqupt.community.dao;

import com.cqupt.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/19 10:43
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {

    /**
     * 细节：
     *             "insert into login_ticket(user_id,ticket,status,expired) ", 换行需要在语句最后添加空格，防止sql语句粘连
     *             "values(#{userId},#{ticket},#{status},#{expired})"
     *
     * @param loginTicket
     * @return
     */
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    /**
     * if判断也能加在这里，但是需要<script> </script> 标签
     * @param ticket
     * @param status
     * @return
     */
    @Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
            "<if test=\"ticket!=null\"> ",
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);

}
