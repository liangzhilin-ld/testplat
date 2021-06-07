package com.techstar.testplat.web.controller;

import io.swagger.annotations.*;
import lombok.extern.apachecommons.CommonsLog;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.autotest.data.mode.TestScheduled;
import com.autotest.data.service.impl.TestScheduledServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.techstar.testplat.common.CodeMsg;
import com.techstar.testplat.common.PageBaseInfo;
import com.techstar.testplat.common.Result;
import com.techstar.testplat.config.TestPlatProperties;
import com.techstar.testplat.service.TestTaskService;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;

import com.techstar.testplat.service.TestDataServiceImpl;

@Api(tags = "测试计划管理")
@Validated
@CommonsLog
@RestController
@RequestMapping("TestScheduled")
public class TestSchedule{
	private @Autowired TestDataServiceImpl dataOp;
	private @Autowired TestTaskService defaultSchedulingConfigurer;
	
	
    @ApiOperation(value = "计划任务添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addTestPlan")
    public Result<Object> addTestPlan(@RequestBody TestScheduled plan) {
    	Result<Object> res=new Result<>();
        log.info("getMemberSmallVO:" + plan);
        try {
        	dataOp.AddScheduled(plan);   
        	if(plan.getIsStartNow()) {
        		defaultSchedulingConfigurer.saveApiReportHistoryList(plan);
        		defaultSchedulingConfigurer.addReport(plan,plan.getHistoryId());
        		String ressult=defaultSchedulingConfigurer.startTest(plan);
            	return res.setSuccess(ressult);
        	}
        	defaultSchedulingConfigurer.addTriggerTask(plan);
        } catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}    	
    	return res;
    }
    @ApiOperation(value = "计划任务停止")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("cancelTestPlan")
    public Result<Object> cancelTestPlan(@RequestParam(value = "uri", required = true) String taskID) {
    	Result<Object> res=new Result<>();
        try {
        	defaultSchedulingConfigurer.cancelTriggerTask(taskID);   	
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
    	return res;
    }
    @ApiOperation(value = "计划任务删除")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("delTestPlan")
    public Result<Object> delTestPlan(@RequestBody List<String> jobid) {
    	Result<Object> res=new Result<>();
        log.info("getMemberSmallVO:" + jobid);
        try {
        	dataOp.delScheduled(jobid);    
        	dataOp.delApiReport(jobid); 
        	for (String taskId : jobid) {
        		defaultSchedulingConfigurer.cancelTriggerTask(taskId);
			}        	
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
    	return res;
    }
    @ApiOperation(value = "计划任务修改")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("updateTestPlan")
    public Result<Object> updateTestPlan(@RequestBody TestScheduled plan) {
    	Result<Object> res=new Result<>();
        try {
        	dataOp.AddScheduled(plan) ;
        	defaultSchedulingConfigurer.resetTriggerTask(plan);     	
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
    	return res;
    }
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "计划列表查询")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("loadTestPlanAll")
    public Result<List<TestScheduled>> loadTestPlanAll(
    		@RequestParam(value = "page", required = false,defaultValue="1") long page,
    		@RequestParam(value = "size", required = false,defaultValue="20") long size,
    		@RequestParam(value = "name", required = false) String name) {
    	Result<List<TestScheduled>> res=new Result<>();
    	QueryWrapper<TestScheduled> wrapper = new QueryWrapper<>();
        try {
        	if(name!=null) {
        		wrapper.lambda().like(TestScheduled::getJobName, name);
        	}
        	PageBaseInfo pb=dataOp.getTestSchedule(page,size,wrapper);
        	res=Result.setSuccess(pb);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
    	return res;
    }
}
