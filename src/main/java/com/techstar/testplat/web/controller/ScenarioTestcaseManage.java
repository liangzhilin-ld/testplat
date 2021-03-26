package com.techstar.testplat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autotest.data.mode.ApiSwagger;
import com.autotest.data.mode.HttpTestcase;
import com.autotest.data.mode.ScenarioReport;
import com.autotest.data.mode.ScenarioTestcase;
import com.autotest.data.service.impl.HttpTestcaseServiceImpl;
import com.autotest.data.service.impl.ScenarioTestcaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.techstar.testplat.common.CodeMsg;
import com.techstar.testplat.common.PageBaseInfo;
import com.techstar.testplat.common.Result;
import com.techstar.testplat.web.utils.HttpHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "场景管理")
@Validated
@RestController
@RequestMapping("Scenario")

public class ScenarioTestcaseManage {
	
	
	private @Autowired ScenarioTestcaseServiceImpl scenariServer;
	private @Autowired HttpTestcaseServiceImpl httpServer;
	
	@ApiOperation(value = "场景添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addScenario")
    public Result<Object> addScenario(@RequestBody ScenarioTestcase api) {
    	Result<Object> res=new Result<>();
    	scenariServer.save(api);
    	res=Result.setSuccess(api);
    	return res;
    }
	@ApiOperation(value = "场景加载")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("loadScenario")
    public Result<Object> loadScenario(@RequestParam int scenarioId) {
    	Result<Object> res=new Result<>();
    	QueryWrapper<ScenarioTestcase> queryWrapper=new QueryWrapper<>();
    	queryWrapper.lambda().eq(ScenarioTestcase::getId, scenarioId);
    	ScenarioTestcase scenario=scenariServer.getOne(queryWrapper);
    	res=Result.setSuccess(scenario);
    	return res;
    }
	
    @ApiOperation(value = "场景引用用例加载")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("loadScenarioTestCase")
    public Result<HttpTestcase> loadScenarioTestCase(@RequestParam(value = "caseId", required = true) List<Integer> caseId) {
    	QueryWrapper<HttpTestcase> wrapper = new QueryWrapper<>();
		wrapper.lambda().in(HttpTestcase::getCaseId, caseId);
		List<HttpTestcase> apiTestcase=httpServer.list(wrapper);
		Result<HttpTestcase> res=Result.setSuccess(apiTestcase);
    	
    	return res;
    }
    
    
    @ApiOperation(value = "场景查询")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("query")
    public Result<ScenarioTestcase> query(
    		@RequestParam(value = "page", required = false,defaultValue="1") long page,
    		@RequestParam(value = "size", required = false,defaultValue="20") long size,
    		@RequestParam(value = "name", required = false) String name,
    		@RequestParam(value = "type", required = false) String type,
    		@RequestParam(value = "prio", required = false) String prio,
    		@RequestParam(value = "director", required = false) String director) {
    	QueryWrapper<ScenarioTestcase> wrapper = new QueryWrapper<>();
    	if(name!=null) {
    		wrapper.lambda().like(ScenarioTestcase::getScenarioName, name);
    	}
    	if(type!=null) {
    		wrapper.lambda().like(ScenarioTestcase::getType, type);
    	}
    	if(prio!=null) {
    		wrapper.lambda().like(ScenarioTestcase::getPriority, prio);
    	}
    	if(director!=null) {
    		wrapper.lambda().like(ScenarioTestcase::getScenarioName, director);
    	}
    	IPage<ScenarioTestcase> ipage = new Page<>(page, size);
    	ipage=scenariServer.page(ipage, wrapper);
		List<ScenarioTestcase> apiTestcase=ipage.getRecords();
		Result<ScenarioTestcase> res=Result.setSuccess(new PageBaseInfo(apiTestcase,page,ipage.getTotal(),size));
		
    	
    	return res;
    }
    
    @ApiOperation(value = "场景调试")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("debug")
    public Result<ScenarioReport> debug(
    		@ApiParam(name="env",value = "itmg: http://172.16.206.127:31100",required = true)
    		@RequestParam(value = "env", required = true) String env,
			@RequestBody ScenarioTestcase api) {
    	Result<ScenarioReport> res=new Result<>();
    	try {
    		String httpres=new HttpHelper("/debugScenario",env).post(api);
    		res=Result.setSuccess(httpres);
        	return res;
		} catch (Exception e) {
			CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
	        codeMsg.setMsg(e.getMessage());
	        res.setData(null);
	        res=res.fail(codeMsg);
	        return res;
		}
    	
    }
}
