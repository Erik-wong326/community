package com.cqupt.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/22 17:35
 */

public class Event {
    //例: mark(102)      点赞(1)              marry(101)
    //   userId    entityType(entityId)     entityUserId
    private String topic;
    private int userId; //事件触发者
    private int entityType; //事件类型(点赞、评论、关注等)
    private int entityId; // 实体(帖子/评论)Id
    private int entityUserId; //实体作者Id
    private Map<String, Object> data = new HashMap<>(); //考虑事件通用性->需要记录其他未知数据

    public String getTopic() {
        return topic;
    }

    /**
     * setTopic时传回Event
     * 便于对其他属性进行设置
     * @param topic
     * @return
     */
    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    //改成传 k-v 输入 map 返回Event,一次只传一个 k-v
    public Event setData(String key,Object value) {
        this.data.put(key,value);
        return this;
    }

}
