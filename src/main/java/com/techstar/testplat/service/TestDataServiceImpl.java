package com.techstar.testplat.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autotest.data.mode.*;
import com.autotest.data.service.impl.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
@Component
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
	private List<ApiHeader> headers;

	public boolean AddScheduled(TestScheduled ts) {
		boolean issuccess=testScheduled.saveOrUpdate(ts);
		//boolean issuccess=ts.insert();
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
			if(header.getProjectId().equals(projectID)&&header.getType().equals("0"))
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
			queryWrapper.eq("JOB_ID", Integer.valueOf(str));
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
	public ApiMock getApiMock(int caseID) {
		List<ApiMock> list=mockData.list();
		for (ApiMock mock : list) {
			if(mock.getCaseId().equals(caseID)) {}
				return mock;
		}
		return null;
	}
}