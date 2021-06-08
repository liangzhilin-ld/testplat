package com.techstar.testplat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autotest.data.mode.TestScheduled;
import com.autotest.data.mode.TestSuites;
import com.autotest.data.service.impl.TestSuitesServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.techstar.testplat.common.CodeMsg;
import com.techstar.testplat.common.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.apachecommons.CommonsLog;

@Api(tags = "用例分组模块")
@Validated
@CommonsLog
@RestController
@RequestMapping("TreeData")
public class TreeData {
	private @Autowired TestSuitesServiceImpl treeData;
	@ApiOperation(value = "添加")
	@ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
	@PostMapping("add")
	public Result<Object> add(@RequestBody TestSuites tree) {
    	Result<Object> res=new Result<>();
        try {
        	treeData.save(tree);
        } catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}    	
    	return res;		

	}
	@ApiOperation(value = "查询")
	@ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
	@GetMapping("query")
	public Result<Object> query(String type,Integer pid) {
    	Result<Object> res=new Result<>();
        try {
        	QueryWrapper<TestSuites> wrapper=new QueryWrapper<>();
        	wrapper.lambda().select(TestSuites::getId,TestSuites::getName,TestSuites::getParentId)
        					.eq(TestSuites::getType, type)
        				    .eq(TestSuites::getProjectId, pid);
        	res=Result.setSuccess(treeData.list(wrapper));
        } catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}    	
    	return res;		

	}
}
