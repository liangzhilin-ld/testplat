package com.techstar.testplat.web.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autotest.data.mode.ApiSwagger;
import com.autotest.data.mode.ProjectManage;
import com.autotest.data.mode.PublicVariable;
import com.autotest.data.mode.custom.SwaggerInfo;
import com.autotest.data.service.IApiSwaggerService;
import com.autotest.data.service.impl.PublicVariableServiceImpl;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.techstar.testplat.common.CodeMsg;
import com.techstar.testplat.common.PageBaseInfo;
import com.techstar.testplat.common.Result;
import com.techstar.testplat.swagger.ApiSwaggerService;
import com.techstar.testplat.web.bean.APIRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
	
	@ApiOperation(value = "同步项目所有swagger url数据")
	@RequestMapping(value = "/pullAllSwagger", method = RequestMethod.POST)
	public Result<Object> pullAllSwagger() {
		//@RequestHeader("Authorization") String token
		Result<Object> res=new Result<>();
		Boolean isSucc=apiSwaggerService.pullSwaggerData();
		if(!isSucc) {
			CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
	        res=res.fail(codeMsg);
		}
        return  res;
	}
	
	@ApiOperation(value = "同步单个url")
	@RequestMapping(value = "/pullSingleSwagger", method = RequestMethod.POST)
	public Result<Object> pullSingleSwagger(Integer pid,@RequestBody SwaggerInfo swg) {
		//@RequestHeader("Authorization") String token
		Result<Object> res=new Result<>();
		Boolean isSucc=apiSwaggerService.pullSwaggerByHander(pid, swg);
		if(!isSucc) {
			CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
	        res=res.fail(codeMsg);
		}
        return  res;
	}
	//@RequestParam(required =false) String name
	@ApiOperation(value = "列表查询")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<List<ApiSwagger>> list(@RequestParam(required = false,defaultValue="1") Integer page,
											@RequestParam(required = false,defaultValue="20") Integer size,
											@RequestParam(required = false) String moduleName,
											@RequestParam(required = false) String id,
											@RequestParam(required = false) String url,
											@RequestParam(required = false) String name) {
		Result<List<ApiSwagger>> res=new Result<>();
		LambdaQueryWrapper<ApiSwagger> queryWrapper=new QueryWrapper<ApiSwagger>().lambda();
		if(id!=null )
			queryWrapper.eq(ApiSwagger::getId, id);
		if(moduleName!=null )
			queryWrapper.eq(ApiSwagger::getModuleName, moduleName);
		if(url!=null )
			queryWrapper.like(ApiSwagger::getApiUri, url);
		if(name!=null )
			queryWrapper.like(ApiSwagger::getApiDesc, name);
		PageBaseInfo pb=apiSwaggerService.selestAll(page,size,queryWrapper);
		res=Result.setSuccess(pb);
        return res;
	}
	
	/**
	 * 创建测试用例调用，将返回数据直接填充至页面对控件
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据id查询")
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	public Result<ApiSwagger> queryById(String id) {
		Result<ApiSwagger> res=new Result<>();
		ApiSwagger api=apiSwaggerService.selestById(id);
		res=Result.setSuccess(api);
        return res;
	}
	
	@ApiOperation(value = "debug")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("selectData")
	public Result<List<PublicVariable>> selectData(@RequestBody APIRequest request) {
		QueryWrapper<PublicVariable> queryWrapper=new QueryWrapper();
		Map<String , Object> params = new HashMap<>(); 
		params.put("VAR_KEY", "__randomStr");
		queryWrapper.allEq(params);
		List<PublicVariable> test=publicVariableService.list(queryWrapper);
		List<PublicVariable> list=publicVariableService.SelectPublicVariable();
		for (PublicVariable publicVariable : list) {
			System.out.println(publicVariable.getVarKey()+"|"+publicVariable.getVarValue());
		}
		Result<List<PublicVariable>> res=Result.setSuccess(list);
        return res;
	}		
	@ApiOperation(value = "获取分组模块")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("findByGroup")
	public Result<Object> findByGroup(Integer projectId) {
		Result<Object> res=Result.setSuccess(apiSwaggerService.getModuleByProject(projectId));
        return res;
	}
}
