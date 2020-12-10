package com.techstar.testplat.entity;

import java.util.List;

import com.autotest.data.mode.*;

public class PostProcessors {
	public List<Beanshell> postBeanshell;
	public List<ProcessorJdbc> postJdbc;
	public List<ProcessorJson> postJson;
	public PostProcessors() {		
		init();
	}
	private void init() {
		for (Beanshell shell : postBeanshell) {
			shell.setBeanshellType("2");
		}
		for (ProcessorJdbc shell : postJdbc) {
			shell.setProcessorType("2");
		}
		for (ProcessorJson shell : postJson) {
			shell.setProcessorType("2");
		}
	}
}
