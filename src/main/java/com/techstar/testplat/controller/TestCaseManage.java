package com.techstar.testplat.controller;

import io.swagger.annotations.*;
import lombok.extern.apachecommons.CommonsLog;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.autotest.data.mapper.ApiTestcaseMapper;
import com.autotest.data.mode.*;
import com.autotest.data.service.IApiTestcaseService;
import com.autotest.data.service.impl.*;
//import com.autotest.data.service.impl.ApiTestcaseServiceImpl;
//import com.autotest.data.service.impl.AssertJsonServiceImpl;
//import com.autotest.data.service.impl.BeanshellServiceImpl;
//import com.autotest.data.service.impl.ProcessorJdbcServiceImpl;
//import com.autotest.data.service.impl.ProcessorJsonServiceImpl;
import com.techstar.testplat.config.CodeMsg;
import com.techstar.testplat.config.Result;
import com.techstar.testplat.service.TestDataServiceImpl;

import cn.hutool.core.bean.BeanUtil;

@Api(tags = "用例管理")
@Validated
@CommonsLog
@RestController
@RequestMapping("TestCaseManage")
public class TestCaseManage{
	private @Autowired TestDataServiceImpl dataOp;
	private @Autowired BeanshellServiceImpl shellServer;
	private @Autowired ApiMockServiceImpl mockserver;
	private @Autowired ProcessorJdbcServiceImpl pocessorJdbcser;
	private @Autowired IApiTestcaseService caseServer;
	private @Autowired IApiTestcaseService casemapper;
	private @Autowired ProcessorJsonServiceImpl jsonServer;
	private @Autowired AssertJsonServiceImpl assertServer;
	private @Autowired AssertJsonServiceImpl assertJson;
	private @Autowired AssertResponseServiceImpl responServer;
    @ApiOperation(value = "计划任务添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addTestCase")
    public Result<Object> addTestCase(@RequestBody ApiTestcase api) {
    	Result<Object> res=new Result<>();
    	if(caseServer.save(api)) {
    		int caseID=api.getCaseId();
    		//保存前置处理器
    		if(api.getApiPre().equals("1")) {
        		String caseid=api.getPreCases();
        	}
        	if(api.getApiPre().equals("2")) { 
        		Beanshell shell =BeanUtil.mapToBean(api.getPreProcessor(), Beanshell.class, true);
        		shell.setCaseId(caseID);
        		shellServer.save(shell);
        	}
        	if(api.getApiPre().equals("3")) {    		
        		ProcessorJdbc preJdbc=BeanUtil.mapToBean(api.getPreProcessor(), ProcessorJdbc.class, true);
        		preJdbc.setCaseId(caseID);
        		pocessorJdbcser.save(preJdbc);
        	}
        	if(api.getApiPre().equals("4")) {    		
        		ApiMock entity=BeanUtil.mapToBean(api.getPreProcessor(), ApiMock.class, true);
        		entity.setCaseId(caseID);
        		if(entity.getApiPre().equals("YES")) {
        			Beanshell shell=BeanUtil.mapToBean(entity.getBeanShell(), Beanshell.class, true);
        			shell.setCaseId(caseID);
        			shellServer.save(shell);
        		}
        		mockserver.save(entity);
        	}
        	//保存后置处理器api.getTcPost().equals("1")，满足processorType==2
        	if(api.getPostProcessor().size()>0) {
        		for (Map<?, ?> postStr : api.getPostProcessor()) {

        			if(postStr.get("suffixAll")!=null) {
        				ProcessorJson postjson=BeanUtil.mapToBean(postStr, ProcessorJson.class, true);
                		postjson.setCaseId(caseID);
                		jsonServer.save(postjson);
                		continue;
        			}
        			if(postStr.get("script")!=null) {
                		Beanshell shell =BeanUtil.mapToBean(postStr, Beanshell.class, true);
                		shell.setCaseId(caseID);
                		shellServer.save(shell);
                		continue;
        			}
        			if(postStr.get("variableNamePool")!=null) {
        				ProcessorJdbc preJdbc=BeanUtil.mapToBean(postStr, ProcessorJdbc.class, true);
                		preJdbc.setCaseId(caseID);
                		pocessorJdbcser.save(preJdbc);
        			}
				}
        		
        	}
        	if(api.getAssertions().size()>0) {
        		for (Map<?, ?> asserts : api.getAssertions()) {
        			if(asserts.get("jsonPath")!=null) {
        				AssertJson assJ=BeanUtil.mapToBean(asserts, AssertJson.class, true);
        				assJ.setCaseId(caseID);
                		assertJson.save(assJ);
                		continue;
        			}
        			
        			if(asserts.get("script")!=null) {
        				Beanshell shell =BeanUtil.mapToBean(asserts, Beanshell.class, true);
                		shell.setCaseId(caseID);
                		shellServer.save(shell);
                		continue;
        			}
        			
        			if(asserts.get("testString")!=null) {
        				AssertResponse asR =BeanUtil.mapToBean(asserts, AssertResponse.class, true);
        				asR.setCaseId(caseID);
        				responServer.save(asR);
        			}
        			
        		}
        	}

        	//配置元件
//        	if(api.getConfigElement().equals("1")) {
//        		ApiHeader headers=BeanUtil.mapToBean(api.getAssertions(), ApiHeader.class, true);
//        	}
//        	if(api.getConfigElement().equals("2")) {
//        		
//        	}
    	}
    	
    	
        try {
        	
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
    	return res;
    }
    @ApiOperation(value = "计划任务添加2")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addTestCase2")
    public Result<Object> addTestCase2(@RequestBody AssertResponse api) {
    	Result<Object> res=new Result<>();
    	return res;
    }
}
