package com.techstar.testplat.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.autotest.data.mode.*;
import com.autotest.data.mode.custom.ProjectInfoEntity;
import com.autotest.data.service.impl.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
@Component
@Transactional(rollbackFor=Exception.class)
public class TestDataServiceImpl  {

	private @Autowired ApiHeaderServiceImpl apiHeader;
	private @Autowired TheadGroupConfigServiceImpl theadGroupConfig;
	private @Autowired ApiTestcaseServiceImpl apiTestcase;
	private @Autowired UserDefinedVariableServiceImpl userDefinedVar;
	private @Autowired TestScheduledServiceImpl testSchedule;
	private @Autowired ApiReportServiceImpl apiReport;
	private @Autowired SyetemDictionaryServiceImpl syetemDic;
	private @Autowired  ProjectManageServiceImpl projectManage;
	private @Autowired  ProcessorJdbcServiceImpl jdbcProcess;
	private @Autowired  SyetemDbServiceImpl sysDb;
	private @Autowired  ApiMockServiceImpl mockData;
	private @Autowired  TestScheduledServiceImpl testScheduled;
	private @Autowired BeanshellServiceImpl shellServer;
	private @Autowired SyetemEnvServiceImpl envServer;
	private List<ApiHeader> headers;

	public boolean AddScheduled(TestScheduled ts) {
		boolean issuccess=testScheduled.saveOrUpdate(ts);
		
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
	public List<ApiHeader> getApiHeader() {
		List<ApiHeader> apiHeaders=apiHeader.list();
		headers=apiHeaders;
		return apiHeaders;
	}
	
	public Map<String, String> getTestPlanHeader(int projectID){
		getApiHeader();
		Map<String, String> headerMap=new HashMap<String, String>();
		for (ApiHeader header : headers) {
			if(header.getProjectId().equals(projectID)&&header.getCaseId().equals(-1))
				headerMap.put(header.getKey(), header.getValue());
		}
		return headerMap;
	} 
	public Map<String, String>  getSamplerHeader(int projectID,int caseID){
		Map<String, String> headerMap=new HashMap<String, String>();
		for (ApiHeader header : headers) {
			if(header.getProjectId().equals(projectID)&&header.getCaseId().equals(caseID))
				headerMap.put(header.getKey(), header.getValue());
		}
		return headerMap;
	}
	public List<SyetemDictionary> getSyetemDic() {
		return syetemDic.list();
	}
	public List<TestScheduled> getTestSchedule() {
		return testSchedule.list();
	}
	public List<ApiReport> getApiReport() {
		return apiReport.list();
	}
	public boolean addApiReport(ApiReport report) {
		return apiReport.save(report);
	}
	public boolean delApiReport(List<String> ts) {
		//QueryWrapper<ApiReport> queryWrapper = new QueryWrapper<>();
		boolean flag=false;
		for (String str : ts) {
			QueryWrapper<ApiReport> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(ApiReport::getJobId, str);//"JOB_ID"
			flag=apiReport.remove(queryWrapper);
		}
        
		return flag;
	}
	public List<TheadGroupConfig> getTheadGroupConfig() {
		return theadGroupConfig.list();
	}
	public List<ApiTestcase> getTestcase() {
		return apiTestcase.list();
	}
	public List<UserDefinedVariable> getUserDefinedVar() {
		return userDefinedVar.list();
	}
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
	public ProcessorJdbc getProcessorJdbc(int id) {
		List<ProcessorJdbc> list=jdbcProcess.list();
		for (ProcessorJdbc processor : list) {
			if(processor.getCaseId().equals(id)) {}
				return processor;
		}
		return null;
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
	
	public ApiMock getApiMock(int caseID) {
		List<ApiMock> list=mockData.list();
		for (ApiMock mock : list) {
			if(mock.getCaseId().equals(caseID)) {}
				return mock;
		}
		return null;
	}
	
	public Boolean addProject(ProjectInfoEntity project) {
		ProjectManage projectInfo=project.getProject();
		Boolean isProjectSucc=projectManage.save(projectInfo);
		if(isProjectSucc) {
			List<ApiHeader> headers=project.getHeader();
			headers.forEach(item->item.setProjectId(projectInfo.getProjectId()));
			Boolean isheaderSucc=apiHeader.saveBatch(headers);
			List<UserDefinedVariable> udvs=project.getUdv();
			udvs.forEach(item->item.setProjectId(projectInfo.getProjectId()));
			Boolean isudvSucc=userDefinedVar.saveBatch(udvs);
			isProjectSucc=isProjectSucc&&isheaderSucc&&isudvSucc;
		}
		return isProjectSucc;
	}
	
	public Boolean updateProject(ProjectInfoEntity project) {
		ProjectManage projectInfo=project.getProject();
		UpdateWrapper<ProjectManage> projectWrapper=new UpdateWrapper<>();
		projectWrapper.lambda().eq(ProjectManage::getProjectId, projectInfo.getProjectId());
		Boolean isProjectSucc=projectManage.update(projectWrapper);
		
		List<ApiHeader> headers=project.getHeader();
		QueryWrapper<ApiHeader> wrapper=new QueryWrapper<>();
		wrapper.lambda().eq(ApiHeader::getProjectId, projectInfo.getProjectId()).eq(ApiHeader::getCaseId, -1);
		apiHeader.remove(wrapper);
		apiHeader.saveBatch(headers);
		
		List<UserDefinedVariable> udvs=project.getUdv();
		QueryWrapper<UserDefinedVariable> udvWrapper=new QueryWrapper<>();
		udvWrapper.lambda().eq(UserDefinedVariable::getProjectId, projectInfo.getProjectId()).eq(UserDefinedVariable::getCaseId, -1);
		userDefinedVar.remove(udvWrapper);
		userDefinedVar.saveBatch(udvs);
		return isProjectSucc;
	}
	
	public Boolean deleteProject(int projectId) throws Exception {
		Boolean isSucc=false;
		UpdateWrapper<ProjectManage> projectWrapper=new UpdateWrapper<>();
		projectWrapper.lambda().eq(ProjectManage::getProjectId, projectId);
		Boolean isProjectSucc=projectManage.remove(projectWrapper);
		
		QueryWrapper<ApiHeader> wrapper=new QueryWrapper<>();
		wrapper.lambda().eq(ApiHeader::getProjectId, projectId).eq(ApiHeader::getCaseId, -1);
		Boolean isHeaderSucc =apiHeader.remove(wrapper);
		
		QueryWrapper<UserDefinedVariable> udvWrapper=new QueryWrapper<>();
		udvWrapper.lambda().eq(UserDefinedVariable::getProjectId, projectId).eq(UserDefinedVariable::getCaseId, -1);
		Boolean isudv =userDefinedVar.remove(udvWrapper);
		isSucc=isProjectSucc&&isHeaderSucc&&isudv;
		return isSucc;
	}
}