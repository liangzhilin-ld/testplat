package com.techstar.testplat.controller;

import io.swagger.annotations.*;
import lombok.extern.apachecommons.CommonsLog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.autotest.data.mode.*;
import com.autotest.data.mode.custom.*;
import com.autotest.data.service.impl.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.autotest.data.service.impl.ApiTestcaseServiceImpl;
//import com.autotest.data.service.impl.AssertJsonServiceImpl;
//import com.autotest.data.service.impl.BeanshellServiceImpl;
//import com.autotest.data.service.impl.ProcessorJdbcServiceImpl;
//import com.autotest.data.service.impl.ProcessorJsonServiceImpl;
import com.techstar.testplat.config.CodeMsg;
import com.techstar.testplat.config.Result;
import com.techstar.testplat.service.TestDataServiceImpl;

@Api(tags = "用例管理")
@Validated
@CommonsLog
@RestController
@RequestMapping("TestCaseManage")
public class TestCaseManage{
//	private @Autowired TestDataServiceImpl dataOp;
//	private @Autowired BeanshellServiceImpl shellServer;
//	private @Autowired ProcessorJdbcServiceImpl pocessorJdbcser;
//	private @Autowired ApiTestcaseServiceImpl caseServer;
	private @Autowired HttpTestcaseServiceImpl httpServer;
//	private @Autowired AssertionsServiceImpl assertServer;
//	private @Autowired PostProcessorsServiceImpl postProcessorServer;
//	private @Autowired PreProcessorsServiceImpl preProcessorServer;
//	private @Autowired ProcessorJsonServiceImpl jsonServer;
//	private @Autowired AssertJsonServiceImpl assertJson;
//	private @Autowired AssertResponseServiceImpl responServer;
//	private @Autowired ApiHeaderServiceImpl headerServer;
//	private @Autowired UserDefinedVariableServiceImpl userDefineServer;
//	private @Autowired ApiMockServiceImpl mockServer;
//	private @Autowired ConfigElementServiceImpl configElementServer;
	
    @ApiOperation(value = "用例添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addTestCase")//@RequestBody ApiTestcase api,
    public Result<Object> addTestCase(@RequestBody HttpTestcase api) {
    	Result<Object> res=new Result<>();
        try {
        	if(httpServer.save(api))
        		res=Result.setSuccess(api);
//        	UpdateWrapper<HttpRequest> updateWrapper=new UpdateWrapper<HttpRequest>();
//        	PreProcessors preProcessors=api2.getPreProcessor();
//        	if(preProcessorServer.addPreProcessors(preProcessors, api2.getCaseId()))
//        		updateWrapper.lambda().set(HttpRequest::getApiPre, preProcessors.getId());
//        	
//        	PostProcessors postProcessors=api2.getPostProcessor();
//        	if(postProcessorServer.addPostProcessors(postProcessors, api2.getCaseId()))
//        		updateWrapper.lambda().set(HttpRequest::getTcPost, postProcessors.getId());
//        	
//        	Assertions asserts=api2.getAssertions();
//        	if(assertServer.addAssertions(asserts, api2.getCaseId()))
//        		updateWrapper.lambda().set(HttpRequest::getCheckPoint, asserts.getId());
//        	
//        	ConfigElement configElement=api2.getConfElements();
//        	if(configElementServer.addConfigElements(configElement, api2.getCaseId()))
//        		updateWrapper.lambda().set(HttpRequest::getConfigElement, configElement.getId());
        	
    //    	UpdateWrapper<HttpRequest> updateWrapper=new UpdateWrapper<HttpRequest>();
//        	updateWrapper.lambda().set(HttpRequest::getApiPre, preProcessors.getId())
//        							.set(HttpRequest::getTcPost, postProcessors.getId())
//        							.set(HttpRequest::getCheckPoint, asserts.getId())
//        							.set(HttpRequest::getConfigElement, configElement.getId())
//        							.eq(HttpRequest::getCaseId, api2.getCaseId());
//        	
//        	if(updateWrapper.lambda().getSqlSet().contains("set ")) {
//        		updateWrapper.lambda().eq(HttpRequest::getCaseId, api2.getCaseId());
//            	httpServer.update(updateWrapper);
//        	}
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
//        	PreProcessors preProcessors=api2.getPreProcessor();
//        	if(preProcessors!=null)
//        		preProcessorServer.updateById(preProcessors);
//        	PostProcessors postProcessors=api2.getPostProcessor();
//        	postProcessorServer.updateById(postProcessors);
//        	Assertions asserts=api2.getAssertions();
//        	assertServer.updateById(asserts);
//        	ConfigElement configElement=api2.getConfElements();
//        	configElementServer.updateById(configElement);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
    	return res;
     }
    
//    @ApiOperation(value = "后置处理器更新")
//    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
//    @PostMapping("updatePostProcessors")
//    public Result<Object> updatePostProcessors(@RequestBody PostProcessorsEntity postProcessors) {
//    	
//    	Result<Object> res=new Result<>();
//    	return res;
//    }
    
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
