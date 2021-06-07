package com.techstar.testplat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.autotest.data.mode.SyetemDictionary;
import com.autotest.data.service.impl.SyetemDictionaryServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.techstar.testplat.common.CodeMsg;
import com.techstar.testplat.common.PageBaseInfo;
import com.techstar.testplat.common.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Api(tags = "系统字典管理")
@Validated
@RestController
@RequestMapping("DictionaryManage")
public class DictionaryManage {
	private @Autowired SyetemDictionaryServiceImpl dicServer;
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "字典查询")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("listDictionary")
    public Result<Object> listDictionary(   		
    		@RequestParam(required = false,defaultValue="1") Integer page,
			@RequestParam(required = false,defaultValue="20") Integer size,
			@RequestParam(required = false) String desc){
		
		Result<Object> res=new Result<>();
		LambdaQueryWrapper<SyetemDictionary> queryWrapper=new QueryWrapper<SyetemDictionary>().lambda();
		try {
			if(desc!=null ) {
				queryWrapper.like(SyetemDictionary::getDescrible, desc);
			}
			List<SyetemDictionary> list=dicServer.listDic(page, size, queryWrapper);
			res=Result.setSuccess(new PageBaseInfo(list,page,list.size(),size));
		} catch (Exception e) {
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		
		return res;
	}
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "添加字典")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("save")
    public Result<Object> save(List<SyetemDictionary> dic){
		Result<Object> res=new Result<>();
		try {
			int maxId=dicServer.getBaseMapper().selectMaxId()+1;
			dic.forEach(item->item.setFunctionalId(maxId));
			res=Result.setSuccess(dicServer.saveBatch(dic));
		} catch (Exception e) {
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "修改字典")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("update")
    public Result<Object> update(List<SyetemDictionary> dic){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dicServer.updateDic(dic));
		} catch (Exception e) {
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		return res;
	}
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "删除字典")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("remove")
    public Result<Object> remove(Integer id){
		Result<Object> res=new Result<>();
		try {
			res=Result.setSuccess(dicServer.removeDic(id));
		} catch (Exception e) {
            CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
		}
		return res;
	}
}
