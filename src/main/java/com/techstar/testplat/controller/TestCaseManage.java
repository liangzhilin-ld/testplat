package com.techstar.testplat.controller;

import io.swagger.annotations.*;
import lombok.extern.apachecommons.CommonsLog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
import com.autotest.data.mode.custom.AssertEntity;
import com.autotest.data.mode.custom.ConfElementEntity;
import com.autotest.data.mode.custom.PostProcessorsEntity;
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
	private @Autowired ProcessorJsonServiceImpl jsonServer;
	private @Autowired AssertJsonServiceImpl assertServer;
	private @Autowired AssertJsonServiceImpl assertJson;
	private @Autowired AssertResponseServiceImpl responServer;
	private @Autowired ApiHeaderServiceImpl headerServer;
	private @Autowired UserDefinedVariableServiceImpl userDefineServer;
    @ApiOperation(value = "用例添加")
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
        	PostProcessorsEntity postProcessor=api.getPostProcessor();
        	List<Beanshell> listBeanshell=postProcessor.getPostBeanshell();
        	List<ProcessorJdbc> postJdbcs=postProcessor.getPostJdbc();
        	List<ProcessorJson> postJson=postProcessor.getPostJson();
//        	Field[] field = postProcessor.getClass().getDeclaredFields();
//        	for (int i = 0; i < field.length; i++) {
//        		String name = field[i].getName();
//        		name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
//        		Object type = field[i].getGenericType();
//        		try {
//					Object value = postProcessor.getClass().getMethod("get" + name).invoke(postProcessor);
//					Boolean flasu=true;
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalArgumentException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (NoSuchMethodException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (SecurityException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//        		
//        	}

        	if(postJson!=null&&postJson.size()>0) {
        		for (ProcessorJson postjson : postJson) {
            		postjson.setCaseId(caseID);
            		jsonServer.save(postjson);
    			}
        	}
        	if(listBeanshell!=null&&listBeanshell.size()>0) {
        		for (Beanshell shell : listBeanshell) {
            		shell.setCaseId(caseID);
            		shellServer.save(shell);
    			}
        	}
        	if(postJdbcs!=null&&postJdbcs.size()>0) {
        		for (ProcessorJdbc ptJdbc : postJdbcs) {
            		ptJdbc.setCaseId(caseID);
            		pocessorJdbcser.save(ptJdbc);
    			}
        	}
        	
        	//断言
        	AssertEntity asserts=api.getAssertions();
        	List<Beanshell> listShell=asserts.getAssertBeanshell();
        	List<AssertJson> listAsertJson=asserts.getAssertJson();
        	List<AssertResponse> listAssertRes=asserts.getAssertResponse();
        	if(listShell!=null&&listShell.size()>0) {
        		for (Beanshell shell : listShell) {
        			shell.setCaseId(caseID);
            		shellServer.save(shell);
				}
        	}
        	if(listAsertJson!=null&&listAsertJson.size()>0) {
        		for (AssertJson assJ : listAsertJson) {
        			assJ.setCaseId(caseID);
            		assertJson.save(assJ);
				}
        	}
        	if(listAssertRes!=null&&listAssertRes.size()>0) {
        		for (AssertResponse asR : listAssertRes) {
        			asR.setCaseId(caseID);
    				responServer.save(asR);
				}
        	}

        	//配置元件
        	ConfElementEntity configElements=api.getConfElements();
        	List<ApiHeader> apiHeaders=configElements.getHeaders();
        	List<UserDefinedVariable> apiUserDefined=configElements.getUserDefinedVar();
        	if(apiHeaders!=null&&apiHeaders.size()>0) {
        		for (ApiHeader head : apiHeaders) {
        			head.setCaseId(caseID);
        			headerServer.save(head);
				}
        	}
        	if(apiUserDefined!=null&&apiUserDefined.size()>0) {
        		for (UserDefinedVariable var : apiUserDefined) {
        			var.setCaseId(caseID);
        			userDefineServer.save(var);
				}
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
    @ApiOperation(value = "用例添加2")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addTestCase2")
    public Result<Object> addTestCase2(@RequestBody AssertResponse api) {
    	Result<Object> res=new Result<>();
    	return res;
    }
}
