package com.techstar.testplat.swagger;

import com.alibaba.fastjson.JSONArray;
import com.autotest.data.enums.TreeType;
import com.autotest.data.mode.ApiSwagger;
import com.autotest.data.mode.ProjectManage;
import com.autotest.data.mode.TestSuites;
import com.autotest.data.mode.custom.SwaggerInfo;
import com.autotest.data.service.impl.ApiSwaggerServiceImpl;
import com.autotest.data.service.impl.ProjectManageServiceImpl;
import com.autotest.data.service.impl.TestSuitesServiceImpl;
import com.autotest.data.utils.JsonDiff;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.techstar.testplat.common.PageBaseInfo;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.apachecommons.CommonsLog;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhengyl
 * @since 2020-09-04
 */
@CommonsLog
@Service
@Primary
//@Transactional
public class ApiSwaggerService{
	private @Autowired SwaggerProcess swaggerProcess;
	private @Autowired ApiSwaggerServiceImpl swaggerService;
	private @Autowired ProjectManageServiceImpl pmServer;
	private @Autowired TestSuitesServiceImpl module;
	
	/**
	 * 自动触发项目中配置的swagger url地址接口信息同步
	 * @return
	 */
	public Boolean pullSwaggerData() {
		Boolean flag=false;
		List<ProjectManage> list=pmServer.list()
				.stream().filter(item->item.getSwaggerUrl().size()>0)
				.collect(Collectors.toList());
	    if(list.size()==0)return false;
	    for (ProjectManage pm : list) {
	    	Integer pid=pm.getProjectId();
	    	if(pm.getSwaggerUrl().size()==0)continue;
	    	List<SwaggerInfo> listUrl=pm.getSwaggerUrl();
	    	if(!(pm.getSwaggerUrl().get(0) instanceof SwaggerInfo)) {
	    		listUrl=(List<SwaggerInfo>)JSONArray.parseArray(listUrl.toString(),SwaggerInfo.class);
	    	}
			for (SwaggerInfo swg :listUrl ) {
				flag=updateSwaggerAPI(pid,swg);
			}
		}
	    this.updateModule();
	    return flag;
	}
	/**
	 * 手动触发
	 * @param pid 项目ID
	 * @param url swagger地址信息
	 * @return
	 */
	public Boolean pullSwaggerByHander(Integer pid,SwaggerInfo swg) {
		Boolean flag=false;
		flag=updateSwaggerAPI(pid,swg);
	    this.updateModule();
	    return flag;
	}
	
	/**
	 * 更新swagger表数据 String url
	 * @param pid
	 * @param url
	 * @return
	 */
	public Boolean updateSwaggerAPI(Integer pid,SwaggerInfo swg) {
		Boolean isSuccess=true;
		swaggerProcess.setApiSwaggerList(pid,swg.getUrl());
		List<ApiSwagger> apiSwaggerList = swaggerProcess.getApiSwaggerList();
		if(apiSwaggerList.size()==0) {
			return isSuccess;
		}else {
			apiSwaggerList.forEach(item->item.setApiUri(swg.getPrefix()+item.getApiUri()));
		}
		for (int i=0;i<apiSwaggerList.size();i++) {
			ApiSwagger actual=apiSwaggerList.get(i);
			QueryWrapper<ApiSwagger> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(ApiSwagger::getApiUri, actual.getApiUri());
			ApiSwagger db=swaggerService.getOne(queryWrapper);
			if(db==null) {
				int maxId=0;
				try {
					maxId=swaggerService.getBaseMapper().selectMaxId()+1;
				} catch (Exception e) {
					maxId=1;
				}
				actual.setId(maxId);
				actual.setModuleNameOld(actual.getModuleName());
				swaggerService.save(actual);
			}else {
				Boolean moduleNameModify=actual.getModuleName().equals(db.getModuleNameOld());
				Boolean paramModify=actual.getApiParameters().equals(db.getApiParameters());
				Boolean methodModify=actual.getApiMethod().equals(db.getApiMethod());
				Boolean apiInModify=actual.getApiIn().equals(db.getApiIn());
				Boolean responseModify=actual.getApiReponses().equals(db.getApiReponses());
				if(moduleNameModify||paramModify||methodModify||apiInModify||responseModify) {
					//JsonDiff.compareJson(JSONObject.fromObject(actual.getApiParameters()), JSONObject.fromObject(db.getApiParameters()),null);
					UpdateWrapper<ApiSwagger> updateWrapper = new UpdateWrapper<>();
					if(!moduleNameModify) {
						updateWrapper.lambda().set(ApiSwagger::getModuleName, actual.getModuleName());
					}
					if(!paramModify) {
						updateWrapper.lambda().set(ApiSwagger::getApiParameters, actual.getApiParameters());
					}
					if(!methodModify) {
						updateWrapper.lambda().set(ApiSwagger::getApiMethod, actual.getApiMethod());
					}
					if(!apiInModify) {
						updateWrapper.lambda().set(ApiSwagger::getApiIn, actual.getApiIn());
					}
					if(!responseModify) {
						updateWrapper.lambda().set(ApiSwagger::getApiReponses, actual.getApiReponses());
					}
					updateWrapper.lambda().set(ApiSwagger::getApiDesc, actual.getApiDesc())
										  .set(ApiSwagger::getUpdateTime, LocalDateTime.now())
								          .eq(ApiSwagger::getApiUri, actual.getApiUri());
					swaggerService.update(updateWrapper);
				}
				
			}
		}
		return isSuccess;
	}
	

	/**
	 * 分页而查询
	 * @param current
	 * @param pageSize
	 * @param queryWrapper
	 * @return
	 */
	public PageBaseInfo selestAll(long current, long pageSize,LambdaQueryWrapper<ApiSwagger> queryWrapper) {
		IPage<ApiSwagger> page = new Page<>(current, pageSize);
		page=swaggerService.page(page,queryWrapper);
		return new PageBaseInfo(page.getRecords(),current,page.getTotal(),pageSize);
	}
	
	
	public ApiSwagger selestById(String id) {
		QueryWrapper<ApiSwagger> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().eq(ApiSwagger::getId, id);
		ApiSwagger api=swaggerService.getOne(queryWrapper);
		Pattern p = Pattern.compile("\\{([^}]*)\\}");//判断是否是url地址中传参
		Matcher matcher = p.matcher(api.getApiUri());
		if(api.getApiIn().equals("path")||api.getApiIn().equals("query")) {
			try {
				String buildstr="";
				JSONObject json=JSONUtil.parseObj(api.getApiParameters());
				for (Entry<String, Object> em : json.entrySet()) {
					String subUri=em.getKey()+"="+em.getValue().toString();
					if(buildstr.isEmpty()) {
						buildstr="?"+subUri;
						continue;
					}
					buildstr=buildstr+"&"+subUri;
				}
				if(!matcher.find())
					api.setApiUri(api.getApiUri()+buildstr);
				api.setApiParameters("");
			} catch (Exception e) {
				log.error("swagger参数解析异常！swagger id:"+api.getId());
			}
			
		}			
		return api;
	}

	public List<ApiSwagger> getModuleByProject(Integer projectId) {
		QueryWrapper<ApiSwagger> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().select(ApiSwagger::getServiceName,ApiSwagger::getModuleName,ApiSwagger::getModuleNameOld)
		    .eq(ApiSwagger::getProjectId, projectId)
			.groupBy(ApiSwagger::getModuleName);
		List<ApiSwagger> list=swaggerService.list(queryWrapper);
		return list;
	}
	/**
	 * 获取swagger表中存在的所有模块名，包括变更的
	 * @return
	 */
	public List<ApiSwagger> findByGroup() {
		QueryWrapper<ApiSwagger> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().select(ApiSwagger::getProjectId,ApiSwagger::getServiceName,ApiSwagger::getModuleName,ApiSwagger::getModuleNameOld)
			.groupBy(ApiSwagger::getModuleName)
			.groupBy(ApiSwagger::getModuleNameOld);
		List<ApiSwagger> list=swaggerService.list(queryWrapper);
		return list;
	}
	/**
	 * swagger模块名如更新，同步树机构当中的模块名称。
	 */
	public void updateModule(){
		List<ApiSwagger> swaggerDb=findByGroup();
		List<TestSuites> tsList=module.list();
		for (int i = 0; i < swaggerDb.size(); i++) {
			String serverName=swaggerDb.get(i).getServiceName();
			String moduleName=swaggerDb.get(i).getModuleName();
			String oldName=swaggerDb.get(i).getModuleNameOld();
			TestSuites isexist=Find(tsList,serverName,swaggerDb.get(i).getProjectId());
    		TestSuites entity =new TestSuites();
			entity.setType(TreeType.API.name());
			if(isexist==null) {
				//添加服务名称作为父节点
				entity.setProjectId(swaggerDb.get(i).getProjectId());
				entity.setName(serverName);
				module.save(entity);
				tsList=module.list();
				
			}
			TestSuites oldExist=Find(tsList,oldName,swaggerDb.get(i).getProjectId());
			if(oldExist==null) {
				//添加子节点
				Integer p_id=isexist!=null?isexist.getId():entity.getId();
				entity.setProjectId(swaggerDb.get(i).getProjectId());
				entity.setParentId(p_id);
				entity.setName(moduleName);
				module.save(entity);
				tsList=module.list();
			}else {
				//子节点变更，更新子节点当中的名称
				if(oldExist.getName().equals(moduleName))continue;
				UpdateWrapper<TestSuites> queryWrapper=new UpdateWrapper<>();
				queryWrapper.lambda().set(TestSuites::getName, moduleName)
				.eq(TestSuites::getProjectId, oldExist.getProjectId())
				.eq(TestSuites::getName, oldExist.getName())
				.eq(TestSuites::getType, TreeType.API.name())
				.eq(TestSuites::getParentId,oldExist.getParentId());
				module.update(queryWrapper);
				tsList=module.list();
			}
		}		
	}
	
    /**
     * 查找模块名在树结构表中是否存在
     * @param list
     * @param str
     * @param pid
     * @return
     */
    private TestSuites Find(List<TestSuites> list,String str,Integer pid) {
    	TestSuites ts=null;
    	for (int i = 0; i < list.size(); i++) {
    		if(list.get(i).getName().equals(str)
    				&&list.get(i).getProjectId().equals(pid)) {
        		ts=list.get(i);
        		break;
        	}
		}
    	return ts;
    }
}
