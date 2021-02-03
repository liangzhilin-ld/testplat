package com.techstar.testplat.controller;

import io.swagger.annotations.*;
import lombok.extern.apachecommons.CommonsLog;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.autotest.data.mode.ApiReport;
import com.autotest.data.mode.TestScheduled;
import com.autotest.data.service.impl.TestScheduledServiceImpl;
import com.techstar.testplat.config.CodeMsg;
import com.techstar.testplat.config.Result;
import com.techstar.testplat.service.TestTaskService;
import com.techstar.testplat.service.TestDataServiceImpl;

@Api(tags = "测试计划管理")
@Validated
@CommonsLog
@RestController
@RequestMapping("TestScheduled")
public class TestSchedule{
	private @Autowired TestDataServiceImpl dataOp;
	private @Autowired TestTaskService defaultSchedulingConfigurer;
	private @Autowired TestScheduledServiceImpl testSchedule;
	
	
    @ApiOperation(value = "计划任务添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addTestPlan")
    public Result<Object> addTestPlan(@RequestBody TestScheduled plan) {
    	Result<Object> res=new Result<>();
        log.info("getMemberSmallVO:" + plan);
        try {
        	dataOp.AddScheduled(plan);   
        	if(plan.getIsStartNow()) {
        		String id=plan.getId();
            	List<Integer> caseids=plan.getTcCaseids().get("samplerIds");
            	for (Integer caseid : caseids) {
            		ApiReport detail = new ApiReport();
            		detail.setCaseId(caseid);
            		detail.setJobId(id);
            		dataOp.addApiReport(detail);
        		}
            	return res;
        	}
        	defaultSchedulingConfigurer.addTriggerTask(plan);
        } catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
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
    @ApiOperation(value = "计划列表查询")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("loadTestPlanAll")
    public Result<List<TestScheduled>> loadTestPlanAll() {
    	Result<List<TestScheduled>> res=new Result<>();
        try {
        	res=Result.setSuccess(testSchedule.list());
        	    	
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
