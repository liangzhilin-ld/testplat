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
        	//保存后置处理器
        	if(api.getTcPost().equals("1")) {
        		ProcessorJson postjson=BeanUtil.mapToBean(api.getPostProcessor(), ProcessorJson.class, true);
        		postjson.setCaseId(caseID);
        		jsonServer.save(postjson);
        	}
        	if(api.getTcPost().equals("2")) {
        		Beanshell shell =BeanUtil.mapToBean(api.getPostProcessor(), Beanshell.class, true);
        		shell.setCaseId(caseID);
        		shellServer.save(shell);
        	}
        	if(api.getTcPost().equals("3")) {
        		ProcessorJdbc preJdbc=BeanUtil.mapToBean(api.getPostProcessor(), ProcessorJdbc.class, true);
        		preJdbc.setCaseId(caseID);
        		pocessorJdbcser.save(preJdbc);
        	}
        	//断言保存
        	if(api.getCheckPoint().equals("1")) {
        		AssertJson assertBean=BeanUtil.mapToBean(api.getAssertions(), AssertJson.class, true);
        		assertBean.setCaseId(caseID);
        		assertServer.save(assertBean);
        	}
        	if(api.getCheckPoint().equals("2")) {
        		Beanshell shell=BeanUtil.mapToBean(api.getAssertions(), Beanshell.class, true);
        		shell.setCaseId(caseID);
        		shellServer.save(shell);
        	}
        	if(api.getCheckPoint().equals("3")) {
        		AssertResponse response=BeanUtil.mapToBean(api.getAssertions(), AssertResponse.class, true);
        		response.setCaseId(caseID);
        		responServer.save(response);
        	}
        	if(api.getConfigElement().equals("1")) {
        		ApiHeader headers=BeanUtil.mapToBean(api.getAssertions(), ApiHeader.class, true);
        	}
        	if(api.getConfigElement().equals("2")) {
        		
        	}
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
}
