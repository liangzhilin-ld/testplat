package com.techstar.testplat.controller;

import io.swagger.annotations.*;
import lombok.extern.apachecommons.CommonsLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.autotest.data.mode.*;
import com.autotest.data.mode.assertions.AssertEntity;
import com.autotest.data.mode.processors.PostProcessors;
import com.autotest.data.mode.processors.PreProcessors;
import com.autotest.data.service.impl.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.autotest.data.service.impl.ApiTestcaseServiceImpl;
//import com.autotest.data.service.impl.AssertJsonServiceImpl;
//import com.autotest.data.service.impl.BeanshellServiceImpl;
//import com.autotest.data.service.impl.ProcessorJdbcServiceImpl;
//import com.autotest.data.service.impl.ProcessorJsonServiceImpl;
import com.techstar.testplat.common.CodeMsg;
import com.techstar.testplat.common.Result;

@Api(tags = "用例管理")
@Validated
@CommonsLog
@RestController
@RequestMapping("TestCaseManage")
public class TestCaseManage{
	private @Autowired HttpTestcaseServiceImpl httpServer;
	
    @ApiOperation(value = "用例添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addTestCase")//@RequestBody ApiTestcase api,
    public Result<Object> addTestCase(@RequestBody HttpTestcase api) {
    	Result<Object> res=new Result<>();
        try {
        	if(httpServer.save(api))
        		res=Result.setSuccess(api);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
       
    	return res;
    }
    
    
    
    @ApiOperation(value = "用例更新")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("updateTestCase")//@RequestBody ApiTestcase api,
    public Result<Object> updateTestCase(@RequestBody HttpTestcase api) {
    	Result<Object> res=new Result<>(); 
        try {
        	if(httpServer.saveOrUpdate(api))
        		res=Result.setSuccess(api);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
    	return res;
     }
    
    @ApiOperation(value = "用例加载")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("loadTestCase")
    public Result<HttpTestcase> loadTestCase(@RequestParam(value = "caseId", required = true) Integer caseId) {
    	QueryWrapper<HttpTestcase> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(HttpTestcase::getCaseId,caseId);
		HttpTestcase apiTestcase=httpServer.getOne(wrapper, false);
		Result<HttpTestcase> res=Result.setSuccess(apiTestcase);
    	                                                                                                                                       
    	return res;
    }

    @ApiOperation(value = "用例调试")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("debugTestCase")
    public Result<Object> debugTestCase(@RequestBody HttpTestcase api) {
    	Result<Object> res=new Result<>();
    	
    	return res;
    }

}
