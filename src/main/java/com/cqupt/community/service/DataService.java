package com.cqupt.community.service;

import com.cqupt.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 网站数据统计业务
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/3/5 16:19
 */
@Service
public class DataService {
    @Autowired
    private RedisTemplate redisTemplate;

    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    /**
     * 每次请求都要计算 UV -> 拦截器 (DAU同理)
     * 将指定的IP计入UV
     * @param ip
     */
    public void recordUV(String ip) {
        String redisKey = RedisKeyUtil.getUVKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey, ip);
    }

    /**
     * 统计指定日期范围内的UV -> DataController  (DAU同理)
     * @param start
     * @param end
     * @return
     */
    public long calculateUV(Date start, Date end) {
        //1.参数空值判断
        if (start == null || end == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 整理该日期范围内的key
        List<String> keyList = new ArrayList<>();
        //运算日期 -> Calendar
        //2.遍历获取每天的UV
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);//获取起始日期
        while (!calendar.getTime().after(end)) {//start事件 < end
            String key = RedisKeyUtil.getUVKey(df.format(calendar.getTime()));//一天的key:UV
            keyList.add(key);//keyList存放每一天的UV
            calendar.add(Calendar.DATE, 1);//加一天
        }

        //3, union合并这些数据
        String redisKey = RedisKeyUtil.getUVKey(df.format(start), df.format(end));
        redisTemplate.opsForHyperLogLog().union(redisKey, keyList.toArray());

        //4. size返回统计的结果
        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    /**
     * 将指定用户计入DAU
     * @param userId
     */
    public void recordDAU(int userId) {
        String redisKey = RedisKeyUtil.getDAUKey(df.format(new Date()));
        redisTemplate.opsForValue().setBit(redisKey, userId, true);
    }

    /**
     * 统计指定日期范围内的DAU
     * 例如:从产品角度定义活跃用户: 假设7天内任何1天访问,就算活跃用户 -> OR 运算
     * @param start
     * @param end
     * @return
     */
    public long calculateDAU(Date start, Date end) {
        //1.参数空值判断
        if (start == null || end == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 整理该日期范围内的key
        List<byte[]> keyList = new ArrayList<>();
        //获取并设置起始日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        //2.遍历获取每天的DAU
        while (!calendar.getTime().after(end)) {
            String key = RedisKeyUtil.getDAUKey(df.format(calendar.getTime()));//一天的Key:DAU
            keyList.add(key.getBytes());//统计每一天的DAU
            calendar.add(Calendar.DATE, 1);//加一天
        }

        //3. 进行OR运算
        return (long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                String redisKey = RedisKeyUtil.getDAUKey(df.format(start), df.format(end));//存放OR运算的结果
                redisConnection.bitOp(RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(), keyList.toArray(new byte[0][0]));
                return redisConnection.bitCount(redisKey.getBytes());
            }
        });
    }

}
