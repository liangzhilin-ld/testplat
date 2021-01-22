package com.techstar.testplat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autotest.data.mode.SyetemDb;
import com.autotest.data.mode.SyetemEnv;
import com.autotest.data.service.impl.SyetemDictionaryServiceImpl;
import com.techstar.testplat.config.CodeMsg;
import com.techstar.testplat.config.Result;
import com.techstar.testplat.service.TestDataServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.apachecommons.CommonsLog;

@Api(tags = "系统配置管理")
@Validated
@CommonsLog
@RestController
@RequestMapping("SystemManage")
public class SystemManage {
	
	private @Autowired SyetemDictionaryServiceImpl dicServer;
	private @Autowired TestDataServiceImpl dataOp;
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "系统字典表")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("getDictionary")
    public Result<Object> getDictionary(){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dicServer.list());
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		
		return res;
	}
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "测试环境地址添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addTestEnv")
    public Result<Object> addTestEnv(@RequestBody SyetemEnv env){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dataOp.addSyetemEnv(env));
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		
		return res;
	}
	
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "测试环境地址编辑")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
	@PostMapping("updateTestEnv")
    public Result<Object> updateTestEnv(@RequestBody SyetemEnv env){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dataOp.updateSyetemEnv(env));
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "测试环境删除")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
	@PostMapping("deleteTestEnv")
    public Result<Object> deleteTestEnv(@RequestParam int envId){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dataOp.deleteSyetemEnv(envId));
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		
		return res;
	}
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "数据库地址添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
	@PostMapping("addTestDb")
    public Result<Object> addTestDb(@RequestBody SyetemDb env){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dataOp.addSyetemDb(env));
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "数据库地址编辑")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
	@PostMapping("updateSyetemDb")
    public Result<Object> updateSyetemDb(@RequestBody SyetemDb env){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dataOp.updateSyetemDb(env));
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "数据库地址删除")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
	@PostMapping("deleteSyetemDb")
    public Result<Object> deleteSyetemDb(@RequestParam int connId){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dataOp.deleteSyetemDb(connId));
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		
		return res;
	}
}
