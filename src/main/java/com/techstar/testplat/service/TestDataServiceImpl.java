package com.techstar.testplat.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.autotest.data.mode.*;
import com.autotest.data.mode.confelement.ApiHeader;
import com.autotest.data.service.impl.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.techstar.testplat.common.PageBaseInfo;
@Component
@Transactional(rollbackFor=Exception.class)
public class TestDataServiceImpl  {

	private @Autowired TheadGroupConfigServiceImpl theadGroupConfig;
//	private @Autowired ApiTestcaseServiceImpl apiTestcase;
	private @Autowired TestScheduledServiceImpl testSchedule;
	private @Autowired ScenarioReportServiceImpl apiReport;
	private @Autowired SyetemDictionaryServiceImpl syetemDic;
	private @Autowired  ProjectManageServiceImpl projectManage;
	private @Autowired  SyetemDbServiceImpl sysDb;
	private @Autowired  ApiMockServiceImpl mockData;
	private @Autowired  TestScheduledServiceImpl testScheduled;
	private @Autowired SyetemEnvServiceImpl envServer;
	private @Autowired TestSuitesServiceImpl treeData;
	public boolean AddScheduled(TestScheduled ts) {
		boolean issuccess=testScheduled.save(ts);
		
		return issuccess;
	}
	
	public boolean updateScheduled(TestScheduled ts) {
		boolean issuccess=ts.updateById();
		return issuccess;
	}
	public boolean delScheduled(List<String> ts) {
		QueryWrapper<TestScheduled> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .isNull("name")
                .ge("age", 12)
                .isNotNull("email");
        boolean issuccess=testScheduled.removeByIds(ts);
		return issuccess;
	}
	
	public Map<String, String> getTestPlanHeader(int projectID){
		QueryWrapper<ProjectManage> queryWrapper=new QueryWrapper<ProjectManage>();
		queryWrapper.lambda().eq(ProjectManage::getProjectId, projectID)
							 .select(ProjectManage::getHeaders);
		List<ApiHeader> headers=projectManage.getOne(queryWrapper).getHeaders();
		Map<String, String> headerMap=new HashMap<String, String>();
		headers.forEach(header->headerMap.put(header.getName(), header.getValue()));
		return headerMap;
	} 
//	public Map<String, String>  getSamplerHeader(int projectID,int caseID){
//		Map<String, String> headerMap=new HashMap<String, String>();
//		for (ApiHeader header : headers) {
//			if(header.getProjectId().equals(projectID)&&header.getCaseId().equals(caseID))
//				headerMap.put(header.getKey(), header.getValue());
//		}
//		return headerMap;
//	}
	public List<SyetemDictionary> getSyetemDic() {
		return syetemDic.list();
	}
	public PageBaseInfo getTestSchedule(long current,long size,QueryWrapper<TestScheduled> wrapper) {
		
		IPage<TestScheduled> pageobj = new Page<>(current, size);
    	pageobj=testSchedule.page(pageobj,wrapper);
    	PageBaseInfo pb=new PageBaseInfo(pageobj.getRecords(), current, pageobj.getTotal(),size);
		
		return pb;
	}
	public List<ScenarioReport> getApiReport() {
		return apiReport.list();
	}
	public boolean addApiReport(ScenarioReport report) {
		return apiReport.save(report);
	}
	
	
	public boolean delApiReport(List<String> ts) {
		//QueryWrapper<ApiReport> queryWrapper = new QueryWrapper<>();
		boolean flag=false;
		for (String str : ts) {
			QueryWrapper<ScenarioReport> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(ScenarioReport::getJobId, str);
			flag=apiReport.remove(queryWrapper);
		}
        
		return flag;
	}
	public List<TheadGroupConfig> getTheadGroupConfig() {
		return theadGroupConfig.list();
	}
//	public List<ApiTestcase> getTestcase() {
//		return apiTestcase.list();
//	}
	public ProjectManage getPoject(String projetID) {
		List<ProjectManage> list=projectManage.list();
		for (ProjectManage projectManage : list) {
			if(projectManage.getProjectId().equals(projetID)) {}
				return projectManage;
		}
		return null;
	}
	public Boolean addSyetemEnv(SyetemEnv env) {
		return envServer.save(env);
	}
	public Boolean updateSyetemEnv(SyetemEnv env) {
		return envServer.updateById(env);
	}
	public Boolean deleteSyetemEnv(int envId) {
		QueryWrapper<SyetemEnv> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().eq(SyetemEnv::getId, envId);
		return envServer.remove(queryWrapper);
	}
	public List<SyetemEnv> getSyetemEnv() {
		
		return envServer.list();
	}
	public SyetemDb getSyetemDb(String cnnName) {
		List<SyetemDb> list=sysDb.list();
		for (SyetemDb dbinfo : list) {
			if(dbinfo.getCnnName().equals(cnnName)) {}
				return dbinfo;
		}
		return null;
	}
	
	public Boolean addSyetemDb(SyetemDb sdb) {
		return sysDb.save(sdb);
	}
	
	public Boolean updateSyetemDb(SyetemDb sdb) {
		return sysDb.updateById(sdb);
	}
	
	public Boolean deleteSyetemDb(int connId) {
		QueryWrapper<SyetemDb> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(SyetemDb::getCnnId,connId);
		return sysDb.remove(queryWrapper);
	}
	public PageBaseInfo listSyetemDb(long current, long pageSize,LambdaQueryWrapper<SyetemDb> queryWrapper) throws Exception {
		
		IPage<SyetemDb> page = new Page<>(current, pageSize);
		page=sysDb.page(page,queryWrapper);
		return new PageBaseInfo(page.getRecords(),current,page.getTotal(),pageSize);
	}
	
	
	
	public ApiMock getApiMock(int caseID) {
		List<ApiMock> list=mockData.list();
		for (ApiMock mock : list) {
			if(mock.getCaseId().equals(caseID)) {}
				return mock;
		}
		return null;
	}
	
	public Boolean addProject(ProjectManage projectInfo) {
		Boolean isProjectSucc=projectManage.save(projectInfo);

		return isProjectSucc;
	}
	
	public Boolean updateProject(ProjectManage projectInfo) {
		UpdateWrapper<ProjectManage> projectWrapper=new UpdateWrapper<>();
		projectWrapper.lambda().eq(ProjectManage::getProjectId, projectInfo.getProjectId());
		Boolean isProjectSucc=projectManage.update(projectInfo, projectWrapper);//(projectInfo,projectWrapper)

		return isProjectSucc;
	}
	
	public Boolean deleteProject(int projectId) throws Exception {
		UpdateWrapper<ProjectManage> projectWrapper=new UpdateWrapper<>();
		projectWrapper.lambda().eq(ProjectManage::getProjectId, projectId);
		Boolean isProjectSucc=projectManage.remove(projectWrapper);
		
		return isProjectSucc;
	}

	public PageBaseInfo listProject(long current, long pageSize,LambdaQueryWrapper<ProjectManage> queryWrapper) throws Exception {
		
		IPage<ProjectManage> page = new Page<>(current, pageSize);
		page=projectManage.page(page,queryWrapper);
		
		return new PageBaseInfo(page.getRecords(),current,page.getTotal(),pageSize);
	}
}