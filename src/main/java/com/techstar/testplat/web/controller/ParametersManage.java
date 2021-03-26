package com.techstar.testplat.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.autotest.data.mode.PublicVariable;
import com.autotest.data.service.impl.PublicVariableServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.techstar.testplat.common.CodeMsg;
import com.techstar.testplat.common.PageBaseInfo;
import com.techstar.testplat.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "公用参数管理")
@Validated
@RestController
@RequestMapping("ParametersManage")
public class ParametersManage {
	private @Autowired PublicVariableServiceImpl dicServer;
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "公用参数查询")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("query")
    public Result<Object> query(   		
    		@RequestParam(required = false,defaultValue="1") Integer page,
			@RequestParam(required = false,defaultValue="20") Integer size,
			@RequestParam(required = false) String name){
		
		Result<Object> res=new Result<>();
		LambdaQueryWrapper<PublicVariable> queryWrapper=new QueryWrapper<PublicVariable>().lambda();
		try {
			if(name!=null ) {
				queryWrapper.like(PublicVariable::getVarName, name);
			}
			List<PublicVariable> list=dicServer.listParam(page, size, queryWrapper);
			res=Result.setSuccess(new PageBaseInfo(list,page,list.size(),size));
		} catch (Exception e) {
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		
		return res;
	}
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "添加参数")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("save")
    public Result<Object> save(PublicVariable param){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dicServer.save(param));
		} catch (Exception e) {
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "修改参数")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("update")
    public Result<Object> update(PublicVariable dic){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dicServer.updateParam(dic));
		} catch (Exception e) {
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		return res;
	}
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "删除参数")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("remove")
    public Result<Object> remove(Integer id){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dicServer.removeParam(id));
		} catch (Exception e) {
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		return res;
	}
}
