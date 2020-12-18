package com.techstar.testplat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import com.autotest.data.mode.ApiReport;
import com.autotest.data.mode.ScheduledTrigger;
import com.autotest.data.service.impl.ApiReportServiceImpl;
import com.autotest.data.service.impl.ScheduledTriggerServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.apachecommons.CommonsLog;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@CommonsLog
public class TestTaskService implements SchedulingConfigurer{
	private @Autowired ScheduledTriggerServiceImpl trigger;
	private @Autowired ApiReportServiceImpl apiReport;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.setScheduler(taskExecutor());
    	
    	for (ScheduledTrigger st : getDynamicCronTask()) {
    		log.info("获取定时任ID:"+st.getId());
    		taskRegistrar.addTriggerTask(new Runnable() {
                @Override
                public void run() {
                    // 任务逻辑
                	QueryWrapper<ApiReport> queryWrapper=new QueryWrapper<>();
                	queryWrapper.lambda().eq(ApiReport::getJobId, st.getJobId())
                						 .select(ApiReport::getJobId);
                	List<ApiReport> apiDeatil=apiReport.list(queryWrapper);
                	apiDeatil.forEach(System.out::println);//item->caseIds.add(item.getCaseId().toString())
                    log.info(st.getId()+"执行任务中。。。。。");
                }
            }, new Trigger() {
                @Override
                public Date nextExecutionTime(TriggerContext triggerContext) {
                    String s = st.getTrigger();//SpringDynamicCronTask();
                    // 任务触发，可修改任务的执行周期
                    CronTrigger trigger = new CronTrigger(s);
                    Date nextExec = trigger.nextExecutionTime(triggerContext);
                    return nextExec;
                }
            });
		}
        
    }
 
    @Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(20);
    }
    
    private List<ScheduledTrigger> getDynamicCronTask() {
    	List<ScheduledTrigger>  crons=trigger.list();
    	log.info("获取定时任务数量:"+crons.size());
    	return crons; 
    }
}
