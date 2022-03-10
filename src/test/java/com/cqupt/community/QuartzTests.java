package com.cqupt.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/3/5 19:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class QuartzTests {
    @Autowired
    private Scheduler scheduler;

    /**
     * 删除 Quartz 数据
     */
    @Test
    public void testDeleteJob(){
        JobKey jobkey = new JobKey("alphaJob","alphaJobGroup");
        try {
            boolean result = scheduler.deleteJob(jobkey);
            System.out.println(result);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
