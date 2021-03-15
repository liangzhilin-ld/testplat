package com.techstar.testplat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.transaction.annotation.Transactional;

import com.autotest.data.mode.ApiReport;
import com.autotest.data.mode.ApiReportHistoryList;
import com.autotest.data.mode.ScenarioReport;
import com.autotest.data.mode.ScenarioTestcase;
import com.autotest.data.mode.TestScheduled;
import com.autotest.data.service.impl.ApiReportHistoryListServiceImpl;
import com.autotest.data.service.impl.ApiReportServiceImpl;
import com.autotest.data.service.impl.ScenarioReportServiceImpl;
import com.autotest.data.service.impl.TestScheduledServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.techstar.testplat.config.TestPlatProperties;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import lombok.extern.apachecommons.CommonsLog;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
@Configuration
@EnableScheduling
@CommonsLog
@Transactional(rollbackFor=Exception.class)
public class TestTaskService implements SchedulingConfigurer{
	private @Autowired TestPlatProperties myProperties;
	private @Autowired TestScheduledServiceImpl trigger;
	private @Autowired ScenarioReportServiceImpl apiReport;
	private @Autowired ApiReportHistoryListServiceImpl historyReport;
	private ScheduledTaskRegistrar taskRegistrar;
	private Map<String, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();
//    private Set<ScheduledFuture<?>> scheduledFutures = null;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    	this.taskRegistrar = taskRegistrar;
        taskRegistrar.setScheduler(taskExecutor());
    	//taskRegistrar.setTaskScheduler(myThreadPoolTaskScheduler);
        List<TestScheduled>  crons=trigger.list();
        TaskScheduler scheduler=taskRegistrar.getScheduler();
        for (TestScheduled schedul : crons) {
        	TriggerTask triggerTask=this.createTriggerTask(schedul);
        	ScheduledFuture<?> future = scheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
        	taskFutures.put(schedul.getId().toString(), future);
		}
        System.out.println(inited());//初始化    
    }
 
    
    /**
     * 添加任务
     */
    public void addTriggerTask(TestScheduled schedul) {
        if (taskFutures.containsKey(schedul.getId())) {
            throw new SchedulingException("the taskId[" + schedul.getId() + "] was added.");
        }
        TaskScheduler scheduler = taskRegistrar.getScheduler();
        TriggerTask triggerTask=createTriggerTask(schedul);
        ScheduledFuture<?> future = scheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
        //getScheduledFutures().add(future);
        taskFutures.put(schedul.getId(), future);
    }
    public TriggerTask createTriggerTask(TestScheduled trig ) {
    	
    	return new TriggerTask(
    			new Runnable(){
    	            @Override
    	            public void run() {
    	            	log.info(trig.getId()+"任务开始执行。。。。。");
    	            	String historyId=DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
    	            	List<Integer> caseIds=trig.getTcCaseids().get("samplerIds");
    	            	if(!caseIds.isEmpty()) {
    	            		ApiReportHistoryList listHistory=saveApiReportHistoryList(trig);
    	            		trig.setTestIp("172.16.206.127");
    	            		trig.setTestPort("31100");
    	            		if(listHistory.getId().length()>0) {
    	            			
    	            			for (Entry<String, List<Integer>> entiy : trig.getTcCaseids().entrySet()) {
    	            				ScenarioReport detail=new ScenarioReport();
    	            				if(entiy.getKey().equals("scenarioIds"))
    	            					detail.setTcType(ScenarioTestcase.TYPE_SCENARIO);
    	            				for (Integer id : entiy.getValue()) {
    	            					detail.setTcId(id.toString());
    	            					detail.setJobId(trig.getId());
    	            					detail.setHistoryId(listHistory.getId());
    	            					apiReport.save(detail);
									}
								}
    	            			//startTest(trig);
    	            		}
    	            		
    	            	}
    	            }
    	    	},
    			new Trigger() {
    	    		@Override
    	    		public Date nextExecutionTime(TriggerContext triggerContext) {
    	    			String s=trig.getExpression();
    	    			if (StringUtils.isEmpty(s)) {
    	    				log.error("任务时间为空");
    	    			}
    	    			// 任务触发，可修改任务的执行周期
    	    			CronTrigger trigger = new CronTrigger(s);//TriggerTask第2个参数可直接用new CronTrigger方式
    	    			Date nextExec = trigger.nextExecutionTime(triggerContext);
    	    			return nextExec;
    	    		}
    	    	});
    }
    
    
    /**
     * 取消任务
     */
    public void cancelTriggerTask(String taskId) {
        ScheduledFuture<?> future = taskFutures.get(taskId);
        if (future != null) {
            future.cancel(true);
        }
        taskFutures.remove(taskId);
//        getScheduledFutures().remove(future);
    }

    /**
     * 重置任务
     */
//    public void resetTriggerTask(String taskId, TriggerTask triggerTask) {
//        cancelTriggerTask(taskId);
//        addTriggerTask(taskId, triggerTask);
//    }
    public void resetTriggerTask(TestScheduled schedul) {
        cancelTriggerTask(schedul.getId());
        addTriggerTask(schedul);
    }
 
    /**
     * 任务编号
     */
    public Set<String> taskIds() {
        return taskFutures.keySet();
    }
 
    /**
     * 是否存在任务
     */
    public boolean hasTask(String taskId) {
        return this.taskFutures.containsKey(taskId);
    }
 
    /**
     * 任务调度是否已经初始化完成
     */
    public boolean inited() {
        return this.taskRegistrar != null && this.taskRegistrar.getScheduler() != null;
    }
    //https://blog.csdn.net/weixin_33812433/article/details/91471261
    @Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(5);
//    	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        // 核心线程数10：线程池创建时候初始化的线程数
//        executor.setCorePoolSize(5);
//        // 最大线程数20：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
//        executor.setMaxPoolSize(10);
//        // 缓冲队列200：用来缓冲执行任务的队列
//        executor.setQueueCapacity(20);
//        // 允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
//        executor.setKeepAliveSeconds(60);
//        // 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
//        executor.setThreadNamePrefix("taskExecutor-");
//        // 线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，
//        // 当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；
//        // 如果执行程序已关闭，则会丢弃该任务
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        return executor;
    }
    @Bean(name = "myThreadPoolTaskScheduler")
    public TaskScheduler getMyThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadNamePrefix("Haina-Scheduled-");
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //调度器shutdown被调用时等待当前被调度的任务完成
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        //等待时长
        taskScheduler.setAwaitTerminationSeconds(60);
        return taskScheduler;
    }
    
    
    public ApiReportHistoryList saveApiReportHistoryList(TestScheduled trig) {
    	ApiReportHistoryList listHistory=new ApiReportHistoryList();
		listHistory.setJobId(trig.getId());
		listHistory.setJobName(trig.getJobName());
		listHistory.setStartTime(LocalDateTime.now());
		listHistory.setCreateTime(LocalDateTime.now());
		listHistory.setNotifyType(trig.getNotifyType());
		listHistory.setSerVersion("");
		historyReport.save(listHistory);
		trig.setHistoryId(listHistory.getId());
		return listHistory;
    }
    public String startTest(TestScheduled trig) {
    	String bb=JSONUtil.toJsonStr(trig);
    	String result=HttpRequest.post(myProperties.getJmeterAgentUrl())
        		.header(Header.ACCEPT, "application/json, text/plain, */*")
        		.header(Header.ACCEPT_ENCODING, "gzip, deflate")
        		.header(Header.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en;q=0.8,fr;q=0.7")
        		.header(Header.CONNECTION, "keep-alive")
        		.header(Header.CONTENT_TYPE, "application/json")
        		.header(Header.USER_AGENT, "Hutool http")
        	    .timeout(20000)//超时，毫秒
        	    .body(JSONUtil.parse(trig))
        	    .execute().body();
    	return result;
    }
    	
}
