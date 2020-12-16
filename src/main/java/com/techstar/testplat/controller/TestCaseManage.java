package com.techstar.testplat.controller;

import io.swagger.annotations.*;
import lombok.extern.apachecommons.CommonsLog;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.autotest.data.mode.*;
import com.autotest.data.mode.custom.*;
import com.autotest.data.service.impl.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.autotest.data.service.impl.ApiTestcaseServiceImpl;
//import com.autotest.data.service.impl.AssertJsonServiceImpl;
//import com.autotest.data.service.impl.BeanshellServiceImpl;
//import com.autotest.data.service.impl.ProcessorJdbcServiceImpl;
//import com.autotest.data.service.impl.ProcessorJsonServiceImpl;
import com.techstar.testplat.config.CodeMsg;
import com.techstar.testplat.config.Result;
import com.techstar.testplat.service.TestDataServiceImpl;

@Api(tags = "用例管理")
@Validated
@CommonsLog
@RestController
@RequestMapping("TestCaseManage")
public class TestCaseManage{
	private @Autowired TestDataServiceImpl dataOp;
	private @Autowired BeanshellServiceImpl shellServer;
	private @Autowired ProcessorJdbcServiceImpl pocessorJdbcser;
	private @Autowired ApiTestcaseServiceImpl caseServer;
	private @Autowired ProcessorJsonServiceImpl jsonServer;
	private @Autowired AssertJsonServiceImpl assertJson;
	private @Autowired AssertResponseServiceImpl responServer;
	private @Autowired ApiHeaderServiceImpl headerServer;
	private @Autowired UserDefinedVariableServiceImpl userDefineServer;
	private @Autowired ApiMockServiceImpl mockServer;
	
    @ApiOperation(value = "用例添加")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("addTestCase")
    public Result<Object> addTestCase(@RequestBody ApiTestcase api) {
    	Result<Object> res=new Result<>();
    	if(caseServer.save(api)) {
    		int caseID=api.getCaseId();
    		//保存前置处理器
    		PreProcessorsEntity preProcessor=api.getPreProcessor();
    		
    		List<Beanshell> preBeanshells=preProcessor.getPreBeanshell();
        	List<ProcessorJdbc> preJdbcs=preProcessor.getPreJdbc();
        	List<ApiMock> preMocks=preProcessor.getPreMock();
        	if(preBeanshells!=null&&preBeanshells.size()>0) {
        		for (Beanshell shell : preBeanshells) {
        			shell.setCaseId(caseID);
            		shellServer.save(shell);
    			}
        	}
          	if(preJdbcs!=null&&preJdbcs.size()>0) {
        		for (ProcessorJdbc ptJdbc : preJdbcs) {
            		ptJdbc.setCaseId(caseID);
            		pocessorJdbcser.save(ptJdbc);
    			}
        	}
          	
          	if(preMocks!=null&&preMocks.size()>0) {
        		for (ApiMock dummy : preMocks) {
        			dummy.setCaseId(caseID);
        			mockServer.save(dummy);
    			}
        	}
//    		if(api.getApiPre().equals("1")) {
//        		String caseid=api.getPreCases();
//        	}
//        	if(api.getApiPre().equals("2")) { 
//        		Beanshell shell =BeanUtil.mapToBean(api.getPreProcessor(), Beanshell.class, true);
//        		shell.setCaseId(caseID);
//        		shellServer.save(shell);
//        	}
//        	if(api.getApiPre().equals("3")) {    		
//        		ProcessorJdbc preJdbc=BeanUtil.mapToBean(api.getPreProcessor(), ProcessorJdbc.class, true);
//        		preJdbc.setCaseId(caseID);
//        		pocessorJdbcser.save(preJdbc);
//        	}
//        	if(api.getApiPre().equals("4")) {    		
//        		ApiMock entity=BeanUtil.mapToBean(api.getPreProcessor(), ApiMock.class, true);
//        		entity.setCaseId(caseID);
//        		if(entity.getApiPre().equals("YES")) {
//        			Beanshell shell=BeanUtil.mapToBean(entity.getBeanShell(), Beanshell.class, true);
//        			shell.setCaseId(caseID);
//        			shellServer.save(shell);
//        		}
//        		mockserver.save(entity);
//        	}
        	//保存后置处理器api.getTcPost().equals("1")，满足processorType==2
        	PostProcessorsEntity postProcessor=api.getPostProcessor();
        	List<Beanshell> listBeanshell=postProcessor.getPostBeanshell();
        	List<ProcessorJdbc> postJdbcs=postProcessor.getPostJdbc();
        	List<ProcessorJson> postJson=postProcessor.getPostJson();
        	if(postJson!=null&&postJson.size()>0) {
        		for (ProcessorJson postjson : postJson) {
            		postjson.setCaseId(caseID);
            		jsonServer.save(postjson);
    			}
        	}
        	if(listBeanshell!=null&&listBeanshell.size()>0) {
        		for (Beanshell shell : listBeanshell) {
            		shell.setCaseId(caseID);
            		shellServer.save(shell);
    			}
        	}
        	if(postJdbcs!=null&&postJdbcs.size()>0) {
        		for (ProcessorJdbc ptJdbc : postJdbcs) {
            		ptJdbc.setCaseId(caseID);
            		pocessorJdbcser.save(ptJdbc);
    			}
        	}
        	
        	//断言
        	AssertEntity asserts=api.getAssertions();
        	List<Beanshell> listShell=asserts.getAssertBeanshell();
        	List<AssertJson> listAsertJson=asserts.getAssertJson();
        	List<AssertResponse> listAssertRes=asserts.getAssertResponse();
        	if(listShell!=null&&listShell.size()>0) {
        		for (Beanshell shell : listShell) {
        			shell.setCaseId(caseID);
            		shellServer.save(shell);
				}
        	}
        	if(listAsertJson!=null&&listAsertJson.size()>0) {
        		for (AssertJson assJ : listAsertJson) {
        			assJ.setCaseId(caseID);
            		assertJson.save(assJ);
				}
        	}
        	if(listAssertRes!=null&&listAssertRes.size()>0) {
        		for (AssertResponse asR : listAssertRes) {
        			asR.setCaseId(caseID);
    				responServer.save(asR);
				}
        	}

        	//配置元件
        	ConfElementEntity configElements=api.getConfElements();
        	List<ApiHeader> apiHeaders=configElements.getHeaders();
        	List<UserDefinedVariable> apiUserDefined=configElements.getUserDefinedVar();
        	if(apiHeaders!=null&&apiHeaders.size()>0) {
        		for (ApiHeader head : apiHeaders) {
        			head.setCaseId(caseID);
        			headerServer.save(head);
				}
        	}
        	if(apiUserDefined!=null&&apiUserDefined.size()>0) {
        		for (UserDefinedVariable var : apiUserDefined) {
        			var.setCaseId(caseID);
        			userDefineServer.save(var);
				}
        	}
        	
    	}
    	
    	
        try {
        	
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
    	return res;
    }
    
    
    
    @ApiOperation(value = "用例更新")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("updateTestCase")
    public Result<Object> updateTestCase(@RequestBody ApiTestcase api) {
    	Result<Object> res=new Result<>();
    	if(caseServer.saveOrUpdate(api)) {
    		int caseID=api.getCaseId();
    		//保存前置处理器
    		PreProcessorsEntity preProcessor=api.getPreProcessor();    		
    		List<Beanshell> preBeanshells=preProcessor.getPreBeanshell();
        	List<ProcessorJdbc> preJdbcs=preProcessor.getPreJdbc();
        	List<ApiMock> preMocks=preProcessor.getPreMock();
        	if(preBeanshells!=null&&preBeanshells.size()>0) {
        		QueryWrapper<Beanshell> wrapper = new QueryWrapper<>();
        		wrapper.lambda().eq(Beanshell::getCaseId,api.getCaseId())
        						.eq(Beanshell::getBeanshellType, "1");
        		shellServer.remove(wrapper);
        		for (Beanshell shell : preBeanshells) {
        			shell.setCaseId(caseID);
            		shellServer.save(shell);
    			}
        	}
          	if(preJdbcs!=null&&preJdbcs.size()>0) {
          		QueryWrapper<ProcessorJdbc> wrapper = new QueryWrapper<>();
        		wrapper.lambda().eq(ProcessorJdbc::getCaseId,api.getCaseId())
        						.eq(ProcessorJdbc::getProcessorType, "1");
        		pocessorJdbcser.remove(wrapper);
        		for (ProcessorJdbc ptJdbc : preJdbcs) {
            		ptJdbc.setCaseId(caseID);
            		pocessorJdbcser.save(ptJdbc);
    			}
        	}
          	     
          	if(preMocks!=null&&preMocks.size()>0) {
          		QueryWrapper<ApiMock> wrapper = new QueryWrapper<>();
        		wrapper.lambda().eq(ApiMock::getCaseId,api.getCaseId());
        		mockServer.remove(wrapper);
        		for (ApiMock dummy : preMocks) {
        			dummy.setCaseId(caseID);
        			mockServer.save(dummy);
    			}
        	}
        	//保存后置处理器api.getTcPost().equals("1")，满足processorType==2
        	PostProcessorsEntity postProcessor=api.getPostProcessor();
        	List<Beanshell> listBeanshell=postProcessor.getPostBeanshell();
        	List<ProcessorJdbc> postJdbcs=postProcessor.getPostJdbc();
        	List<ProcessorJson> postJson=postProcessor.getPostJson();
        	if(postJson!=null&&postJson.size()>0) {
        		QueryWrapper<ProcessorJson> wrapper = new QueryWrapper<>();
        		wrapper.lambda().eq(ProcessorJson::getCaseId,api.getCaseId())
        						.eq(ProcessorJson::getProcessorType, "1");
        		jsonServer.remove(wrapper);
        		for (ProcessorJson postjson : postJson) {
            		postjson.setCaseId(caseID);
            		jsonServer.save(postjson);
    			}
        	}
        	if(listBeanshell!=null&&listBeanshell.size()>0) {
        		QueryWrapper<Beanshell> wrapper = new QueryWrapper<>();
        		wrapper.lambda().eq(Beanshell::getCaseId,api.getCaseId())
        						.eq(Beanshell::getBeanshellType, "2");
        		shellServer.remove(wrapper); 
        		for (Beanshell shell : listBeanshell) {
            		shell.setCaseId(caseID);
            		shellServer.save(shell);
    			}
        	}
        	if(postJdbcs!=null&&postJdbcs.size()>0) {
        		QueryWrapper<ProcessorJdbc> wrapper = new QueryWrapper<>();
        		wrapper.lambda().eq(ProcessorJdbc::getCaseId,api.getCaseId())
        						.eq(ProcessorJdbc::getProcessorType, "2");
        		pocessorJdbcser.remove(wrapper);
        		for (ProcessorJdbc ptJdbc : postJdbcs) {
            		ptJdbc.setCaseId(caseID);
            		pocessorJdbcser.save(ptJdbc);
    			}
        	}
        	
        	//断言
        	AssertEntity asserts=api.getAssertions();
        	List<Beanshell> listShell=asserts.getAssertBeanshell();
        	List<AssertJson> listAsertJson=asserts.getAssertJson();
        	List<AssertResponse> listAssertRes=asserts.getAssertResponse();
        	if(listShell!=null&&listShell.size()>0) {
        		QueryWrapper<Beanshell> wrapper = new QueryWrapper<>();
        		wrapper.lambda().eq(Beanshell::getCaseId,api.getCaseId())
        						.eq(Beanshell::getBeanshellType, "3");
        		shellServer.remove(wrapper); 
        		
        		for (Beanshell shell : listShell) {
        			shell.setCaseId(caseID);
            		shellServer.save(shell);
				}
        	}
        	if(listAsertJson!=null&&listAsertJson.size()>0) {
        		QueryWrapper<AssertJson> wrapper = new QueryWrapper<>();
        		wrapper.lambda().eq(AssertJson::getCaseId,api.getCaseId());
        		assertJson.remove(wrapper); 
        		
        		for (AssertJson assJ : listAsertJson) {
        			assJ.setCaseId(caseID);
            		assertJson.save(assJ);
				}
        	}
        	if(listAssertRes!=null&&listAssertRes.size()>0) {
        		QueryWrapper<AssertResponse> wrapper = new QueryWrapper<>();
        		wrapper.lambda().eq(AssertResponse::getCaseId,api.getCaseId());
        		responServer.remove(wrapper); 
        		for (AssertResponse asR : listAssertRes) {
        			asR.setCaseId(caseID);
    				responServer.save(asR);
				}
        	}

        	//配置元件
        	ConfElementEntity configElements=api.getConfElements();
        	List<ApiHeader> apiHeaders=configElements.getHeaders();
        	List<UserDefinedVariable> apiUserDefined=configElements.getUserDefinedVar();
        	if(apiHeaders!=null&&apiHeaders.size()>0) {
        		QueryWrapper<ApiHeader> wrapper = new QueryWrapper<>();
        		wrapper.lambda().eq(ApiHeader::getCaseId,api.getCaseId());
        		headerServer.remove(wrapper); 
        		for (ApiHeader head : apiHeaders) {
        			head.setCaseId(caseID);
        			headerServer.save(head);
				}
        	}
        	if(apiUserDefined!=null&&apiUserDefined.size()>0) {
        		QueryWrapper<UserDefinedVariable> wrapper = new QueryWrapper<>();
        		wrapper.lambda().eq(UserDefinedVariable::getCaseId,api.getCaseId());
        		userDefineServer.remove(wrapper); 
        		for (UserDefinedVariable var : apiUserDefined) {
        			var.setCaseId(caseID);
        			userDefineServer.save(var);
				}
        	}
        	
    	}
    	
    	
        try {
        	
		} catch (Exception e) {
			log.info(e.getMessage(), e);
            CodeMsg codeMsg = CodeMsg.PARAMS_INVALID_DETAIL;
            codeMsg.setMsg(e.getMessage());
            res=res.fail(codeMsg);
			// TODO: handle exception
		}    	
    	return res;
     }
    @ApiOperation(value = "用例加载")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("loadTestCase")
    public Result<ApiTestcase> addTestCase2(@RequestParam(value = "caseId", required = true) Integer caseId) {
    	QueryWrapper<ApiTestcase> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(ApiTestcase::getCaseId,caseId);
		ApiTestcase apiTestcase=caseServer.getOne(wrapper, false);
		
		//前置
		PreProcessorsEntity preProcessor=new PreProcessorsEntity();
		//前置beanshell
		QueryWrapper<Beanshell> wrapperShell = new QueryWrapper<>();
		wrapperShell.lambda().eq(Beanshell::getCaseId,caseId);
		List<Beanshell> listBeanshell=shellServer.list(wrapperShell);
		if(listBeanshell.size()>0) {
			List<Beanshell> preBeanshell=listBeanshell.stream()
					.filter(b->b.getBeanshellType().equals("1"))
					.collect(Collectors.toList());
			if(preBeanshell.size()>0)
				preProcessor.setPreBeanshell(preBeanshell);
		}
		//前置JDBC
		QueryWrapper<ProcessorJdbc> wrapperProcessorJdbc = new QueryWrapper<>();
		wrapperProcessorJdbc.lambda().eq(ProcessorJdbc::getCaseId,caseId);
		List<ProcessorJdbc> processorJdbc=pocessorJdbcser.list(wrapperProcessorJdbc);  
		if(processorJdbc.size()>0) {
			List<ProcessorJdbc> preJdbc=processorJdbc.stream()
					.filter(b->b.getProcessorType().equals("1"))
					.collect(Collectors.toList());
			if(preJdbc.size()>0)
				preProcessor.setPreJdbc(preJdbc);
		}
		
		
		
		
		//前置MOCK
		QueryWrapper<ApiMock> wrapperMock = new QueryWrapper<>();
		wrapperMock.lambda().eq(ApiMock::getCaseId,caseId);
		List<ApiMock> preMocks=mockServer.list(wrapperMock);
		if(preMocks.size()>0)
			preProcessor.setPreMock(preMocks);
		
		//后置
		PostProcessorsEntity postProcessor=new PostProcessorsEntity();
		//后置Json
		QueryWrapper<ProcessorJson> wrapperJson = new QueryWrapper<>();
		wrapperJson.lambda().eq(ProcessorJson::getCaseId,caseId)
						.eq(ProcessorJson::getProcessorType, "2");
		List<ProcessorJson> postJsons=jsonServer.list(wrapperJson);
		if(postJsons.size()>0)
			postProcessor.setPostJson(postJsons);
		
		//后置beanshell
		if(listBeanshell.size()>0) {
			List<Beanshell> postBeanshell=listBeanshell.stream()
					.filter(b->b.getBeanshellType().equals("2"))
					.collect(Collectors.toList());
			if(postBeanshell.size()>0)
				postProcessor.setPostBeanshell(postBeanshell);
		}
		//后置Jdbc
		if(processorJdbc.size()>0) {
			List<ProcessorJdbc> postJdbc=processorJdbc.stream()
					.filter(b->b.getProcessorType().equals("2"))
					.collect(Collectors.toList());
			if(postJdbc.size()>0)
				postProcessor.setPostJdbc(postJdbc);;
		}
		
		//断言
		AssertEntity assertList=new AssertEntity();
		QueryWrapper<AssertJson> wrapperAstJson = new QueryWrapper<>();
		wrapperAstJson.lambda().eq(AssertJson::getCaseId,caseId);
		List<AssertJson> astJsons=assertJson.list(wrapperAstJson); 
		if(astJsons.size()>0)
			assertList.setAssertJson(astJsons);
		
		
		
		QueryWrapper<AssertResponse> wrapperAstResp = new QueryWrapper<>();
		wrapperAstResp.lambda().eq(AssertResponse::getCaseId,caseId);
		List<AssertResponse> listResponses=responServer.list(wrapperAstResp); 
		if(listResponses.size()>0)
			assertList.setAssertResponse(listResponses);
		
		
		if(listBeanshell.size()>0) {
			List<Beanshell> assertBeanshell=listBeanshell.stream()
					.filter(b->b.getBeanshellType().equals("3"))
					.collect(Collectors.toList());
			if(assertBeanshell.size()>0)
				assertList.setAssertBeanshell(assertBeanshell);
		}
		
		//配置元件
		ConfElementEntity confElements=new ConfElementEntity();
		QueryWrapper<ApiHeader> wrapperHeader = new QueryWrapper<>();
		wrapperHeader.lambda().eq(ApiHeader::getCaseId,caseId);
		List<ApiHeader> listHeaders=headerServer.list(wrapperHeader); 
		if(listHeaders.size()>0)
			confElements.setHeaders(listHeaders);
		
		
		
		QueryWrapper<UserDefinedVariable> wrapperUDVar = new QueryWrapper<>();
		wrapperUDVar.lambda().eq(UserDefinedVariable::getCaseId,caseId)
							 .eq(UserDefinedVariable::getType,"1");
		List<UserDefinedVariable> udvs=userDefineServer.list(wrapperUDVar); 
		if(udvs.size()>0)
			confElements.setUserDefinedVar(udvs);
		
		apiTestcase.setPreProcessor(preProcessor);
		apiTestcase.setPostProcessor(postProcessor);
		apiTestcase.setAssertions(assertList);
		apiTestcase.setConfElements(confElements);
    	Result<ApiTestcase> res=new Result<ApiTestcase>();
    	res.success(apiTestcase);
    	return res;
    }
}
