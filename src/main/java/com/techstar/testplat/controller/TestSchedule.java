package com.techstar.testplat.controller;

import io.swagger.annotations.*;
import lombok.extern.apachecommons.CommonsLog;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.autotest.data.mode.ApiReport;
import com.autotest.data.mode.TestScheduled;
import com.techstar.testplat.config.CodeMsg;
import com.techstar.testplat.config.Result;
import com.techstar.testplat.service.TestDataServiceImpl;

@Api(tags = "测试计划管理")
@Validated
@CommonsLog
@RestController
@RequestMapping("TestScheduled")
public class TestSchedule{
	private @Autowired TestDataServiceImpl dataOp;     
    @ApiOperation(value = "计划任务添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addTestPlan")
    public Result<Object> addTestPlan(@RequestBody TestScheduled plan) {
    	Result<Object> res=new Result<>();
        log.info("getMemberSmallVO:" + plan);
        try {
        	dataOp.AddScheduled(plan);        	
        	int id=plan.getId();        	
        	for (Integer caseid : plan.getCaseIds()) {
        		ApiReport detail = new ApiReport();
        		detail.setCaseId(caseid);
        		detail.setJobId(id);
        		dataOp.addApiReport(detail);
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
  
    @ApiOperation(value = "计划任务删除")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("delTestPlan")
    public Result<Object> delTestPlan(@RequestBody List<String> jobid) {
    	Result<Object> res=new Result<>();
        log.info("getMemberSmallVO:" + jobid);
        try {
        	dataOp.delScheduled(jobid);    
        	dataOp.delApiReport(jobid);        	
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
