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
import com.autotest.data.mode.ApiMock;
import com.autotest.data.mode.ApiReport;
import com.autotest.data.mode.ApiTestcase;
import com.autotest.data.mode.Beanshell;
import com.autotest.data.mode.ProcessorJdbc;
import com.autotest.data.mode.TestScheduled;
import com.autotest.data.service.IApiTestcaseService;
import com.autotest.data.service.impl.ApiMockServiceImpl;
import com.autotest.data.service.impl.ApiTestcaseServiceImpl;
import com.autotest.data.service.impl.BeanshellServiceImpl;
import com.autotest.data.service.impl.ProcessorJdbcServiceImpl;
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
	
    @ApiOperation(value = "计划任务添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addTestCase")
    public Result<Object> addTestCase(@RequestBody ApiTestcase api) {
    	Result<Object> res=new Result<>();
    	caseServer.save(api);
    	if(api.getApiPre().equals("1")) {
    		String caseid=api.getPreCases();
    	}
    	if(api.getApiPre().equals("2")) { 
    		Beanshell shell =BeanUtil.mapToBean(api.getPreProcessor(), Beanshell.class, true);
    		shellServer.save(shell);
    	}
    	if(api.getApiPre().equals("3")) {    		
    		ProcessorJdbc preJdbc=BeanUtil.mapToBean(api.getPreProcessor(), ProcessorJdbc.class, true);
    		pocessorJdbcser.save(preJdbc);
    	}
    	if(api.getApiPre().equals("4")) {    		
    		ApiMock entity=BeanUtil.mapToBean(api.getPreProcessor(), ApiMock.class, true);
    		mockserver.save(entity);
    	}
    	
    	
    	
    	
        //log.info("getMemberSmallVO:" + api);
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
