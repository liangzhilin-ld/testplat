package com.techstar.testplat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autotest.data.mode.HttpTestcase;
import com.autotest.data.mode.ScenarioTestcase;
import com.autotest.data.service.impl.HttpTestcaseServiceImpl;
import com.autotest.data.service.impl.ScenarioTestcaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.techstar.testplat.common.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
}
