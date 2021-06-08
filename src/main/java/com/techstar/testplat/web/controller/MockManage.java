package com.techstar.testplat.web.controller;

import io.swagger.annotations.*;
import lombok.extern.apachecommons.CommonsLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.autotest.data.mode.*;
import com.autotest.data.mode.assertions.AssertEntity;
import com.autotest.data.mode.processors.PostProcessors;
import com.autotest.data.mode.processors.PreProcessors;
import com.autotest.data.service.impl.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.autotest.data.service.impl.ApiTestcaseServiceImpl;
//import com.autotest.data.service.impl.AssertJsonServiceImpl;
//import com.autotest.data.service.impl.BeanshellServiceImpl;
//import com.autotest.data.service.impl.ProcessorJdbcServiceImpl;
//import com.autotest.data.service.impl.ProcessorJsonServiceImpl;
import com.techstar.testplat.common.CodeMsg;
import com.techstar.testplat.common.PageBaseInfo;
import com.techstar.testplat.common.Result;

@Api(tags = "Mock数据管理")
@Validated
@CommonsLog
@RestController
@RequestMapping("MockManage")
public class MockManage{
	private @Autowired ApiMockServiceImpl httpServer;
	
    @ApiOperation(value = "mock添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addMock")//@RequestBody ApiTestcase api,
    public Result<Object> addMock(@RequestBody ApiMock api) {
    	Result<Object> res=new Result<>();
        try {
        	if(httpServer.save(api))
        		res=Result.setSuccess(api);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
       
    	return res;
    }
    
    
    
    @ApiOperation(value = "Mock更新")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("updateAddMock")//@RequestBody ApiTestcase api,
    public Result<Object> updateAddMock(@RequestBody ApiMock api) {
    	Result<Object> res=new Result<>(); 
        try {
        	if(httpServer.saveOrUpdate(api))
        		res=Result.setSuccess(api);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
    	return res;
     }
    
    @ApiOperation(value = "Mock删除")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("deleteMock")
    public Result<ApiMock> deleteMock(@RequestParam(value = "caseId", required = true) Integer id) {
    	QueryWrapper<ApiMock> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(ApiMock::getId,id);
		Boolean apiTestcase=httpServer.remove(wrapper);
		Result<ApiMock> res=Result.setSuccess(apiTestcase);
    	                                                                                                                                       
    	return res;
    }

    
    @ApiOperation(value = "Mock查询")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("query")
    public Result<List<ApiMock>> query(@RequestParam(required = false,defaultValue="1") Integer page,
								@RequestParam(required = false,defaultValue="20") Integer size,
								@RequestParam(required = false) String uri,
								@RequestParam(required = false) String name) {
 
    	QueryWrapper<ApiMock> wrapper = new QueryWrapper<>();
    	if(name!=null) {
    		wrapper.lambda().like(ApiMock::getName, name);
    	}
    	if(uri!=null) {
    		wrapper.lambda().like(ApiMock::getUrl, uri);
    	}
    	IPage<ApiMock> ipage = new Page<>(page, size);
    	ipage=httpServer.page(ipage, wrapper);
		List<ApiMock> apiTestcase=ipage.getRecords();
		Result<List<ApiMock>> res=Result.setSuccess(new PageBaseInfo(apiTestcase,page,ipage.getTotal(),size));
    	return res;
    }
}
