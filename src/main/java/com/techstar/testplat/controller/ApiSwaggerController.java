package com.techstar.testplat.controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.autotest.data.mode.ApiSwagger;
import com.autotest.data.mode.ProjectManage;
import com.autotest.data.mode.PublicVariable;
import com.autotest.data.service.IApiSwaggerService;
import com.autotest.data.service.impl.PublicVariableServiceImpl;
import com.techstar.testplat.common.CodeMsg;
import com.techstar.testplat.common.PageBaseInfo;
import com.techstar.testplat.common.Result;
import com.techstar.testplat.swagger.ApiSwaggerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liangzhilin
 * @since 2020-09-04
 */
@Api(tags = "Swagger API管理")
@RequestMapping("swagger")
@RestController
//@RequestMapping("/")
public class ApiSwaggerController {
	
	@Autowired
	private ApiSwaggerService apiSwaggerService;
	@Autowired
	private PublicVariableServiceImpl publicVariableService;
	
	@ApiOperation(value = "同步swagger数据")
	@RequestMapping(value = "/insertData", method = RequestMethod.POST)
	public Result<Object> insertData(@RequestHeader("Authorization") String token) {
		Result<Object> res=new Result<>();
		Boolean isSucc=apiSwaggerService.insertData();
		if(!isSucc) {
			CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
	        res=res.fail(codeMsg);
		}
        return  res;
	}
	
	@ApiOperation(value = "查询数据")
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public Result<List<ApiSwagger>> listAll(int page, int pageSize) {
		Result<List<ApiSwagger>> res=new Result<>();
		PageBaseInfo<?> gg=apiSwaggerService.selestAll(page,pageSize);
		res=Result.setSuccess(gg);
        return res;
	}
	
	@ApiOperation(value = "debug")
	@RequestMapping(value = "/selectData", method = RequestMethod.GET)
	public List<PublicVariable> selectData(@RequestBody ProjectManage projectInfo) {
		List<PublicVariable> list=publicVariableService.SelectPublicVariable();
		for (PublicVariable publicVariable : list) {
			System.out.println(publicVariable.getVarKey()+"|"+publicVariable.getVarValue());
		}
        return list;
	}
}
