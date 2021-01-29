package com.techstar.testplat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autotest.data.mode.ApiTestcase;
import com.autotest.data.mode.ProjectManage;
import com.techstar.testplat.config.CodeMsg;
import com.techstar.testplat.config.Result;
import com.techstar.testplat.service.TestDataServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.apachecommons.CommonsLog;

@Api(tags = "测试项目管理")
@Validated
@CommonsLog
@RestController
@RequestMapping("PoojectManage")
public class PoojectManage {
	
	private @Autowired TestDataServiceImpl dataop;
	
	   @ApiOperation(value = "项目添加")
	    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
	    @PostMapping("addProject")
	    public Result<Object> addProject(@RequestBody ProjectManage projectInfo) {
		   Result<Object> res=new Result<>();
		   Boolean isSucc=dataop.addProject(projectInfo);
		   if(isSucc) {
			   res=Result.setSuccess(projectInfo);
		   }else
		   {
			   CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
	           res=res.fail(codeMsg);
		   }
		   return res;
	   }
	   @ApiOperation(value = "项目编辑")
	    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
	    @PostMapping("updateProject")
	    public Result<Object> updateProject(@RequestBody ProjectManage projectInfo) {
		   Result<Object> res=new Result<>();
		   Boolean isSucc=dataop.updateProject(projectInfo);
		   if(isSucc) {
			   res=Result.setSuccess(projectInfo);
		   }else
		   {
			   CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
	           res=res.fail(codeMsg);
		   }
		   return res;
	   }
	   @ApiOperation(value = "项目删除")
	    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
	    @PostMapping("deleteProject")
	    public Result<Object> deleteProject(@RequestParam int projectId) {
		   Result<Object> res=new Result<>();
		   try {
			dataop.deleteProject(projectId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
	        res=res.fail(codeMsg);
		}
		   return res;
	   }
}
